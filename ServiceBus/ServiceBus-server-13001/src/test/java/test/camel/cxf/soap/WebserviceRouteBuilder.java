package test.camel.cxf.soap;

import java.io.InputStream;

import javax.xml.transform.dom.DOMSource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class WebserviceRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		//camel调用webservice
		from("timer://foo?repeatCount=1")
		.setBody(constant("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:beij=\"http://beijingtong.zhengtoon.com/\"><soapenv:Header/><soapenv:Body><beij:queryCarInfomation><!--Optional:--><arg0>111</arg0></beij:queryCarInfomation></soapenv:Body></soapenv:Envelope>"))
		.to("cxf:"  
		+ "http://localhost:9098/service/queryInfo" //service address  
		+ "?"  
		+ "wsdlURL=http://localhost:9098/service/queryInfo?wsdl"    //wsdl url 
		+ "&"  
		+ "dataFormat=RAW"        //dataformat type  
		).convertBodyTo(String.class);
		
		//camel发布webservice
//		from("cxf:"  
//				+ "http://localhost:9998/service/queryInfo" //service address  
//				+ "?"  
//				+ "wsdlURL=http://localhost:9098/service/queryInfo?wsdl"    //wsdl url  
//				+ "&serviceClass=com.zhengtoon.beijingtong.QueryService"
//				+ "&"  
//				+ "dataFormat=CXF_MESSAGE"        //dataformat type  
//				).process(new HttpGetProcessor());

	}

}
