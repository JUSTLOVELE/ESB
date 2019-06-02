package com.esb.service.route;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Message;
import org.apache.camel.RuntimeCamelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esb.entity.EsbExceptionEntity;
import com.esb.service.ESBExceptionService;
import com.esb.util.Constant;
import com.esb.util.JSONUtil;
import com.esb.util.UUIDUtil;


/**
 * @Description:动态路由
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-31
 * @version 1.00.00
 * @history:
 */
@Component
public class DynamicRouter {
	
	private final static Log _logger = LogFactory.getLog(DynamicRouter.class);
	
	@Autowired
	private ESBExceptionService _esbExceptionService;
	
	private void saveErrorMsg(Exchange exchange, String errorMsg) {
		
		String routeId = exchange.getFromRouteId();
		String key = exchange.getFromEndpoint().getEndpointKey();
		String uri = exchange.getFromEndpoint().getEndpointUri();
		Map<String, Object> heads = exchange.getIn().getHeaders();
		String siteCode = heads.get(Constant.Key.SITE_CODE).toString();
		String serviceCode = heads.get(Constant.Key.SERVICE_CODE).toString();
		EsbExceptionEntity entity = new EsbExceptionEntity(UUIDUtil.getUUID(), routeId, key, uri, null, errorMsg, siteCode, serviceCode);
		_esbExceptionService.saveESBExceptionEntity(entity);

	}
	
	public String selectRoute(Exchange exchange) {
		
		Message in = exchange.getIn();
		Map<String, Object> heads = in.getHeaders();
		Map<String, Object> esbHeadInvoke = (Map<String, Object>) heads.get(Constant.Key.ESB_HEAD_INVOKE);
		
		if(esbHeadInvoke.containsKey(Constant.Key.COUNT_SELECT_SITE)) {
			
			Integer count = (Integer) esbHeadInvoke.get(Constant.Key.COUNT_SELECT_SITE);
			
			if(count > Integer.valueOf(Constant.getConst(Constant.Key.COUNT_SELECT_SITE))) {
				//超过重连次数
				String errorMsg = "找不到路由对象,重连次数超过10次";
				saveErrorMsg(exchange, errorMsg);
				exchange.getOut().setBody(JSONUtil.errorReturn(errorMsg));
				return null;
			}
			
			esbHeadInvoke.put(Constant.Key.COUNT_SELECT_SITE, count++);
		}else {
			esbHeadInvoke.put(Constant.Key.COUNT_SELECT_SITE, 1);
		}
		
		Boolean isInvoke = (Boolean) esbHeadInvoke.get(Constant.HeadParam.IS_INVOKE);
		
		if(!isInvoke) {
			
			String route = "direct:esb_" + esbHeadInvoke.get(Constant.Key.SITE_CODE) + "_" + esbHeadInvoke.get(Constant.Key.SERVICE_CODE);
			_logger.info("selectRoute = " + route);
			return route;
		}else {
			_logger.info("selectRoute is end");
			return null;
		}
	}

	/**
	 * 选择优先级路由
	 * 	根据当前登录用户的等级来进行修正
	 * @param exchange
	 * @return
	 */
	//通过@Header(SystemConstant.HEADER_NAME_INVOKE_PRIORITY)Integer invokePriority 可以访问头参数的数据
	public String routeByPriority(Exchange exchange, @Header(Constant.HeadParam.INVOKEPRIORITY) Integer invokePriority){
		
		Map<String, Object> heads = exchange.getIn().getHeaders();
		Map<String, Object> esbHeadInvoke = (Map<String, Object>) heads.get(Constant.Key.ESB_HEAD_INVOKE);
		
		if(esbHeadInvoke.containsKey(Constant.Key.COUNT_INVOKEPRIORITY)) {
			
			Integer count = (Integer) esbHeadInvoke.get(Constant.Key.COUNT_INVOKEPRIORITY);
			
			if(count > Integer.valueOf(Constant.getConst(Constant.Key.COUNT_INVOKEPRIORITY))) {
				//超过重连次数
				String errorMsg = "连接不到生产者消息队列超过10次";
				saveErrorMsg(exchange, errorMsg);
				exchange.getOut().setBody(JSONUtil.errorReturn(errorMsg));
				return null;
			}
			
			esbHeadInvoke.put(Constant.Key.COUNT_INVOKEPRIORITY, count++);
		}else {
			esbHeadInvoke.put(Constant.Key.COUNT_INVOKEPRIORITY, 1);
		}
		
		if(invokePriority==null) {
			throw new RuntimeCamelException("invokePriority未知=" + invokePriority);
		}else if(invokePriority > 5) {
			return RouteUtil.Direct.DIRECT_PRODUCEHIGH;
		}else if(invokePriority<5 && invokePriority>0) {
			return RouteUtil.Direct.DIRECT_PRODUCENORMAL;
		}else if(invokePriority == Constant.HeadParam.END_QUEUE) {
			return null;
		}else {
			throw new RuntimeCamelException("invokePriority未知=" + invokePriority);
		}
	}
}
