package com.esb.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.esb.core.BaseDao;
import com.esb.dao.EsbRouteDao;
import com.esb.entity.EsbRouteEntity;

/**
 * @Description:
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-6
 * @version 1.00.00
 * @history:
 */
@Repository
public class EsbRouteDaoImpl extends BaseDao implements EsbRouteDao {

	private final static Log _logger = LogFactory.getLog(EsbRouteDaoImpl.class);
	
	@Override
	public void saveEsbRouteEntity(EsbRouteEntity e) {
		
		if(queryisExistRoute(e.getRouteId())) {
			
			String sql = "update esb_route_tbl set endpoint_uri = ? where route_id = ?";
			_logger.info(sql);
			this.getJdbcTemplate().update(sql, new Object[] {e.getEndpointUri(), e.getRouteId()});
		}else {
			this.save(e);
		}
	}

	@Override
	public boolean queryisExistRoute(String routeId) {
		
		String sql = "SELECT COUNT(*) num FROM esb_route_tbl a WHERE a.`route_id` = ?";
		_logger.info(sql);
		Integer num = this.getJdbcTemplate().queryForObject(sql, new Object[] {routeId}, Integer.class);
		
		if(num == 0) {
			return false;
		}else {
			return true;
		}
	}

	@Override
	public void deleteRouteInfo(String routeId) {
		
		String sql = "delete from esb_route_tbl where route_id = ?";
		_logger.info(sql);
		this.getJdbcTemplate().update(sql, new Object[] {routeId});
	}

	@Override
	public List<Map<String, Object>> queryRoute(String routeId) {
		
		String sql = "SELECT a.`endpoint_uri` endpointURI, site_code siteCode, service_code serviceCode,route_type routeType FROM esb_route_tbl a WHERE a.`route_id` = ?";
		_logger.info(sql);
		return this.getJdbcTemplate().queryForList(sql, new Object[] {routeId});
	}

	@Override
	public boolean queryisExistRoute(String userOpId, String siteCode, String serviceCode) {
		
		String sql = "SELECT COUNT(*) num FROM esb_route_tbl a WHERE a.`create_user_op_id` = ? AND a.`site_code` = ? AND a.`service_code` = ?";
		_logger.info(sql);
		Integer num = this.getJdbcTemplate().queryForObject(sql, new Object[] {userOpId, siteCode, serviceCode}, Integer.class);
		
		if(num == 0) {
			return false;
		}else {
			return true;
		}
	}

}
