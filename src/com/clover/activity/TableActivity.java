package com.clover.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.fancontroll.R;
import com.clover.entity.Report;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.utils.ParamType;
import com.utils.URLPath;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/*
 * 报表Activity
 */
public class TableActivity extends Activity {

	private LineChart lineChart;
    private XAxis xAxis;                //X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYaxis;           //右侧Y轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线
    
    private List<Report> list = new ArrayList<Report>(); //存储报表数据
    private Spinner spinner;
    private static final String CHART_DESC = "有功功率 / 10s"; //底部报表描述

    private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ParamType.SUCCESS:
				
				initData(msg.obj);
				//展示图表
				showLineChart(list, chartDesc, Color.CYAN);
				
				break;
				
			case ParamType.NO_PARAM:
				Toast.makeText(TableActivity.this, "服务器尚无该风机的参数信息", 0).show();
				break;
				
			case ParamType.ERROR:
				Toast.makeText(TableActivity.this, "尚无该风机报表数据", 0).show();
				break;
			default:
				break;
			}
		};
	};
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table);
		
		//初始化控件
		lineChart = (LineChart) findViewById(R.id.power_chart);
		spinner = (Spinner) findViewById(R.id.number_spinner);
		
		initChart(lineChart);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, 
                    int pos, long id) {
           
                String[] number = getResources().getStringArray(R.array.number);
                requestData(number[pos]);
                list.clear();
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
		
	}
	
	/**
	 * 访问服务器，获取数据
	 */
	private void requestData(final String fan_number){
		
		AsyncHttpClient client = new AsyncHttpClient();
		
		RequestParams params = new RequestParams();
		
		client.post(URLPath.REPORT_URL+fan_number, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				//解析回送的数据
				String res = new String(responseBody);
				
				try {
					//将返回的字符串转为json对象
					JSONObject jsonObject = new JSONObject(res);

					//“data”域的数据转为json数组
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					
					//是否有数据
					if (jsonArray != null) {
						
						//通知更新ui
						Message msg = Message.obtain();
						msg.obj = jsonArray;
						msg.what = ParamType.SUCCESS;
						mHandler.sendMessage(msg);
					}else {
						//返回数据为空
						Message msg = Message.obtain();
						msg.what = ParamType.NO_PARAM;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					Message msg = Message.obtain();
					msg.what = ParamType.ERROR;
					mHandler.sendMessage(msg);
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Toast.makeText(TableActivity.this, "请检查网络！", 0).show();
				
			}
		});
	}
	
	/**
	 * 数据解析
	 */
	private void initData(Object object){
		//返回json数组
    	JSONArray jsonArray = (JSONArray) object;
    	
		//每一条历史记录
    	JSONObject item; 
    	String time;
    	try {
			for (int i = 0; i < jsonArray.length(); i++) {
				item = (JSONObject) jsonArray.get(i);
				
				Report report = new Report();
				report.setPowerVal(item.getDouble("powerVal"));
				report.setEnvirTemp(item.getDouble("envirTemp"));
				report.setMotorAngle(item.getDouble("motorAngle"));
				
				list.add(report);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化图表
	 */
	private void initChart(LineChart lineChart) {
	    /***图表设置***/
	    //是否展示网格线
	    lineChart.setDrawGridBackground(false);
	    //是否显示边界
	    lineChart.setDrawBorders(true);
	    //是否可以拖动
	    lineChart.setDragEnabled(false);
	    //是否有触摸事件
	    lineChart.setTouchEnabled(true);
	    //设置XY轴动画效果
	    lineChart.animateY(2500);
	    lineChart.animateX(1500);
	    
	    Description desc = new Description();
	    desc.setText(CHART_DESC);
	    lineChart.setDescription(desc);
	 
	    /***XY轴的设置***/
	    xAxis = lineChart.getXAxis();
	    leftYAxis = lineChart.getAxisLeft();
	    rightYaxis = lineChart.getAxisRight();
	    //X轴设置显示位置在底部
	    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
	    xAxis.setAxisMinimum(0f);
	    xAxis.setGranularity(1f);
	    //保证Y轴从0开始，不然会上移一点
	    leftYAxis.setAxisMinimum(0f);
	    rightYaxis.setAxisMinimum(0f);
	 
	    /***折线图例 标签 设置***/
	    legend = lineChart.getLegend();
	    //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
	    legend.setForm(Legend.LegendForm.LINE);
	    legend.setTextSize(12f);
	    //显示位置 左下方
	    legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
	    legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
	    legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
	    //是否绘制在图表里面
	    legend.setDrawInside(false);
	}
	
	/**
	 * 曲线初始化设置 一个LineDataSet 代表一条曲线
	 *
	 * @param lineDataSet 线条
	 * @param color       线条颜色
	 * @param mode
	 */
	private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
	    lineDataSet.setColor(color);
	    lineDataSet.setCircleColor(color);
	    lineDataSet.setLineWidth(1f);
	    lineDataSet.setCircleRadius(3f);
	    //设置曲线值的圆点是实心还是空心
	    lineDataSet.setDrawCircleHole(false);
	    lineDataSet.setValueTextSize(10f);
	    //设置折线图填充
	    lineDataSet.setDrawFilled(true);
	    lineDataSet.setFormLineWidth(1f);
	    lineDataSet.setFormSize(15.f);
	    if (mode == null) {
	        //设置曲线展示为圆滑曲线（如果不设置则默认折线）
	        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
	    } else {
	        lineDataSet.setMode(mode);
	    }
	}
	
	/**
	 * 展示曲线
	 *
	 * @param dataList 数据集合
	 * @param name     曲线名称
	 * @param color    曲线颜色
	 */
	public void showLineChart(List<Report> dataList, String name, int color) {
	    
		/*if (dataList.size() < 0 || dataList == null) {
			Toast.makeText(TableActivity.this, "尚无该风机报表数据", 0).show();
			return;
		}*/
		
		List<Entry> entries = new ArrayList<Entry>();
	    for (int i = 0; i < dataList.size(); i++) {
	    	Report data = dataList.get(i);
	        /**
	         * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
	         * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
	         */
	        Entry entry = new Entry(i, (float)data.getPowerVal());
	        entries.add(entry);
	    }
	    // 每一个LineDataSet代表一条线
	    LineDataSet lineDataSet = new LineDataSet(entries, name);
	    initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
	    LineData lineData = new LineData(lineDataSet);
	    lineChart.setData(lineData);
	}
}
