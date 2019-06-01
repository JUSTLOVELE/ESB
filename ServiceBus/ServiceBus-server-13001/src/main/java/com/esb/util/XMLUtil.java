package com.esb.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
		sb.append("<orgCode><![CDATA[" + json.getString(Constant.Key.ORG_CODE) + "]]></orgCode>");
		sb.append("<serviceCode><![CDATA[" + json.getString(Constant.Key.SERVICE_CODE) + "]]></serviceCode>");
		sb.append("<url><![CDATA[" + json.getString(Constant.Key.URL) + "]]></url>");
		sb.append("<type><![CDATA[" + json.getInt(Constant.Key.TYPE) + "]]></type>");
		sb.append("<registerType>1</registerType>");
		
		if(json.containsKey(Constant.Key.PARAMS)) {
			
			JSONArray array = json.getJSONArray(Constant.Key.PARAMS);
			
			if(array != null && array.size() > 0) {
				
				for(int i=0; i<array.size(); i++) {
					
					sb.append("<param>");
					sb.append("<key><![CDATA[" + array.getJSONObject(i).getInt(Constant.Key.KEY) + "]]></key>");
					sb.append("<type><![CDATA[" + array.getJSONObject(i).getInt(Constant.Key.TYPE) + "]]></type>");
					sb.append("</param>");
				}
			}
		}

		sb.append("</root>");
		
		return sb.toString();
	}
}
