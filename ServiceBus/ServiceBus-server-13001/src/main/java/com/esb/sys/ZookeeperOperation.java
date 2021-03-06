package com.esb.sys;

/**
 * @Description:zookeeper操作枚举
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-2
 * @version 1.00.00
 * @history:
 */
public enum ZookeeperOperation {

	ADD(0, "新增"),
	DELETE(1, "删除"),
	EDIT(2, "修改"),
	UPDATE(3, "更新"),
	INIT(4,"初始化")
	;
	
	private int value;
	
	private String text;
	
	ZookeeperOperation(int value, String text){
	
		this.value = value;
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
