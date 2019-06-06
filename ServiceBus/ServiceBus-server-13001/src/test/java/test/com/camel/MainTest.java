package test.com.camel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import com.esb.core.Base;

import net.sf.json.JSONObject;

public class MainTest extends Base{
	
	
	
	@Test
	public void binaryImgTest() {
		
		try {
			File file = new File("D:/11.jpg");
			byte[] bytes = FileCopyUtils.copyToByteArray(file);
			String s = new String(bytes, "ISO-8859-1");
			String name = file.getName();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("binary", s);
			String json = getJSON(map);
			//----------------
			JSONObject j = JSONObject.fromObject(json);
			String a = j.getString("binary");
			file = new File("D:/", "44.jpg");
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
