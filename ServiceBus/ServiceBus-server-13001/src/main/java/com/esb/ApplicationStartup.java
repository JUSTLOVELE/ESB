package com.esb;

import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
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
import com.esb.service.ZookeeperService;
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
	
	@Autowired
	private ZookeeperService _zookeeperService;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		startService(event);
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
			
			_zookeeperService.initCreateZookeeperService();
//			List<Route> routes = _camelContext.getRoutes();
//			
//			for(Route r: routes) {
//				System.out.println(r.getId());
//				System.out.println(r.getEndpoint().getEndpointUri());
//			}
//			
//			Route route = _camelContext.getRoute("esb_350003_WebService");
//			_camelContext.getRouteController().stopRoute("esb_350003_WebService");
//			boolean s = _camelContext.removeRoute("esb_350003_WebService");
//			System.out.println(s);
//			Route a = _camelContext.getRoute("esb_350003_WebService");
//			System.out.println(a);
			
		} catch (Exception e) {
			_logger.error("", e);
		}
	}

}
