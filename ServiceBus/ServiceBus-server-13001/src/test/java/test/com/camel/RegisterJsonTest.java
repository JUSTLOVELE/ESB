package test.com.camel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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

import net.sf.json.JSONObject;

public class RegisterJsonTest extends Base{
	
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
