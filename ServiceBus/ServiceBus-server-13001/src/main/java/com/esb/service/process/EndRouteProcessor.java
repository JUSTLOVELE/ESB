package com.esb.service.process;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.esb.util.Constant;

/**
 * @Description:整个路由结束之后的处理器
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-1
 * @version 1.00.00
 * @history:
 */
@Component
public class EndRouteProcessor implements Processor{
	
	private final static Log _logger = LogFactory.getLog(EndRouteProcessor.class);
	
	
	

	@Override
	public void process(Exchange exchange) throws Exception {
		
		Message in = exchange.getIn();
		Map<String, Object> heads = in.getHeaders();
		Map<String, Object> esbHeadInvoke = (Map<String, Object>) heads.get(Constant.Key.ESB_HEAD_INVOKE);
		Map<String, Object> esbHeadRegister = (Map<String, Object>) heads.get(Constant.Key.ESB_HEAD_REGISTER);
		//完成路由覆盖参数动态路由参数,使得路由不会一直循环
		esbHeadInvoke.put(Constant.HeadParam.INVOKEPRIORITY, Constant.HeadParam.END_QUEUE);
		esbHeadInvoke.put(Constant.HeadParam.IS_INVOKE, Boolean.valueOf(true));
		//是否被调用过
		String data = exchange.getIn().getBody(String.class);
		_logger.info("data = " + data);
		String outData = exchange.getOut().getBody(String.class);
		_logger.info("outData = " + outData);
		Message out = exchange.getOut();
		Map<String, Object> outheads = out.getHeaders();
		//要把调用的头信息和注册的头信息加入到返回的头参数中
		outheads.put(Constant.Key.ESB_HEAD_INVOKE, esbHeadInvoke);
		outheads.put(Constant.Key.ESB_HEAD_REGISTER, esbHeadRegister);
		exchange.getOut().setBody(data);
		//保存数据入库
	}
}
