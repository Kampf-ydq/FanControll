package com.clover.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.clover.adapter.ContactLVAdapter;
import com.clover.adapter.FanInforLVAdapter;
import com.clover.adapter.ViewPageAdapter;
import com.clover.entity.Contact;
import com.clover.entity.FanParam;
import com.example.fancontroll.R;
import com.utils.AccessServer;
import com.utils.JsonType;
import com.utils.ParamType;
import com.utils.URLPath;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 应用主页Activity
 */
public class HomeActivity extends Activity implements OnClickListener,
		OnPageChangeListener {
	
	// 底部3个LinearLayout
	private LinearLayout ll_fan;
	private LinearLayout ll_contact;
	private LinearLayout ll_more;

	// 底部3个ImageView
	private ImageView iv_fan;
	private ImageView iv_contact;
	private ImageView iv_more;

	// 底部3个菜单标题
	private TextView tv_fan;
	private TextView tv_contact;
	private TextView tv_more;

	// 中间内容区域
	private ViewPager viewPager;

	// ViewPager适配器ContentAdapter
	private ViewPageAdapter adapter;

	private List<View> views;
	
	private List<FanParam> fanParamList = new ArrayList<FanParam>();
	private List<Contact> contactsList = new ArrayList<Contact>();
	
	/*
	 * 子页面控件
	 */
	//page_01控件
	private Spinner spinner;
	private ListView lv_fan_param;
	
	//page_02控件
	private ListView lv_contact;
	
	//page_03控件
	private LinearLayout ll_history;
	private LinearLayout ll_table;
	private LinearLayout ll_about;
	
	private Handler mHandler = new Handler(){

			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case ParamType.SUCCESS:
					//显示风机各项参数
					/*
					 * 【扩展---MVC模式】
					 * M: 绑定到屏幕上的数据(Model)
					 * V: 视图，这里为ListView(View)
					 * C: 适配器，控制数据在平面上显示的方式，这里为ListViewAdapter(Controller)
					 */
					initFanInfor(msg.obj);
					lv_fan_param.setAdapter(new FanInforLVAdapter(HomeActivity.this,R.layout.param_item,fanParamList));
					break;
					
				case ParamType.NO_PARAM:
					Toast.makeText(HomeActivity.this, "服务器尚无该风机的参数信息", 0).show();
					break;
					
				case ParamType.ERROR:
					Toast.makeText(HomeActivity.this, "网络出错", 0).show();
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
		setContentView(R.layout.page_main);

		// 初始化控件
		initView();

		// 初始化事件
		initEvent();
	}

	// 初始化控件
	private void initView() {

		this.ll_fan = (LinearLayout) findViewById(R.id.ll_fan);
		this.ll_contact = (LinearLayout) findViewById(R.id.ll_contact);
		this.ll_more = (LinearLayout) findViewById(R.id.ll_more);

		this.iv_fan = (ImageView) findViewById(R.id.iv_fan);
		this.iv_contact = (ImageView) findViewById(R.id.iv_contact);
		this.iv_more = (ImageView) findViewById(R.id.iv_more);

		this.tv_fan = (TextView) findViewById(R.id.tv_fan);
		this.tv_contact = (TextView) findViewById(R.id.tv_contact);
		this.tv_more = (TextView) findViewById(R.id.tv_more);

		// 中间内容区域ViewPager
		this.viewPager = (ViewPager) findViewById(R.id.vp_content);

		// 适配器
		View page_01 = View.inflate(HomeActivity.this, R.layout.page_01, null);
		View page_02 = View.inflate(HomeActivity.this, R.layout.page_02, null);
		View page_03 = View.inflate(HomeActivity.this, R.layout.page_03, null);

		views = new ArrayList<View>();
		views.add(page_01);
		views.add(page_02);
		views.add(page_03);

		this.adapter = new ViewPageAdapter(views);
		viewPager.setAdapter(adapter);
		
		//初始化每个子页面的控件
		spinner = (Spinner) page_01.findViewById(R.id.number_spinner);
		lv_fan_param = (ListView) page_01.findViewById(R.id.lv_fan_param);
		
		lv_contact = (ListView) page_02.findViewById(R.id.lv_contact);
		
		ll_history = (LinearLayout) page_03.findViewById(R.id.ll_history);
		ll_table = (LinearLayout) page_03.findViewById(R.id.ll_table);
		ll_about = (LinearLayout) page_03.findViewById(R.id.ll_about);
		
	}

	// 初始化底部按钮事件
	private void initEvent() {

		// 设置按钮监听
		ll_fan.setOnClickListener(this);
		ll_contact.setOnClickListener(this);
		ll_more.setOnClickListener(this);

		// 设置ViewPager滑动监听
		viewPager.setOnPageChangeListener(this);
		
		//每个子页面的事件监听
		//【1】风机编号选择事件监听
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, 
                    int pos, long id) {
           
                String[] number = getResources().getStringArray(R.array.number);
                getParamFromServer(number[pos]);
                
                //清空List,为显示下一个风机参数留存空间
				fanParamList.clear();
                //Toast.makeText(HomeActivity.this, "你点击的是:"+number[pos], 2000).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
		
		//【2】联系人信息查看点击事件
		lv_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//获取当前点击的联系人对象
				Contact contact = contactsList.get(position);
				
				//利用Bundle传递数据
				Bundle bundle = new Bundle();
				bundle.putString("name", contact.getContactName());
				bundle.putString("phone", contact.getContactPhone());
				bundle.putString("email", contact.getContactEmail());
				bundle.putString("room", contact.getContactRoom());
				
				//启动下一个活动
				Intent intent = new Intent(HomeActivity.this, ContactInfor.class);
				intent.putExtras(bundle);
				
				startActivity(intent);
				
			}
		});
		
		//【3】更多页菜单监听事件
		ll_history.setOnClickListener(moreHomeListener);
	}

	OnClickListener moreHomeListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_history:
				Intent toHistory = new Intent();
				toHistory.setClass(HomeActivity.this, HistoryActivity.class);
				startActivity(toHistory);
				break;
			case R.id.ll_table:
							
				break;
			case R.id.ll_about:
				
				break;

			default:
				break;
			}
			
		}
	};
	
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		
		restartButton();
		
		//当前view被选择的时候,改变底部菜单图片，文字颜色
		switch (arg0) {
		case 0:
			iv_fan.setImageResource(R.drawable.fan_pressed);
			tv_fan.setTextColor(Color.parseColor("#629540"));
			break;

		case 1:
			iv_contact.setImageResource(R.drawable.person_pressed);
			tv_contact.setTextColor(Color.parseColor("#629540"));
			
			//访问服务器数据
			AccessServer.getData(URLPath.CON_URL,ConHandler,JsonType.JsonArray);
			contactsList.clear();
			
			break;

		case 2:
			iv_more.setImageResource(R.drawable.more_pressed);
			tv_more.setTextColor(Color.parseColor("#629540"));
			
			break;

		default:
			break;
		}

	}

	private Handler ConHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ParamType.SUCCESS:
				initConInfor(msg.obj);
				lv_contact.setAdapter(new ContactLVAdapter(HomeActivity.this, R.layout.contact_item, contactsList));
				break;

			case ParamType.NO_PARAM:
				Toast.makeText(HomeActivity.this, "服务器尚无该风机的参数信息", 0).show();
				break;
				
			case ParamType.ERROR:
				Toast.makeText(HomeActivity.this, "网络出错", 0).show();
				break;
				
			default:
				break;
			}
		};
	};
	
	
	@Override
	public void onClick(View arg0) {
		
		restartButton();
		
		switch (arg0.getId()) {
		case R.id.ll_fan:
			iv_fan.setImageResource(R.drawable.fan_pressed);
			tv_fan.setTextColor(Color.parseColor("#629540"));
			viewPager.setCurrentItem(0);
			break;

		case R.id.ll_contact:
			iv_contact.setImageResource(R.drawable.person_pressed);
			tv_contact.setTextColor(Color.parseColor("#629540"));
			viewPager.setCurrentItem(1);
			break;

		case R.id.ll_more:
			iv_more.setImageResource(R.drawable.more_pressed);
			tv_more.setTextColor(Color.parseColor("#629540"));
			viewPager.setCurrentItem(2);
			break;

		default:
			break;
		}
	}
	
	private void restartButton(){
		//ImageView设置为黑色
		iv_fan.setImageResource(R.drawable.fan);
		iv_contact.setImageResource(R.drawable.personal);
		iv_more.setImageResource(R.drawable.more);
		
		//TextView设置为黑色
		tv_fan.setTextColor(Color.parseColor("#000000"));
		tv_contact.setTextColor(Color.parseColor("#000000"));
		tv_more.setTextColor(Color.parseColor("#000000"));
		
	}
	
	private void getParamFromServer(final String fan_number){
	
    	//判断是否为空
    	if (TextUtils.isEmpty(fan_number) || TextUtils.isEmpty(fan_number)) {
			Toast.makeText(HomeActivity.this, "请选择风机编号", Toast.LENGTH_SHORT).show();
			return;
		}
    	
    	//向服务器获取风机参数
		String wholeUrl;
		try {
			wholeUrl = URLPath.FAN_URL + URLEncoder.encode(fan_number, "UTF-8");
			AccessServer.getData(wholeUrl, mHandler,JsonType.JsonObject);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
 
    }

    private void initFanInfor(Object fanParam){
    	JSONObject JsonObj = (JSONObject) fanParam; 
    	try {
			FanParam pressure = new FanParam("电压", JsonObj.getString("pressure"));
			fanParamList.add(pressure);
			
			FanParam electric = new FanParam("电流", JsonObj.getString("electric"));
			fanParamList.add(electric);
			
			FanParam activePower = new FanParam("有功功率", JsonObj.getString("activePower"));
			fanParamList.add(activePower);
			
			FanParam reactivePower = new FanParam("无功功率", JsonObj.getString("reactivePower"));
			fanParamList.add(reactivePower);
			
			FanParam windSpeed = new FanParam("风速", JsonObj.getString("windSpeed"));
			fanParamList.add(windSpeed);
			
			FanParam windAngle = new FanParam("风向", JsonObj.getString("windAngle"));
			fanParamList.add(windAngle);
			
			FanParam rotationSpeed = new FanParam("转速", JsonObj.getString("rotationSpeed"));
			fanParamList.add(rotationSpeed);
			
			FanParam temperature = new FanParam("温度", JsonObj.getString("temperature"));
			fanParamList.add(temperature);
			
			FanParam cableAngle = new FanParam("电缆扭转角度", JsonObj.getString("cableAngle"));
			fanParamList.add(cableAngle);
			
			FanParam dynamoTem = new FanParam("发电机温度", JsonObj.getString("dynamoTem"));
			fanParamList.add(dynamoTem);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }

    private void initConInfor(Object conInfor){
    	//返回json数组
    	JSONArray jsonArray = (JSONArray) conInfor;
    	
    	//每一条联系人记录
    	JSONObject item; 
    	try {
    		for (int i = 0; i < jsonArray.length(); i++) {
    			item = (JSONObject) jsonArray.get(i);
    			Contact contact = new Contact(item.getString("name"),item.getString("number"),item.getString("phone"),
    								  item.getString("email"),item.getString("room"));
    			
    			contactsList.add(contact);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
