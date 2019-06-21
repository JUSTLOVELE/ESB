package test.com.camel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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

import net.sf.json.JSONObject;

public class InvokeJsomTest extends Base{
	
	/**
	 * 三个参数调用加图片上传
	 */
	@Test
	public void invokeWithThreeParamJsonImageTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350004");
			map.put(Constant.Key.SERVICE_CODE, "SinglefileUpload");
			map.put(Constant.Key.OFFLINE, 0);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			Map<String, Object> p1 = new HashMap<String, Object>();
			p1.put("key", "arg0");
			p1.put("value", "nimacaocao");
			params.add(p1);
			Map<String, Object> p2 = new HashMap<String, Object>();
			p2.put("key", "arg1");
			p2.put("value", "nibacaocao");
			params.add(p2);
			map.put("params", params);
			List<Map<String, Object>> files = new ArrayList<Map<String, Object>>();
			Map<String, Object> fileMap = new HashMap<String, Object>();
			fileMap.put("fileName", "test111.jpg");
			fileMap.put("fileType", "jpg");
			files.add(fileMap);
			map.put("files", files);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			//HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
			HttpPost post = new HttpPost("http://localhost:13002/ESB/invokeAction/invokeUploadWithJson");
			String token = "test" + Constant.SPLIT_SIGN + "123456";
			String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHDpXYwv93+kl5DKoMIkn4dAVY6Qtp7ra8BlANXtavEFZW1+z+c4gQoiXQW89y0DCFpvPZdDG/VyvxwghRE1a0CAwEAAQ==";
			post.addHeader("Authorization", RSA.encryptByPublic(token, publicKeyStr));
			File file = new File("D:/11.jpg");
			HttpEntity entity = MultipartEntityBuilder.create()
						.addTextBody("param", getJSON(map))
						.addBinaryBody("file", file).build();
		    post.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(post);
	        String s = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
	        System.out.println(s);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void invokeWithTwoParamJsonWebServiceTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350003");
			map.put(Constant.Key.SERVICE_CODE, "WebService");
			map.put(Constant.Key.OFFLINE, 0);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("key", "s");
			p.put("value", "helloworld");
			params.add(p);
			Map<String, Object> p1 = new HashMap<String, Object>();
			p1.put("key", "t");
			p1.put("value", "testtest");
			params.add(p1);
			map.put("params", params);
			
			CloseableHttpClient httpClient = HttpClients.createDefault();
			//HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
			HttpPost post = new HttpPost("http://localhost:13002/ESB/invokeAction/invokeWithJson");
			String token = "test" + Constant.SPLIT_SIGN + "123456";
			String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHDpXYwv93+kl5DKoMIkn4dAVY6Qtp7ra8BlANXtavEFZW1+z+c4gQoiXQW89y0DCFpvPZdDG/VyvxwghRE1a0CAwEAAQ==";
			post.addHeader("Authorization", RSA.encryptByPublic(token, publicKeyStr));
			String param = getJSON(map);
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
	public void invokeWithOneParamJsonWebServiceTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350003");
			map.put(Constant.Key.SERVICE_CODE, "WebService");
			map.put(Constant.Key.OFFLINE, 0);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("key", "s");
			p.put("value", "helloworld");
			params.add(p);
			map.put("params", params);
			
			CloseableHttpClient httpClient = HttpClients.createDefault();
			//HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
			HttpPost post = new HttpPost("http://localhost:13002/ESB/invokeAction/invokeWithJson");
			String token = "test" + Constant.SPLIT_SIGN + "123456";
			String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHDpXYwv93+kl5DKoMIkn4dAVY6Qtp7ra8BlANXtavEFZW1+z+c4gQoiXQW89y0DCFpvPZdDG/VyvxwghRE1a0CAwEAAQ==";
			post.addHeader("Authorization", RSA.encryptByPublic(token, publicKeyStr));
			String param = getJSON(map);
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
	 * 二进制参数调用
	 */
	@Test
	public void invokeWithBinaryJsonTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350002");
			map.put(Constant.Key.SERVICE_CODE, "binaryImgTest");
			map.put(Constant.Key.OFFLINE, 0);
			
			CloseableHttpClient httpClient = HttpClients.createDefault();
			//HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
			HttpPost post = new HttpPost("http://localhost:13002/ESB/invokeAction/invokeWithJson");
			String token = "test" + Constant.SPLIT_SIGN + "123456";
			String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHDpXYwv93+kl5DKoMIkn4dAVY6Qtp7ra8BlANXtavEFZW1+z+c4gQoiXQW89y0DCFpvPZdDG/VyvxwghRE1a0CAwEAAQ==";
			post.addHeader("Authorization", RSA.encryptByPublic(token, publicKeyStr));
			String param = getJSON(map);
			StringEntity entity = new StringEntity(param, "utf-8");
			entity.setContentEncoding("utf-8");
	        entity.setContentType("application/json");  
		    post.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(post);
	        httpResponse.setHeader("Content-Type", "text/plain;charset=utf-8");
	        String s = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
	        System.out.println(s);
	        JSONObject j = JSONObject.fromObject(s);
	        s = j.getString("data");
	        j = JSONObject.fromObject(s);
			String a = j.getString("binary");
			File file = new File("D:/", "44.jpg");
			InputStream in = new ByteArrayInputStream(a.getBytes("ISO-8859-1"));
			FileOutputStream fos = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int nRead = 0;
			
