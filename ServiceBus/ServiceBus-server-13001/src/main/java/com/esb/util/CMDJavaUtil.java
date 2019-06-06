package com.esb.util;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.tools.common.ToolContext;
import org.apache.cxf.tools.wsdlto.WSDLToJava;


/**
 * @Description:
 * @Copyright: Copyright (c) 2015 ECAN All Rights Reserved
 * @Company: 福建亿能达信息技术有限公司
 * @author zhangsm 2015-10-27
 * @version 1.00.00
 * @history:
 */
public class CMDJavaUtil {
	private static final Log logger = LogFactory.getLog(CMDJavaUtil.class);
	
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	
	public static void wsdl2Java(String packagePath,String dir,String wsdlUrl) throws Exception {
		WSDLToJava wsdlToJava = new WSDLToJava(new String[]
				{"-p", packagePath,"-client","-d", 
				dir,  
				wsdlUrl});  
		wsdlToJava.run(new ToolContext()); 
	}
	
	public static void execCMDAsync(final String cmd,int waitSecond){
		final Callable<Process> task = new Callable<Process>(){
            @Override public Process call() throws Exception {
                return Runtime.getRuntime().exec(cmd);
            }
        };
       
        final Future<Process> result = scheduler.submit(task);
        final Runnable checker = new Runnable(){
                @Override public void run(){
                    Process p = null;
                    int exitValue = 0;
                    try {
                        p = result.get();
                        exitValue = p.exitValue();
                    } catch (Exception xe) {
                        if(p != null)p.destroy();
                    }
                    scheduler.shutdownNow();
                    System.exit(exitValue);
                }
            };
        scheduler.schedule(checker, waitSecond, TimeUnit.SECONDS);
	}
	
	public static  String execCMD(String cmd)  throws Exception{
		
	      Runtime rt = Runtime.getRuntime();
	      Process ppp = rt.exec(cmd);
	      String result ="";
	      //input
	      InputStreamReader ir = new InputStreamReader(ppp.getInputStream(),"GBK");
	      LineNumberReader input = new LineNumberReader(ir);
	
	      StringBuffer sb = new StringBuffer();
	      String line;
	      while ((line = input.readLine()) != null){
	    	  sb.append(line).append("\n");
	      }
	      if(sb.length()>0){
	    	  result = sb.toString();
	      }else{
	    	  //error
	    	  ir = new InputStreamReader(ppp.getErrorStream(),"GBK");
	    	  input = new LineNumberReader(ir);
	    	  while ((line = input.readLine()) != null){
	    		  sb.append(line).append("\n");
	    	  }
	    	  result = sb.toString();
	      }
	      ppp.waitFor();
	      if(ppp.exitValue()!=0){
	    	  throw new RuntimeException(result);
	      }
	      return result;
		
	}
	
	public static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            // 直接返回true
            return true;
        }
    }
	
	public static void main(String[] args) throws Exception {
		
		wsdl2Java("ws.com.company.webservice",
				"/E:/Tomcat7/webapps/CamelCxfTest/WEB-INF/classes",
				"http://192.168.1.93:9080/services/webService?wsdl");
	}
}
