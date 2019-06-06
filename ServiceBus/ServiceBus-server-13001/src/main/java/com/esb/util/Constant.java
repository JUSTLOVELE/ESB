package com.esb.util;

import java.util.ArrayList;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	
	public static String Constant_PUBLIC_KEY = "";

	public static String Constant_PRIVATE_KEY = "";
	
	public static String COMPILE_PATH = null;
	
	public static String ENCRYPT_KEY = "";
	
	public final static String SUCCESS_SAVE = "保存成功";
	
	public final static String SPLIT_SIGN = "&&&&";
	
	public final static String COMPILE_BASE_PACKAGENAME = "com.esb.ws";
	
	public final static String COMPILE_BASE_PACKAGENAME_PATH = "com/esb/ws";
	
	public interface Status {
		
		public final static int SUCCESS_CODE = 100;
		
		public final static int FAILURE_CODE = 101;
	}
	
	public interface CamelComponent{
		public String ACTIVEMQ = "activemq";
	}
	
	public interface HeadParam{
		/**是否被调用**/
		public String ESB_IS_INVOKE = "esb_isinvoke";
		/**状态**/
		public int END_QUEUE = -100;
		/**用户OPID**/
		public String ESB_USER_OP_ID = "esb_useropid";
		
		public String ESB_USER_ID = "esb_user_id";
		
		public String ESB_USER_PHONE = "esb_user_phone";
		
		public String ESB_USER_EMAIL = "esb_user_email";
		
		public String AUTHORIZATION = "Authorization";
		
		public String ESB_SITE_CODE = "esb_site_code";
		
		public String ESB_SERVICE_CODE = "esb_service_code";
		
		public String ESB_OFFLINE = "esb_offline";
		
		public String ESB_PARAMS = "esb_params";
		
		public String ESB_INVOKE_DATA_TYPE = "esb_invoke_data_type";
		/**选择站点动态路由调用次数**/
		public String ESB_COUNT_SELECT_SITE = "esb_countSelectSite";
		/**选择优先级队列动态路由调用次数**/
		public String ESB_COUNT_ROUTE_PRIORITY = "esb_countroutePriority";
	}
	
	public interface RouteId {
		/**http请求来后的路由,消息格式為json**/
		public String HTTP_START_JSON_ID = "http_start_json_id";
		/**http请求来后的路由,消息格式為xml**/
		public String HTTP_START_XML_ID = "http_start_xml_id";
		/**生产者普通队列**/
		public String PRODUCE_ACTIVEMQ_NORMAL = "produce_activemq_normal";
		/**生产者高速队列**/
		public String PRODUCE_ACTIVEMQ_HIGH = "produce_activemq_high";
		/**消费者普通队列**/
		public String CONSUME_ACTIVEMQ_NORMAL = "consume_activemq_normal";
	}


	public interface Key{
		
		public String ENDPOINTURI = "endpointURI";
		
		public String COUNTINVOKEPRIORITY = "countInvokePriority";
		
		public String ENCRYPT_KEY = "ENCRYPT_KEY";
		
		public String CONSTANTPUBLICKEY = "constantPublickey";
		
		public String CONSTANTPRIVATEKEY = "constantPrivateKey";
		
		public String ACTIVEMQ_ADDRESS = "activemqAddress";
		
		public String ZOOKEEPER_ADDRESS = "zookeeperAddress";
		
		public String COMPILE_PATH = "compilePath";
		
		public String PATH_ROOT = "/esb";
		
		public String PATH_ESB = "esb";
		/**站点编码**/
		public String SITE_CODE = "siteCode";
		/**编码**/
		public String CODE = "code";
		
		public String DESC = "desc";
		
		public String SUCCESS = "success";
		/**服务号**/
		public String SERVICE_CODE = "serviceCode";
		
		public String URL = "url";
		
		public String CREATEUSEROPID = "createUserOpId";
		
		public String TYPE = "type";
		
		public String KEY = "key";
		
		public String PARAM = "param";
		
		public String PARAMS = "params";
		
		public String POST = "POST";
		
		public String GET = "GET";
		
		public String JSON = "json";
		
		public String XML = "xml";
		
		public String DATA = "data";
		
		public String OFFLINE = "offline";
		
		public String VALUE = "value";
		
		public String USER_NAME = "userName";
		
		public String PASSWORD = "password";
		
		public String TOKEN = "token";
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
