package com.esb.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esb.dao.ESBExceptionDao;
import com.esb.entity.EsbExceptionEntity;
import com.esb.service.ESBExceptionService;

/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-1
 * @version 1.00.00
 * @history:
 */
@Service
@Transactional
public class ESBExceptionServiceImpl implements ESBExceptionService {

	
	@Autowired
	private ESBExceptionDao _eSBExceptionDao;
	
	@Override
	public void saveESBExceptionEntity(EsbExceptionEntity e) {
		_eSBExceptionDao.saveESBExceptionEntity(e);		
	}

}
