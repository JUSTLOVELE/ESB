package com.esb.service.impl;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.component.cxf.CxfEndpointUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.Bus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esb.core.BaseRes;
import com.esb.service.EsbWsService;
import com.esb.util.CMDJavaUtil;
import com.esb.util.Constant;
import com.esb.util.FileUtil;

/**
 * @Description:cxf webservice
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-6
 * @version 1.00.00
 * @history:
 */
@Service
public class EsbWsServiceImpl extends BaseRes implements EsbWsService {
	
	private final static Log _logger = LogFactory.getLog(EsbWsServiceImpl.class);
	
	@Autowired
	private CamelContext _camelContext;

	/**
	 * wsimport [options] <WSDL_URI>
		比较常用的[options]有：
		1. -d <directory>
		   在指定的目录生成class文件
		2. -clientjar <jarfile>
		   在当前目录生成jar文件，结合-d <directory>可以在指定的目录生成jar文件
		3. -s <directory>
		   在指定的目录生成java源文件
		4. -p <pkg>
		   指定生成文件的包结构
		5. -keep
		   在生成class文件，或者jar包时，同时保留java源文件
		  -----------------------------------------
		 设置cxfEndpoint的bus，如果没有设置，则cxfEndpoint会自己生成bus，这个bus是spring内置的，
	与cxf:webServicePublisher节点为同一个bus,后续在移除router 或者 CxfEndpoint时会将这个bus shutdown掉，
	 会导致设置为 此bus的其它cxfendpoint都不可用。（参见CxfEndpoint源代码doStop()部分）
	 */
	@Override
	public CxfEndpoint createCxfEndpoinf(String wsdlAddress, String siteCode, String serviceCode) {

		try {
			//获取xml文件并解析,主要是为了得到其端口等信息
			String xmlFileName = getCreateXMLFileName(siteCode, serviceCode);
			String xmlPath = getSiteCompilePath(siteCode, serviceCode);
			List<String> portAndNamespace = FileUtil.saveWsdl(wsdlAddress, xmlPath, xmlFileName);
			String serviceName = portAndNamespace.get(0);
			String packageName = getPackageName(siteCode, serviceCode);
			//根据wsdl生成客户端类,并反射
			String cmd = "wsimport -p " + packageName + " -d " + Constant.COMPILE_PATH + " -s " + Constant.COMPILE_PATH + " " + wsdlAddress;
			CMDJavaUtil.execCMD(cmd);
			Class<?> c = Class.forName(packageName + "." + serviceName);
			Method getPortObjMethod = c.getMethod("get"+upperCaseFirst(portAndNamespace.get(1)).replace("_", ""));
			Object serviceClassObj = getPortObjMethod.invoke(c.newInstance());
			Class<?> serviceClass = serviceClassObj.getClass().getInterfaces()[0];
			_logger.info("------parse serviceClass:"+serviceClass); 
			//根据反射的class生成endpoint
			int index = wsdlAddress.lastIndexOf("?");
			String serviceURL = wsdlAddress.substring(0, index);//去掉wsdl
			String endpointURI = getEndpointURI(serviceURL, serviceClass.getName());
			boolean contains = isExistEndpoint(endpointURI);
			CxfEndpoint cxfEndpoint = (CxfEndpoint) _camelContext.getEndpoint(endpointURI);
			
			if(!contains){
				
				Bus cxfBus = CxfEndpointUtils.createBus(_camelContext);
				cxfEndpoint.setBus(cxfBus);
			}
			
			return cxfEndpoint;
			
		} catch (Exception e) {
			_logger.error("", e);
		}
		
		return null;
	}
	
	public String upperCaseFirst(String s){
		return s.substring(0, 1).toUpperCase()+s.substring(1);
	}
	
	public String getEndpointURI(String serviceURL, String className){
		
		String endpointURI = "cxf://" + serviceURL + "?serviceClass=" + className;
								//+"&synchronous=true";//ws方式为同步调用
				//+"&bus=#cxfBus";//多个endpoint为同一个bus，如果endpoint在移除时没有销毁，则下回生成此wsdl描述的endpoint时还是用的上一次的endpoint
		return endpointURI;
	}
}
