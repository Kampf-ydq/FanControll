package com.clover.activity;

import java.util.ArrayList;
import java.util.List;

import com.clover.adapter.ContentAdapter;
import com.example.fancontroll.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
	private ContentAdapter adapter;

	private List<View> views;
	
	private Spinner spinner;
	
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

		this.adapter = new ContentAdapter(views);
		viewPager.setAdapter(adapter);
		
		//初始化每个子页面的控件
		spinner = (Spinner) page_01.findViewById(R.id.number_spinner);
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
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, 
                    int pos, long id) {
           
                String[] number = getResources().getStringArray(R.array.number);
                Toast.makeText(HomeActivity.this, "你点击的是:"+number[pos], 2000).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
	}

	
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
			break;

		case 2:
			iv_more.setImageResource(R.drawable.more_pressed);
			tv_more.setTextColor(Color.parseColor("#629540"));
			break;

		default:
			break;
		}

	}

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
}
