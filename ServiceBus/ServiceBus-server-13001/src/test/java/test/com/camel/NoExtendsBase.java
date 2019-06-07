package test.com.camel;

import org.junit.Test;

public class NoExtendsBase {

	@Test
	public void testSTRING() {
		
		String url = "http://localhost:9001/HelloWorld?wsd?l";
		int index = url.indexOf("?");
		String a = url.substring(0, index);
		String b = url.substring(index+1, url.length());
		System.out.println(a + "&&&" + b);
		
	}
}
