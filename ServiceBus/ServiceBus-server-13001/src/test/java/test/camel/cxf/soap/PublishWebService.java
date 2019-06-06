package test.camel.cxf.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService
public class PublishWebService {

	@WebMethod
	public String sayHello(String s) {
		return "{\"data\":\"" + s + "\"}";
	}
	
	public static void main(String[] args) {
		
		String address = "http://localhost:9001/HelloWorld";
		PublishWebService p = new PublishWebService();
		Endpoint.publish(address, p);
		System.out.println("发布成功:" + address);
	}
	
}
