package com.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

/*
 * 访问服务器类
 */
public class AccessServer {

	/*
	 * @param path：访问路径
	 * @param handler: 处理器
	 * @param JsonType: 待转换的json数据类型(JSONArray、JSONObject)
	 */
	public static void getData(final String path,final Handler handler,final String JsonType) {
		//向服务器获取数据
    	new Thread(){
    		public void run() {
    			
    			//连接网络
    	    	try {
        			
    				URL url = new URL(path);
    				
    				//建立一个连接---Connection对象
    				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    				
    				//设置请求方式
    				conn.setRequestMethod("GET");
    				conn.setConnectTimeout(5000);
    				
    				//获得服务器返回的状态码，根据状态码判断访问是否成功
    				int code = conn.getResponseCode();
    				if (code == 200) { //成功处理请求
    					
    					InputStream in = conn.getInputStream();
    					
    					//将字符流转为字符串
    					String res = StreamTool.decodeStream(in);
    					
    					//System.out.println(data);
    					
    					//解析服务端回送的json格式的数据
    					JSONObject jsonObject = new JSONObject(res);
    					
    					//判断回送的数据是否为空
    					String result = jsonObject.getString("data");
    					
    					if (result == null) { //判断是否有数据返回
    						
    						//返回数据为空
    						Message msg = Message.obtain();
							msg.what = ParamType.NO_PARAM;
							handler.sendMessage(msg);
							
						}else {
							Object json = null;
							if ("JSON_ARRAY".equals(JsonType)) {
								
								//转为JSON数组
								json = jsonObject.getJSONArray("data");
							}
							
							if ("JSON_OBJ".equals(JsonType)) {
								
								//转为JSON对象
								json = jsonObject.getJSONObject("data");
							}
							
							//通知更新ui
							Message msg = Message.obtain();
							msg.obj = json;
							msg.what = ParamType.SUCCESS;
							handler.sendMessage(msg);
						}
    				}
    				
    			} catch (Exception e) {
    				
    				e.printStackTrace();
    				Message msg = Message.obtain();
					msg.what = ParamType.ERROR;
					handler.sendMessage(msg);
    			}
    		};
    	}.start();
	}
}
