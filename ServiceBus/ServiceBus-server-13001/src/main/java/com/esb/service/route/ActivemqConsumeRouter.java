package com.esb.service.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.esb.util.Constant;

/**
 * @Description:动态路由
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-31
 * @version 1.00.00
 * @history:
 */
@Component
public class ActivemqConsumeRouter extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		
		errorHandler(deadLetterChannel("bean:routerErrorHandler?method=handlerActivemqConsumeRouter"));
		from(RouteUtil.invokeNormalQueueEndpointConsume).routeId(Constant.RouteId.CONSUME_ACTIVEMQ_NORMAL).dynamicRouter(method(DynamicRouter.class, "selectRoute"));
		//from(RouteUtil.invokeHighQueueEndpointConsume);
	}

}
