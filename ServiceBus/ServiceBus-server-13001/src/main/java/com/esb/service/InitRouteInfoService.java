package com.esb.service;

/**
 * @Description:初始化路由
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-1
 * @version 1.00.00
 * @history:
 */
public interface InitRouteInfoService {

	public boolean initRouteWithZK(String path, String data);
}
