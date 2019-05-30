package com.clover.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.clover.adapter.HistoryLVAdapter;
import com.clover.entity.History;
import com.example.fancontroll.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.utils.ConvertTime;
import com.utils.ParamType;
import com.utils.URLPath;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.Toast;

/*
 * 历史记录Activity
 */
public class HistoryActivity extends Activity {

	private ListView lv_his;

	private List<History> historyList = new ArrayList<History>();

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ParamType.SUCCESS:
				// historyList.clear();
				initHistoryInfor(msg.obj);
				System.out.println(historyList);
				lv_his.setAdapter(new HistoryLVAdapter(HistoryActivity.this,
						R.layout.history_item, historyList));
				break;

			case ParamType.NO_PARAM:
				Toast.makeText(HistoryActivity.this, "服务器尚无该风机的参数信息", 0).show();
				break;

			case ParamType.ERROR:
				Toast.makeText(HistoryActivity.this, "网络出错", 0).show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_page);

		// 先清空historyList,确保无残留数据
		// historyList.clear();

		// 初始化控件
		lv_his = (ListView) findViewById(R.id.lv_history);

		// 访问服务器，取数据(使用开源框架async-http-client)
		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams params = new RequestParams();

		client.post(URLPath.HIS_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// 解析回送的数据
				String res = new String(responseBody);

				try {
					// 将返回的字符串转为json对象
					JSONObject jsonObject = new JSONObject(res);

					// “data”域的数据转为json数组
					JSONArray jsonArray = jsonObject.getJSONArray("data");

					// 是否有数据
					if (jsonArray != null) {

						// 通知更新ui
						Message msg = Message.obtain();
						msg.obj = jsonArray;
						msg.what = ParamType.SUCCESS;
						mHandler.sendMessage(msg);
					} else {
						// 返回数据为空
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
				Toast.makeText(HistoryActivity.this, "请检查网络！", 0).show();
			}
		});

	}

	private void initHistoryInfor(Object object) {
		// 返回json数组
		JSONArray jsonArray = (JSONArray) object;

		// 每一条历史记录
		JSONObject item;
		String time;
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				item = (JSONObject) jsonArray.get(i);

				// 格式化日期
				time = ConvertTime.getStrTime(item.getString("createTime"));

				History history = new History(item.getString("number"),
						item.getString("statu"),
						item.getString("contactNumber"), time);

				historyList.add(history);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
