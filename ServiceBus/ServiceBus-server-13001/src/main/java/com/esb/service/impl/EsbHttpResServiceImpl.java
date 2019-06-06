package com.esb.service.impl;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.util.URISupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esb.core.BaseRes;
import com.esb.service.EsbHttpResService;


/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-6
 * @version 1.00.00
 * @history:
 */
@Service
public class EsbHttpResServiceImpl extends BaseRes implements EsbHttpResService {

	@Autowired
	private CamelContext _camelContext;
	
	private final static Log _logger = LogFactory.getLog(EsbHttpResServiceImpl.class);
}
