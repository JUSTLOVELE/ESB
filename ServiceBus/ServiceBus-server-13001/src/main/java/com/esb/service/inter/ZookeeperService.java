package com.esb.service.inter;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.CreateMode;

/**
 * @Description:zookeeper服务接口
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-15
 * @version 1.00.00
 * @history:
 */
public interface ZookeeperService {
	
	public void close();

	/**
	 * 注册子目录监听器
     * PathChildrenCache：对指定路径节点的一级子目录监听，不对该节点的操作监听，对其子目录的增删改操作监听
     * @param nodePath 节点路径
     * @param listener 监控事件的回调接口
     * @return org.apache.curator.framework.recipes.cache.PathChildrenCache
     */
	public PathChildrenCache registerPathChildListener(String nodePath, PathChildrenCacheListener listener);

	/**
	 * 注册目录监听器
	 * @param nodePath:节点路径
	 * @param maxDepth:自定义监控深度
	 * @param listener:监控事件的回调接口
	 * @return TreeCache:对指定路径节点的一级子目录监听，不对该节点的操作监听，对其子目录的增删改操作监听
	 */
	public TreeCache registerTreeCacheListener(String nodePath, int maxDepth, TreeCacheListener listener);

	/**
	 * 注册节点监听器
	 * 
	 * @param nodePath
	 * @return NodeCache:对一个节点进行监听,监听事件包括制定指定路径的增删改操作
	 */
	public NodeCache registerNodeCacheListener(String nodePath);

	/**
	 * 判断节点是否存在
	 * 
	 * @param newClient
	 * @param path
	 * @return
	 */
	public boolean checkExists(String path);

	/**
	 * 删除节点
	 * 
	 * @param newClient
	 * @param path
	 */
	public void deleteNode(String path);

	/**
	 * 更新节点
	 * 
	 * @param newClient
	 * @param path
	 * @param data
	 */
	public void updateNodeDate(String path, String data);

	/**
	 * 获取节点数据
	 * 
	 * @param client
	 * @param path
	 * @return
	 */
	public String getData(String path);

	/**
	 * 创建zookeeper服务
	 */
	public void initCreateZookeeperService();

	/**
	 * 创建节点
	 * 
	 * @param client
	 * @param path
	 * @param createMode
	 * @param data
	 */
	public void createNode(String path, CreateMode createMode, String data);
}
