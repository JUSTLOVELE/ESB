package com.esb.service.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esb.entity.EsbUserEntity;
import com.esb.service.UserService;
import com.esb.sys.InvokeDataType;
import com.esb.util.Constant;
import com.esb.util.XMLUtil;
import com.esb.util.encrypt.RSA;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-31
 * @version 1.00.00
 * @history:
 */
@Component
public class StartHttpProcessor implements Processor{
	
	private final static Log _logger = LogFactory.getLog(StartHttpProcessor.class);
	
	@Autowired
	private UserService _userService;
	
	public StartHttpProcessor() {
		
	}
	
	/**
	 * 处理xml数据,并将对应的数据保存到head参数中
	 * @param data
	 * @param head
	 */
	private void handlerXMLData(String xml, Map<String, Object> head) {
		
		Element root = XMLUtil.getRootElement(xml);
		Element siteCodeElement = root.getChild(Constant.Key.SITE_CODE);
		Element serviceCodeElement = root.getChild(Constant.Key.SERVICE_CODE);
		Element offlineElement = root.getChild(Constant.Key.OFFLINE);
		
		if(siteCodeElement == null || siteCodeElement.getValue() == null || "".equals(siteCodeElement.getValue())) {
			throw new RuntimeCamelException("orgCode为必填项");
		}
		
		if(serviceCodeElement == null || serviceCodeElement.getValue() == null || "".equals(serviceCodeElement.getValue())) {
			throw new RuntimeCamelException("serviceCode为必填项");
		}
		
		if(offlineElement == null || offlineElement.getValue() == null || "".equals(offlineElement.getValue())) {
			throw new RuntimeCamelException("serviceCode为必填项");
		}
		
		head.put(Constant.HeadParam.ESB_SITE_CODE, siteCodeElement.getValue());
		head.put(Constant.HeadParam.ESB_SERVICE_CODE, serviceCodeElement.getValue());
		head.put(Constant.HeadParam.ESB_OFFLINE, Integer.valueOf(offlineElement.getValue()));
		head.put(Constant.HeadParam.ESB_INVOKE_DATA_TYPE, InvokeDataType.XML.getValue());
	}
	
	/***
	 * 处理JSON数据,并将对应的数据保存到head参数中
	 * @param data
	 * @param head
	 */
	private void handlerJSONData(String data, Map<String, Object> head) {

		JSONObject json = JSONObject.fromObject(data);
		
		if(!json.containsKey(Constant.Key.SITE_CODE)) {
			throw new RuntimeCamelException("siteCode为必填项");
		}
		
		if(json.getString(Constant.Key.SITE_CODE) == null || "".equals(json.getString(Constant.Key.SITE_CODE))) {
			throw new RuntimeCamelException("siteCode为不能为空");
		} 
		
		if(!json.containsKey(Constant.Key.SERVICE_CODE)) {
			throw new RuntimeCamelException("serviceCode为必填项");
		}
		
		if(json.getString(Constant.Key.SERVICE_CODE) == null || "".equals(json.getString(Constant.Key.SERVICE_CODE))) {
			throw new RuntimeCamelException("serviceCode为不能为空");
		}
		
		if(!json.containsKey(Constant.Key.OFFLINE)) {
			throw new RuntimeCamelException("offline为不能为空");
		}
		
		head.put(Constant.HeadParam.ESB_SITE_CODE, json.getString(Constant.Key.SITE_CODE));
		head.put(Constant.HeadParam.ESB_SERVICE_CODE, json.getString(Constant.Key.SERVICE_CODE));
		head.put(Constant.HeadParam.ESB_OFFLINE, json.getString(Constant.Key.OFFLINE));
		head.put(Constant.HeadParam.ESB_INVOKE_DATA_TYPE, InvokeDataType.JSON.getValue());
	}

	@Override
	public void process(Exchange exchange) throws Exception {

		//验证用户
		Message in = exchange.getIn();
		Map<String, Object> head = in.getHeaders();
		String token = (String) head.get(Constant.HeadParam.AUTHORIZATION);
		token = RSA.decryptByPrivate(token, Constant.Constant_PRIVATE_KEY);
		
		if(token == null || "".equals(token)) {
			throw new RuntimeCamelException("token错误");
		}
		
		String[] array = token.split(Constant.SPLIT_SIGN);
		
		if(array == null || array.length != 2) {
			throw new RuntimeCamelException("密码格式错误导致token解析失败");
		}
		
		EsbUserEntity user = _userService.userLogin(array[0], array[1]);
		
		if(user == null) {
			throw new RuntimeCamelException("登录失败");
		}
		
		//-----------------------------
		//Map<String, Object> esbHeadInvoke = new HashMap<String, Object>();
		//因为没有做用户的等级所有固定放1,意味着只走普通队列
		head.put(Constant.HeadParam.ESB_COUNT_ROUTE_PRIORITY, user.getUserLevel());
		head.put(Constant.HeadParam.ESB_IS_INVOKE, Boolean.valueOf(false));
		head.put(Constant.HeadParam.ESB_USER_EMAIL, user.getUserEmail());
		head.put(Constant.HeadParam.ESB_USER_ID, user.getUserId());
		head.put(Constant.HeadParam.ESB_USER_OP_ID, user.getOpId());
		head.put(Constant.HeadParam.ESB_USER_PHONE, user.getUserPhone());
		
		String method = head.get(Exchange.HTTP_METHOD).toString();
		String data = null;
		
		if(Constant.Key.POST.equals(method)) {
			 data = exchange.getIn().getBody(String.class);
		}else if(Constant.Key.GET.equals(method)) {
			 String queryString = head.get(Exchange.HTTP_QUERY).toString();
			 Map<String, String> map = Stream.of(queryString.split("&")).map(obj -> obj.split("=")).collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
			 data = map.get(Constant.Key.PARAM);
			 
		}else {
			throw new RuntimeCamelException("method既不是post也不是get");
		}
		
		_logger.info(("data = " + data));
		String routeId = exchange.getFromRouteId();
		
		if(routeId.contains(Constant.Key.JSON)) {
			handlerJSONData(data, head);
		}else if(routeId.contains(Constant.Key.XML)){
			handlerXMLData(data, head);
		}else {
			throw new RuntimeCamelException("未知路由ID:" + routeId);
		}
	}
}
