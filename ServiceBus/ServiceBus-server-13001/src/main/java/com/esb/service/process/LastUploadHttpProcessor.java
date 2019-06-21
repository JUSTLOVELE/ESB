package com.esb.service.process;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
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
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-15
 * @version 1.00.00
 * @history:
 */
@Component
public class LastUploadHttpProcessor implements Processor {
	
	private final static Log _logger = LogFactory.getLog(LastUploadHttpProcessor.class);
	
	@Autowired
	private ZookeeperService _zookeeperService;
	
	/**
	 * 解析并获取params参数
	 * @param invokeDataType
	 * @param data
	 * @return
	 */
	private Map<String, Object> getInvokeParams(int invokeDataType, String data){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(invokeDataType == InvokeDataType.JSON.getValue()) {
			
			JSONObject json = JSONObject.fromObject(data);
			
			if(json.containsKey(Constant.Key.PARAMS)) {
				
				JSONArray array = json.getJSONArray(Constant.Key.PARAMS);
				List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
				
				for(int i=0; i<array.size(); i++) {
					
					JSONObject param = array.getJSONObject(i);
					Map<String, Object> m = new LinkedHashMap<String, Object>();
					m.put(Constant.Key.VALUE, param.get(Constant.Key.VALUE));
					m.put(Constant.Key.KEY, param.get(Constant.Key.KEY));
					params.add(m);
				}
				
				map.put(Constant.Key.PARAMS, params);
			}
			
			if(json.containsKey(Constant.Key.FILES)) {
				
				JSONArray array = json.getJSONArray(Constant.Key.FILES);
				List<Map<String, Object>> files = new ArrayList<Map<String, Object>>();
				
				for(int i=0; i<array.size(); i++) {
					
					JSONObject param = array.getJSONObject(i);
					Map<String, Object> m = new LinkedHashMap<String, Object>();
					m.put(Constant.Key.FILE_NAME, param.get(Constant.Key.FILE_NAME));
					m.put(Constant.Key.FILE_TYPE, param.get(Constant.Key.FILE_TYPE));
					files.add(m);
				}
				
				map.put(Constant.Key.FILES, files);
			}
			
			return map;
			
		}else if(invokeDataType == InvokeDataType.XML.getValue()) {
			
			Element root = XMLUtil.getRootElement(data);
			List<Element> paramElements = root.getChildren(Constant.Key.PARAMS);
			List<Element> fileElements = root.getChildren(Constant.Key.FILES);
			
			if(paramElements != null && paramElements.size() > 0) {
				
				List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
				
				for(Element param: paramElements) {
					
					Map<String, Object> m = new LinkedHashMap<String, Object>();
					m.put(Constant.Key.VALUE, param.getChild(Constant.Key.VALUE).getValue());
					m.put(Constant.Key.KEY, param.getChild(Constant.Key.KEY).getValue());
					params.add(m);
				}
				
				map.put(Constant.Key.PARAMS, params);
			}
			
			if(fileElements != null && fileElements.size() > 0) {
				
				List<Map<String, Object>> files = new ArrayList<Map<String, Object>>();
				
				for(Element param: fileElements) {
					
					Map<String, Object> m = new LinkedHashMap<String, Object>();
					m.put(Constant.Key.FILE_NAME, param.getChild(Constant.Key.FILE_NAME).getValue());
					m.put(Constant.Key.FILE_TYPE, param.getChild(Constant.Key.FILE_TYPE).getValue());
					files.add(m);
				}
				
				map.put(Constant.Key.FILES, files);
			}
			
			return map;
		}else {
			throw new RuntimeCamelException("LastHttpRouteProcessor.getInvokeParams invokeDataType数值异常 ");
		}
	}
	
	@Override
	public void process(Exchange exchange) throws Exception {
		// 获取zookeeper资源
		Message in = exchange.getIn();
		Map<String, Object> head = in.getHeaders();
		String siteCode = head.get(Constant.HeadParam.ESB_SITE_CODE).toString();
		String serviceCode = head.get(Constant.HeadParam.ESB_SERVICE_CODE).toString();
		head.put(Constant.HeadParam.ESB_ROUTE_ID, Constant.HeadParam.ESB_ROUTE_ID);
		String zkPath = Constant.Key.PATH_ROOT + "/" + siteCode + "/" + serviceCode;
		String registerInfo = _zookeeperService.getData(zkPath);
		// 要比较传进来的调用参数和注册的调用参数数目和key是否一致
		Map<String, Object> esbHeadRegister = XMLUtil.getReigsterInfo(registerInfo);
		List<Map<String, Object>> registerParams = (List<Map<String, Object>>) esbHeadRegister.get(Constant.Key.PARAMS);
		Map<String, Object> p = getInvokeParams(
				Integer.valueOf(head.get(Constant.HeadParam.ESB_INVOKE_DATA_TYPE).toString()),
				exchange.getIn().getHeader(Constant.Key.PARAM).toString());
		 
		List<Map<String, Object>> invokeParams = (List<Map<String, Object>>) p.get(Constant.Key.PARAMS);
		List<Map<String, Object>> invokeFiles = (List<Map<String, Object>>) p.get(Constant.Key.FILES);
		String fileName = (String) invokeFiles.get(0).get(Constant.Key.FILE_NAME);
		byte[] array = (byte[]) exchange.getIn().getBody();
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
		multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		ContentBody c = new ByteArrayBody(array, fileName);
		FormBodyPart b = FormBodyPartBuilder.create(fileName, c).build();
		multipartEntityBuilder.addPart(b);

		if (registerParams == null && invokeParams != null) {

			throw new RuntimeCamelException("注册中没有注册参数,实际调用中传递了参数");
			// 没有参数就不要写body了
		} else if (invokeParams == null && registerParams != null) {
			throw new RuntimeCamelException("实际调用中没有传递参数,但是注册中注册了参数");
		} else if (invokeParams == null && registerParams == null) {

		} else {

/*			if (registerParams.size() != invokeParams.size()) {
				throw new RuntimeCamelException("注册中的参数的数量同调用的参数值的数量不匹配");
			}*/
			
			for (Map<String, Object> registerParam : registerParams) {

				String key = registerParam.get(Constant.Key.KEY).toString();
				Integer registerType = (Integer) registerParam.get(Constant.Key.TYPE);
				
				if(registerType == 1) {
					
					boolean isFind = false;
					
					for (Map<String, Object> invokeParam : invokeParams) {

						String invokeKey = (String) invokeParam.get(Constant.Key.KEY);

						if (invokeKey.equals(key)) {
							
							isFind = true;
							multipartEntityBuilder.addTextBody(key, invokeParam.get(Constant.Key.VALUE).toString());
							break;
						}
					}

					if (!isFind) {
						throw new RuntimeCamelException("没有找到key,key=" + key);
					}
				}
			}

			//in.setHeader(Exchange.CONTENT_TYPE, "application/x-www-form-urlencoded");
			in.setBody(multipartEntityBuilder.build());
		}
	
		/*Set<String> attachmentNames = exchange.getIn().getAttachmentNames();
		InputStream inputStream = exchange.getIn().getAttachment("file").getInputStream();
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); 
		int rc = 0; 
		byte[] buff = new byte[100];
		while ((rc = inputStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);  
		}

		byte[] a = swapStream.toByteArray();

		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
		multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		ContentBody c = new ByteArrayBody(a, "11.jpg");
		FormBodyPart b = FormBodyPartBuilder.create("fileTest", c).build();
		multipartEntityBuilder.addPart(b).addTextBody("param", "xxxxxxx");
		exchange.getIn().setBody(multipartEntityBuilder.build());*/
	}

}
