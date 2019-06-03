package com.esb.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.esb.core.BaseDao;
import com.esb.dao.UserDao;
import com.esb.entity.EsbUserEntity;
import com.esb.util.Constant;
import com.esb.util.DateUtil;
import com.esb.util.UUIDUtil;
import com.esb.util.encrypt.TripleDesUtil;

/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-3
 * @version 1.00.00
 * @history:
 */
@Repository
public class UserDaoImpl extends BaseDao implements UserDao {
	
	private final static Log _logger = LogFactory.getLog(UserDaoImpl.class);


	@Override
	public EsbUserEntity userLogin(String userId, String pwd) {
		
		pwd = TripleDesUtil.encode(pwd, Constant.ENCRYPT_KEY);
		String hql = "From EsbUserEntity where userId =:userId and userPwd =:userPwd and user_status = 1";
		List<EsbUserEntity> l = this.createQuery(hql).setString("userId", userId).setString("userPwd", pwd).list();
		
		if(l != null && l.size() == 1) {
			return l.get(0);
		}
		
		return null;
	}

	@Override
	public void Register(String userName, String userPhone, String userEmail, String pwd) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("insert into esb_user ");
		sb.append("(op_id, create_date, user_id, user_name, user_phone, user_email, user_status, pwd)");
		sb.append("value(?,(str_to_date(?, '%Y-%m-%d %H:%i:%s'),?,?,?,?,?,?)");
		String sql = sb.toString();
		_logger.info(sql);
		pwd = TripleDesUtil.encode(pwd, Constant.ENCRYPT_KEY);
		
		this.getJdbcTemplate().update(sql, new Object[] {
				UUIDUtil.getUUID(),
				DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS),
				userPhone,
				userName,
				userPhone,
				userEmail,
				0,
				pwd
		});
	}

}
