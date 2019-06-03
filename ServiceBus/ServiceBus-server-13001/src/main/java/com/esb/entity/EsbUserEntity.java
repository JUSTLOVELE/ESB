package com.esb.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @Description:用户类
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @Company: 
 * @author yangzl 2019-6-3
 * @version 1.00.00
 * @history:
 */
@Entity
@Table(name="esb_user")
public class EsbUserEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "op_id", length = 32)
	private String opId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;
	
	@Column(name = "user_id", length = 32)
	private String userId;
	
	@Column(name = "user_name", length = 64)
	private String userName;
	
	@Column(name = "user_phone", length = 64)
	private String userPhone;
	
	@Column(name = "user_email", length = 64)
	private String userEmail;
	
	@Column(name = "user_level", length = 2)
	private int userLevel;
	
	@Column(name = "user_pwd", length = 64)
	private String userPwd;
	
	@Column(name = "user_status", length = 1)
	private Integer userStatus;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}
}
