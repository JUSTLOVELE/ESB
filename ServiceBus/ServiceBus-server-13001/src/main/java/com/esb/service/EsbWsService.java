package com.esb.service;

import org.apache.camel.component.cxf.CxfEndpoint;

/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-6
 * @version 1.00.00
 * @history:
 */
public interface EsbWsService {

	public CxfEndpoint createCxfEndpoinf(String wsdlAddress, String siteCode, String serviceCode);
	
}
