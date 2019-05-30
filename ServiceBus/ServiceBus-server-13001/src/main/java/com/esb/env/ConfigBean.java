package com.esb.env;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.jms.JmsMessageType;
import org.apache.camel.component.jms.ReplyToType;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.client.RestTemplate;

import com.esb.util.Constant;

/**
 * @Description:配置实体化bean
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @Company: 
 * @author yangzl 2019-5-14
 * @version 1.00.00
 * @history:
 */
@Configuration
//@ImportResource(locations="classpath:camel-endpoint.xml")
public class ConfigBean {
	
	private final static Log _logger = LogFactory.getLog(ConfigBean.class);
	
	@Bean
	public JmsTemplate jmsTemplate() {
		
		JmsTemplate j = new JmsTemplate(cachingConnectionFactory());
		return j;
	}
	
	
	@Bean
	public ActiveMQQueue invokehightQueue() {
		//高速队列
		ActiveMQQueue q = new ActiveMQQueue("esb.high.invokHighQueue");
		return q;
	}
	
	@Bean
	public ActiveMQQueue invokeNormalQueue() {
		//普通队列
		ActiveMQQueue q = new ActiveMQQueue("esb.normal.invokeNormalQueue");
		
		return q;
	}
	
	@Bean
	public ActiveMQComponent activemq() {
		
		ActiveMQComponent activemq = new ActiveMQComponent();
		activemq.setConfiguration(jmsConfig());
		return activemq;
	}
	
	@Bean
	public JmsConfiguration jmsConfig() {
		//camel集成activemq
		JmsConfiguration jms = new JmsConfiguration(cachingConnectionFactory());
		jms.setReplyToType(ReplyToType.Temporary);
		jms.setJmsMessageType(JmsMessageType.Object);
		return jms;
	}
	
	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		//缓存连接池
		
		if("".equals(Constant.ACTIVEMQ_ADDRESS)) {
			throw new RuntimeException("消息队列无初始化地址");
		}
		
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("failover:(" + Constant.ACTIVEMQ_ADDRESS + ")");
		//为camel添加schema组件
		camelContext().addComponent(Constant.CamelComponent.ACTIVEMQ, JmsComponent.jmsComponentAutoAcknowledge(factory));
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setTargetConnectionFactory(factory);
		return cachingConnectionFactory;
	}
	
	@Bean
	public CamelContext camelContext() {
		//在InitStartComponent中start
		try {
			CamelContext context = new DefaultCamelContext();
			context.start();
			return context;
		} catch (Exception e) {
			_logger.error("", e);
			return null;
		}
	}
	

	@Bean
	//@LoadBalanced
	public RestTemplate restTemplate() {
		
		return new RestTemplate();
	}

}
