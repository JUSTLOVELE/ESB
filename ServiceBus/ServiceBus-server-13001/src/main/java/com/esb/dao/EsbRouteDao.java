package com.esb.dao;

import java.util.List;
import java.util.Map;

import com.esb.entity.EsbRouteEntity;

/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-6
 * @version 1.00.00
 * @history:
 */
public interface EsbRouteDao {
	
	/**
	 * 查询路由信息
	 * @param routeId
	 * @return
	 */
	public List<Map<String, Object>> queryRoute(String routeId);
	
	
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
	 * 保存路由
	 * @param e
	 */
	public void saveEsbRouteEntity(EsbRouteEntity e);
}
