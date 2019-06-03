package com.esb.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description:调用成功表
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @Company: 
 * @author yangzl 2019-6-3
 * @version 1.00.00
 * @history:
 */
@Entity
@Table(name="esb_success_tbl")
public class EsbSuccessEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "op_id", length = 32)
	private String opId;
	
	@Column(name = "create_date")
	private Date createDate;
	
	@Column(name = "user_op_id", length=32)
	private String userOpId;
	
	@Column(name = "site_code", length = 64)
	private String siteCode;
	
	@Column(name = "service_date", length = 64)
	private String serviceCode;
	
	@Column(name = "esb_flow", length = 20)
	private Integer esbFlow;
	
	public EsbSuccessEntity() {
		
	}
	

	public EsbSuccessEntity(String opId, String userOpId, String siteCode, String serviceCode,
			Integer esbFlow) {
		super();
		this.opId = opId;
		this.userOpId = userOpId;
		this.siteCode = siteCode;
		this.serviceCode = serviceCode;
		this.esbFlow = esbFlow;
	}



	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserOpId() {
		return userOpId;
	}

	public void setUserOpId(String userOpId) {
		this.userOpId = userOpId;
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

	public Integer getEsbFlow() {
		return esbFlow;
	}

	public void setEsbFlow(Integer esbFlow) {
		this.esbFlow = esbFlow;
	}
}
