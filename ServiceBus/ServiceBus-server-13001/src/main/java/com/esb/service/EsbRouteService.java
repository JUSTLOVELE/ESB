package com.esb.service;

import com.esb.entity.EsbRouteEntity;

/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-6
 * @version 1.00.00
 * @history:
 */
public interface EsbRouteService {
	
	
	/**
	 * 判断这个路由ID对应的路由是否存在
	 * @param routeId
	 * @return
	 */
	public boolean queryisExistRoute(String userOpId, String siteCode, String serviceCode);
	
	/**
	 * 根据路由ID删除
	 * @param routeId
	 * @return
	 */
	public void deleteRouteInfo(String routeId);
	
	/**
	 * 判断这个路由ID对应的路由是否存在
	 * @param routeId
	 * @return
	 */
	public boolean queryisExistRoute(String routeId);

	/**
	 * 保存路由信息
	 * @param e
	 */
	public void saveEsbRouteEntity(EsbRouteEntity e);
}
