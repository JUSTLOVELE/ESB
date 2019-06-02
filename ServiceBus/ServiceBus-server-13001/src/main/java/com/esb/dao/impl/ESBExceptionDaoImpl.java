package com.esb.dao.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.esb.core.BaseDao;
import com.esb.dao.ESBExceptionDao;
import com.esb.entity.EsbExceptionEntity;
import com.esb.util.DateUtil;

/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-1
 * @version 1.00.00
 * @history:
 */
@Repository
public class ESBExceptionDaoImpl extends BaseDao implements ESBExceptionDao {

	private final static Log _logger = LogFactory.getLog(ESBExceptionDaoImpl.class);
	
	@Override
	public void saveESBExceptionEntity(EsbExceptionEntity e) {
		 
		StringBuffer sb = new StringBuffer();
		sb.append("insert into esb_exception_tbl ");
		sb.append("(create_date, endpoint_key, endpoint_uri, exception_msg, route_id, op_id, site_code, service_code)");
		sb.append("values (str_to_date(?, '%Y-%m-%d %H:%i:%s'), ?, ?, ?, ?, ?, ?, ?)");
		String sql = sb.toString();
		_logger.info(sql);
		this.getJdbcTemplate().update(sql, new Object[] {DateUtil.parseDateToStr(new Date(), 
				DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS), 
				e.getEndpointKey(), 
				e.getEndpointUri(), 
				e.getExceptionMsg(), 
				e.getRouteId(), 
				e.getOpId(),
				e.getSiteCode(),
				e.getServiceCode()
		});
		
		//save(e);
	}

}
