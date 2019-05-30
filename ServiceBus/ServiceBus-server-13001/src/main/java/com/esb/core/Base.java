package com.esb.core;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.esb.util.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Description:启动类
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-15
 * @version 1.00.00
 * @history:
 */
public abstract class Base {
	
	private final static Log _logger = LogFactory.getLog(Base.class);
	
	public String returnErrorCode(String desc) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.Key.CODE, Constant.Status.FAILURE_CODE);
		map.put(Constant.Key.DESC, desc);
		map.put(Constant.Key.SUCCESS, false);
		return getJSON(map);
	}
	
	public String returnGeneralJSONCode(int code, String desc, boolean success) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.Key.CODE, code);
		map.put(Constant.Key.DESC, desc);
		map.put(Constant.Key.SUCCESS, success);
		return getJSON(map);
	}
	
	public void renderJson(HttpServletResponse httpServletResponse, String json) {
		
		httpServletResponse.setContentType("text/html; charset=utf-8");
		
		try {
			
			_logger.debug(json);
			httpServletResponse.getWriter().print(json);
		} catch (Exception e) {
			
			_logger.debug(e.getMessage());
		}
		
		json = null;
	}
	
	public void renderObject(HttpServletResponse httpServletResponse,Object result) {
		renderJson(httpServletResponse,getJSON(result));
	}
	
	public String rederAjax(boolean success, String msg, boolean status){
		
		Map<String, Object> displayMap = new HashMap<String, Object>();
		displayMap.put("success", success);
		displayMap.put("msg", msg);
		displayMap.put("status", status);
		String json = getJSON(displayMap);
		clearMap(displayMap);
		return json;
	}
	
	@SuppressWarnings("rawtypes")
	public void clearMap(Map map){
		map.clear();
		map = null;
	}
	
	public String renderResult(Object result, boolean flag) {
		
		Map<String, Object> displayMap = new HashMap<String, Object>();
		displayMap.put("success", flag);
		displayMap.put("result", result);
		String json = getJSON(displayMap);
		clearMap(displayMap);
		
		return json;
	}
	
	public String rederAjax(boolean success, String msg){
		
		Map<String, Object> displayMap = new HashMap<String, Object>();
		displayMap.put("success", success);
		displayMap.put("msg", msg);
		String json = getJSON(displayMap);
		clearMap(displayMap);
		return json;
	}
	
	public String getJSON(Object obj) {
		
		ObjectMapper mapper = new ObjectMapper();
		String msg = "";
		try {
			msg = mapper.writeValueAsString(obj);
		} catch (Exception e) {
			_logger.debug(e.getMessage());
		}
		_logger.info(msg);
		return msg ;
	}
}
