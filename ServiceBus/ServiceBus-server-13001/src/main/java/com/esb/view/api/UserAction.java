package com.esb.view.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.esb.core.Base;
import com.esb.service.UserService;
import com.esb.util.Constant;
import com.esb.util.UUIDUtil;
import com.esb.util.encrypt.RSA;

/**
 * @Description:调用类
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-16
 * @version 1.00.00
 * @history:
 */
@RequestMapping("/userAction")
@Controller
public class UserAction extends Base {

	private final static Log _logger = LogFactory.getLog(UserAction.class);
	
	@Autowired
	private UserService _userService;
	
	@RequestMapping(value="/register", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String register(String userName, String userPhone, String userEmail, String pwd) {
		
		try {
			_userService.Register(userName, userPhone, userEmail, pwd);
			Map<String, Object> map = new HashMap<String, Object>();
			String token = userPhone + Constant.SPLIT_SIGN + pwd;
			//公钥加密,私钥解密
			map.put(Constant.Key.TOKEN, RSA.encryptByPublic(token, Constant.Constant_PUBLIC_KEY));
			map.put(Constant.Key.CODE, Constant.Status.SUCCESS_CODE);
			return getJSON(map);
			
		} catch (Exception e) {
			
			_logger.error("", e);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constant.Key.DESC, e.getMessage());
			map.put(Constant.Key.CODE, Constant.Status.FAILURE_CODE);
			return getJSON(map);
		}
		
	}
}
