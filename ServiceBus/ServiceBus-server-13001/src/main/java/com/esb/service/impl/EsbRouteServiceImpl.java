package com.esb.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.util.URISupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esb.core.BaseRes;
import com.esb.dao.EsbRouteDao;
import com.esb.entity.EsbRouteEntity;
import com.esb.service.EsbRouteService;
import com.esb.sys.InvokeType;
import com.esb.util.Constant;
import com.esb.util.FileUtil;
/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-6
 * @version 1.00.00
 * @history:
 */
@Service
@Transactional
public class EsbRouteServiceImpl extends BaseRes implements EsbRouteService {

	@Autowired
	private EsbRouteDao _esbRouteDao;
	
	@Autowired
	private CamelContext _camelContext;
	
	private final static Log _logger = LogFactory.getLog(EsbRouteServiceImpl.class);
	
	@Override
	public void saveEsbRouteEntity(EsbRouteEntity e) {
		_esbRouteDao.saveEsbRouteEntity(e);
	}

	@Override
	public boolean queryisExistRoute(String routeId) {
		return _esbRouteDao.queryisExistRoute(routeId);
	}

	@Override
	public void deleteRouteInfo(String routeId) {
		
		removeCamelEndPoint(routeId);
		_esbRouteDao.deleteRouteInfo(routeId);		
	}
	
	public void removeCamelEndPoint(String routeId) {

		List<Map<String, Object>> routes = _esbRouteDao.queryRoute(routeId);
		
		if(routes == null || routes.size() != 1) {
			throw new RuntimeException("根据路由id查询不到路由信息");
		}
		
		String endpointURI = (String) routes.get(0).get(Constant.Key.ENDPOINTURI);
		
		if(endpointURI == null || "".equals(endpointURI)) {
			return;
		}
		
		Integer type = (Integer) routes.get(0).get(Constant.Key.ROUTE_TYPE);
		
		if(type == InvokeType.WEBSERVICE.getValue()) {
			//要删除class
			String siteCode = (String) routes.get(0).get(Constant.Key.SITE_CODE);
			String serviceCode = (String) routes.get(0).get(Constant.Key.SERVICE_CODE);
			String path = getSiteCompilePath(siteCode, serviceCode);
			
			try {
				
				FileUtil.deleteAll(new File(path));
			} catch (Exception e) {
				_logger.error("", e);
			}
		}
		
		
		String endpointKey = null;
		
		try {
			
			String resolveURI = _camelContext.resolvePropertyPlaceholders(endpointURI);
			endpointKey = URISupport.normalizeUri(resolveURI);
		} catch ( Exception e) {
			
			_logger.info("异常endpointURI :"+endpointURI);
			_logger.info(e.getMessage(), e);
		}
		
		boolean contains = _camelContext.getEndpointMap().containsKey(endpointKey);
		
		if(contains){
			
			_logger.info("------remove endpoint :"+endpointURI);
			Endpoint endpoint = _camelContext.getEndpoint(endpointURI);
			
			try {
				endpoint.stop();
				_camelContext.removeEndpoint(endpoint);
			} catch (Exception e) {
				_logger.error("", e);
			}
		}
	}

	@Override
	public boolean queryisExistRoute(String userOpId, String siteCode, String serviceCode) {
		return _esbRouteDao.queryisExistRoute(userOpId, siteCode, serviceCode);
	}

}
