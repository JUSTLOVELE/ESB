package com.esb.service.route;

public class RouteUtil {

	public final static String invokeNormalQueueEndpointConsume = "activemq:esb.normal.invokeNormalQueue?concurrentConsumers=5&maxConcurrentConsumers=5";
	
	public final static String invokeHighQueueEndpointConsume = "activemq:esb.high.invokHighQueue?concurrentConsumers=10&maxConcurrentConsumers=10";
	
	public final static String invokeNormalEndpointProduce = "activemq:esb.normal.invokeNormalQueue?replyToConcurrentConsumers=2&replyToMaxConcurrentConsumers=2&requestTimeout=60000";
	
	public final static String invokeHighEndpointProduce = "activemq:esb.high.invokHighQueue?replyToConcurrentConsumers=2&replyToMaxConcurrentConsumers=2&requestTimeout=60000";
}
