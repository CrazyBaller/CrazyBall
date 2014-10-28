package com.edu.seu.UI;

import java.sql.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.edu.seu.crazyball2.GdxApplication;
import com.edu.seu.message.Data;
import com.edu.seu.message.GameMessages;
import com.edu.seu.message.SendData;
import com.edu.seu.message.GameMessages.AbstractGameMessage;
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
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReadyActivity extends Activity {

	private static final int RECEIVED_UNFITY_ID = 7;
	private static final int RECEIVED_MYSTATE = 8;
	private static final int RECEIVED_START = 9;
	private int readyflag = 0;
	Button readybtn = null;
	Button exitbtn = null;
	TextView modetextview = null;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			JSONObject json;
			switch (msg.what) {
			case RECEIVED_MYSTATE:

				// System.out.println("8888888888888"+(String)msg.obj);
				try {
					json = new JSONObject((String) msg.obj);

					Data.state.set(json.getInt("id"), json.getInt("state"));

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (Data.inviter == true) {

					int temp = 0;
					for (int i = 0; i < Data.mode; i++) {
						if (Data.state.get(i) == 0) {
							temp = 1;
							break;
						}
					}

					if (temp == 0) {
						SendData send = new SendData();
						send.start();
						startgame(Data.mode);
					}

				}

				break;
			case RECEIVED_START:

				try {
					json = new JSONObject((String) msg.obj);

					startgame(json.getInt("mode"));

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ready);

		
		Data.mGameShare = new GameShare(getApplicationContext());
		Data.mGameShare.bind(mBindlistener);
		Data.mGameShare.addUserListener(mUserListener);
		Data.mGameShare.addMessageListener(mMessageListener);

		readybtn = (Button) findViewById(R.id.ready_ready);
		readybtn.setOnClickListener(mClickListener);

		exitbtn = (Button) findViewById(R.id.ready_exit);
		exitbtn.setOnClickListener(mClickListener);

		modetextview = (TextView) findViewById(R.id.ready_mode);

		switch (Data.mode) {
		case 1:
			modetextview.setText("单人版");
			break;
		case 2:
			modetextview.setText("二人版");
			break;
		case 3:
			modetextview.setText("三人版");
			break;
		case 4:
			modetextview.setText("四人版");
			break;
		default:
			modetextview.setText("单人版");

		}

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
				// System.out.println("11111111111111"+message.getType());
				if (message.getType().equals(GameMessages.TYPE_STATE)) {
					// System.out.println("999999999999"+message.getMessage());
					mHandler.sendMessage(mHandler.obtainMessage(
							RECEIVED_MYSTATE, message.getMessage()));
				} else if (message.getType().equals(
						GameMessages.TYPE_START_GAME)) {
					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_START,
							message.getMessage()));
				}

			} catch (JSONException e) {
				return;
			}
		}
	};

	private void startgame(int mode) {
		for(int i=0;i<Data.mode;i++)
			Data.state.set(i, 2);
		
		Intent intent = null;

		intent = new Intent(ReadyActivity.this, GdxApplication.class);
		startActivity(intent);
		ReadyActivity.this.finish();

	}

	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ready_ready:
				if (readyflag == 0) {
					readyflag = 1;
					Data.state.set(Data.myID, 1);
					readybtn.setText("已准备");

					SendData send = new SendData();
					send.myState();
					if (Data.inviter == true) {
						int temp = 0;

						for (int i = 0; i < Data.mode; i++) {
							if (Data.state.get(i) == 0) {
								temp = 1;
								break;
							}
						}
						if (temp == 0) {

							send.start();
							startgame(Data.mode);
						}

					}
				} else if (readyflag == 1) {

					readyflag = 0;
					Data.state.set(Data.myID, 0);
					readybtn.setText("准备");
					SendData send = new SendData();
					send.myState();
				}
				break;
			case R.id.ready_exit:

				Data.mGameShare.quitGame();
				break;

			default:
				break;
			}
		}
	};

}
