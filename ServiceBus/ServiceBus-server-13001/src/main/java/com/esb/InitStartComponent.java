package com.esb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.esb.service.impl.ZookeeperServiceImpl;
import com.esb.util.Constant;


/**
 * @Description:初始组件
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-15
 * @version 1.00.00
 * @history:
 */
@Component
@Lazy(value=false)
public class InitStartComponent {

	private static final Log _logger = LogFactory.getLog(InitStartComponent.class);
	
	public InitStartComponent() {
		loadLocalConfig();
		setSystemParam();
	}
	
	@Autowired
	private ZookeeperServiceImpl _zookeeperService;
	
	@PostConstruct
	public void start() {
		
		
		_logger.info("ESB启动成功！");
	}

	
	private void setSystemParam() {
		
		//Constant.ZOOKEEPER_ADDRESS = "192.168.1.150:2181,192.168.1.151:2181,192.168.1.2:2181";
		Constant.ZOOKEEPER_ADDRESS = Constant.getConstObject(Constant.Key.ZOOKEEPER_ADDRESS);
		Constant.ACTIVEMQ_ADDRESS = Constant.getConstObject(Constant.Key.ACTIVEMQ_ADDRESS);
		Constant.Constant_PUBLIC_KEY = Constant.getConst(Constant.Key.CONSTANTPUBLICKEY);
		Constant.Constant_PRIVATE_KEY = Constant.getConst(Constant.Key.CONSTANTPRIVATEKEY);
		Constant.COMPILE_PATH = Constant.getConst(Constant.Key.COMPILE_PATH);
		Constant.ENCRYPT_KEY = Constant.getConst(Constant.Key.ENCRYPT_KEY);
		_logger.info("zookeeper address=" + Constant.ZOOKEEPER_ADDRESS);
		_logger.info("activemq address=" + Constant.ACTIVEMQ_ADDRESS);
	}
	
	private void loadLocalConfig(){

		try {
			_logger.info("开始初始化配置信息...");
			PropertiesConfiguration config = new PropertiesConfiguration("config.properties");
			FileChangedReloadingStrategy strategy = new FileChangedReloadingStrategy();
			strategy.setRefreshDelay(60);//60秒检测1次配置文件
			config.setReloadingStrategy(strategy);
			Constant.setCfg(config);
			
		} catch (ConfigurationException e) {
			_logger.info(e.toString());
		}
		
	
	}
	
	@PreDestroy
	public void stop(){
		_zookeeperService.close();
		_logger.info("关闭成功！");
	}
}
