package com.clover.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.example.fancontroll.R;
import com.utils.StreamTool;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;

/*
 * 用户登录Activity
 */
public class MainActivity extends Activity {
	
	private EditText username;
	
	private EditText password;
	
	private Button loginBtn;
	
	private final static String PATH = "http://10.0.2.2:8080/user/";

	protected static final int SUCCESS = 0;

	protected static final int INVALID_USERNAME = 1;

	protected static final int ERROR = 2;
	
	private String wholeUrl;
	
	private String inputPwd;

	private Handler mHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				
				String msgPassword = (String) msg.obj;
				if (msgPassword.equals(inputPwd)) {
					Toast.makeText(MainActivity.this, "欢迎来到三叶草", 0).show();
					
					//进入主页面
					
				}else { //密码错误
					Toast.makeText(MainActivity.this, "密码错误", 0).show();
				}
				break;

			case INVALID_USERNAME:
				Toast.makeText(MainActivity.this, "管理员不存在", 0).show();
				break;
			case ERROR:
				Toast.makeText(MainActivity.this, "网络不可用", 0).show();
				break;
			default:
				break;
			}
		};
		
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login);
        
        loginBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				loginByUsername();
			}
		});
    }
    
    private void loginByUsername(){
    	
    	//获取登录信息
    	final String user = username.getText().toString().trim();
    	inputPwd = password.getText().toString().trim();
    	
    	//判断是否为空
    	if (TextUtils.isEmpty(user) || TextUtils.isEmpty(inputPwd)) {
			Toast.makeText(MainActivity.this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
			return;
		}
    	
    	//向服务器提交数据进行验证
    	new Thread(){
    		public void run() {
    			
    			//连接网络
    	    	try {
    	    		//url编码传输
        			wholeUrl = PATH + URLEncoder.encode(user, "UTF-8");
        			
        			//String testUrl = "https://www.baidu.com/s?wd=%E4%BB%8A%E6%97%A5%E6%96%B0%E9%B2%9C%E4%BA%8B&tn=SE_Pclogo_6ysd4c7a&sa=ire_dl_gh_logo&rsv_dl=igh_logo_pc";
    				URL url = new URL(wholeUrl);
    				
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
    					
    					//System.out.println(res);
    					
    					//解析服务端回送的json格式的数据
    					JSONObject jsonObject = new JSONObject(res);
    					
    					//判断回送的数据是否为空
    					/*
    					 * 服务器返回的数据格式: 
    					 * 	成功：{"status":200,"msg":"OK","data":{"id":3,"username":"admin","password":"000000"}}
    					 *  失败：{"status":200,"msg":"OK","data":null}
    					 */
    					String result = jsonObject.getString("data");
    					
    					//判断返回数据中的data部分是否为空
    					if (result == null) { //该管理员不存在
    						
    						//返回数据为空
    						Message msg = Message.obtain();
							msg.what = INVALID_USERNAME;
							mHandler.sendMessage(msg);
							
						}else {
							
							//取出密码
							JSONObject dataObj = jsonObject.getJSONObject("data");
							String dbPwd = dataObj.getString("password");
							
							//通知更新ui
							Message msg = Message.obtain();
							msg.obj = dbPwd;
							msg.what = SUCCESS;
							mHandler.sendMessage(msg);
						}
    				}
    				
    			} catch (Exception e) {
    				
    				e.printStackTrace();
    				Message msg = Message.obtain();
					msg.what = ERROR;
					mHandler.sendMessage(msg);
    			}
    		};
    	}.start();
    }
}
