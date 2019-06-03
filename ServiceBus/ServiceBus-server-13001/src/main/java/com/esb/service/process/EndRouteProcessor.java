package com.esb.service.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esb.core.Base;
import com.esb.entity.EsbSuccessEntity;
import com.esb.entity.EsbUserEntity;
import com.esb.service.EsbSuccessService;
import com.esb.sys.InvokeDataType;
import com.esb.util.Constant;
import com.esb.util.UUIDUtil;

/**
 * @Description:整个路由结束之后的处理器
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-1
 * @version 1.00.00
 * @history:
 */
@Component
public class EndRouteProcessor extends Base implements Processor{
	
	private final static Log _logger = LogFactory.getLog(EndRouteProcessor.class);
	
	@Autowired
	private EsbSuccessService _esbSuccessService;

	@Override
	public void process(Exchange exchange) throws Exception {
		
		Message in = exchange.getIn();
		Map<String, Object> heads = in.getHeaders();
		String data = exchange.getIn().getBody(String.class);
		_logger.info("data = " + data);
		String outData = exchange.getOut().getBody(String.class);
		_logger.info("outData = " + outData);
		Message out = exchange.getOut();
		Map<String, Object> outheads = out.getHeaders();
		//要把调用的头信息和注册的头信息加入到返回的头参数中
		
		for(Map.Entry<String, Object> entry: heads.entrySet()) {
			
			String key = entry.getKey();
			
			if(key.startsWith("esb_")) {
				
				outheads.put(key, entry.getValue());
			}
		}
		//完成路由覆盖参数动态路由参数,使得路由不会一直循环
		outheads.put(Constant.HeadParam.ESB_COUNT_ROUTE_PRIORITY, Constant.HeadParam.END_QUEUE);
		outheads.put(Constant.HeadParam.ESB_IS_INVOKE, Boolean.valueOf(true));
		
		
		//m.put(key, value)
		Integer type = Integer.valueOf(heads.get(Constant.HeadParam.ESB_INVOKE_DATA_TYPE).toString());
		
		if(type == InvokeDataType.JSON.getValue()) {
			
			Map<String, Object> m = new HashMap<String, Object>();
			m.put(Constant.Key.CODE, Constant.Status.SUCCESS_CODE);
			m.put(Constant.Key.SUCCESS, true);
			m.put(Constant.Key.DATA, data);
			data = getJSON(m);
			
		}else if(type == InvokeDataType.XML.getValue()) {
			
			StringBuffer sb = new StringBuffer();
			sb.append("<root>");
			sb.append("<code>100<code>");
			sb.append("<success>true</success>");
			sb.append("<data><![CDATA[" + data + "]]></data>");
			sb.append("</root>");
			data = sb.toString();
		}
		
		exchange.getOut().setBody(data);
		//保存数据入库
		String userOpId = (String) heads.get(Constant.HeadParam.ESB_USER_OP_ID);
		EsbSuccessEntity e = new EsbSuccessEntity(UUIDUtil.getUUID(), 
				userOpId, 
				heads.get(Constant.HeadParam.ESB_SITE_CODE).toString(), 
				heads.get(Constant.HeadParam.ESB_SERVICE_CODE).toString(), 
				data.getBytes().length);
		_esbSuccessService.saveEsbSuccessEntity(e);
	}
}
