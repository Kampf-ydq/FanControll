package com.utils;

/*
 * 访问服务器的路径参数
 */
public interface URLPath {

	//风机
	String FAN_URL = "http://10.0.2.2:8080/fan/";
	
	//联系人
	String CON_URL = "http://10.0.2.2:8080/contact";
	
	//历史记录
	String HIS_URL = "http://10.0.2.2:8080/history";
	
	//http://localhost:8080/contact/lookContact/15001
	String CON_NUMBER_URL = "http://10.0.2.2:8080/contact/lookContact/";
}
