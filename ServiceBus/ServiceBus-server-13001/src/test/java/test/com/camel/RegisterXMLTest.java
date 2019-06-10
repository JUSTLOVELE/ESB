package test.com.camel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.esb.util.Constant;
import com.esb.util.encrypt.RSA;

public class RegisterXMLTest {

	/**
	 * 三个参数注册
	 */
	@Test
	public void registerWithThreeParamJsonTest() {
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("<root>");
			sb.append("<siteCode>350002</siteCode>");
			sb.append("<serviceCode>threeParam</serviceCode>");
			sb.append("<url>http://localhost:13001/ESB/testAction/threeParam</url>");
			sb.append("<type>1</type>");
			sb.append("<param>");
			sb.append("<key><![CDATA[arg0]]></key>");
			sb.append("<value><![CDATA[nima]]></value>");
			sb.append("</param>");
			sb.append("<param>");
			sb.append("<key><![CDATA[arg1]]></key>");
			sb.append("<value><![CDATA[B]]></value>");
			sb.append("</param>");
			sb.append("<param>");
			sb.append("<key><![CDATA[arg2]]></key>");
			sb.append("<value><![CDATA[BBB]]></value>");
			sb.append("</param>");
			sb.append("</root>");
			
			String param = sb.toString();
			System.out.println(param);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithXML");
			String token = "test" + Constant.SPLIT_SIGN + "123456";
			String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHDpXYwv93+kl5DKoMIkn4dAVY6Qtp7ra8BlANXtavEFZW1+z+c4gQoiXQW89y0DCFpvPZdDG/VyvxwghRE1a0CAwEAAQ==";
			post.addHeader("Authorization", RSA.encryptByPublic(token, publicKeyStr));
			//HttpPost post = new HttpPost("http://localhost:13002/ESB/invokeAction/invokeWithJson");
			StringEntity entity = new StringEntity("param=" + param, "utf-8");
          //  entity.setContentType("application/json;charset=UTF-8");
            entity.setContentEncoding("utf-8");
            entity.setContentType("application/x-www-form-urlencoded");  
	        post.setEntity(entity);
	        HttpResponse httpResponse = httpClient.execute(post);
	        httpResponse.setHeader("Content-Type", "text/plain;charset=utf-8");
	        String s = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
	        System.out.println(s);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
