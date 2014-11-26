package com.edu.seu.UI;

import java.util.Timer;
import java.util.TimerTask;
import com.badlogic.gdx.Gdx;
import com.edu.seu.message.Data;
import com.edu.seu.tool.Tool;
import com.example.crazyball2.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultActivity extends Activity {

	Timer timer = new Timer();
	int count = 0;

	TextView NO0_name = null;
	TextView NO1_name = null;
	TextView NO2_name = null;
	TextView NO3_name = null;

	TextView NO0_time = null;
	TextView NO1_time = null;
	TextView NO2_time = null;
	TextView NO3_time = null;

	LinearLayout NO0_tall = null;
	LinearLayout NO1_tall = null;
	LinearLayout NO2_tall = null;
	LinearLayout NO3_tall = null;

	LinearLayout.LayoutParams linearParams0 = null;
	LinearLayout.LayoutParams linearParams1 = null;
	LinearLayout.LayoutParams linearParams2 = null;
	LinearLayout.LayoutParams linearParams3 = null;

	int tall0 = 0;
	int tall1 = 0;
	int tall2 = 0;
	int tall3 = 0;

	Button back = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Transparent);
		setContentView(R.layout.activity_result);

		back = (Button) findViewById(R.id.result_backbtn);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for (int i = 0; i < Data.mode; i++)
					Data.state.set(i, 0);

				Gdx.app.exit();
				Intent intent = new Intent(ResultActivity.this,
						ReadyActivity.class);
				startActivity(intent);
				finish();

				onDestroy();
				// System.exit(0);
			}
		});

		NO0_name = (TextView) findViewById(R.id.result_NO0_name);
		NO1_name = (TextView) findViewById(R.id.result_NO1_name);
		NO2_name = (TextView) findViewById(R.id.result_NO2_name);
		NO3_name = (TextView) findViewById(R.id.result_NO3_name);

		NO0_time = (TextView) findViewById(R.id.result_NO0_time);
		NO1_time = (TextView) findViewById(R.id.result_NO1_time);
		NO2_time = (TextView) findViewById(R.id.result_NO2_time);
		NO3_time = (TextView) findViewById(R.id.result_NO3_time);

		NO0_tall = (LinearLayout) findViewById(R.id.result_NO0_tall);
		NO1_tall = (LinearLayout) findViewById(R.id.result_NO1_tall);
		NO2_tall = (LinearLayout) findViewById(R.id.result_NO2_tall);
		NO3_tall = (LinearLayout) findViewById(R.id.result_NO3_tall);

		linearParams0 = (LinearLayout.LayoutParams) NO0_tall.getLayoutParams();
		linearParams1 = (LinearLayout.LayoutParams) NO1_tall.getLayoutParams();
		linearParams2 = (LinearLayout.LayoutParams) NO2_tall.getLayoutParams();
		linearParams3 = (LinearLayout.LayoutParams) NO3_tall.getLayoutParams();

		Tool tool = new Tool();
		int maxtime = tool.maxtime();
		int mintime = tool.mintime();
		int timelong = maxtime - mintime;

		if (Data.mode == 1) {
			NO0_name.setVisibility(View.GONE);
			NO0_tall.setVisibility(View.GONE);
			NO0_time.setVisibility(View.GONE);

			NO2_name.setVisibility(View.GONE);
			NO2_tall.setVisibility(View.GONE);
			NO2_time.setVisibility(View.GONE);

			NO3_name.setVisibility(View.GONE);
			NO3_tall.setVisibility(View.GONE);
			NO3_time.setVisibility(View.GONE);

			NO1_name.setText(Data.name.get(0));

			NO1_time.setText(tool.changetimetoString(Data.time.get(0)));

			tall1 = 300;

		} else if (Data.mode == 2) {
			NO0_name.setVisibility(View.GONE);
			NO0_tall.setVisibility(View.GONE);
			NO0_time.setVisibility(View.GONE);

			NO3_name.setVisibility(View.GONE);
			NO3_tall.setVisibility(View.GONE);
			NO3_time.setVisibility(View.GONE);

			NO1_name.setText(Data.name.get(0));
			NO1_time.setText(tool.changetimetoString(Data.time.get(0)));

			NO2_name.setText(Data.name.get(1));
			NO2_time.setText(tool.changetimetoString(Data.time.get(1)));

			if (Data.time.get(0) < Data.time.get(1)) {
				tall1 = 200;
				tall2 = 300;
			} else if (Data.time.get(0) > Data.time.get(1)) {
				tall1 = 300;
				tall2 = 200;
			}

		} else if (Data.mode == 3) {
			NO3_name.setVisibility(View.GONE);
			NO3_tall.setVisibility(View.GONE);
			NO3_time.setVisibility(View.GONE);

			NO0_name.setText(Data.name.get(0));
			NO0_time.setText(tool.changetimetoString(Data.time.get(0)));

			NO1_name.setText(Data.name.get(1));
			NO1_time.setText(tool.changetimetoString(Data.time.get(1)));

			NO2_name.setText(Data.name.get(2));
			NO2_time.setText(tool.changetimetoString(Data.time.get(2)));

			if (timelong == 0) {
				tall0 = 250;
				tall1 = 250;
				tall2 = 250;
			} else {
				tall0 = 50 + 250 * (Data.time.get(0) - mintime) / timelong;
				tall1 = 50 + 250 * (Data.time.get(1) - mintime) / timelong;
				tall2 = 50 + 250 * (Data.time.get(2) - mintime) / timelong;
			}
		} else {

			NO0_name.setText(Data.name.get(0));
			NO0_time.setText(tool.changetimetoString(Data.time.get(0)));

			NO1_name.setText(Data.name.get(1));
			NO1_time.setText(tool.changetimetoString(Data.time.get(1)));

			NO2_name.setText(Data.name.get(2));
			NO2_time.setText(tool.changetimetoString(Data.time.get(2)));

			NO3_name.setText(Data.name.get(3));
			NO3_time.setText(tool.changetimetoString(Data.time.get(3)));

			if (timelong == 0) {
				tall0 = 250;
				tall1 = 250;
				tall2 = 250;
				tall3 = 250;
			} else {
				tall0 = 50 + 250 * (Data.time.get(0) - mintime) / timelong;
				tall1 = 50 + 250 * (Data.time.get(1) - mintime) / timelong;
				tall2 = 50 + 250 * (Data.time.get(2) - mintime) / timelong;
				tall3 = 50 + 250 * (Data.time.get(3) - mintime) / timelong;
			}
		}

		linearParams0.height = tall0;
		linearParams1.height = tall1;
		linearParams2.height = tall2;
		linearParams3.height = tall3;

		// timer.schedule(task, 0,3);

	}

	TimerTask task = new TimerTask() {
		@Override
		public void run() {

			runOnUiThread(new Runnable() { // UI thread
				@Override
				public void run() {
					count++;

					if (count < 150) {

					} else {
						timer.cancel();
					}
				}
			});
		}
	};

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

}
