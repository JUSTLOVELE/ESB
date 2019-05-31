package com.esb.route.bean;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

/**
 * @Description:测试类,该包下的类都会被注册到camel中
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-31
 * @version 1.00.00
 * @history:
 */
@Component
public class RouteBean {

	public void HelloWolrd(Exchange ex) {
		String data = ex.getIn().getBody(String.class);
		System.out.println("helloworld:" + data);
	}
}
