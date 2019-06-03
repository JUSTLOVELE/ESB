package com.esb.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Description:工具类
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-16
 * @version 1.00.00
 * @history:
 */
public class XMLUtil {

	private final static Log _logger = LogFactory.getLog(XMLUtil.class);
	
	public static Map<String, Object> getReigsterInfo(String xml){
		
		Element root = getRootElement(xml);
		Element siteCodeElement = root.getChild(Constant.Key.SITE_CODE);
		Element serviceCodeElement = root.getChild(Constant.Key.SERVICE_CODE);
		Element urlElement = root.getChild(Constant.Key.URL);
		Element typeElement = root.getChild(Constant.Key.TYPE);
		List<Element> paramElements = root.getChildren(Constant.Key.PARAMS);
		List<Map<String, Object>> params = null;
		
		if(paramElements != null && paramElements.size() > 0) {
			
			params = new ArrayList<Map<String, Object>>();
			
			for(Element param: paramElements) {
				
				Map<String, Object> m = new HashMap<String, Object>();
				m.put(Constant.Key.TYPE, Integer.valueOf(param.getChild(Constant.Key.TYPE).getValue()));
				m.put(Constant.Key.KEY, param.getChild(Constant.Key.KEY).getValue());
				params.add(m);
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.Key.SITE_CODE, siteCodeElement.getValue());
		map.put(Constant.Key.SERVICE_CODE, serviceCodeElement.getValue());
		map.put(Constant.Key.URL, urlElement.getValue());
		map.put(Constant.Key.TYPE, Integer.valueOf(typeElement.getValue()));
		map.put(Constant.Key.PARAMS, params);
		
		return map;
	}
	 
	/**
	 * 获取xml的根对象
	 * @param xml
	 * @return
	 */
	public static Element getRootElement(String xml) {
		
		try {
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(new StringReader(xml));
			return doc.getRootElement();
		} catch (Exception e) {
			_logger.error("", e);
		}
		
		return null;
	}
	
	/**
	 * 返回错误信息
	 * @param msg
	 * @return
	 */
	public static String errorReturn(String msg) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("<root>");
		sb.append("<code>101</code>");
		sb.append("<success>false</success>");
		sb.append("<data><![CDATA[" + msg + "]]></data>");
		sb.append("</root>");
		
		return sb.toString();
	}
	
	/**
	 * 把json格式的注册信息转换为XML文本
	 * @param json
	 * @return
	 */
	public static String parseJSONToRegisterXMLInfo(JSONObject json) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("<root>");
		sb.append("<siteCode><![CDATA[" + json.getString(Constant.Key.SITE_CODE) + "]]></siteCode>");
		sb.append("<serviceCode><![CDATA[" + json.getString(Constant.Key.SERVICE_CODE) + "]]></serviceCode>");
		sb.append("<url><![CDATA[" + json.getString(Constant.Key.URL) + "]]></url>");
		sb.append("<type><![CDATA[" + json.getInt(Constant.Key.TYPE) + "]]></type>");
		sb.append("<registerType>1</registerType>");
		
		if(json.containsKey(Constant.Key.PARAMS)) {
			
			JSONArray array = json.getJSONArray(Constant.Key.PARAMS);
			
			if(array != null && array.size() > 0) {
				
				for(int i=0; i<array.size(); i++) {
					
					sb.append("<params>");
					sb.append("<key><![CDATA[" + array.getJSONObject(i).getString(Constant.Key.KEY) + "]]></key>");
					sb.append("<type><![CDATA[" + array.getJSONObject(i).getInt(Constant.Key.TYPE) + "]]></type>");
					sb.append("</params>");
				}
			}
		}

		sb.append("</root>");
		
		return sb.toString();
	}
}
