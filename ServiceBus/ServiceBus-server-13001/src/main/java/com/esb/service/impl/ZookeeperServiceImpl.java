package com.esb.service.impl;

import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esb.core.Base;
import com.esb.service.inter.ZookeeperService;
import com.esb.util.Constant;

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
	private CamelContext _camelContext;
	
	public ZookeeperServiceImpl() {
		initCreateZookeeperService();
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
    public PathChildrenCache registerPathChildListener(String nodePath, PathChildrenCacheListener listener){
        try {
            //1. 创建一个PathChildrenCache
            PathChildrenCache pathChildrenCache = new PathChildrenCache(_client, nodePath, true);
            //2. 添加目录监听器
            pathChildrenCache.getListenable().addListener(listener);
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
	public NodeCache registerNodeCacheListener(String nodePath) {
		
		try {
			
			NodeCache nodeCache = new NodeCache(_client, nodePath);
			nodeCache.getListenable().addListener(() -> {
				
				ChildData childData = nodeCache.getCurrentData();
				_logger.info("-----------ZookeeperServiceImpl.registerNodeCacheListener()--------------");
				_logger.info("Path:" + childData.getPath());
				String routeId = childData.getPath();
				//把原来的路由信息删除然后新增路由
				_camelContext.removeRoute(routeId);
				
				if(childData != null) {
					
					String data = new String(childData.getData());
					Document registerXML = DocumentHelper.parseText(data);
					registerXML.getRootElement();
				}
			});
			
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
			//registerNodeCacheListener(Constant.Key.PATH_ROOT);
		}else {
			
			try {
				
				List<String> orglist = _client.getChildren().forPath(Constant.Key.PATH_ROOT);
				
				for(String org: orglist) {
					
					String path  = Constant.Key.PATH_ROOT + "/" + org;
					List<String> serviceList = _client.getChildren().forPath(path);
					
					for(String service: serviceList) {
						//监听数据变化
						String p = path + "/" + service;
						registerNodeCacheListener(p);
						String routeDirect = "direct:" + Constant.Key.PATH_ESB + "_" + org + "_" + service;
						_logger.info(routeDirect);
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
