package com.esb.service.impl;


import java.io.ByteArrayOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esb.core.Base;
import com.esb.entity.EsbUserEntity;
import com.esb.service.EsbRouteService;
import com.esb.service.route.RouteUtil;
import com.esb.sys.InvokeType;
import com.esb.util.Constant;
import com.esb.util.XMLUtil;

import net.sf.json.JSONObject;

/**
 * @Description:调用类
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-16
 * @version 1.00.00
 * @history:
 */
@Service
public class InvokeService extends Base{
	
	@Autowired
	private ZookeeperServiceImpl _zookeeperService;
	
	@Autowired
	private EsbRouteService _esbRouteService;
	
	private final static Log _logger = LogFactory.getLog(InvokeService.class);
	
	/**
	 * 删除ESB服务
	 * @param param
	 * @param user
	 * @return
	 */
	public String removeESBService(String param, EsbUserEntity user) {
		
		JSONObject json = JSONObject.fromObject(param);
		
		if(!json.containsKey(Constant.Key.SITE_CODE)) {
			return returnErrorCode("siteCode为必填项目");
		}
		
		if(!json.containsKey(Constant.Key.SERVICE_CODE)) {
			return returnErrorCode("serviceCode为必填项目");
		}
		
		String userOpId = user.getOpId();
		String siteCode = json.getString(Constant.Key.SITE_CODE);
		String serviceCode = json.getString(Constant.Key.SERVICE_CODE);
		if(_esbRouteService.queryisExistRoute(userOpId, siteCode, serviceCode)) {
			
			String path = RouteUtil.getServicePath(siteCode, serviceCode);
			_zookeeperService.deleteNode(path);
			return returnGeneralJSONCode(Constant.Status.SUCCESS_CODE, "删除成功", true);
		}else {
			return returnErrorCode("无权删除该服务");
		}
	}
	

	/**
	 * 
	 *     {
			"orgCode":"机构编码",
			"serviceCode":"服务号",
			"url":"资源地址",
			"type":1,//1:http资源;2:webservice资源
			"param":[{"key":"","type":1},{}...{}]//1:文本;2:file
			}
	 * 
	 * @param param
	 * @return
	 */
	public String registerWithJson(String param, EsbUserEntity user) {
		
		JSONObject json = JSONObject.fromObject(param);
		
		if(!json.containsKey(Constant.Key.SITE_CODE)) {
			return returnErrorCode("siteCode为必填项目");
		}
		
		if(!json.containsKey(Constant.Key.SERVICE_CODE)) {
			return returnErrorCode("serviceCode为必填项目");
		}
		
		if(!json.containsKey(Constant.Key.TYPE)) {
			return returnErrorCode("type为必填");
		}
		
		int type = json.getInt(Constant.Key.TYPE);
		
		if(type == InvokeType.SOAP.getValue()) {
			
			if(!json.containsKey(Constant.Key.SOAP)) {
				return returnErrorCode("type=3,soap为必填项目");
			}
		}
		
		String siteCode = json.getString(Constant.Key.SITE_CODE);
		String serviceCode = json.getString(Constant.Key.SERVICE_CODE);
		param = XMLUtil.parseJSONToRegisterXMLInfo(json, user);
		String sitePath = RouteUtil.getSitePath(siteCode);
		
		if(!_zookeeperService.checkExists(sitePath)) {
			
			_zookeeperService.createNode(sitePath, CreateMode.PERSISTENT, null);
			_zookeeperService.registerPathChildListener(sitePath);
		}
		
		String path = RouteUtil.getServicePath(siteCode, serviceCode);
		
		if(!_zookeeperService.checkExists(path)) {
			_zookeeperService.createNode(path, CreateMode.PERSISTENT, param);
		}else {
			_zookeeperService.updateNodeDate(path, param);
		}
		
		return returnGeneralJSONCode(Constant.Status.SUCCESS_CODE, Constant.SUCCESS_SAVE, true);
	}
	
	public String registerWithXML(String param, EsbUserEntity user) {
		
		Element rootElement = XMLUtil.getRootElement(param);
		Element siteCodeElement = rootElement.getChild(Constant.Key.SITE_CODE);
		Element serviceCodeElement = rootElement.getChild(Constant.Key.SERVICE_CODE);
		Element urlElement = rootElement.getChild(Constant.Key.URL);
		Element typeCodeElement = rootElement.getChild(Constant.Key.TYPE);
		
		if(siteCodeElement == null || siteCodeElement.getValue() == null || "".equals(siteCodeElement.getValue())) {
			return returnErrorCode("siteCode为必填");
		}
		
		if(serviceCodeElement == null || serviceCodeElement.getValue() == null || "".equals(serviceCodeElement.getValue())) {
			return returnErrorCode("serviceCode为必填");
		}
		
		if(urlElement == null || urlElement.getValue() == null || "".equals(urlElement.getValue())) {
			return returnErrorCode("url为必填");
		}
		
		if(typeCodeElement == null || typeCodeElement.getValue() == null || "".equals(typeCodeElement.getValue())) {
			return returnErrorCode("type为必填");
		}
		
		int type = Integer.valueOf(typeCodeElement.getValue());
		Element soapElement = null;
		
		if(type == InvokeType.SOAP.getValue()) {
			
			soapElement = rootElement.getChild(Constant.Key.SOAP);
			
			if(soapElement == null || soapElement.getValue() == null || "".equals(soapElement.getValue())) {
				return returnErrorCode("type=3时,soap必填");
			}
		}
		
		Element registerTypeElement = new Element("registerType");
		registerTypeElement.setText("2");
		Element createUserOpIdElement = new Element("createUserOpId");
		createUserOpIdElement.setText(user.getOpId());
		rootElement.addContent(registerTypeElement);
		rootElement.addContent(createUserOpIdElement);
		
		String siteCode = siteCodeElement.getValue();
		String serviceCode = serviceCodeElement.getValue();
		String sitePath = RouteUtil.getSitePath(siteCode);
		
		if(!_zookeeperService.checkExists(sitePath)) {
			
			_zookeeperService.createNode(sitePath, CreateMode.PERSISTENT, null);
			_zookeeperService.registerPathChildListener(sitePath);
		}
		
		String path = RouteUtil.getServicePath(siteCode, serviceCode);
		Format format = Format.getCompactFormat();
	    format.setEncoding("UTF-8");
		XMLOutputter xmlout = new XMLOutputter(format);
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		String xml = null;
		
		try {
			xmlout.output(rootElement, bo);
			xml = bo.toString().trim();
		} catch (Exception e) {
			_logger.error("", e);
		}
		
		if(xml == null) {
			return returnErrorCode("xml转换失败");
		}
		
		if(!_zookeeperService.checkExists(path)) {
			_zookeeperService.createNode(path, CreateMode.PERSISTENT, xml);
		}else {
			_zookeeperService.updateNodeDate(path, rootElement.toString());
		}
		
		return returnGeneralJSONCode(Constant.Status.SUCCESS_CODE, Constant.SUCCESS_SAVE, true);
	}
}
