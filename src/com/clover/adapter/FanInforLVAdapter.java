package com.clover.adapter;

import java.util.List;

import com.clover.entity.FanParam;
import com.example.fancontroll.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FanInforLVAdapter extends ArrayAdapter<FanParam> {

	private Context con;
	
	private int item_page;
	
	public FanInforLVAdapter(Context context, int resource, List<FanParam> objects) {
		super(context, resource, objects);
		con = context;
		item_page = resource;
	}

	//在每个子项显示在屏幕上时会被Android系统调用
	/*
	 * position: 当前子项的位置信息
	 * convertView:做优化
	 * parent:当前子项的父控件
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//获取当前FanParam实例
		FanParam fanParam = getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(item_page, null);//加载子项布局
			viewHolder = new ViewHolder();
			viewHolder.paramName = (TextView) view.findViewById(R.id.param_name);
			viewHolder.paramVal = (TextView) view.findViewById(R.id.param_val);
			
			//将ViewHolder存储在view中
			view.setTag(viewHolder);
			
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();//重新获取ViewHolder
		}
		
		viewHolder.paramName.setText(fanParam.getParamName());
		viewHolder.paramVal.setText(fanParam.getParamVal());
		return view;
	}

	//对控件的实例进行缓存
	class ViewHolder{
		TextView paramName;
		TextView paramVal;
	}
}
