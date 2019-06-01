package com.esb.service.impl;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esb.service.InitRouteInfoService;
import com.esb.util.Constant;

/**
 * @Description:初始化路由
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-1
 * @version 1.00.00
 * @history:
 */
@Service
public class InitRouteInfoServiceImpl implements InitRouteInfoService {
	
	@Autowired
	private CamelContext _camelContext;
	
	private final static Log _logger = LogFactory.getLog(InitRouteInfoServiceImpl.class);

	@Override
	public boolean initRouteWithZK(String direct, String endpoint) {
		
		try {
			_camelContext.addRoutes(new RouteBuilder() {
				
				@Override
				public void configure() throws Exception {
					
					errorHandler(deadLetterChannel("bean:routerErrorHandler?method=handlerHttp"));
					from("direct:esb_350000_checkVersion").routeId("test").process(new Processor() {
						
						@Override
						public void process(Exchange exchange) throws Exception {
							_logger.info("----route = " + direct + "--------------ß");
						}
					}).to(ExchangePattern.InOut, "http4://www.fjjkkj.com/HY-GS/mobileSystemAction/api/checkVersion?source=1&bridgeEndpoint=true&throwExceptionOnFailure=false")
					.process(new Processor() {
						
						@Override
						public void process(Exchange exchange) throws Exception {
							
							Message in = exchange.getIn();
							//是否被调用过
							in.getHeader(Constant.HeadParam.IS_INVOKE, Boolean.valueOf(true));
							String data = exchange.getIn().getBody(String.class);
							_logger.info("data = " + data);
							String outData = exchange.getOut().getBody(String.class);
							Message out = exchange.getOut();
							out.getHeaders().put(Constant.HeadParam.INVOKEPRIORITY, Constant.HeadParam.END_QUEUE);
							out.getHeaders().put(Constant.HeadParam.IS_INVOKE, Boolean.valueOf(true));
							_logger.info("outData = " + outData);
							exchange.getOut().setBody(data);
							
						}
					}).end()
					
					;
				}
			});
			
			System.out.println("cacacaa");
			Route r = _camelContext.getRoute("test");
			System.out.println(r.toString());
		} catch (Exception e) {
			_logger.error("", e);
		}
		
		return false;
	}

}
