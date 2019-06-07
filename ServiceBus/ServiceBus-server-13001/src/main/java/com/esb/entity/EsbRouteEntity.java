package com.esb.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description:路由类
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @Company: 
 * @author yangzl 2019-5-14
 * @version 1.00.00
 * @history:
 */
@Entity
@Table(name="esb_route_tbl")
public class EsbRouteEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "op_id", length = 32)
	private String opId;
	
	@Column(name = "route_id", length = 128)
	private String routeId;
	
	@Column(name = "endpoint_uri", length = 1024)
	private String endpointUri;
	
	@Column(name = "create_user_op_id", length = 32)
	private String createUserOpId;
	
	@Column(name = "site_code", length = 128)
	private String siteCode;
	
	@Column(name = "service_code", length = 128)
	private String serviceCode;
	
	@Column(name = "route_type", length=1)
	private Integer routeType;
	
	public EsbRouteEntity() {
		
	}

	public EsbRouteEntity(String opId, String routeId, String endpointUri, String createUserOpId, String siteCode,
			String serviceCode) {
		super();
		this.opId = opId;
		this.routeId = routeId;
		this.endpointUri = endpointUri;
		this.createUserOpId = createUserOpId;
		this.siteCode = siteCode;
		this.serviceCode = serviceCode;
	}


	public String getCreateUserOpId() {
		return createUserOpId;
	}

	public void setCreateUserOpId(String createUserOpId) {
		this.createUserOpId = createUserOpId;
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

	public String getEndpointUri() {
		return endpointUri;
	}

	public void setEndpointUri(String endpointUri) {
		this.endpointUri = endpointUri;
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

	public Integer getRouteType() {
		return routeType;
	}

	public void setRouteType(Integer routeType) {
		this.routeType = routeType;
	}
}
