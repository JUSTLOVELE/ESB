package com.esb.service.impl;


import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esb.core.Base;
import com.esb.sys.ZookeeperOperation;
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

	public String invokeWithJson(String param) {
		return null;
	}
	
	public String invokeWithXML(String param) {
		return null;
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
	public String registerWithJson(String param) {
		
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
		
		String siteCode = json.getString(Constant.Key.SITE_CODE);
		String serviceCode = json.getString(Constant.Key.SERVICE_CODE);
		//String url = json.getString(Constant.Key.URL);
		//int type = json.getInt(Constant.Key.TYPE);
		param = XMLUtil.parseJSONToRegisterXMLInfo(json);
		String sitePath = Constant.Key.PATH_ROOT + "/" + siteCode;
		
		if(!_zookeeperService.checkExists(sitePath)) {
			
			_zookeeperService.createNode(sitePath, CreateMode.PERSISTENT, null);
			_zookeeperService.registerPathChildListener(sitePath);
		}
		
		String path = Constant.Key.PATH_ROOT + "/" + siteCode + "/" + serviceCode;
		
		if(!_zookeeperService.checkExists(path)) {
			_zookeeperService.createNode(path, CreateMode.PERSISTENT, param);
		}else {
			_zookeeperService.updateNodeDate(path, param);
		}
		
		return returnGeneralJSONCode(Constant.Status.SUCCESS_CODE, Constant.SUCCESS_SAVE, true);
	}
	
	public String registerWithXML(String param) {
		return null;
	}
}
