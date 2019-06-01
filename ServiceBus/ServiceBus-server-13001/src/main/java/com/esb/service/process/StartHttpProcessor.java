package com.esb.service.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.esb.util.Constant;
import com.esb.util.JSONUtil;
import com.esb.util.XMLUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
		String method = head.get(Exchange.HTTP_METHOD).toString();
		String data = null;
		
		if(Constant.Key.POST.equals(method)) {
			 data = exchange.getIn().getBody(String.class);
		}else if(Constant.Key.GET.equals(method)) {
			 String queryString = head.get(Exchange.HTTP_QUERY).toString();
			 Map<String, String> map = Stream.of(queryString.split("&")).map(obj -> obj.split("=")).collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
			 data = map.get(Constant.Key.PARAM);
			 
		}else {
			throw new RuntimeCamelException("method既不是post也不是get");
		}
		
		//因为没有做用户的等级所有固定放1,意味着只走普通队列
		in.getHeaders().put(Constant.HeadParam.INVOKEPRIORITY, 1);
		in.getHeaders().put(Constant.HeadParam.IS_INVOKE, Boolean.valueOf(false));
		_logger.info(("data = " + data));
		String routeId = exchange.getFromRouteId();
		
		if(routeId.contains(Constant.Key.JSON)) {
			
			JSONObject json = JSONObject.fromObject(data);
			
			if(!json.containsKey(Constant.Key.ORG_CODE)) {
				throw new RuntimeCamelException("orgCode为必填项");
			}
			
			if(json.getString(Constant.Key.ORG_CODE) == null || "".equals(json.getString(Constant.Key.ORG_CODE))) {
				throw new RuntimeCamelException("orgCode为不能为空");
			} 
			
			if(!json.containsKey(Constant.Key.SERVICE_CODE)) {
				throw new RuntimeCamelException("serviceCode为必填项");
			}
			
			if(json.getString(Constant.Key.SERVICE_CODE) == null || "".equals(json.getString(Constant.Key.SERVICE_CODE))) {
				throw new RuntimeCamelException("orgCode为不能为空");
			}
			
			if(!json.containsKey(Constant.Key.OFFLINE)) {
				throw new RuntimeCamelException("offline为不能为空");
			}
			
			head.put(Constant.Key.ORG_CODE, json.getString(Constant.Key.ORG_CODE));
			head.put(Constant.Key.SERVICE_CODE, json.getString(Constant.Key.SERVICE_CODE));
			head.put(Constant.Key.OFFLINE, json.getString(Constant.Key.OFFLINE));
			
			if(json.containsKey(Constant.Key.PARAMS)) {
				
				JSONArray array = json.getJSONArray(Constant.Key.PARAMS);
				List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
				
				for(int i=0; i<array.size(); i++) {
					
				}
			}
			
		}else if(routeId.contains(Constant.Key.XML)){
			
		}else {
			throw new RuntimeCamelException("未知路由ID:" + routeId);
		}
		
	}
}
