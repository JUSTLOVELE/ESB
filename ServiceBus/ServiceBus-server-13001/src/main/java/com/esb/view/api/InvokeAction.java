package com.esb.view.api;

import javax.servlet.http.HttpServletRequest;

import org.apache.camel.RuntimeCamelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.esb.core.Base;
import com.esb.entity.EsbUserEntity;
import com.esb.service.UserService;
import com.esb.service.impl.InvokeService;
import com.esb.util.Constant;
import com.esb.util.JSONUtil;
import com.esb.util.encrypt.RSA;

/**
 * @Description:调用类
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-16
 * @version 1.00.00
 * @history:
 */
@RequestMapping("/invokeAction")
@Component
public class InvokeAction extends Base{
	
	@Autowired
	private InvokeService _invokeService;
	
	@Autowired
	private UserService _userService;
	
	private EsbUserEntity getUser(String token) {
		
		token = RSA.decryptByPrivate(token, Constant.Constant_PRIVATE_KEY);
		
		if(token == null || "".equals(token)) {
			throw new RuntimeException("token错误");
		}
		
		String[] array = token.split(Constant.SPLIT_SIGN);
		
		if(array == null || array.length != 2) {
			throw new RuntimeException("密码格式错误导致token解析失败");
		}
		
		EsbUserEntity user = _userService.userLogin(array[0], array[1]);
		return user;
	}
	
	private final static Log _logger = LogFactory.getLog(InvokeAction.class);
	
	@RequestMapping(value="/registerWithJson", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String registerWithJson(String param, HttpServletRequest request) {
		
		_logger.info(param);
		EsbUserEntity user = null;
		
		try {
			user = getUser(request.getHeader(Constant.HeadParam.AUTHORIZATION));
			
			if(user == null) {
				return JSONUtil.errorReturn("登录失败");
			}
			
		} catch (Exception e) {
			_logger.error("", e);
			return JSONUtil.errorReturn(e.getMessage());
		}
		
		return _invokeService.registerWithJson(param, user);
	}
	
	@RequestMapping(value = "/registerWithXML", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String registerWithXML(String param, HttpServletRequest request) {
		
		_logger.info(param);
		EsbUserEntity user = null;
		
		try {
			user = getUser(request.getHeader(Constant.HeadParam.AUTHORIZATION));
			
			if(user == null) {
				return JSONUtil.errorReturn("登录失败");
			}
			
		} catch (Exception e) {
			_logger.error("", e);
			return JSONUtil.errorReturn(e.getMessage());
		}
		
		return _invokeService.registerWithXML(param, user);
	}
	
	@RequestMapping(value = "/helloworld", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String helloworld() {
		return "无参数测试";
	}
}
