package com.esb.service.route;

import com.esb.util.Constant;

public class RouteUtil {
	
	public final static String HTTP_INVOKE_JSON_ADDRESS = "netty4-http:http://0.0.0.0:13002/ESB/invokeAction/invokeWithJson";
	
	public final static String HTTP_INVOKE_XML_ADDRESS = "netty4-http:http://0.0.0.0:13002/ESB/invokeAction/invokeWithXML";
	/**普通消费者**/
	public final static String invokeNormalQueueEndpointConsume = "activemq:esb.normal.invokeNormalQueue?concurrentConsumers=5&maxConcurrentConsumers=5";
	/**高速消费者**/
	public final static String invokeHighQueueEndpointConsume = "activemq:esb.high.invokHighQueue?concurrentConsumers=10&maxConcurrentConsumers=10";
	/**普通生产者**/
	public final static String invokeNormalEndpointProduce = "activemq:esb.normal.invokeNormalQueue?concurrentConsumers=2&maxConcurrentConsumers=2&requestTimeout=60000";
	/**高速生产者**/
	public final static String invokeHighEndpointProduce = "activemq:esb.high.invokHighQueue?concurrentConsumers=2&maxConcurrentConsumers=2&requestTimeout=60000";

	public interface Direct{
		
		public String DIRECT_PRODUCENORMAL = "direct:produceNormal";
		
		public String DIRECT_PRODUCEHIGH = "direct:produceHigh";
	}
	
	public static String getServicePath(String siteCode, String serviceCode) {
		return Constant.Key.PATH_ROOT + "/" + siteCode + "/" + serviceCode;
	}
	
	public static String getSitePath(String siteCode) {
		return Constant.Key.PATH_ROOT + "/" + siteCode;
	}
	
	/**
	 * 获取路由ID
	 * @param root
	 * @param siteCode
	 * @param serviceCode
	 * @return
	 */
	public static String getRouteId(String root, String siteCode, String serviceCode) {
		
		String routeId = root + "_" + siteCode + "_" + serviceCode;
		return routeId;
	}
}
