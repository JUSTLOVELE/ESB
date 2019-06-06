package com.esb.core;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.util.URISupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.esb.util.Constant;

/**
 * @Description:基础资源(http,webservice)
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-16
 * @version 1.00.00
 * @history:
 */
public abstract class BaseRes {
	
	private final static Log _logger = LogFactory.getLog(BaseRes.class);
	
	@Autowired
	private CamelContext _camelContext;
	
	/**
	 * 判断是否存在endpoint
	 * @param endpointURI
	 * @return
	 */
	public boolean isExistEndpoint(String endpointURI) {
		
		Map<String,Endpoint> endpointsMap = _camelContext.getEndpointMap();
		String endpointKey = null;
		
		try {
			
			String resolveURI = _camelContext.resolvePropertyPlaceholders(endpointURI);
			endpointKey = URISupport.normalizeUri(resolveURI);
		} catch ( Exception e) {
			
			_logger.info("未知 endpointURI :"+endpointURI);
			//logger.info(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		
		return endpointsMap.containsKey(endpointKey);
	}
	
	/**
	 * 获取site所在的编译路径
	 * @param siteCode
	 * @param serviceCode
	 * @return
	 */
	public String getSiteCompilePath(String siteCode, String serviceCode) {
		return Constant.COMPILE_PATH + "/" + Constant.COMPILE_BASE_PACKAGENAME_PATH + "/site_" + siteCode + "_" + serviceCode;
	}
	
	/**
	 * 获取编译包名
	 * @param siteCode
	 * @param serviceCode
	 * @return
	 */
	public String getPackageName(String siteCode, String serviceCode) {
		return Constant.COMPILE_BASE_PACKAGENAME + ".site_" + siteCode + "_" + serviceCode;
	}
	
	/**
	 * 获取WSDL的文件
	 * @param siteCode
	 * @param serviceCode
	 * @return
	 */
	public String getCreateXMLFileName(String siteCode, String serviceCode) {
		
		return siteCode + "_" + serviceCode + ".xml";
	}
}
