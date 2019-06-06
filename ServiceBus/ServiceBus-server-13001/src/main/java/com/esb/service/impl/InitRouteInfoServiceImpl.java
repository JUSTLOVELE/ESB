package com.esb.service.impl;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esb.entity.EsbRouteEntity;
import com.esb.service.EsbRouteService;
import com.esb.service.EsbWsService;
import com.esb.service.InitRouteInfoService;
import com.esb.service.process.EndRouteProcessor;
import com.esb.service.process.LastHttpRouteProcessor;
import com.esb.service.route.RouteUtil;
import com.esb.sys.RegisterType;
import com.esb.util.Constant;
import com.esb.util.UUIDUtil;
import com.esb.util.XMLUtil;

/**
 * @Description:初始化路由
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-1
 * @version 1.00.00
 * @history:
 */
@Service
public class InitRouteInfoServiceImpl implements InitRouteInfoService {
	
	@Autowired
	private CamelContext _camelContext;
	
	@Autowired
	private EndRouteProcessor _endRouteProcessor;
	
	@Autowired
	private LastHttpRouteProcessor _lastHttpRouteProcessor;
	
	@Autowired
	private EsbWsService _esbWsService;
	
	@Autowired
	private EsbRouteServiceImpl _esbRouteService;
	
	private final static Log _logger = LogFactory.getLog(InitRouteInfoServiceImpl.class);
	
	private void saveRoute(String routeId, String endpointUri, String createUserOpId, String siteCode, String serviceCode) {
		
		EsbRouteEntity e = new EsbRouteEntity(UUIDUtil.getUUID(), routeId, endpointUri, createUserOpId, siteCode, serviceCode);
		_esbRouteService.saveEsbRouteEntity(e);
	}
	
	private void addWebService(String route, String routeId, String wsdlAddress, String siteCode, String serviceCode, String createUserOpId) throws Exception {
		
		_logger.info("addWebService = " + routeId);
		
		try {
			_esbRouteService.removeCamelEndPoint(routeId);
		} catch (Exception e) {
			_logger.error("", e);
		}
		
		Endpoint cxfEndpoint = _esbWsService.createCxfEndpoinf(wsdlAddress, siteCode, serviceCode);
		
		if(cxfEndpoint == null) {
			throw new RuntimeException("创建cxf失败");
		}
		
		_camelContext.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				
				errorHandler(deadLetterChannel("bean:routerErrorHandler?method=handlerWSRoute"));
				from(route)
				.process(_lastHttpRouteProcessor)
				.to(ExchangePattern.InOut, cxfEndpoint)
				.process(_endRouteProcessor).routeId(routeId);
			}
		});
		
		saveRoute(routeId, cxfEndpoint.getEndpointUri(), createUserOpId, siteCode, serviceCode);
	}
	
	private void addHttp(String route, String routeId, String url, String siteCode, String serviceCode, String createUserOpId) throws Exception {
		
		_logger.info("addHttpRoute = " + routeId);
		_camelContext.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				
				errorHandler(deadLetterChannel("bean:routerErrorHandler?method=handlerHttpSiteRoute"));
				from(route).routeId(routeId)
				.process(_lastHttpRouteProcessor)
				.to(ExchangePattern.InOut, url + "?bridgeEndpoint=true&throwExceptionOnFailure=false")
				.process(_endRouteProcessor).end();
			}
		});
		
		saveRoute(routeId, url + "?bridgeEndpoint=true&throwExceptionOnFailure=false", createUserOpId, siteCode, serviceCode);
	}
	
	private void addRoute(String route, String routeId, String data, String siteCode, String serviceCode) throws Exception {
		
		Map<String, Object> registerInfo = XMLUtil.getReigsterInfo(data);
		Integer type = (Integer) registerInfo.get(Constant.Key.TYPE);
		String url = (String) registerInfo.get(Constant.Key.URL);
		String createUserOpId = (String) registerInfo.get(Constant.Key.CREATEUSEROPID);
		
		switch (RegisterType.getValue(type)) {
		case HTTP:
			addHttp(route, routeId, url, siteCode, serviceCode, createUserOpId);
			break;
			
		case WEBSERVICE:
			addWebService(route, routeId, url, siteCode, serviceCode, createUserOpId);
			break;
			
		case SOAP:
			break;
			
		default:
			break;
		}
	}

	@Override
	public boolean addRouteWithZK(String path, String data) {
		
		try {
			
			path = path.substring(1, path.length());
			String [] p = path.split("/");
			String root = p[0];
			String siteCode = p[1];
			String serviceCode = p[2];
			String route = "direct:" + root + "_" + siteCode + "_" + serviceCode;
			String routeId = RouteUtil.getRouteId(root, siteCode, serviceCode);
			_logger.info(route);
			addRoute(route, routeId, data, siteCode, serviceCode);
			
		} catch (Exception e) {
			_logger.error("", e);
		}
		
		return false;
	}

	@Override
	public boolean updateRouteWithZK(String path, String data) {
		
		try {
			
			path = path.substring(1, path.length());
			String [] p = path.split("/");
			String root = p[0];
			String siteCode = p[1];
			String serviceCode = p[2];
			String route = "direct:" + root + "_" + siteCode + "_" + serviceCode;
			String routeId = RouteUtil.getRouteId(root, siteCode, serviceCode);
			_logger.info(route);
			_camelContext.removeRoute(routeId);
			_esbRouteService.deleteRouteInfo(routeId);
			addRoute(route, routeId, data, siteCode, serviceCode);
			
		} catch (Exception e) {
			_logger.error("", e);
		}
		
		return false;
	}

	@Override
	public boolean deleteRouteWithZK(String path) {
		
		try {
			
			path = path.substring(1, path.length());
			String [] p = path.split("/");
			String root = p[0];
			String siteCode = p[1];
			String serviceCode = p[2];
			String route = "direct:" + root + "_" + siteCode + "_" + serviceCode;
			String routeId = RouteUtil.getRouteId(root, siteCode, serviceCode);
			_logger.info(route);
			_camelContext.removeRoute(routeId);
			_esbRouteService.deleteRouteInfo(routeId);
			
		} catch (Exception e) {
			_logger.error("", e);
		}
		
		return false;
	}

}
