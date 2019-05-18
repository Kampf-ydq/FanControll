package com.clover.adapter;

import java.util.List;

import com.clover.entity.History;
import com.example.fancontroll.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HistoryLVAdapter extends ArrayAdapter<History> {

	private int item_page;
	
	public HistoryLVAdapter(Context context, int resource, List<History> objects) {
		super(context, resource, objects);
		item_page = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//获取当前History实例
		History history = getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(item_page, null);//加载子项布局
			viewHolder = new ViewHolder();
			viewHolder.fanNumber = (TextView) view.findViewById(R.id.fan_number);
			viewHolder.status = (TextView) view.findViewById(R.id.status);
			viewHolder.contact = (TextView) view.findViewById(R.id.contact_number);
			viewHolder.time = (TextView) view.findViewById(R.id.create_time);
			
			//将ViewHolder存储在view中
			view.setTag(viewHolder);
			
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();//重新获取ViewHolder
		}
		
		viewHolder.fanNumber.setText(history.getNumber());
		viewHolder.status.setText(history.getStatus());
		viewHolder.contact.setText(history.getContact());
		viewHolder.time.setText(history.getTime());
		return view;
	}

	//对控件的实例进行缓存
	class ViewHolder{
		TextView fanNumber;
		TextView status;
		TextView contact;
		TextView time;
	}
}
