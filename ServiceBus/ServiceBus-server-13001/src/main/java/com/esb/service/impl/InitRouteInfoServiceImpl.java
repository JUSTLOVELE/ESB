package com.esb.service.impl;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esb.service.InitRouteInfoService;
import com.esb.service.process.EndRouteProcessor;
import com.esb.service.process.LastHttpRouteProcessor;
import com.esb.sys.RegisterType;
import com.esb.util.Constant;
import com.esb.util.XMLUtil;
import com.netflix.discovery.converters.Auto;

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
	
	private final static Log _logger = LogFactory.getLog(InitRouteInfoServiceImpl.class);
	
	private void addHttp(String route, String routeId, String uri) throws Exception {
		
		_logger.info("addHttpRoute = " + routeId);
		_camelContext.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				
				errorHandler(deadLetterChannel("bean:routerErrorHandler?method=handlerHttpSiteRoute"));
				from(route).routeId(routeId)
				.process(_lastHttpRouteProcessor)
				.to(ExchangePattern.InOut, uri + "?bridgeEndpoint=true&throwExceptionOnFailure=false")
				.process(_endRouteProcessor).end();
			}
		});
	}
	
	private void addRoute(String route, String routeId, String data) throws Exception {
		
		Map<String, Object> registerInfo = XMLUtil.getReigsterInfo(data);
		Integer type = (Integer) registerInfo.get(Constant.Key.TYPE);
		String url = (String) registerInfo.get(Constant.Key.URL);
		
		switch (RegisterType.getValue(type)) {
		case HTTP:
			addHttp(route, routeId, url);
			break;
			
		case WEBSERVICE:
			break;
			
		case FILE:
			break;
			
		case COMPLEX:
			break;
			
		case MESSAGE_SEND:
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
			String routeId = root + "_" + siteCode + "_" + serviceCode;
			_logger.info(route);
			addRoute(route, routeId, data);
			
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
			String routeId = root + "_" + siteCode + "_" + serviceCode;
			_logger.info(route);
			_camelContext.removeRoute(routeId);
			addRoute(route, routeId, data);
			
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
			String routeId = root + "_" + siteCode + "_" + serviceCode;
			_logger.info(route);
			_camelContext.removeRoute(routeId);
			
		} catch (Exception e) {
			_logger.error("", e);
		}
		
		return false;
	}

}
