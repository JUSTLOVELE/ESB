package com.esb.route.bean;


import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esb.entity.EsbExceptionEntity;
import com.esb.service.ESBExceptionService;
import com.esb.util.Constant;
import com.esb.util.JSONUtil;
import com.esb.util.UUIDUtil;
import com.esb.util.XMLUtil;



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
	
	@Autowired
	private ESBExceptionService _esbExceptionService;
	
	/***
	 * 保存異常信息
	 */
	private void saveExceptionMsg(String routeId, String key, String uri, String errorMsg, Exchange exchange) {
		
		//Date date = new Date(new java.util.Date().getTime());
		Map<String, Object> heads = exchange.getIn().getHeaders();
		EsbExceptionEntity entity = new EsbExceptionEntity(UUIDUtil.getUUID(), routeId, key, uri, errorMsg);
		String siteCode = null;
		String serviceCode = null;
		
		if(heads.containsKey(Constant.Key.SITE_CODE)) {
			siteCode = (String) heads.get(Constant.Key.SITE_CODE);
		}
		
		if(heads.containsKey(Constant.Key.SERVICE_CODE)) {
			serviceCode = heads.get(Constant.Key.SERVICE_CODE).toString();
		}
		
		entity.setSiteCode(siteCode);
		entity.setServiceCode(serviceCode);
		_esbExceptionService.saveESBExceptionEntity(entity);
		String data = "失敗路由id:" + entity.getOpId() + ";失敗消息:" + errorMsg;
		
		if(routeId.contains(Constant.Key.JSON)) {
			data = JSONUtil.errorReturn(data);
		}else if(routeId.contains(Constant.Key.XML)){
			data = XMLUtil.errorReturn(data);
		}else {
			//默认xml
			data = XMLUtil.errorReturn(data);
			_logger.info("未知routeId类型");
		}
		
		exchange.getOut().setBody(data);
	}
	
	/**
	 * 根据站点生成路由报错(http资源)
	 * @param exchange
	 */
	public void handlerHttpSiteRoute(Exchange exchange) {
		
		Exception exce = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,Exception.class);
		_logger.info("FromrouteId = " + exchange.getFromRouteId());
		_logger.info("endpointKey = " + exchange.getFromEndpoint().getEndpointKey());//请求的key,例如:http://0.0.0.0:13002/ESB/invokeAction/invokeWithJson
		_logger.info("endpointuri = " + exchange.getFromEndpoint().getEndpointUri());//例如:http://0.0.0.0:13002/ESB/invokeAction/invokeWithJson
		_logger.info("---RouterErrorHandler.handlerHttp,"+exce.getMessage(),exce);
		Message out = exchange.getOut();
		Map<String, Object> outheads = out.getHeaders();
		outheads.put(Constant.HeadParam.ESB_COUNT_ROUTE_PRIORITY, Constant.HeadParam.END_QUEUE);
		outheads.put(Constant.HeadParam.ESB_IS_INVOKE, Boolean.valueOf(true));
		saveExceptionMsg(exchange.getFromRouteId(), exchange.getFromEndpoint().getEndpointKey(), exchange.getFromEndpoint().getEndpointUri(), exce.getMessage(), exchange);
	
	}
	
	/**
	 * ActivemqConsumeRouter报错
	 * @param exchange
	 */
	public void handlerActivemqConsumeRouter(Exchange exchange) {
		
		Exception exce = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,Exception.class);
		_logger.info("FromrouteId = " + exchange.getFromRouteId());
		_logger.info("endpointKey = " + exchange.getFromEndpoint().getEndpointKey());//请求的key,例如:http://0.0.0.0:13002/ESB/invokeAction/invokeWithJson
		_logger.info("endpointuri = " + exchange.getFromEndpoint().getEndpointUri());//例如:http://0.0.0.0:13002/ESB/invokeAction/invokeWithJson
		_logger.info("---RouterErrorHandler.handlerHttp,"+exce.getMessage(),exce);
		Message out = exchange.getOut();
		Map<String, Object> outheads = out.getHeaders();
		outheads.put(Constant.HeadParam.ESB_COUNT_ROUTE_PRIORITY, Constant.HeadParam.END_QUEUE);
		outheads.put(Constant.HeadParam.ESB_IS_INVOKE, Boolean.valueOf(true));
		saveExceptionMsg(exchange.getFromRouteId(), exchange.getFromEndpoint().getEndpointKey(), exchange.getFromEndpoint().getEndpointUri(), exce.getMessage(), exchange);
	}
	
	/**
	 * HttpPublisherRouter报错
	 * @param exchange
	 */
	public void handlerHttpPublisherRouter(Exchange exchange) {
		
		Exception exce = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,Exception.class);
		_logger.info("FromrouteId = " + exchange.getFromRouteId());
		_logger.info("endpointKey = " + exchange.getFromEndpoint().getEndpointKey());//请求的key,例如:http://0.0.0.0:13002/ESB/invokeAction/invokeWithJson
		_logger.info("endpointuri = " + exchange.getFromEndpoint().getEndpointUri());//例如:http://0.0.0.0:13002/ESB/invokeAction/invokeWithJson
		_logger.info("---RouterErrorHandler.handlerHttp,"+exce.getMessage(),exce);
		saveExceptionMsg(exchange.getFromRouteId(), exchange.getFromEndpoint().getEndpointKey(), exchange.getFromEndpoint().getEndpointUri(), exce.getMessage(), exchange);
	}
}
