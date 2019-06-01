package com.esb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.esb.core.Base;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6.1
 * @version 1.00.00
 * @history:
 */
public class JSONUtil {
	
	private final static Log _logger = LogFactory.getLog(JSONUtil.class);

	public static String errorReturn(String msg) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.Key.CODE, Constant.Status.FAILURE_CODE);
		map.put(Constant.Key.SUCCESS, false);
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(Constant.Key.DESC, msg);
		datas.add(data);
		map.put(Constant.Key.DATA, datas);
		
		return getJSON(map);
	}
	
	
	public static String getJSON(Object obj) {
		
		ObjectMapper mapper = new ObjectMapper();
		String msg = "";
		try {
			msg = mapper.writeValueAsString(obj);
		} catch (Exception e) {
			_logger.debug(e.getMessage());
		}
		_logger.info(msg);
		return msg;
	}
}
