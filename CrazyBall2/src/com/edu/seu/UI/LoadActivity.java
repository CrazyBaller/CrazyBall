package com.edu.seu.UI;

import static com.edu.seu.message.Data.board2_x;
import static com.edu.seu.message.Data.board2_y;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.edu.seu.message.Data;
import com.edu.seu.message.GameMessages;
import com.edu.seu.message.SendData;
import com.edu.seu.message.GameMessages.AbstractGameMessage;
import com.edu.seu.message.GameMessages.HelloMessage;
import com.edu.seu.message.GameMessages.RemoteLocationMessage;
import com.example.crazyball2.R;
import com.example.crazyball2.R.layout;
import com.example.crazyball2.R.menu;
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
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

public class LoadActivity extends Activity {

	private  boolean f = false;

	private int waittime = 2;
	Timer timer = new Timer();

	private static final int RECEIVED_UNFITY_ID = 7;
	// private GameShare mGameShare;
	// private GameUserInfo mLocalUser;
	// private List<GameUserInfo> mRemoteUser;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			

			if (f) {
				if (Data.inviter == true) {
					SendData send = new SendData();
					send.unfity();
				}
			}

			switch (msg.what) {
			case RECEIVED_UNFITY_ID:
				if (Data.inviter == false) {
					System.out.println("8888888888888" + (String) msg.obj);

					JSONObject json;
					try {
						json = new JSONObject((String) msg.obj);
						Data.myID = json.getInt("myid");
						// Data.myID=1;
						Data.hostID = json.getString("hostid");

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Toast.makeText(getApplicationContext(),
						"id: "+Data.myID, Toast.LENGTH_LONG).show();
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

		
		timer.schedule(task, 0, 1000);
		
		if (f) {
			if (Data.inviter == true) {
				data = new Data();
				data.initstate();
//				if(Data.mGameShare!=null)
//				{
//					Data.mLocalUser = Data.mGameShare.getLocalUser();
//					Data.mRemoteUser = Data.mGameShare.getRemoteUsers();
//				}
				
				SendData send = new SendData();
				send.unfity();
			}
		}

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

					if (waittime < 0) {
						timer.cancel();
						Intent intent = new Intent(LoadActivity.this,
								ReadyActivity.class);
						startActivity(intent);
						finish();
					}
				}
			});
		}
	};

	private Bindlistener mBindlistener = new Bindlistener() {
		@Override
		public void onBind(boolean success) {
			// Log.v(TAG, "onBind, is bind success : " + success);
			// get local user and remote users in this game

			if (success) {
				f = true;
				System.out.println("onBind, is bind success : " + success);
				Data.mLocalUser = Data.mGameShare.getLocalUser();
				Data.mRemoteUser = Data.mGameShare.getRemoteUsers();

				if (Data.inviter == true) {
					SendData send = new SendData();
					send.unfity();
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
			System.out.println("onLocalUserChanged, eventType : " + type
					+ ", userInfo : " + user);
			Data.mLocalUser = user;
		}

		@Override
		public void onRemoteUserChanged(UserEventType type, GameUserInfo user) {
			// Log.v(TAG, "onRemoteUserChanged, eventType : " + type +
			// ", userInfo : " + user);
			System.out.println("onRemoteUserChanged, eventType : " + type
					+ ", userInfo : " + user);
			
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
				System.out.println("11111111111111" + message.getType());
				if (message.getType().equals(GameMessages.TYPE_UNIFY_ID)) {
					System.out.println("999999999999" + message.getMessage());
					mHandler.sendMessage(mHandler.obtainMessage(
							RECEIVED_UNFITY_ID, message.getMessage()));
				}

			} catch (JSONException e) {
				return;
			}
		}
	};

}
