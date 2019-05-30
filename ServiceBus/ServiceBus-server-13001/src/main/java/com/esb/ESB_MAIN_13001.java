package com.esb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;

/**
 * @Description:启动类
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-14
 * @version 1.00.00
 * @history:
 */
@SpringBootApplication
@ServletComponentScan
//@EnableEurekaClient//自动注册到eureka服务中
//@EnableDiscoveryClient//服务发现
public class ESB_MAIN_13001 {
	
	private static final Log _logger = LogFactory.getLog(InitStartComponent.class);

	public static void main(String[] args) {
		
		ApplicationContext context = SpringApplication.run(ESB_MAIN_13001.class, args);
	}
}
