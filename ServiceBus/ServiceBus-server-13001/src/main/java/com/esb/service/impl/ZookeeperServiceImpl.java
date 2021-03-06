package com.esb.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esb.core.Base;
import com.esb.service.InitRouteInfoService;
import com.esb.service.ZookeeperService;
import com.esb.util.Constant;
import com.esb.util.ThreadPoolUtil;

/**
 * @Description:zookeeper服务接口实现
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-15
 * @version 1.00.00
 * @history:
 */
@Component
public class ZookeeperServiceImpl extends Base implements ZookeeperService {
	
	private final static Log _logger = LogFactory.getLog(ZookeeperServiceImpl.class);
	
	private static CuratorFramework _client = null;
	
	@Autowired
	private InitRouteInfoService _initRouteInfoService;
	
	public ZookeeperServiceImpl() {
		//initCreateZookeeperService();
	}
	
	@Override
    public TreeCache registerTreeCacheListener(String nodePath, int maxDepth, TreeCacheListener listener){
        try {
            //1. 创建一个TreeCache
            TreeCache treeCache = TreeCache.newBuilder(_client, nodePath)
                    .setCacheData(true)
                    .setMaxDepth(maxDepth)
                    .build();
            //2. 添加目录监听器
            treeCache.getListenable().addListener(listener);
            //3. 启动监听器
            treeCache.start();
            //4. 返回TreeCache
            return treeCache;
        } catch (Exception e) {
            _logger.error("", e);
        }
        return null;
    }
	
	@Override
    public PathChildrenCache registerPathChildListener(String nodePath){
        try {
            //1. 创建一个PathChildrenCache
            PathChildrenCache pathChildrenCache = new PathChildrenCache(_client, nodePath, true);
            //2. 添加目录监听器
            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
				
				@Override
				public void childEvent(CuratorFramework arg0, PathChildrenCacheEvent event) throws Exception {
					// TODO Auto-generated method stub
					String path = event.getData().getPath();
					PathChildrenCacheEvent.Type eventType = event.getType();
					
					switch (eventType) {
					case CHILD_ADDED:
						
						_logger.info("child_add=" + path);
						_initRouteInfoService.addRouteWithZK(path, new String(event.getData().getData()));
						break;
					case CHILD_UPDATED:
						
						_logger.info("child_update=" + path);
						_initRouteInfoService.updateRouteWithZK(path, new String(event.getData().getData()));
						break;
						
					case CHILD_REMOVED:
						
						_logger.info("child_remove=" + path);
						_initRouteInfoService.deleteRouteWithZK(path);
						break;

					default:
						break;
					}
				}
			}, ThreadPoolUtil.getThreadPool());
            //3. 启动监听器
            pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            //4. 返回PathChildrenCache
            return pathChildrenCache;
        } catch (Exception e) {
            _logger.error("" ,e);
        }
        return null;
    }
	
	@Override
	@Deprecated
	public NodeCache registerNodeCacheListener(String nodePath) {
		
		try {
			
			NodeCache nodeCache = new NodeCache(_client, nodePath);
			/*nodeCache.getListenable().addListener(() -> {
				ChildData childData = nodeCache.getCurrentData();
				_logger.info("-----------ZookeeperServiceImpl.registerNodeCacheListener()--------------");
				String path = childData.getPath();
				_logger.info("Path:" + path);
				String data = new String(childData.getData());
				_initRouteInfoService.initRouteWithZK(path, data);
			});*/
			
			nodeCache.start();
			return nodeCache;
			
		} catch (Exception e) {
			_logger.error("注册异常", e);
		}
		return null;
	}

	@Override
	public void initCreateZookeeperService() {
		
		if(_client != null) {
			//单实例
			return ;
		}
		
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,  3);
		_client = CuratorFrameworkFactory.newClient(Constant.ZOOKEEPER_ADDRESS, 5000,3000, retryPolicy);
		_client.start();
		
		if(!checkExists(Constant.Key.PATH_ROOT)) {
			
			createNode(Constant.Key.PATH_ROOT, CreateMode.PERSISTENT, null);
		}else {
			
			try {
				
				List<String> sitelist = _client.getChildren().forPath(Constant.Key.PATH_ROOT);
				
				for(String site: sitelist) {
					
					String sitePath  = Constant.Key.PATH_ROOT + "/" + site;
					registerPathChildListener(sitePath);
					List<String> servicelist = _client.getChildren().forPath(sitePath);

					for(String service: servicelist) {
						
						String path = sitePath + "/" + service;
						String data = getData(path);
						_initRouteInfoService.addRouteWithZK(path, data);
					}
				}
				
			} catch (Exception e) {
				_logger.error("", e);
			}
		}
	}

	@Override
	public void createNode(String path, CreateMode createMode, String data) {
		
		try {
			
			if(data != null && !"".equals(data)) {
				_client.create().creatingParentContainersIfNeeded().withMode(createMode).forPath(path, data.getBytes());
			}else {
				_client.create().withMode(createMode).forPath(path);
			}
			
		} catch (Exception e) {
			_logger.error("", e);
		}
	}

	@Override
	public String getData(String path) {
		
		try {
			return new String(_client.getData().forPath(path));
		} catch (Exception e) {
			_logger.error("", e);
			return null;
		}
	}

	@Override
	public void deleteNode(String path) {
		
		try {
	        _client.delete().forPath(path);
	    } catch (Exception e) {
	    	_logger.error("", e);
	    }
	}

	@Override
	public void updateNodeDate(String path, String data) {

		try {
	        _client.setData().forPath(path, data.getBytes());
	    } catch (Exception e) {
	    	_logger.error("", e);
	    }
	}

	@Override
	public boolean checkExists(String path) {

		try {
	        if(_client.checkExists().forPath(path) == null) {
	        	return false;
	        }else {
	        	return true;
	        }
	    } catch (Exception e) {
	    	_logger.error("", e);
	    	return false;
	    }
	}

	@Override
	public void close() {
		_client.close();
	}
	
}
