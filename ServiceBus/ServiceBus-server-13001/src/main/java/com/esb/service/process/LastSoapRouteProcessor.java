package com.esb.service.process;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esb.service.ZookeeperService;
import com.esb.sys.InvokeDataType;
import com.esb.util.Constant;
import com.esb.util.XMLUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Description:调用webservice前最后调用
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-7
 * @version 1.00.00
 * @history:
 */
@Component
public class LastSoapRouteProcessor implements Processor {
	
	@Autowired
	private ZookeeperService _zookeeperService;
	
	/**
	 * 解析并获取params参数
	 * @param invokeDataType
	 * @param data
	 * @return
	 */
	private List<Map<String, Object>> getInvokeParams(int invokeDataType, String data){
		
		if(invokeDataType == InvokeDataType.JSON.getValue()) {
			
			JSONObject json = JSONObject.fromObject(data);
			
			List<Map<String, Object>> params = null;
			
			if(json.containsKey(Constant.Key.PARAMS)) {
				
				JSONArray array = json.getJSONArray(Constant.Key.PARAMS);
				params = new ArrayList<Map<String, Object>>();
				
				for(int i=0; i<array.size(); i++) {
					
					JSONObject param = array.getJSONObject(i);
					Map<String, Object> m = new LinkedHashMap<String, Object>();
					m.put(Constant.Key.VALUE, param.get(Constant.Key.VALUE));
					m.put(Constant.Key.KEY, param.get(Constant.Key.KEY));
					params.add(m);
				}
			}
			
			return params;
			
		}else if(invokeDataType == InvokeDataType.XML.getValue()) {
			
			Element root = XMLUtil.getRootElement(data);
			List<Element> paramElements = root.getChildren(Constant.Key.PARAMS);
			
			List<Map<String, Object>> params = null;
			
			if(paramElements != null && paramElements.size() > 0) {
				
				params = new ArrayList<Map<String, Object>>();
				
				for(Element param: paramElements) {
					
					Map<String, Object> m = new LinkedHashMap<String, Object>();
					m.put(Constant.Key.VALUE, param.getChild(Constant.Key.VALUE).getValue());
					m.put(Constant.Key.KEY, param.getChild(Constant.Key.KEY).getValue());
					params.add(m);
				}
			}
			
			return params;
		}else {
			throw new RuntimeCamelException("LastHttpRouteProcessor.getInvokeParams invokeDataType数值异常 ");
		}
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		// 获取zookeeper资源
		Message in = exchange.getIn();
		Map<String, Object> head = in.getHeaders();
		//Map<String, Object> esbHeadInvoke = (Map<String, Object>) head.get(Constant.Key.ESB_HEAD_INVOKE);
		String siteCode = head.get(Constant.HeadParam.ESB_SITE_CODE).toString();
		String serviceCode = head.get(Constant.HeadParam.ESB_SERVICE_CODE).toString();
		head.put(Constant.HeadParam.ESB_ROUTE_ID, Constant.HeadParam.ESB_ROUTE_ID);
		String zkPath = Constant.Key.PATH_ROOT + "/" + siteCode + "/" + serviceCode;
		String registerInfo = _zookeeperService.getData(zkPath);
		//要比较传进来的调用参数和注册的调用参数数目和key是否一致
		Map<String, Object> esbHeadRegister = XMLUtil.getReigsterInfo(registerInfo);
		
		if(!esbHeadRegister.containsKey(Constant.Key.SOAP)) {
			throw new RuntimeCamelException("找不到soap");
		}
		
		List<Map<String, Object>> registerParams = (List<Map<String, Object>>) esbHeadRegister.get(Constant.Key.PARAMS);
		List<Map<String, Object>> invokeParams = getInvokeParams(Integer.valueOf(head.get(Constant.HeadParam.ESB_INVOKE_DATA_TYPE).toString()),
				in.getBody(String.class));
		
		if(registerParams == null && invokeParams != null) {
			
			throw new RuntimeCamelException("注册中没有注册参数,实际调用中传递了参数");
			//没有参数就不要写body了
		}else if(invokeParams == null && registerParams != null) {
			throw new RuntimeCamelException("实际调用中没有传递参数,但是注册中注册了参数");
		}else if(invokeParams == null && registerParams == null) {
			
		}else {
			
			if(registerParams.size() != invokeParams.size()) {
				throw new RuntimeCamelException("注册中的参数的数量同调用的参数值的数量不匹配");
			}
			
			String soap = (String) esbHeadRegister.get(Constant.Key.SOAP);
			
			for(int i=0; i<registerParams.size(); i++) {
				
				Map<String, Object> registerParam = registerParams.get(i);
				String key = registerParam.get(Constant.Key.KEY).toString();
				boolean isFind = false;
				
				for(Map<String, Object> invokeParam: invokeParams) {
					
					String invokeKey = (String) invokeParam.get(Constant.Key.KEY);
					
					if(invokeKey.equals(key)) {
						
						int index = soap.indexOf("?");
						String left = soap.substring(0, index);
						String right = soap.substring(index+1, soap.length());
						soap = left + invokeParam.get(Constant.Key.VALUE) + right;
						isFind = true;
						break;
					}
				}
				
				if(!isFind) {
					throw new RuntimeCamelException("没有找到key,key=" + key);
				}
			}
			
			in.setBody(soap);
		}
	}

}
