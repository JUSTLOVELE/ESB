package com.esb;

import org.apache.camel.CamelContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.esb.service.route.HttpPublisherRouter;

/**
 * @Description:项目启动后执行
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-24
 * @version 1.00.00
 * @history:
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	
	private static final Log _logger = LogFactory.getLog(ApplicationStartup.class);

	@Autowired
	private CamelContext _camelContext;
	
	@Autowired
	private HttpPublisherRouter _httpPublisherRouter;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		startService();		
	}
	
	private  void startService() {
		
		try {
			_camelContext.addRoutes(_httpPublisherRouter);
		} catch (Exception e) {
			_logger.error("", e);
		}
	}

}
