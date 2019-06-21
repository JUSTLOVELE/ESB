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

import com.esb.core.Base;
import com.esb.util.Constant;
import com.esb.util.encrypt.RSA;

public class RegisterJsonTest extends Base{
	
	/**
	 * 文件上传注册
	 */
	@Test
	public void registerWithtwoParamJsonFileUpload() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350004");
			map.put(Constant.Key.SERVICE_CODE, "SinglefileUpload");
			map.put(Constant.Key.URL, "http://localhost:8080/ESBTest/testAction/fileTest");
			map.put(Constant.Key.TYPE, 4);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("key", "arg0");
			p.put("type", 1);
			params.add(p);
			Map<String, Object> p1 = new HashMap<String, Object>();
			p1.put("key", "arg1");
			p1.put("type", 1);
			params.add(p1);
			Map<String, Object> p2 = new HashMap<String, Object>();
			p2.put("key", "file");
			p2.put("type", 2);
			params.add(p2);
			map.put(Constant.Key.PARAMS, params);
			String param = getJSON(map);
			System.out.println(param);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
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
	
	@Test
	public void userRegister() {
		
		try {
			String param = "userName=test&userPhone=xxxxx&userEmail=694105388@qq.com&pwd=123456";
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost("http://localhost:13001/ESB/userAction/register");
			StringEntity entity = new StringEntity(param, "utf-8");
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
	

	/**
	 * 2个参数测试
	 */
	@Test
	public void registerWithtwoParamJsonSoapTest() {
		
		try {
			
			StringBuffer sb = new StringBuffer();
			sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:zuul=\"http://zuul.esb.com/\">");
			sb.append("   <soapenv:Header/>");
			sb.append("   <soapenv:Body>");
			sb.append("      <zuul:sayHello>");
			sb.append("         <arg0>?</arg0>");
			sb.append("         <arg1>?</arg1>");
			sb.append("      </zuul:sayHello>");
			sb.append("   </soapenv:Body>");
			sb.append("</soapenv:Envelope>	");		
			String soap = sb.toString();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350003");
			map.put(Constant.Key.SERVICE_CODE, "WebService");
			map.put(Constant.Key.URL, "http://localhost:9001/HelloWorld?wsdl");
			map.put(Constant.Key.TYPE, 3);
			map.put(Constant.Key.SOAP, soap);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("key", "s");
			p.put("type", 1);
			params.add(p);
			Map<String, Object> p1 = new HashMap<String, Object>();
			p1.put("key", "t");
			p1.put("type", 1);
			params.add(p1);
			map.put(Constant.Key.PARAMS, params);
			String param = getJSON(map);
			System.out.println(param);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
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
	
	/**
	 * 2个参数测试
	 */
	@Test
	public void registerWithtwoParamJsonWebServiceTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350003");
			map.put(Constant.Key.SERVICE_CODE, "WebService");
			map.put(Constant.Key.URL, "http://localhost:9001/HelloWorld?wsdl");
			map.put(Constant.Key.TYPE, 2);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("key", "s");
			p.put("type", 1);
			params.add(p);
			Map<String, Object> p1 = new HashMap<String, Object>();
			p1.put("key", "t");
			p1.put("type", 1);
			params.add(p1);
			map.put(Constant.Key.PARAMS, params);
			String param = getJSON(map);
			System.out.println(param);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
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
	
	/**
	 * 一个参数测试
	 */
	@Test
	public void registerWithOneParamJsonWebServiceTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350003");
			map.put(Constant.Key.SERVICE_CODE, "WebService");
			map.put(Constant.Key.URL, "http://localhost:9001/HelloWorld?wsdl");
			map.put(Constant.Key.TYPE, 2);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("key", "s");
			p.put("type", 1);
			params.add(p);
			map.put(Constant.Key.PARAMS, params);
			String param = getJSON(map);
			System.out.println(param);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
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
	
	/**
	 * 二进制文件注册
	 */
	@Test
	public void registerWithbinaryJsonTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350002");
			map.put(Constant.Key.SERVICE_CODE, "binaryImgTest");
			map.put(Constant.Key.URL, "http://localhost:13001/ESB/testAction/binaryImgTest");
			map.put(Constant.Key.TYPE, 1);
			String param = getJSON(map);
			System.out.println(param);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
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
	
	/**
	 * 三个参数注册
	 */
	@Test
	public void registerWithThreeParamJsonTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350002");
			map.put(Constant.Key.SERVICE_CODE, "threeParam");
			map.put(Constant.Key.URL, "http://localhost:13001/ESB/testAction/threeParam");
			map.put(Constant.Key.TYPE, 1);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			
			for(int i=0; i<3; i++) {
				
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("key", "arg" + i);
				p.put("type", 1);
			
				params.add(p);
			}
			
			map.put(Constant.Key.PARAMS, params);
			String param = getJSON(map);
			System.out.println(param);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
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
	
	/**
	 * 两个参数注册
	 */
	@Test
	public void registerWithTwoParamJsonTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350002");
			map.put(Constant.Key.SERVICE_CODE, "twoParam");
			map.put(Constant.Key.URL, "http://localhost:13001/ESB/testAction/twoParam");
			map.put(Constant.Key.TYPE, 1);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			
			for(int i=0; i<2; i++) {
				
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("key", "arg" + i);
				p.put("type", 1);
			
				params.add(p);
			}
			
			map.put(Constant.Key.PARAMS, params);
			String param = getJSON(map);
			System.out.println(param);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
			String token = "test" + Constant.SPLIT_SIGN + "123456";
			String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHDpXYwv93+kl5DKoMIkn4dAVY6Qtp7ra8BlANXtavEFZW1+z+c4gQoiXQW89y0DCFpvPZdDG/VyvxwghRE1a0CAwEAAQ==";
			post.addHeader("Authorization", RSA.encryptByPublic(token, publicKeyStr));
			StringEntity entity = new StringEntity("param=" + param, "utf-8");
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
	
	/**
	 * 一个参数测试
	 */
	@Test
	public void registerWithOneParamJsonTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350000");
			map.put(Constant.Key.SERVICE_CODE, "checkVersion");
			map.put(Constant.Key.URL, "http://www.fjjkkj.com/HY-GS/mobileSystemAction/api/checkVersion");
			map.put(Constant.Key.TYPE, 1);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("key", "source");
			p.put("type", 1);
			params.add(p);
			map.put(Constant.Key.PARAMS, params);
			String param = getJSON(map);
			System.out.println(param);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
			String token = "test" + Constant.SPLIT_SIGN + "123456";
			String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHDpXYwv93+kl5DKoMIkn4dAVY6Qtp7ra8BlANXtavEFZW1+z+c4gQoiXQW89y0DCFpvPZdDG/VyvxwghRE1a0CAwEAAQ==";
			post.addHeader("Authorization", RSA.encryptByPublic(token, publicKeyStr));
			StringEntity entity = new StringEntity("param=" + param, "utf-8");
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

	/**
	 * 无参数测试
	 */
	@Test
	public void registerWithNoParamJsonTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350002");
			map.put(Constant.Key.SERVICE_CODE, "HelloWorld");
			map.put(Constant.Key.URL, "http://localhost:13001/ESB/invokeAction/helloworld");
			map.put(Constant.Key.TYPE, 1);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			map.put(Constant.Key.PARAMS, params);
			
			String param = getJSON(map);
			System.out.println(param);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
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
