package com.edu.seu.UI;

import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;
import com.edu.seu.message.Data;
import com.edu.seu.message.GameMessages;
import com.edu.seu.message.SendData;
import com.edu.seu.message.GameMessages.AbstractGameMessage;
import com.example.crazyball2.R;
import com.lenovo.game.GameMessage;
import com.lenovo.game.GameMessageListener;
import com.lenovo.game.GameUserInfo;
import com.lenovo.game.GameUserListener;
import com.lenovo.gamesdk.GameShare;
import com.lenovo.gamesdk.GameShare.Bindlistener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;

import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

public class LoadActivity extends Activity {

	private int waittime = 5;
	Timer timer = new Timer();
	boolean flag = true; // 只能跳转一次
	private int unfityflag[] = { 0, 0, 0 }; // 主机判断其他机器接收情况

	private static final int RECEIVED_UNFITY_ID = 7;
	private static final int RECEIVED_ANSWER_UNIFY = 8;
	// private GameShare mGameShare;
	// private GameUserInfo mLocalUser;
	// private List<GameUserInfo> mRemoteUser;
	Intent intentactivity=null; 
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {			
			case RECEIVED_UNFITY_ID:
				if (Data.inviter == false) {

					JSONObject json;
					try {
						json = new JSONObject((String) msg.obj);
						Data.myID = json.getInt("myid");
						// Data.myID=1;
						Data.hostID = json.getString("hostid");

						SendData send = new SendData();
						send.answerunfity();

						if (flag) {
							flag = false;
							if(intentactivity==null){
								intentactivity= new Intent(LoadActivity.this,
										ReadyActivity.class);
								startActivity(intentactivity);
								onDestroy();
								finish();
							}
							
							// onDestroy();
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				break;

			case RECEIVED_ANSWER_UNIFY:
				if (Data.inviter == true) {

					JSONObject json;
					try {
						json = new JSONObject((String) msg.obj);
						int temp = json.getInt("id");
						unfityflag[temp - 1] = 1;
						int totalone = 0;
						// Data.myID=1;
						for (int i = 0; i < Data.mode - 1; i++) {
							if (unfityflag[i] == 0) {
								totalone = 1;
								break;
							}
						}

						if (totalone == 0) {
							timer.cancel();
							if(intentactivity==null){
								 intentactivity= new Intent(LoadActivity.this,
											ReadyActivity.class);
									startActivity(intentactivity);
									onDestroy();
									finish();
							}
		
							// onDestroy();
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				break;

			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_load);

		Intent intent = getIntent();
		 intentactivity=null; 
		// get the game mode, single, double or multi
		if (intent.hasExtra("mode")) {
			Bundle bundle = intent.getExtras();
			int mode = bundle.getInt("mode");
			Data data = new Data();
			data.setMode(mode);

		}

		// get the local user's role, inviter or be invited.
		if (intent.hasExtra("inviter")) {
			Bundle bundle = intent.getExtras();
			boolean isInviter = bundle.getBoolean("inviter");
			Data data = new Data();
			data.setInviter(isInviter);

		}

		Data data = new Data();
		data.initstate();

		Data.mGameShare = new GameShare(getApplicationContext());
		// bind SHAREIt application
		Data.mGameShare.bind(mBindlistener);

		Data.mGameShare.addUserListener(mUserListener);
		Data.mGameShare.addMessageListener(mMessageListener);

	}

	@Override
	protected void onDestroy() {
		
		Data.mGameShare.removeMessageListener(mMessageListener);
		Data.mGameShare.removeUserListener(mUserListener);
		Data.mGameShare.unbind(mBindlistener);
		super.onDestroy();
	}
	



	TimerTask task = new TimerTask() {
		@Override
		public void run() {

			runOnUiThread(new Runnable() { // UI thread
				@Override
				public void run() {
					waittime--;

					if (waittime > 0) {
						SendData send = new SendData();
						send.unfity();

					} else {
						timer.cancel();
						dialog_linkfail();
					}
				}
			});
		}
	};

	private void dialog_linkfail() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提示");
		builder.setMessage("多次连接好友失败，请检查网络设置，游戏被迫退出");
		builder.setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				LoadActivity.this.finish();
				System.exit(0);
			}
		});
		builder.create().show();
	}

	private Bindlistener mBindlistener = new Bindlistener() {
		@Override
		public void onBind(boolean success) {
			// Log.v(TAG, "onBind, is bind success : " + success);
			// get local user and remote users in this game

			if (success) {

				Data.mLocalUser = Data.mGameShare.getLocalUser();
				Data.mRemoteUser = Data.mGameShare.getRemoteUsers();

				if (Data.inviter == true) {
					if (Data.mode == 1) {
						Intent intent = new Intent(LoadActivity.this,
								ReadyActivity.class);
						startActivity(intent);
						
						onDestroy();
						finish();
					} else {
						timer.schedule(task, 0, 500);

					}

				}

			} else {

				Toast.makeText(getApplicationContext(), "Bind Service failed.",
						Toast.LENGTH_LONG).show();
				return;
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

		}
	};

	private GameMessageListener mMessageListener = new GameMessageListener() {

		// received the message from others in the game.
		@Override
		public void onMessage(GameMessage gameMessage) {
			// Log.v(TAG, "onMessage, message : " + gameMessage.toString());
			try {
				AbstractGameMessage message = GameMessages
						.createAbstractGameMessage(gameMessage.getType(),
								gameMessage.getMessage());
				if (message.getType().equals(GameMessages.TYPE_UNIFY_ID)) {
					mHandler.sendMessage(mHandler.obtainMessage(
							RECEIVED_UNFITY_ID, message.getMessage()));
				} else if (message.getType().equals(
						GameMessages.TYPE_ANSWER_UNIFY)) {
					mHandler.sendMessage(mHandler.obtainMessage(
							RECEIVED_ANSWER_UNIFY, message.getMessage()));
				}

			} catch (JSONException e) {
				return;
			}
		}
	};

}
