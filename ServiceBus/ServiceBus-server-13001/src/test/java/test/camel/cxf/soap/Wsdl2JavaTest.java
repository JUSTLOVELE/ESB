package test.camel.cxf.soap;

import org.apache.camel.component.cxf.CxfEndpoint;
import org.junit.Test;

public class Wsdl2JavaTest {

	@Test
	public void saveWsdl() {
		
		try {
			 /*List<String> list = FileUtil.saveWsdl("http://localhost:9001/HelloWorld?wsdl", "D:/logs/webservice", "test.xml");
			 
			 for(String s : list) {
				 System.out.println(s);
			 }
			 
			String cmd = "wsimport -p com.esb.ws -d D:\\logs\\webservice -s D:\\logs\\webservice http://localhost:9001/HelloWorld?wsdl";
			String result = CMDJavaUtil.execCMD(cmd);
			System.out.println(result);*/
			//要把class放在编译路径下面
			Class<?> c = Class.forName("com.esb.ws.PublishWebService");
			CxfEndpoint cxfEndpoint = new CxfEndpoint();
			cxfEndpoint.setServiceClass(c);
			System.out.println(c.getMethods()[0].getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
