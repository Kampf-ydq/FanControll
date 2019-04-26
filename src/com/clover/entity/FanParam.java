package com.clover.entity;

/*
 * @FanParam
 *   
 *   参数单位均为国际单位
 */
public class FanParam {

	private String paramName;
	
	private String paramVal;

	public String getParamName() {
		return paramName;
	}

	
	
	public FanParam(String paramName, String paramVal) {
		super();
		this.paramName = paramName;
		this.paramVal = paramVal;
	}



	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamVal() {
		return paramVal;
	}

	public void setParamVal(String paramVal) {
		this.paramVal = paramVal;
	}
	
	
	
	
}
