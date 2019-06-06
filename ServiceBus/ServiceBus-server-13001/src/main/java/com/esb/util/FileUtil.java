package com.esb.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;

import com.esb.util.CMDJavaUtil.TrustAnyHostnameVerifier;


/**
 * @Description:
 * @Copyright: Copyright (c) 2015 ECAN All Rights Reserved
 * @author yangzl 2019-6-6
 * @version 1.00.00
 * @history:
 */
public class FileUtil {
	
	private static final Log _logger = LogFactory.getLog(FileUtil.class);
	
	/**
	 * 递归删除指定路径下的所有文件（包含文件夹），当前文件夹也会删除
	 * */
	public static void deleteAll(File file) throws Exception{
		
	  if(file==null || !file.exists()) {
		  return;
	  }
	  
	  if(file.isFile() || file.list().length == 0){
		  
		  file.delete();
		  _logger.info("---delete file:"+file.getCanonicalPath());
		  
	  }else{
		  
		   File[] files = file.listFiles();
		   
		   for(File f : files){
			    deleteAll(f);
		   }
		   
		   file.delete();
		   _logger.info("---delete file:"+file.getCanonicalPath());
	   }
	 }
	

	/**
	 * 返回的数组第0位置serviceName,1位置portName,2位置postType,3位置namespace,4位置serviceURL

	 * @param wsdl
	 * @param fileDir
	 * @param fileName
	 * @return
	 */
	public static List<String> saveWsdl(String wsdl,String fileDir,String fileName){
		long t1 = System.currentTimeMillis();
		try {
			saveFileFromURL(wsdl,fileDir,fileName);
			_logger.info("----test wsdl waste:"+(System.currentTimeMillis()-t1));
			List<String> portAndNamespace = getPortTypeAndNamespace(wsdl,fileDir,fileName);
			return portAndNamespace;
		} catch (Exception e) {
			_logger.info(wsdl+",wsdl文件保存失败!");
			throw new RuntimeException(e);
		}
	}
	
	public static void saveFileFromURL(String classURL,String saveDir,String fileName) {
        try {  
        	URLConnection  con = getConnection(classURL);
        	InputStream ins = con.getInputStream(); 
            saveFileFromInputStream(ins,saveDir,fileName);
        } catch (Exception e) {  
        	throw new RuntimeException(e); 
        }  
    } 
	
	public static void saveFileFromInputStream(InputStream is,String fileDir,String fileName){
    	OutputStream os = null;
        try {
        	File saveFileDir = new File(fileDir);
        	boolean isCreated = saveFileDir.mkdirs();
        	_logger.info("------filedirs:"+saveFileDir.getPath()+":"+isCreated);
			 os = new FileOutputStream(saveFileDir.getPath()+"/"+fileName);
			byte[] buffer = new byte[400];
			int length = 0;
			while ((length = is.read(buffer)) > 0) 
			{
			    os.write(buffer, 0, length);
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
            try
            {
                if(is!=null)
                    is.close();
                if(os!=null)
                    os.close();
            }catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
}
	
	public static URLConnection getConnection(String classURL) throws Exception{
		URL url = null;
		URLConnection  con = null;
		if(classURL.startsWith("https")){
			url = new URL(null,classURL,new sun.net.www.protocol.https.Handler());
			con = url.openConnection();
			((HttpsURLConnection)con).setHostnameVerifier(new TrustAnyHostnameVerifier());
		}else{
			url = new URL(classURL);
			con = url.openConnection();
		}
		con.setReadTimeout(10*1000);
		con.connect();
		return con;
	}
	
	/**
	 * 返回的数组第0位置serviceName,1位置portName,2位置postType,3位置namespace,4位置serviceURL
	 * @param wsdl
	 * @param fileDir
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static List<String> getPortTypeAndNamespace(String wsdl,String fileDir,String fileName) throws Exception{
		String wsdlPath = fileDir +"/"+fileName;
		//String[] values = new String[5];
		List<String> wsdlArgs = new ArrayList<>();
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(wsdlPath));
		Element root = document.getRootElement(); 
		QName qname = root.getQName();
		
		QName serviceQN = new QName("service", qname.getNamespace());
		Element serviceEl = root.element(serviceQN);
		wsdlArgs.add(serviceEl.attributeValue("name"));
		
		QName portQN = new QName("port", qname.getNamespace());
		Element portEl = serviceEl.element(portQN);
		wsdlArgs.add(portEl.attributeValue("name"));
		
		QName portTypeQN = new QName("portType", qname.getNamespace());
		Element portTypeEl = root.element(portTypeQN);
		wsdlArgs.add(portTypeEl.attributeValue("name"));
		
		QName importQN = new QName("import", qname.getNamespace());
		Element importEl = root.element(importQN);
		if(null!=importEl){
			wsdlArgs.add( importEl.attributeValue("namespace"));
			
			String schemaURL = importEl.attributeValue("location");
			String schemaFileName = fileName.substring(0,fileName.lastIndexOf("."))+"_schema.wsdl";
			FileUtil.saveFileFromURL(schemaURL,fileDir,schemaFileName);
		}else{
			wsdlArgs.add( root.attributeValue("targetNamespace"));
		}
		
		
		
		
		QName addressQN = new QName("address", root.getNamespaceForPrefix("soap"));
		Element addressP = root.element(new QName("service", qname.getNamespace()))
				.element(new QName("port", qname.getNamespace()));
		Element address = addressP.element(addressQN);
		wsdlArgs.add( address.attributeValue("location"));
		return wsdlArgs ;
	}
}
