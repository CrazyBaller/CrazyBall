package com.edu.seu.UI;

import static com.edu.seu.crazyball2.Constant.warningSound;
import static com.edu.seu.message.Data.state;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.edu.seu.message.Data;
import com.edu.seu.message.GameMessages;
import com.edu.seu.message.SendData;
import com.edu.seu.message.GameMessages.AbstractGameMessage;
import com.edu.seu.tool.Tool;
import com.example.crazyball2.R;
import com.lenovo.game.GameMessage;
import com.lenovo.game.GameMessageListener;
import com.lenovo.game.GameUserInfo;
import com.lenovo.game.GameUserListener;
import com.lenovo.game.GameUserListener.UserEventType;
import com.lenovo.gamesdk.GameShare;
import com.lenovo.gamesdk.GameShare.Bindlistener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {

	Timer timer = new Timer();
	int count = 0;
	
	TextView NO0_name =null;
	TextView NO1_name =null;
	TextView NO2_name =null;
	TextView NO3_name =null;
	
	TextView NO0_time =null;
	TextView NO1_time =null;
	TextView NO2_time =null;
	TextView NO3_time =null;
	
	LinearLayout NO0_background = null;
	LinearLayout NO1_background  = null;
	LinearLayout NO2_background  = null;
	LinearLayout NO3_background  = null;
	
	LinearLayout NO0_tall = null;
	LinearLayout NO1_tall = null;
	LinearLayout NO2_tall = null;
	LinearLayout NO3_tall = null;
	
	LinearLayout.LayoutParams linearParams0 = null;
	LinearLayout.LayoutParams linearParams1 = null;
	LinearLayout.LayoutParams linearParams2 = null;
	LinearLayout.LayoutParams linearParams3 = null;
	
	int tall0=0;
	int tall1=0;
	int tall2=0;
	int tall3=0;
	
	int maxtall=0;
	
	Button back = null;
	
	TextView title = null;
	
	Typeface fontFace;
	private Intent intent=null;
	
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {			
			case 104:
				dialog_offline();
				break;
			}
		}

	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setTheme(R.style.Transparent);
		setContentView(R.layout.activity_result);
		
		
		Data.mGameShare = new GameShare(getApplicationContext());
		// bind SHAREIt application
		Data.mGameShare.bind(mBindlistener);

		Data.mGameShare.addUserListener(mUserListener);
		Data.mGameShare.addMessageListener(mMessageListener);
		
		Sound winnerSound = Gdx.audio.newSound(Gdx.files.internal("sound/winner.mp3"));
		Sound loserSound = Gdx.audio.newSound(Gdx.files.internal("sound/Lose.mp3"));
		title = (TextView)findViewById(R.id.result_title);
		back = (Button)findViewById(R.id.result_backbtn);
		intent=null;
	
		if(Data.myID==0){
			back.setBackgroundResource(R.drawable.result_exit0);
			if(Data.state.get(Data.myID)==4){
				title.setBackgroundResource(R.drawable.winner0);
				winnerSound.play(30);
			}else {
				title.setBackgroundResource(R.drawable.loser0);
				loserSound.play(30);
			}
		}else if(Data.myID==1){
			
			back.setBackgroundResource(R.drawable.result_exit1);
			
			if(Data.state.get(Data.myID)==4){
				title.setBackgroundResource(R.drawable.winner1);
				winnerSound.play(30);
			}else {
				title.setBackgroundResource(R.drawable.loser1);
				loserSound.play(30);
			}
		}else if(Data.myID==2){
			
			back.setBackgroundResource(R.drawable.result_exit2);
			
			if(Data.state.get(Data.myID)==4){
				title.setBackgroundResource(R.drawable.winner2);
				winnerSound.play(30);
			}else {
				title.setBackgroundResource(R.drawable.loser2);
				loserSound.play(30);
			}
		}else if(Data.myID==3){
			

			back.setBackgroundResource(R.drawable.result_exit3);
			
			if(Data.state.get(Data.myID)==4){
				
				title.setBackgroundResource(R.drawable.winner3);
				winnerSound.play(30);
			}else {
				title.setBackgroundResource(R.drawable.loser3);
				loserSound.play(30);
			}

		}
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Data.state.set(Data.myID,0);
				SendData send = new SendData();
				send.myState();
				if(intent==null){
					intent=new Intent(ResultActivity.this,ReadyActivity.class);
					startActivity(intent);
				}
				onDestroy();
				finish();
				
			}
		});
		
		
		NO0_background = (LinearLayout)findViewById(R.id.result_NO0_background);
		NO1_background = (LinearLayout)findViewById(R.id.result_NO1_background);
		NO2_background = (LinearLayout)findViewById(R.id.result_NO2_background);
		NO3_background = (LinearLayout)findViewById(R.id.result_NO3_background);
		
		fontFace = Typeface.createFromAsset(getAssets(), "fonts/font1.TTF");
		
		NO0_name = (TextView)findViewById(R.id.result_NO0_name);
		NO1_name = (TextView)findViewById(R.id.result_NO1_name);
		NO2_name = (TextView)findViewById(R.id.result_NO2_name);
		NO3_name = (TextView)findViewById(R.id.result_NO3_name);
		
		NO0_name.setTypeface(fontFace);
		NO1_name.setTypeface(fontFace);
		NO2_name.setTypeface(fontFace);
		NO3_name.setTypeface(fontFace);
		
		NO0_time = (TextView)findViewById(R.id.result_NO0_time);
		NO1_time = (TextView)findViewById(R.id.result_NO1_time);
		NO2_time = (TextView)findViewById(R.id.result_NO2_time);
		NO3_time = (TextView)findViewById(R.id.result_NO3_time);
		
