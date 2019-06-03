package test.com.camel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.esb.core.Base;
import com.esb.util.Constant;
import com.esb.util.encrypt.RSA;

public class MainTest extends Base{
	
	
	@Test
	public void activemqConnect() {
		
		try {
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("failover:(tcp://192.168.1.152:61616,tcp://192.168.1.151:61616,tcp://192.168.1.150:61616)");
			Connection connection = factory.createConnection();
			connection.start();
			Session session = connection.createSession();
			connection.close();
			
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
		
		/*Map<String, Object> m = new HashMap<String, Object>();
		m.put("param", param);*/
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
		
		/*Map<String, Object> m = new HashMap<String, Object>();
		m.put("param", param);*/
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
	
	@Test
	public void registerWithJsonTest() {
		
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

	@Test
	public void traversalZookeeper() {
		
		try {
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,  3);
			CuratorFramework client = CuratorFrameworkFactory.newClient("106.52.117.118:2181", 5000,3000, retryPolicy);
			client.start();
			/*String s = new String(client.getData().forPath("/root/350000/checkVersion"));
			System.out.println(s);*/
			/*List<String> list = client.getChildren().forPath("/root");
			System.out.println(list.toString());*/
			//client.delete().deletingChildrenIfNeeded().forPath("/root/350000");
			List<String> sitelist = client.getChildren().forPath("/esb");
			System.out.println("********************");
			
			for(String site: sitelist) {
				
				String path  = "/esb" + "/" + site;
				List<String> serviceList = client.getChildren().forPath(path);
				
				for(String service: serviceList) {

					String p = path + "/" + service;
					System.out.println(p);
					System.out.println(new String(client.getData().forPath(p)));
					client.setData().forPath(p, "hello world 2".getBytes());
				}
			}
			System.out.println("********************");
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getPath() {
		
		String path = "/esb/350000/checkversion";
		path = path.substring(1, path.length());
		String [] p = path.split("/");
		System.out.println(p.length);
		for(String s : p) {
			System.out.println(s);
		}
	}
	
	
}
