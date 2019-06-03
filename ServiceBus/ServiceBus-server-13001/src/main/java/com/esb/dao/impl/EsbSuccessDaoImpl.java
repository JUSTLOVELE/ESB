package com.esb.dao.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.esb.core.BaseDao;
import com.esb.dao.EsbSuccessDao;
import com.esb.entity.EsbSuccessEntity;
import com.esb.util.DateUtil;

/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-3
 * @version 1.00.00
 * @history:
 */
@Repository
public class EsbSuccessDaoImpl extends BaseDao implements EsbSuccessDao {
	
	private final static Log _logger = LogFactory.getLog(EsbSuccessDaoImpl.class);

	@Override
	public void saveEsbSuccessEntity(EsbSuccessEntity e) {
		
		String date = DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
		StringBuffer sb = new StringBuffer();
		sb.append("insert into esb_success_tbl ");
		sb.append("(op_id, create_date, site_code, service_code, esb_flow, user_op_id)");
		sb.append("value('" + e.getOpId() + "',str_to_date('" + date + "', '%Y-%m-%d %H:%i:%s'),'" + e.getSiteCode() + 
				"','" + e.getServiceCode() +"', " + e.getEsbFlow() + ",'" + e.getUserOpId() + "')");
		String sql = sb.toString();
		_logger.info(sql);
		
		this.getJdbcTemplate().update(sql);
	}

}
