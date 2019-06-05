package test.camel.cxf.soap;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.Builder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class TestWebservice {

    public static void main(String[] args) {
    	try {
	    	CamelContext camelContext = new DefaultCamelContext();
	    	WebserviceRouteBuilder webserviceRouteBuilder = new WebserviceRouteBuilder();
        
			camelContext.addRoutes(webserviceRouteBuilder);
			camelContext.start();
			
	        ProducerTemplate template = camelContext.createProducerTemplate();
	        template.setDefaultEndpointUri("direct:start");
	        //发布一个webservice接口
//	        Exchange exchange = template.send("cxf:"  
//	                + "http://localhost:9998/service/queryInfo" //service address  
//	                + "?"  
//	                + "wsdlURL=http://localhost:9098/service/queryInfo?wsdl"    //wsdl url  
//	                + "&"  
//	                + "dataFormat=CXF_MESSAGE",in);
	        //Exchange exchange = template.send("timer:foo?repeatCount=1",in);
	        Thread.sleep(5000);
	        camelContext.stop();
    	} catch (Exception e) {
			e.printStackTrace();
		}
        
	}
}
