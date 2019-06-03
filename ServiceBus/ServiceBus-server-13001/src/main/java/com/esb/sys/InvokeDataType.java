package com.esb.sys;

public enum InvokeDataType {

	JSON(1, "json"),
	XML(2, "xml")
	;
	
	private int value;
	
	private String text;
	
	InvokeDataType(int value, String text){
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
