package com.clover.activity;

import com.example.fancontroll.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/*
 * 报表Activity
 */
public class TableActivity extends Activity {

	private LineChart mChart;
	
	private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getData();
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table);
		
		//初始化控件
		mChart = (LineChart) findViewById(R.id.power_chart);
		
		Description desc = new Description();
		desc.setText("power chart");
		//在chart的右下角添加描述
		mChart.setDescription(desc);
		
		//添加网格
		mChart.setGridBackgroundColor(Color.rgb(255, 255, 255));
		
		//设置是否可以触摸
		mChart.setTouchEnabled(true);
		
		//设置是否可以拖拽，缩放
		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);
		
		//设置警戒值
		LimitLine limitLine = new LimitLine(95f, "警戒值（下限值）");
		limitLine.setLineWidth(2f);
		limitLine.setLabelPosition(LimitLabelPosition.LEFT_TOP);
		limitLine.setTextSize(15f);
		
		XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxisPosition.BOTTOM); // 设置X轴的数据在底部显示
        xl.setTextSize(10f); // 设置字体大小

        YAxis yl = mChart.getAxisLeft();
        yl.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
//      yl.setAxisMaxValue(220f);
        yl.addLimitLine(limitLine);
        yl.setTextSize(10f); // 设置字体大小 
        yl.setAxisMinValue(90f);
        yl.setStartAtZero(false);
//      yl.setLabelCount(5); // 设置Y轴最多显示的数据个数

        YAxis y2 = mChart.getAxisRight();
        y2.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        y2.setTextSize(10f); // 设置字体大小
        y2.setAxisMinValue(90f);
        y2.setStartAtZero(false);
        getData();
        new Thread(mRunnable).start();
	}
	
	private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while(true) {
                  try {
                       Thread.sleep(15*1000);//每隔15s刷新一次，可以看到动态图
                       mHandler.sendMessage(mHandler.obtainMessage());
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                  }
                  }
        }
    };
    
    private void getData() {
        LogUtils.d(TAG, "getData() " + DateUtils.getCurrentDate()); 
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBHelper.getInstance().init();
                String sql = "select *from suc_rate_chart_0614";
                final String[] xx = DBHelper.getInstance().query(sql,2); 
                final String[] yy = DBHelper.getInstance().query(sql,3); 
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onChanged(xx, yy);
                    }
                });
            }
        }).start();
    }
}
