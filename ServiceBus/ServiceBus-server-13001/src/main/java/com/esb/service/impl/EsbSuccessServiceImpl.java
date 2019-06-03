package com.esb.service.impl;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esb.dao.EsbSuccessDao;
import com.esb.entity.EsbSuccessEntity;
import com.esb.service.EsbSuccessService;

/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-3
 * @version 1.00.00
 * @history:
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class EsbSuccessServiceImpl implements EsbSuccessService {
	
	private final static Log _logger = LogFactory.getLog(EsbSuccessServiceImpl.class);
	
	@Autowired
	private EsbSuccessDao _esbSuccessDao;

	@Override
	public void saveEsbSuccessEntity(EsbSuccessEntity e) {
		
		try {
			_esbSuccessDao.saveEsbSuccessEntity(e);
		} catch (Exception e2) {
			_logger.error("", e2);
		}
	}

}
