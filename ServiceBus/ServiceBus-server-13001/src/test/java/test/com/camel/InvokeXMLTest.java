package test.com.camel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.esb.core.Base;
import com.esb.util.Constant;
import com.esb.util.encrypt.RSA;

public class InvokeXMLTest  extends Base{
	
	/**
	 * 三个参数调用加图片上传
	 */
	@Test
	public void invokeWithThreeParamXMLImageTest() {
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("<root>");
			sb.append("<siteCode>350004</siteCode>");
			sb.append("<serviceCode>SinglefileUpload</serviceCode>");
			sb.append("<offline>0</offline>");
			sb.append("<files>");
			sb.append("<fileName>test222.jpg</fileName>");
			sb.append("<fileType>jpg</fileType>");
			sb.append("</files>");
			sb.append("<params>");
			sb.append("<key><![CDATA[arg0]]></key>");
			sb.append("<value><![CDATA[nimacaocao]]></value>");
			sb.append("</params>");
			sb.append("<params>");
			sb.append("<key><![CDATA[arg1]]></key>");
			sb.append("<value><![CDATA[nibacaocao]]></value>");
			sb.append("</params>");
			sb.append("</root>");
			CloseableHttpClient httpClient = HttpClients.createDefault();
			//HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
			HttpPost post = new HttpPost("http://localhost:13002/ESB/invokeAction/invokeUploadWithXML");
			String token = "test" + Constant.SPLIT_SIGN + "123456";
			String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHDpXYwv93+kl5DKoMIkn4dAVY6Qtp7ra8BlANXtavEFZW1+z+c4gQoiXQW89y0DCFpvPZdDG/VyvxwghRE1a0CAwEAAQ==";
			post.addHeader("Authorization", RSA.encryptByPublic(token, publicKeyStr));
			File file = new File("D:/11.jpg");
			HttpEntity entity = MultipartEntityBuilder.create()
						.addTextBody("param", sb.toString())
						.addBinaryBody("file", file).build();
		    post.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(post);
	        String s = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
	        System.out.println(s);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 三个参数调用
	 */
	@Test
	public void invokeWithThreeParamXMLTest() {
		
		try {
			
			StringBuffer sb = new StringBuffer();
			sb.append("<root>");
			sb.append("<siteCode>350002</siteCode>");
			sb.append("<serviceCode>threeParam</serviceCode>");
			sb.append("<offline>0</offline>");
			sb.append("<params>");
			sb.append("<key><![CDATA[arg0]]></key>");
			sb.append("<value><![CDATA[nima]]></value>");
			sb.append("</params>");
			sb.append("<params>");
			sb.append("<key><![CDATA[arg1]]></key>");
			sb.append("<value><![CDATA[B]]></value>");
			sb.append("</params>");
			sb.append("<params>");
			sb.append("<key><![CDATA[arg2]]></key>");
			sb.append("<value><![CDATA[CCCC]]></value>");
			sb.append("</params>");
			sb.append("</root>");
			
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost("http://localhost:13002/ESB/invokeAction/invokeWithXML");
			String token = "test" + Constant.SPLIT_SIGN + "123456";
			String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHDpXYwv93+kl5DKoMIkn4dAVY6Qtp7ra8BlANXtavEFZW1+z+c4gQoiXQW89y0DCFpvPZdDG/VyvxwghRE1a0CAwEAAQ==";
			post.addHeader("Authorization", RSA.encryptByPublic(token, publicKeyStr));
			String param = sb.toString();
			StringEntity entity = new StringEntity(param, "utf-8");
			entity.setContentEncoding("utf-8");
	        entity.setContentType("application/json");  
		    post.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(post);
	        httpResponse.setHeader("Content-Type", "text/plain;charset=utf-8");
	        String s = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
	        System.out.println(s);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 两个参数调用
	 */
	@Test
	public void invokeWithTwoParamJsonTest(){
		
		try {
			
			StringBuffer sb = new StringBuffer();
			sb.append("<root>");
			sb.append("<siteCode>350002</siteCode>");
			sb.append("<serviceCode>twoParam</serviceCode>");
			sb.append("<offline>0</offline>");
			sb.append("<params>");
			sb.append("<key><![CDATA[arg0]]></key>");
			sb.append("<value><![CDATA[nima]]></value>");
			sb.append("</params>");
			sb.append("<params>");
			sb.append("<key><![CDATA[arg1]]></key>");
			sb.append("<value><![CDATA[B]]></value>");
			sb.append("</params>");
			sb.append("</root>");
			
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost("http://localhost:13002/ESB/invokeAction/invokeWithXML");
			String token = "test" + Constant.SPLIT_SIGN + "123456";
			String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHDpXYwv93+kl5DKoMIkn4dAVY6Qtp7ra8BlANXtavEFZW1+z+c4gQoiXQW89y0DCFpvPZdDG/VyvxwghRE1a0CAwEAAQ==";
			post.addHeader("Authorization", RSA.encryptByPublic(token, publicKeyStr));
			String param = sb.toString();
			StringEntity entity = new StringEntity(param, "utf-8");
			entity.setContentEncoding("utf-8");
	        entity.setContentType("application/json");  
		    post.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(post);
	        httpResponse.setHeader("Content-Type", "text/plain;charset=utf-8");
	        String s = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
	        System.out.println(s);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void invokeWithTwoParamXMLSoapTest() {
		
		try {
			
			StringBuffer sb = new StringBuffer();
			sb.append("<root>");
			sb.append("<siteCode>350003</siteCode>");
			sb.append("<serviceCode>WebService</serviceCode>");
			sb.append("<offline>0</offline>");
			sb.append("<params>");
			sb.append("<key><![CDATA[s]]></key>");
			sb.append("<value><![CDATA[heelo]]></value>");
			sb.append("</params>");
			sb.append("<params>");
			sb.append("<key><![CDATA[t]]></key>");
			sb.append("<value><![CDATA[test]]></value>");
			sb.append("</params>");
			sb.append("</root>");
			CloseableHttpClient httpClient = HttpClients.createDefault();
			//HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
			HttpPost post = new HttpPost("http://localhost:13002/ESB/invokeAction/invokeWithXML");
			String token = "test" + Constant.SPLIT_SIGN + "123456";
			String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHDpXYwv93+kl5DKoMIkn4dAVY6Qtp7ra8BlANXtavEFZW1+z+c4gQoiXQW89y0DCFpvPZdDG/VyvxwghRE1a0CAwEAAQ==";
			post.addHeader("Authorization", RSA.encryptByPublic(token, publicKeyStr));
			String param = sb.toString();
			StringEntity entity = new StringEntity(param, "utf-8");
			entity.setContentEncoding("utf-8");
	        entity.setContentType("application/json");  
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
