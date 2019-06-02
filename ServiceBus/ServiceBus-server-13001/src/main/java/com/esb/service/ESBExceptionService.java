package com.esb.service;

import com.esb.entity.EsbExceptionEntity;

/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-1
 * @version 1.00.00
 * @history:
 */
public interface ESBExceptionService {

	public void saveESBExceptionEntity(EsbExceptionEntity e);
	
	public void saveESBExceptionEntity(String id, String routeId, String key, String uri, String errorMsg, String siteCode, String serviceCode);
}
