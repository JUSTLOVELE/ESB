package com.esb.service;

/**
 * @Description:初始化路由
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-1
 * @version 1.00.00
 * @history:
 */
public interface InitRouteInfoService {

	/**
	 * 初始化路由
	 * @param path: zookeeper的路径,例如/esb/25000/check
	 * @param data: 注册在zookeeper的节点数据
	 * @return
	 */
	public boolean addRouteWithZK(String path, String data);
	
	/**
	 * 更新路由
	 * 	要先remove之前的路由,再新增
	 * @param path: zookeeper的路径,例如/esb/25000/check
	 * @param data: 注册在zookeeper的节点数据
	 * @return
	 */
	public boolean updateRouteWithZK(String path, String data);
	
	/**
	 * 删除路由
	 * @param path: zookeeper的路径,例如/esb/25000/check
	 * @return
	 */
	public boolean deleteRouteWithZK(String path);
}
