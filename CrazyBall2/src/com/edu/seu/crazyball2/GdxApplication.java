package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;
import static com.edu.seu.message.Data.*;
import org.json.JSONException;
import org.json.JSONObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.edu.seu.message.Data;
import com.edu.seu.message.GameMessages;
import com.edu.seu.message.GameMessages.AbstractGameMessage;
import com.edu.seu.message.GameMessages.HelloMessage;
import com.edu.seu.message.GameMessages.RemoteLocationMessage;
import com.lenovo.game.GameMessage;
import com.lenovo.game.GameMessageListener;
import com.lenovo.game.GameUserInfo;
import com.lenovo.game.GameUserListener;
import com.lenovo.gamesdk.GameShare;
import com.lenovo.gamesdk.GameShare.Bindlistener;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class GdxApplication extends AndroidApplication {
	/** Called when the activity is first created. */

	private boolean f = false;

	private static final int RECEIVED_Remote_Location = 3;
	private static final int RECEIVED_BALL = 4;
	private static final int RECEIVED_BOARD = 5;
	private static final int RECEIVED_STATE = 6;
	private static final int RECEIVED_UNFITY_ID = 7;
	private static final int RECEIVED_GAME_RESULT = 8;
	private static final int RECEIVED_BOARD_SIZE = 9;
	private static final int RECEIVED_BALL_SIZE = 10;
	

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			JSONObject json;
			switch (msg.what) {

			case RECEIVED_BALL:
				if (Data.inviter == false) {
					
					try {
						json = new JSONObject((String) msg.obj);
						float f = (float) json.getDouble("x");
						Data.ball.set(0, f);
						f = (float) json.getDouble("y");
						Data.ball.set(1, f);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				break;
				
			case RECEIVED_BALL_SIZE:
				if (Data.inviter == false) {
					
					try {
						json = new JSONObject((String) msg.obj);
						Data.ballsize=json.getInt("size");

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				break;
			case RECEIVED_BOARD:
				
				try {

					System.out.println("8888888888888" + (String) msg.obj);
					json = new JSONObject((String) msg.obj);
					float f = (float) json.getDouble("x");
					Data.location.set(json.getInt("id"), f);
					// System.out.println("222222222222"+Data.ball.get(json.getInt("id")));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
				
			case RECEIVED_BOARD_SIZE:
				
				try {

					System.out.println("8888888888888" + (String) msg.obj);
					json = new JSONObject((String) msg.obj);
					
					Data.boardsize.set(json.getInt("id"),json.getInt("size"));
					// System.out.println("222222222222"+Data.ball.get(json.getInt("id")));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
				
			case RECEIVED_GAME_RESULT:
				
				try {

					
					json = new JSONObject((String) msg.obj);
					Data.state.set(json.getInt("id"), json.getInt("result"));
					
					if(json.getInt("result")==3)
						{
						if(json.getInt("id")==Data.myID){
					     dialog();
						}else{
							Toast.makeText(getApplicationContext(),
									json.getString("name")+" is dead ", Toast.LENGTH_LONG).show();
						}
						}else
						{
							if(json.getInt("id")==Data.myID){
								Toast.makeText(getApplicationContext(),
										"you win", Toast.LENGTH_LONG).show();
								}else{
									Toast.makeText(getApplicationContext(),
											json.getString("name")+" is dead ", Toast.LENGTH_LONG).show();
								}
						}
					// System.out.println("222222222222"+Data.ball.get(json.getInt("id")));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;	

			}
		}

	};

	private Handler windowHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch(msg.what){
			case SHOW_DIALOG:
				dialog();
				break;
			case SHOW_MYDEAD_DIALOG:
				try {
					System.out.println("1111111"+msg.obj);
					JSONObject json =new JSONObject((String) msg.obj);
					
					Data.state.set(json.getInt("id"),json.getInt("result"));
					
				
					if(json.getInt("result")==4)	
					{
						if(json.getInt("id")==0){
						Toast.makeText(getApplicationContext(),
								"you win ", Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(getApplicationContext(),
									Data.mRemoteUser.get(json.getInt("id")-1).name+" win ", Toast.LENGTH_LONG).show();
						}
					}else
					{
						if(json.getInt("id")==0){
							//dialog_mydead();
							Toast.makeText(getApplicationContext(),
									"you are dead ", Toast.LENGTH_LONG).show();
							}else{
								Toast.makeText(getApplicationContext(),
										Data.mRemoteUser.get(json.getInt("id")-1).name+" is dead ", Toast.LENGTH_LONG).show();
						
								}
					}	
					}
					 catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case SHOW_TOAST:
				Toast.makeText(getApplicationContext(),"ftp:"+ 1.0/Gdx.graphics.getDeltaTime(), 10000).show();
				
			}
		
		}
	};

	private void dialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提示");
		builder.setMessage("确定退出么？");
		builder.setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Gdx.app.exit();
			}
		});
		builder.setNegativeButton("再来一局", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});
		builder.create().show();
	}
	private void dialog_mydead() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提示");
		builder.setMessage("您已经死了，如果退出将导致游戏结束，请观战");
		builder.setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Gdx.app.exit();
			}
		});
		builder.create().show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Data.mGameShare = new GameShare(getApplicationContext());
		Data.mGameShare.bind(mBindlistener);

		Data.mGameShare.addUserListener(mUserListener);
		Data.mGameShare.addMessageListener(mMessageListener);

		DisplayMetrics dm = new DisplayMetrics(); // 获取屏幕尺寸
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 得到屏幕的宽度和高度
		if (dm.widthPixels < dm.heightPixels) { // 判断宽度与高度的大小
			SCREEN_WIDTH = dm.widthPixels; // 为SCREEN_WIDTH赋值
			SCREEN_HEIGHT = dm.heightPixels; // 为SCREEN_HEIGHT赋值
		} else {
			SCREEN_WIDTH = dm.heightPixels; // 为SCREEN_WIDTH赋值
			SCREEN_HEIGHT = dm.widthPixels; // 为SCREEN_HEIGHT赋值
		}
		SCREEN_WIDTH /= 10;
		SCREEN_HEIGHT /= 10;
		set_x = SCREEN_WIDTH * 5;
		set_y = SCREEN_HEIGHT * 5;

		switch (mode) {
		case 1:
			initialize(new SoloMode(windowHandler), false);
			break;
		case 2:
			if (inviter)
				initialize(new TwoMode(windowHandler), false);
			else
				initialize(new TwoModeClient(windowHandler), false);
			break;
		case 3:
			if (inviter)
				initialize(new ThreeMode(windowHandler), false);
			else
				initialize(new ThreeModeClient(windowHandler), false);
			break;
		case 4:
			if (inviter)
				initialize(new FourMode(windowHandler), false);
			else
				initialize(new FourModeClient(windowHandler), false);
			break;
		default:
			Gdx.app.exit();
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
			System.out.println("onBind, is bind success : " + success);
			Data.mLocalUser = Data.mGameShare.getLocalUser();
			Data.mRemoteUser = Data.mGameShare.getRemoteUsers();

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

				if (message.getType().equals(GameMessages.TYPE_BOARD_LOCATION)) {

					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_BOARD,
							message.getMessage()));
				} else if (message.getType().equals(
						GameMessages.TYPE_BALL_LOCATION)) {

					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_BALL,
							message.getMessage()));
				} else if (message.getType().equals(GameMessages.TYPE_STATE)) {

					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_STATE,
							message.getMessage()));
				} else if (message.getType().equals(GameMessages.TYPE_GAME_RESULT)) {

					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_GAME_RESULT,
							message.getMessage()));
				}else if (message.getType().equals(GameMessages.TYPE_BOARD_SIZE)) {

					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_BOARD_SIZE,
							message.getMessage()));
				}else if (message.getType().equals(GameMessages.TYPE_BALL_SIZE)) {

					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_BALL_SIZE,
							message.getMessage()));
				}

			} catch (JSONException e) {
				return;
			}
		}
	};

}