package com.esb.service.process;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.esb.util.Constant;

/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-31
 * @version 1.00.00
 * @history:
 */
@Component
public class StartHttpProcessor implements Processor{
	
	private final static Log _logger = LogFactory.getLog(StartHttpProcessor.class);
	
	public StartHttpProcessor() {
		
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		
		Message in = exchange.getIn();
		Map<String, Object> head = in.getHeaders();
		
		/*for (Map.Entry<String, Object> entry : heads.entrySet()) { 
			  System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
		}*/
		String method = head.get(Exchange.HTTP_METHOD).toString();
		String data = null;
		
		if(Constant.Key.POST.equals(method)) {
			 data = exchange.getIn().getBody(String.class);
		}else if(Constant.Key.GET.equals(method)) {
			 data = head.get(Exchange.HTTP_QUERY).toString();
		}else {
			throw new RuntimeCamelException("method既不是post也不是get");
		}
		
		//因为没有做用户的等级所有固定放1,意味着只走普通队列
		in.getHeaders().put(Constant.HeadParam.INVOKEPRIORITY, 1);
		in.getHeaders().put(Constant.HeadParam.IS_INVOKE, Boolean.valueOf(false));
		_logger.info(("data = " + data));
		
		/*Message in = exchange.getIn();
		Map<String, Object> heads = in.getHeaders();
		
		for (Map.Entry<String, Object> entry : heads.entrySet()) { 
			  System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
		}*/
		
		//String queryString = exchange.getIn().getHeader(Exchange.HTTP_QUERY, String.class);
		
		//exchange.getOut().setBody(data);
	
	}
}
