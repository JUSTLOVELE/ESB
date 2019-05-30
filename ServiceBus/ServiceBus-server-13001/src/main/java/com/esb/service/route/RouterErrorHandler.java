package com.esb.service.route;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-16
 * @version 1.00.00
 * @history:
 */
@Component("routerErrorHandler")
public class RouterErrorHandler {

	private Log _logger = LogFactory.getLog(RouterErrorHandler.class);
	
	public void handlerWS(Exchange change) {
		
		_logger.info("handlerWS");
	}
	
	public void handlerHttp(Exchange change) {
		
		_logger.info("handlerHttp");
	}
}
