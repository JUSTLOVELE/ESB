	package com.esb.service.route;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esb.service.process.HttpProcessor;

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
	
	@Autowired
	private HttpProcessor _httpProcessor;
	
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
		//from(RouteUtil.HTTP_INVOKE_JSON_ADDRESS).process(new HttpProcessor()).bean(InvokeAction.class, "HelloWolrd").end();
		
		from(RouteUtil.HTTP_INVOKE_JSON_ADDRESS).process(_httpProcessor).dynamicRouter(method(DynamicRouter.class, "routeByPriority"));
		from(RouteUtil.Direct.DIRECT_PRODUCENORMAL).to(ExchangePattern.InOut, RouteUtil.invokeNormalEndpointProduce);
		from(RouteUtil.Direct.DIRECT_PRODUCEHIGH).to(ExchangePattern.InOut, RouteUtil.invokeNormalEndpointProduce);
		//to(ExchangePattern.InOut, RouteUtil.invokeNormalEndpointProduce);
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
