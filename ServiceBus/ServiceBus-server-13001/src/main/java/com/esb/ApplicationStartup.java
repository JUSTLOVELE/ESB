package com.esb;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.Registry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.esb.service.InitRouteInfoService;
import com.esb.service.route.ActivemqConsumeRouter;
import com.esb.service.route.HttpPublisherRouter;

/**
 * @Description:项目启动后执行
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-24
 * @version 1.00.00
 * @history:
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	
	private static final Log _logger = LogFactory.getLog(ApplicationStartup.class);

	@Autowired
	private CamelContext _camelContext;
	
	@Autowired
	private HttpPublisherRouter _httpPublisherRouter;
	
	@Autowired
	private ActivemqConsumeRouter _activemqConsumeRouter;
	
	@Autowired
	@Qualifier("initRouteInfoServiceImpl")
	private InitRouteInfoService _initRouteInfoService;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		startService(event);
		startRoute();
	}
	
	private void startRoute() {
		_initRouteInfoService.initRouteWithZK("direct:esb_350000_checkVersion", "http://www.fjjkkj.com/HY-GS/mobileSystemAction/api/checkVersion?source=1");
	}
	
	/**
	 * 			System.out.println("BeanName:" + beanName);
				System.out.println("Bean的类型：" + beanType);
				System.out.println("Bean所在的包：" + beanType.getPackage());
				System.out.println("Bean：" + app.getBean(beanName));
	 * @param event
	 */
	private  void startService(ContextRefreshedEvent event) {
		
		try {
			_camelContext.addRoutes(_httpPublisherRouter);
			_camelContext.addRoutes(_activemqConsumeRouter);
			ApplicationContext app = event.getApplicationContext();
			String[] beans = app.getBeanDefinitionNames();
			Registry registry = _camelContext.getRegistry();
			
			for (String beanName : beans) {
				
				Class<?> beanType = app.getType(beanName);
				
				if(beanType.getPackage() != null) {
					
					String packageName = beanType.getPackage().getName();
					
					if(packageName.contains("com.esb.route.bean")){
						
						registry.bind(beanName, app.getBean(beanName));
					}
				}
			}
			
		} catch (Exception e) {
			_logger.error("", e);
		}
	}

}
