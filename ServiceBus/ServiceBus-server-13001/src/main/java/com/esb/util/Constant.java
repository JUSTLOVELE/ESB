package com.esb.util;

import java.util.ArrayList;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.esb.InitStartComponent;

/**
 * @Description:常熟类
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-15
 * @version 1.00.00
 * @history:
 */
public class Constant {
	
	private static final Log _logger = LogFactory.getLog(Constant.class);
	
	public static String ZOOKEEPER_ADDRESS = null;
	
	public static String ACTIVEMQ_ADDRESS = null;
	
	public final static String SUCCESS_SAVE = "保存成功";
	
	public interface Status {
		
		public final static int SUCCESS_CODE = 100;
		
		public final static int FAILURE_CODE = 101;
	}
	
	public interface CamelComponent{
		public String ACTIVEMQ = "activemq";
	}
	
	public interface HeadParam{
		/**优先级**/
		public String INVOKEPRIORITY = "invokePriority";
		/**是否被调用**/
		public String IS_INVOKE = "isInvoke";
		
		public int END_QUEUE = -100;
	}


	public interface Key{
		
		public String ACTIVEMQ_ADDRESS = "activemqAddress";
		
		public String ZOOKEEPER_ADDRESS = "zookeeperAddress";
		
		public String PATH_ROOT = "/esb";
		
		public String PATH_ESB = "esb";
		/**医院编码**/
		public String ORG_CODE = "orgCode";
		/**编码**/
		public String CODE = "code";
		
		public String DESC = "desc";
		
		public String SUCCESS = "success";
		/**服务号**/
		public String SERVICE_CODE = "serviceCode";
		
		public String URL = "url";
		
		public String TYPE = "type";
		
		public String KEY = "key";
		
		public String PARAM = "param";
		
		public String PARAMS = "params";
		
		public String POST = "POST";
		
		public String GET = "GET";
	}
	
	//本地配置文件
	private static PropertiesConfiguration cfg = null;
	
	public static void setCfg(PropertiesConfiguration config){
			cfg = config;
	}
	
	public static String getConstObject(String key){
		
		if(key == null || "".equals(key)) {
			return null;
		}
		
		try {
			
			Object value = cfg.getProperty(key);
			StringBuffer sb = new StringBuffer();
			
			if(value instanceof ArrayList) {
				
				ArrayList<String> list = (ArrayList<String>) value;
				
				if(list.size() > 0) {
					
					sb.append(list.get(0));
					
					for(int i=1; i<list.size(); i++) {
						
						sb.append("," + list.get(i));
					}
					
				}
				
			}else {
				sb.append(value.toString());
			}
			return sb.toString();
		} catch (Exception e) {
			_logger.info(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 本地常量优先级高与远程常量
	 * @param key
	 * @return
	 */
	public static String getConst(String key){
		
		if(key == null || "".equals(key)) {
			return null;
		}
		
		String value = null;
		
		try {
			value = (String)cfg.getProperty(key);
		} catch (Exception e) {
			_logger.info(e.getMessage(), e);
		}
		return value;
	}
	
	public static void setConst(String key, String value){
		
		try {
			cfg.setProperty(key, value);
		} catch (Exception e) {
			_logger.info(e.getMessage(), e);
		}
	}
}
