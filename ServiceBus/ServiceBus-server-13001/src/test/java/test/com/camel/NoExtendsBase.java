package test.com.camel;

import org.junit.Test;

public class NoExtendsBase {

	@Test
	public void testSTRING() {
		
		String url = "http://localhost:9001/HelloWorld?wsdl";
		int index = url.lastIndexOf("?");
		url.substring(0, index);
	}
}
