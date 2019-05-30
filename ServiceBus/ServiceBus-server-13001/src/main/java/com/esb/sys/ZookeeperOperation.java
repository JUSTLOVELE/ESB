package com.esb.sys;

public enum ZookeeperOperation {

	ADD(0, "新增"),
	DELETE(1, "删除"),
	EDIT(2, "修改"),
	UPDATE(3, "更新")
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
