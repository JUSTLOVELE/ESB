package com.esb.sys;

/**
 * @Description:注册类型枚举
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6-2
 * @version 1.00.00
 * @history:
 */
public enum RegisterType {

	HTTP(1, "HTTP"),
	WEBSERVICE(2, "webservice"),
	SOAP(3, "soap")
	;
	private int value;
	
	private String text;
	
	RegisterType(int value, String text){
		
		this.value = value;
		this.text = text;
	}
	

	public static RegisterType getValue(int v) {
		
		for(RegisterType r: values()) {
			
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
