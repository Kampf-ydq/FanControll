package com.clover.activity;

import com.example.fancontroll.R;
import com.utils.AccessServer;
import com.utils.URLPath;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

/*
 * 联系人详细信息Activity
 */
public class ContactInfor extends Activity {

	private TextView back;
    private TextView name;
    private TextView phone;
    private TextView email;
    private TextView room;
    
    private String Name;
    private String Phone;
    private String Email;
    private String Room;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact_infor);
		
		//初始化控件
		initView();
		
		//显示联系人详细信息
		contactShow();
	}
	
	private void contactShow() {
		Bundle bundle = getIntent().getExtras();
		Name = bundle.getString("name");
		Phone = bundle.getString("phone");
		Email = bundle.getString("email");
		Room = bundle.getString("room");
		
		name.setText(Name);
		phone.setText(Phone);
		email.setText(Email);
		room.setText(Room);
	}

	private void initView(){
		back = (TextView) findViewById(R.id.back);
		name = (TextView) findViewById(R.id.con_name);
		phone = (TextView) findViewById(R.id.con_phone);
		email = (TextView) findViewById(R.id.con_email);
		room = (TextView) findViewById(R.id.con_room);
	}
	
}