//		NO0_time.setTypeface(fontFace);
//		NO1_time.setTypeface(fontFace);
//		NO2_time.setTypeface(fontFace);
//		NO3_time.setTypeface(fontFace);
		
		NO0_tall = (LinearLayout)findViewById(R.id.result_NO0_tall);
		NO1_tall = (LinearLayout)findViewById(R.id.result_NO1_tall);
		NO2_tall = (LinearLayout)findViewById(R.id.result_NO2_tall);
		NO3_tall = (LinearLayout)findViewById(R.id.result_NO3_tall);
		
		linearParams0 =(LinearLayout.LayoutParams) NO0_tall.getLayoutParams();  
		linearParams1 =(LinearLayout.LayoutParams) NO1_tall.getLayoutParams();
		linearParams2 =(LinearLayout.LayoutParams) NO2_tall.getLayoutParams();
		linearParams3 =(LinearLayout.LayoutParams) NO3_tall.getLayoutParams();
		
		Tool tool = new Tool();
		int maxtime = tool.maxtime();
		int mintime = tool.mintime();
		int timelong = maxtime-mintime;
	
		
		
		if(Data.mode==1)
		{
			
			NO0_name.setVisibility(View.GONE);
			NO0_tall.setVisibility(View.GONE);
			NO0_time.setVisibility(View.GONE);
			NO0_background.setVisibility(View.GONE);
			
			NO2_name.setVisibility(View.GONE);
			NO2_tall.setVisibility(View.GONE);
			NO2_time.setVisibility(View.GONE);
			NO2_background.setVisibility(View.GONE);
			
			NO3_name.setVisibility(View.GONE);
			NO3_tall.setVisibility(View.GONE);
			NO3_time.setVisibility(View.GONE);
			NO3_background.setVisibility(View.GONE);
			
			NO1_name.setText(Data.name.get(0));
			
			NO1_time.setText(tool.changetimetoshow(Data.time.get(0)));
			
			NO1_tall.setBackgroundColor(Color.parseColor("#4db6af"));
			NO1_time.setTextColor(Color.parseColor("#03a9f4"));
			NO1_name.setTextColor(Color.parseColor("#03a9f4"));
		
			
			tall1=300;
			
			
		}else if(Data.mode==2){
			NO0_name.setVisibility(View.GONE);
			NO0_tall.setVisibility(View.GONE);
			NO0_time.setVisibility(View.GONE);
			NO0_background.setVisibility(View.GONE);
			
			NO3_name.setVisibility(View.GONE);
			NO3_tall.setVisibility(View.GONE);
			NO3_time.setVisibility(View.GONE);
			NO3_background.setVisibility(View.GONE);
		
			
			
			NO1_name.setText(Data.name.get(0));
			NO1_time.setText(tool.changetimetoshow(Data.time.get(0)));
			
			NO1_tall.setBackgroundColor(Color.parseColor("#4db6af"));
			NO1_time.setTextColor(Color.parseColor("#03a9f4"));
			NO1_name.setTextColor(Color.parseColor("#03a9f4"));
			
			NO2_name.setText(Data.name.get(1));
			NO2_time.setText(tool.changetimetoshow(Data.time.get(1)));
			
			NO2_tall.setBackgroundColor(Color.parseColor("#f26d6e"));
			NO2_time.setTextColor(Color.parseColor("#de0000"));
			NO2_name.setTextColor(Color.parseColor("#de0000"));
		
			
			
			if(Data.time.get(0)<Data.time.get(1)){
				tall1=200;
				tall2=300;
			}else if(Data.time.get(0)>Data.time.get(1)){
				tall1=300;
				tall2=200;
			}
			
			
		}else if(Data.mode==3){
			NO3_name.setVisibility(View.GONE);
			NO3_tall.setVisibility(View.GONE);
			NO3_time.setVisibility(View.GONE);
			NO3_background.setVisibility(View.GONE);
			
			NO0_name.setText(Data.name.get(0));
			NO0_time.setText(tool.changetimetoshow(Data.time.get(0)));
			
			NO1_name.setText(Data.name.get(1));
			NO1_time.setText(tool.changetimetoshow(Data.time.get(1)));
			
			NO2_name.setText(Data.name.get(2));
			NO2_time.setText(tool.changetimetoshow(Data.time.get(2)));
			
			if(timelong==0){
				 tall0=250;
				 tall1=250;
				 tall2=250;
			}else {
				tall0 = 50+250*(Data.time.get(0)-mintime)/timelong;
				tall1 = 50+250*(Data.time.get(1)-mintime)/timelong;
				tall2 = 50+250*(Data.time.get(2)-mintime)/timelong;
			}
		}else{
			
			NO0_name.setText(Data.name.get(0));
			NO0_time.setText(tool.changetimetoshow(Data.time.get(0)));
			
			NO1_name.setText(Data.name.get(1));
			NO1_time.setText(tool.changetimetoshow(Data.time.get(1)));
			
			NO2_name.setText(Data.name.get(2));
			NO2_time.setText(tool.changetimetoshow(Data.time.get(2)));
			
			NO3_name.setText(Data.name.get(3));
			NO3_time.setText(tool.changetimetoshow(Data.time.get(3)));
			
			if(timelong==0){
				 tall0=250;
				 tall1=250;
				 tall2=250;
				 tall3=250;
			}else {
				tall0 = 50+250*(Data.time.get(0)-mintime)/timelong;
				tall1 = 50+250*(Data.time.get(1)-mintime)/timelong;
				tall2 = 50+250*(Data.time.get(2)-mintime)/timelong;
				tall3 = 50+250*(Data.time.get(3)-mintime)/timelong;
			}
		}
		
		linearParams0.height=tall0;
		linearParams1.height=tall1;
		linearParams2.height=tall2;
		linearParams3.height=tall3;
		
	
		
		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 0f, 1f,Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF,1f);
		scaleAnimation.setDuration(1500);
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(scaleAnimation);
		animationSet.setFillAfter(true);
		NO0_tall.startAnimation(animationSet);
		
		ScaleAnimation scaleAnimation1 = new ScaleAnimation(1f, 1f, 0f, 1f,Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF,1f);
		scaleAnimation1.setDuration(1500);
		AnimationSet animationSet1 = new AnimationSet(true);
		animationSet1.addAnimation(scaleAnimation1);
		animationSet1.setFillAfter(true);
		NO1_tall.startAnimation(animationSet1);
		
		ScaleAnimation scaleAnimation2 = new ScaleAnimation(1f, 1f, 0f, 1f,Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF,1f);
		scaleAnimation2.setDuration(1500);
		AnimationSet animationSet2 = new AnimationSet(true);
		animationSet2.addAnimation(scaleAnimation2);
		animationSet2.setFillAfter(true);
		NO2_tall.startAnimation(animationSet2);
		
		ScaleAnimation scaleAnimation3 = new ScaleAnimation(1f, 1f, 0f, 1f,Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF,1f);
		scaleAnimation3.setDuration(1500);
		AnimationSet animationSet3 = new AnimationSet(true);
		animationSet3.addAnimation(scaleAnimation3);
		animationSet3.setFillAfter(true);
		NO3_tall.startAnimation(animationSet3);
		
    	

	}
	
   private void dialog_offline() {
			AlertDialog.Builder builder = new Builder(ResultActivity.this);
			builder.setTitle("警告");
			builder.setMessage("您的小伙伴离开了游戏，游戏可能无法正常进行，请退出游戏");
			CharSequence cs = "确定";
			builder.setPositiveButton(cs, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
						
						arg0.dismiss();
						Data.mGameShare.quitGame();
						ResultActivity.this.finish();
						System.exit(0);
	                  
					
				}
			});
			
			
			 builder.setOnKeyListener(new OnKeyListener() {

		            @Override
		            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		                if (keyCode == KeyEvent.KEYCODE_BACK) {
		                    return true;
		                }
		                return false;
		            }
		        });
			 
			
			
			builder.create().show();
		}
	    
	    private void dialog_back() {
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("提示");
			boolean flag = false;
			for (int i = 0; i < Data.mode; i++) {
				if (state.get(i) == 2)
					flag = true;
			}
			if (flag)
				builder.setMessage("您的小伙伴正在游戏中，您的退出将导致他们无法愉快玩耍，您确定退出么？");
			else {
				builder.setMessage("确定退出么？");
			}
			if (Data.mode == 1) {
				builder.setMessage("确定退出么？");
			}
			CharSequence cs = "确定";
			builder.setPositiveButton(cs, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
						
						arg0.dismiss();
						Data.mGameShare.quitGame();
						ResultActivity.this.finish();
						System.exit(0);
	                  
					
				}
			});
			
			cs = "取消";
			
			builder.setNegativeButton(cs,  new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
						
						arg0.dismiss();

				}
			});
			
			 builder.setOnKeyListener(new OnKeyListener() {

		            @Override
		            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		                if (keyCode == KeyEvent.KEYCODE_BACK) {
		                    return true;
		                }
		                return false;
		            }
		        });
			 

			builder.create().show();
		}


		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			
			if(keyCode==KeyEvent.KEYCODE_BACK){
				dialog_back();
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}

	    
	    
		@Override
		protected void onDestroy() {
			Data.mGameShare.removeMessageListener(mMessageListener);
			Data.mGameShare.removeUserListener(mUserListener);
			Data.mGameShare.unbind(mBindlistener);
			super.onDestroy();
		}
	 
		private Bindlistener mBindlistener = new Bindlistener() {
			@Override
			public void onBind(boolean success) {
				// Log.v(TAG, "onBind, is bind success : " + success);
				// get local user and remote users in this game

				if (success) {

					Data.mLocalUser = Data.mGameShare.getLocalUser();
					Data.mRemoteUser = Data.mGameShare.getRemoteUsers();

				}
			}
		};

		private GameUserListener mUserListener = new GameUserListener() {

			@Override
			public void onLocalUserChanged(UserEventType type, GameUserInfo user) {
				// Log.v(TAG, "onLocalUserChanged, eventType : " + type +
				// ", userInfo : " + user);

				Data.mLocalUser = user;
			}

			@Override
			public void onRemoteUserChanged(UserEventType type, GameUserInfo user) {
				
				switch (type) {
	            case OFFLINE:
	            	
	                    mHandler.sendEmptyMessage(104);
	               
	                break;
	            default:
	                break;
	        }

			}
		};

		private GameMessageListener mMessageListener = new GameMessageListener() {

			// received the message from others in the game.
			@Override
			public void onMessage(GameMessage gameMessage) {
				// Log.v(TAG, "onMessage, message : " + gameMessage.toString());
			
			}
		};



}
