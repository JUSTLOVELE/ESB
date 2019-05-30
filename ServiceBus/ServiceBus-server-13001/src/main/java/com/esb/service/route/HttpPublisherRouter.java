package com.esb.service.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.esb.view.api.InvokeAction;

/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-16
 * @version 1.00.00
 * @history:
 */
@Component
public class HttpPublisherRouter extends RouteBuilder {

	private Log _logger = LogFactory.getLog(HttpPublisherRouter.class);
	
/*	@Resource
	private Endpoint invokeNormalQueueEndpointConsume;*/
	
	/**
	 * 
	 *  from(netty4-http:http://localhost:13002/ESB/invokeAction/invokeWithJson):这样能监听到但是端口会被占用
	 *  from(jetty:http://localhost:8080/myapp/myservice):这样能监听到但是端口会被占用
	 *  from(servlet:myservlet)
        from("direct:getContact").to("https://free-api.heweather.com/s6/weather/forecast?parameters&&bridgeEndpoint=true");
        from("activemq:esb.normal.invokeNormalQueue?concurrentConsumers=5&maxConcurrentConsumers=5")
	 * 
	 */
	@Override
	public void configure() throws Exception {
		
		_logger.info("----HttpPublisherRouter----");
		
		errorHandler(deadLetterChannel("bean:routerErrorHandler?method=handlerHttp"));
		from(RouteUtil.invokeNormalQueueEndpointConsume).process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				_logger.info("direct:invokeWithJson");
				String queryString = exchange.getIn().getHeader(Exchange.HTTP_QUERY, String.class);
				String data = exchange.getIn().getBody(String.class);
				System.out.println("data = " + data);
				System.out.println("queryString = " + queryString);
				System.out.println("out = " + exchange.getOut().toString());
				exchange.getOut().setBody(data);
			}
		}).bean(InvokeAction.class, "HelloWolrd")
		
		//to("bean:invokeAction?method=HelloWolrd")
		
		.end();
		
		
		/*_logger.info("----default cxf SpringBus:"+webServicePublisher.getBus());
		
		errorHandler(deadLetterChannel("bean:routerErrorHandler?method=handlerWS"));
		from("cxf:bean:webServicePublisher").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				_logger.info("WebServicePublisherRouter..process..");
			}
		}).to("bean:loginService?method=invokeLogin")
		
		;*/
	}
}
