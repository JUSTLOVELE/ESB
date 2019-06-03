package com.esb.service.impl;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esb.dao.UserDao;
import com.esb.entity.EsbUserEntity;
import com.esb.service.UserService;


/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-3
 * @version 1.00.00
 * @history:
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao _userDao;

	private final static Log _logger = LogFactory.getLog(UserServiceImpl.class);
	
	@Override
	public EsbUserEntity userLogin(String userId, String pwd) {
		
		try {
			return _userDao.userLogin(userId, pwd);
		} catch (Exception e) {
			_logger.error("", e);
			return null;
		}
	}

	@Override
	public void Register(String userName, String userPhone, String userEmail, String pwd) {
		_userDao.Register(userName, userPhone, userEmail, pwd);
	}
}
