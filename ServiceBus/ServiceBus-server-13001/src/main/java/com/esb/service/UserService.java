package com.esb.service;

import com.esb.entity.EsbUserEntity;

/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-3
 * @version 1.00.00
 * @history:
 */
public interface UserService {
	
	/**
	 * 注册
	 * @param userName
	 * @param userPhone
	 * @param userEmail
	 */
	public void Register(String userName, String userPhone, String userEmail, String pwd);

	/**
	 * 用户登录
	 * @param name
	 * @param pwd
	 * @return
	 */
	public EsbUserEntity userLogin(String userId, String pwd);
}
