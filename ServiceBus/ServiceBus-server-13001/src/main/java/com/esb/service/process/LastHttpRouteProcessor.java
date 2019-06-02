package com.esb.service.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

/**
 * @Description:调用目的http资源前的最后处理
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-1
 * @version 1.00.00
 * @history:
 */
@Component
public class LastHttpRouteProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		// 获取zookeeper资源
		
	}

}