			while ((nRead = in.read(b)) != -1) {
                fos.write(b, 0, nRead);
            }
			
            fos.flush();
            fos.close();
            in.close();
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 三个参数调用
	 */
	@Test
	public void invokeWithThreeParamJsonTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350002");
			map.put(Constant.Key.SERVICE_CODE, "threeParam");
			map.put(Constant.Key.OFFLINE, 0);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			Map<String, Object> p1 = new HashMap<String, Object>();
			p1.put("key", "arg0");
			p1.put("value", "nimacaocao");
			params.add(p1);
			Map<String, Object> p2 = new HashMap<String, Object>();
			p2.put("key", "arg1");
			p2.put("value", "nibacaocao");
			params.add(p2);
			Map<String, Object> p3 = new HashMap<String, Object>();
			p3.put("key", "arg2");
			p3.put("value", "nibacaocao");
			params.add(p3);
			map.put("params", params);
			
			CloseableHttpClient httpClient = HttpClients.createDefault();
			//HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
			HttpPost post = new HttpPost("http://localhost:13002/ESB/invokeAction/invokeWithJson");
			String token = "test" + Constant.SPLIT_SIGN + "123456";
			String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHDpXYwv93+kl5DKoMIkn4dAVY6Qtp7ra8BlANXtavEFZW1+z+c4gQoiXQW89y0DCFpvPZdDG/VyvxwghRE1a0CAwEAAQ==";
			post.addHeader("Authorization", RSA.encryptByPublic(token, publicKeyStr));
			String param = getJSON(map);
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
	public void invokeWithTwoParamJsonTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350002");
			map.put(Constant.Key.SERVICE_CODE, "twoParam");
			map.put(Constant.Key.OFFLINE, 0);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			Map<String, Object> p1 = new HashMap<String, Object>();
			p1.put("key", "arg0");
			p1.put("value", "nimacaocao");
			params.add(p1);
			Map<String, Object> p2 = new HashMap<String, Object>();
			p2.put("key", "arg1");
			p2.put("value", "nibacaocao");
			params.add(p2);
			map.put("params", params);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			//HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
			HttpPost post = new HttpPost("http://localhost:13002/ESB/invokeAction/invokeWithJson");
			String token = "test" + Constant.SPLIT_SIGN + "123456";
			String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHDpXYwv93+kl5DKoMIkn4dAVY6Qtp7ra8BlANXtavEFZW1+z+c4gQoiXQW89y0DCFpvPZdDG/VyvxwghRE1a0CAwEAAQ==";
			post.addHeader("Authorization", RSA.encryptByPublic(token, publicKeyStr));
			String param = getJSON(map);
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
	public void invokeWithOneParamJsonTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350000");
			map.put(Constant.Key.SERVICE_CODE, "checkVersion");
			map.put(Constant.Key.OFFLINE, 0);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("key", "source");
			p.put("value", 1);
			params.add(p);
			map.put("params", params);
			
			CloseableHttpClient httpClient = HttpClients.createDefault();
			//HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
			HttpPost post = new HttpPost("http://localhost:13002/ESB/invokeAction/invokeWithJson");
			String token = "test" + Constant.SPLIT_SIGN + "123456";
			String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHDpXYwv93+kl5DKoMIkn4dAVY6Qtp7ra8BlANXtavEFZW1+z+c4gQoiXQW89y0DCFpvPZdDG/VyvxwghRE1a0CAwEAAQ==";
			post.addHeader("Authorization", RSA.encryptByPublic(token, publicKeyStr));
			String param = getJSON(map);
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
	public void invokeWithNoParamJsonTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.SITE_CODE, "350002");
			map.put(Constant.Key.SERVICE_CODE, "HelloWorld");
			map.put(Constant.Key.OFFLINE, 0);
			
			CloseableHttpClient httpClient = HttpClients.createDefault();
			//HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
			HttpPost post = new HttpPost("http://localhost:13002/ESB/invokeAction/invokeWithJson");
			String token = "test" + Constant.SPLIT_SIGN + "123456";
			String publicKeyStr = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHDpXYwv93+kl5DKoMIkn4dAVY6Qtp7ra8BlANXtavEFZW1+z+c4gQoiXQW89y0DCFpvPZdDG/VyvxwghRE1a0CAwEAAQ==";
			post.addHeader("Authorization", RSA.encryptByPublic(token, publicKeyStr));
			String param = getJSON(map);
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
