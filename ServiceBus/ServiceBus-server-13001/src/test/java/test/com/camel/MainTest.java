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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.esb.core.Base;
import com.esb.util.Constant;

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
	public void registerWithJsonTest() {
		
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.ORG_CODE, "350000");
			map.put(Constant.Key.SERVICE_CODE, "checkVersion");
			map.put(Constant.Key.URL, "http://192.168.1.151/HY-GS/mobileSystemAction/api/checkVersion?source=2");
			map.put(Constant.Key.TYPE, 1);
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			map.put(Constant.Key.PARAMS, params);
			
			String param = getJSON(map);
			System.out.println(param);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost("http://localhost:13001/ESB/invokeAction/registerWithJson");
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
		
		/*Map<String, Object> m = new HashMap<String, Object>();
		m.put("param", param);*/
		
		
	}

	@Test
	public void traversalZookeeper() {
		
		try {
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,  3);
			CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.1.150:2181,192.168.1.151:2181,192.168.1.2:2181", 5000,3000, retryPolicy);
			client.start();
			/*String s = new String(client.getData().forPath("/root/350000/checkVersion"));
			System.out.println(s);*/
			/*List<String> list = client.getChildren().forPath("/root");
			System.out.println(list.toString());*/
			//client.delete().deletingChildrenIfNeeded().forPath("/root/350000");
			List<String> orglist = client.getChildren().forPath("/root");
			
			for(String org: orglist) {
				
				String path  = "/root" + "/" + org;
				List<String> serviceList = client.getChildren().forPath(path);
				
				for(String service: serviceList) {

					String p = path + "/" + service;
					System.out.println(p);
					System.out.println(new String(client.getData().forPath(p)));
				}
			}
			
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}