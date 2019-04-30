package com.utils;

/*
 * 对服务器返回的json数据进行转换
 * 
 * @JsonArray 将数据转为json数组，主要针对返回的多条记录
 *
 * @JsonObject 将数据转为json对象，主要针对返回单条记录
 */
public interface JsonType {

	String JsonArray = "JSON_ARRAY";
	
	String JsonObject = "JSON_OBJ";
	
}
