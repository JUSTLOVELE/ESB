package com.esb.sys;

/**
 * @Description:注册类型枚举
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-2
 * @version 1.00.00
 * @history:
 */
public enum InvokeType {

	HTTP(1, "HTTP"),
	WEBSERVICE(2, "webservice"),
	SOAP(3, "soap"),
	UPLOAD(4, "upload")
	;
	private int value;
	
	private String text;
	
	InvokeType(int value, String text){
		
		this.value = value;
		this.text = text;
	}
	

	public static InvokeType getValue(int v) {
		
		for(InvokeType r: values()) {
			
			if(r.getValue() == v) {
				return r;
			}
		}
		
		return null;
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
