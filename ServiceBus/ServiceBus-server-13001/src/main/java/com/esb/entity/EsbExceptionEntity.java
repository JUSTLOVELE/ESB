package com.esb.entity;


import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Description:异常类
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @Company: 
 * @author yangzl 2019-5-14
 * @version 1.00.00
 * @history:
 */
@Entity
@Table(name="esb_exception_tbl")
public class EsbExceptionEntity {

	@Id
	@Column(name = "op_id", length = 32)
	private String opId;
	
	@Column(name = "route_id", length = 128)
	private String routeId;
	
	@Column(name = "endpoint_key", length = 1024)
	private String endpointKey;
	
	@Column(name = "endpoint_uri", length = 1024)
	private String endpointUri;
	
	//@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;
	
	@Column(name = "exception_msg", length = 1024)
	private String exceptionMsg;
	
	@Column(name = "site_code", length = 64)
	private String siteCode;
	
	@Column(name = "service_code", length = 64)
	private String serviceCode;
	
	public EsbExceptionEntity() {
		
	}

	public EsbExceptionEntity(String opId, String routeId, String endpointKey, String endpointUri, Date createDate,
			String exceptionMsg, String siteCode, String serviceCode) {
		super();
		this.opId = opId;
		this.routeId = routeId;
		this.endpointKey = endpointKey;
		this.endpointUri = endpointUri;
		this.createDate = createDate;
		this.exceptionMsg = exceptionMsg;
		this.siteCode = siteCode;
		this.serviceCode = serviceCode;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getEndpointKey() {
		return endpointKey;
	}

	public void setEndpointKey(String endpointKey) {
		this.endpointKey = endpointKey;
	}

	public String getEndpointUri() {
		return endpointUri;
	}

	public void setEndpointUri(String endpointUri) {
		this.endpointUri = endpointUri;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
}
