package com.esb.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;


/**
 * @Description:基础dao
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-1
 * @version 1.00.00
 * @history:
 */
public abstract class BaseDao extends JdbcDaoSupport{

	private final static Log _logger = LogFactory.getLog(BaseDao.class);
	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	public void setBaseDaoSessionFactory(DataSource druid){
		super.setDataSource(druid);
	}
	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	public void save(Object obj){
		getSession().save(obj);
	}
	
	public <T> T findById(Class<T> clazz, Serializable id){
		return (T) sessionFactory.getCurrentSession().get(clazz, id);
	}
	
	public void update(Object obj){
		getSession().update(obj);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteById(Class clazz,Serializable id){
		Object obj = getSession().get(clazz, id);
		getSession().delete(obj);
	}

	/**
	 * 根据入参创建query
	 * @param sql
	 * @return
	 */
	public Query createQuery(String sql){
		return sessionFactory.getCurrentSession().createQuery(sql);
	}
	
	/**
	 * 根据入参创建sqlQuery
	 * @param sql
	 * @return
	 */
	public SQLQuery createSqlQuery(String sql){
		return sessionFactory.getCurrentSession().createSQLQuery(sql);
	}
	
	public void clearList(List list){
		list.clear();
		list = null;
	}
	
	public void clearMap(Map map){
		map.clear();
		map = null;
	}
}
