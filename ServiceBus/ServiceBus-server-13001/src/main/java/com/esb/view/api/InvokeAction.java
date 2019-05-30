package com.esb.view.api;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.esb.core.Base;
import com.esb.service.impl.InvokeService;

/**
 * @Description:调用类
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-16
 * @version 1.00.00
 * @history:
 */
@RequestMapping("/invokeAction")
@Component
public class InvokeAction extends Base{
	
	@Autowired
	private InvokeService _invokeService;
	
	private final static Log _logger = LogFactory.getLog(InvokeAction.class);
	
	@RequestMapping("/invokeWithJson")
	@ResponseBody
	public String invokeWithJson(String param) {
		return _invokeService.invokeWithJson(param);
	}
	
	@RequestMapping("/invokeWithXML")
	@ResponseBody
	public String invokeWithXML(String param) {
		return _invokeService.invokeWithXML(param);
	}
	
	@RequestMapping(value="/registerWithJson", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String registerWithJson(String param) {
		_logger.info(param);
		return _invokeService.registerWithJson(param);
	}
	
	@RequestMapping("/registerWithXML")
	@ResponseBody
	public String registerWithXML(String param) {
		return _invokeService.registerWithXML(param);
	}
	
	public void HelloWolrd(Exchange ex) {
		String data = ex.getIn().getBody(String.class);
		System.out.println("helloworld:" + data);
	}
}
