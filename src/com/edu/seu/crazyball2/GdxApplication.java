package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;
import static com.edu.seu.message.Data.*;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.edu.seu.UI.LoadActivity;
import com.edu.seu.UI.ResultActivity;
import com.edu.seu.message.Data;
import com.edu.seu.message.GameMessages;
import com.edu.seu.message.GameMessages.AbstractGameMessage;
import com.edu.seu.message.SendData;
import com.edu.seu.props.PropsObservable;
import com.edu.seu.props.PropsObserver;
import com.lenovo.game.GameMessage;
import com.lenovo.game.GameMessageListener;
import com.lenovo.game.GameUserInfo;
import com.lenovo.game.GameUserListener;
import com.lenovo.gamesdk.GameShare;
import com.lenovo.gamesdk.GameShare.Bindlistener;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.Toast;

public class GdxApplication extends AndroidApplication {
	/** Called when the activity is first created. */

	private static final int RECEIVED_BALL = 4;
	private static final int RECEIVED_BOARD = 5;
	private static final int RECEIVED_STATE = 6;
	private static final int RECEIVED_GAME_RESULT = 8;
	private static final int RECEIVED_BOARD_SIZE = 9;
	private static final int RECEIVED_BALL_SIZE = 10;
	private static final int RECEIVED_PROPS = 11;
	private static final int RECEIVED_PROPS_IMAGE = 12;
	private static final int RECEIVED_EAT_BLOCK = 13;
	private static final int RECEIVED_TIME = 14;
	private static final int RECEIVED_ANY_STATE = 15;
	private static final int RECEIVED_CONTROLID = 16;
	private static final int RECEIVED_PROPS_ACTIVITY = 17;

	private PropsObservable po;	

	Timer timer = new Timer();
	 static int time = 0;

	Context context = null;
	private Intent intent=null;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			JSONObject json;
			switch (msg.what) {
			
			case 102:
				
				dialog_offline();
				break;

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
						Data.ballsize = json.getInt("size");

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				break;
			case RECEIVED_BOARD:

				try {

					json = new JSONObject((String) msg.obj);
					float f = (float) json.getDouble("x");
					Data.location.set(json.getInt("id"), f);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			case RECEIVED_BOARD_SIZE:

				try {

					json = new JSONObject((String) msg.obj);

					Data.boardsize.set(json.getInt("id"), json.getInt("size"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			case RECEIVED_GAME_RESULT:

				if (Data.inviter == false)
					try {

						json = new JSONObject((String) msg.obj);
						Data.state
								.set(json.getInt("id"), json.getInt("result"));

						if (json.getInt("result") == 3) {
							
							if(Data.myID==1){
								tBoard1.setTransform(250.0f, 1000.0f,0);
							}else if(Data.myID==2){
								tBoard2.setTransform(250.0f, 1000.0f,0);
							}else if(Data.myID==3){
								tBoard3.setTransform(250.0f, 1000.0f,0);
							}
							
							if (json.getInt("id") == Data.myID) {
							
								dialog_mydead();
							} else {
								Toast.makeText(getApplicationContext(),
										json.getString("name") + " is dead ",
										Toast.LENGTH_LONG).show();
							}
						} else {
							if (json.getInt("id") == Data.myID) {
								Toast.makeText(getApplicationContext(),
										"you win", Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(getApplicationContext(),
										json.getString("name") + " is dead ",
										Toast.LENGTH_LONG).show();
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				break;

			case RECEIVED_PROPS:

				try {
					json = new JSONObject((String) msg.obj);
					if (json.getInt("type") > 30 && json.getInt("type") < 35) {//被动
						po.setChange(json.getInt("type"), json.getInt("id"));
					} else {
						if(json.getInt("id")==Data.myID){
							if(Data.mode==2)
								TwoModeClient.propsbar.addbutton(json.getInt("type"));
							else if(Data.mode==3)
								ThreeModeClient.propsbar.addbutton(json.getInt("type"));
							else if(Data.mode==4)
								FourModeClient.propsbar.addbutton(json.getInt("type"));
						}
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			case RECEIVED_PROPS_ACTIVITY:

				try {
					json = new JSONObject((String) msg.obj);
					if(json.getInt("id")== Data.myID){
					warningSound.play(30);
					}
					po.setChange(json.getInt("type"),json.getInt("id"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			case RECEIVED_PROPS_IMAGE:
				try {

					JSONArray arrayid = new JSONArray();
					JSONArray arrayx = new JSONArray();
					JSONArray arrayy = new JSONArray();
					json = new JSONObject((String) msg.obj);

					arrayid = json.getJSONArray("propsimageid");
					arrayx = json.getJSONArray("propsimagex");
					arrayy = json.getJSONArray("propsimagey");
					
					Data.propsimageid.clear();
					Data.propsimagex.clear();
					Data.propsimagey.clear();

					for (int i = 0; i < arrayid.length(); i++) {
						Data.propsimageid.add(arrayid.getInt(i));
						float f = (float) arrayx.getDouble(i);
						Data.propsimagex.add(f);
						f = (float) arrayy.getDouble(i);
						Data.propsimagey.add(f);
					}
					isUpdate=false;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			case RECEIVED_EAT_BLOCK:
				if (Data.inviter == false) {

					try {
						json = new JSONObject((String) msg.obj);
						int id = json.getInt("id");
						for (int i = 0; i < Data.blockList.size(); i++) {
							BodyData bd = ((BodyData) Data.blockList.get(i)
									.getUserData());
							if (bd.getId() == id) {
								bd.health = 0;
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				break;

			case RECEIVED_TIME:

				try {
					json = new JSONObject((String) msg.obj);
					JSONArray array = json.getJSONArray("time");
					JSONArray arrayname = json.getJSONArray("name");
					for (int i = 0; i < Data.mode; i++) {
						Data.time.set(i, array.getInt(i));
						Data.name.set(i, arrayname.getString(i));
					}
					if(intent==null){
						intent = new Intent(GdxApplication.this,
								ResultActivity.class);					
						startActivity(intent);	
						Destroy();
					}
					if(sendtimer!=null){
						sendtimer.cancel();
					}
					Gdx.app.exit();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			case RECEIVED_ANY_STATE:

				try {

					json = new JSONObject((String) msg.obj);
					Data.state.set(json.getInt("id"), json.getInt("state"));
					if (json.getInt("state") == 3) {
						//PolygonShape shapeRect;
						switch (json.getInt("id")) {
						case 0:
							tBoard0.setTransform(250.0f, 1000.0f, 0);
							break;
						case 1:
							tBoard1.setTransform(250.0f, 1000.0f, 0);
							break;
						case 2:
							tBoard2.setTransform(250.0f, 1000.0f, 0);
							break;
						case 3:
							tBoard3.setTransform(500.0f,1000.0f, 0);
							break;
						default:
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			case RECEIVED_STATE:

				try {

					json = new JSONObject((String) msg.obj);
					Data.state.set(json.getInt("id"), json.getInt("state"));
					if (json.getInt("state") == 3) {
						switch (json.getInt("id")) {
						case 0:
							tBoard0.setTransform(250.0f, 1000.0f, 0);
							break;
						case 1:
							tBoard1.setTransform(250.0f, 1000.0f, 0);
							break;
						case 2:
							tBoard2.setTransform(250.0f, 1000.0f, 0);
							break;
						case 3:
							tBoard3.setTransform(250.0f, 1000.0f, 0);
							break;
						default:
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case RECEIVED_CONTROLID:
				if (Data.inviter == false) {

					try {
						json = new JSONObject((String) msg.obj);
						CONTROL_ID = json.getInt("controlId");

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				break;

			}
		}

	};

	private Handler windowHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case SHOW_DIALOG2: // 返回键
				dialog2();
				break;
			case SHOW_MYDEAD_DIALOG:
				try {
					
					//JSONObject json = new JSONObject((String) msg.obj);
					JSONObject json = new JSONObject(msg.obj.toString());
					Data.state.set(json.getInt("id"), json.getInt("result"));

					if (json.getInt("result") == 4) {
						
						tBall.setLinearVelocity(0, 0);
						if (json.getInt("id") == 0) {
							Data.time.set(0, time);
							SendData send = new SendData();
							send.time();
							Data.state.set(0, 4);
							send.myState();

							Toast.makeText(getApplicationContext(), "you win ",
									Toast.LENGTH_LONG).show();
							if(intent==null){
								intent = new Intent(GdxApplication.this,
										ResultActivity.class);
								startActivity(intent);
								Destroy();
							}
							if(sendtimer!=null){
								sendtimer.cancel();
							}
							Gdx.app.exit();
						} else {
							Data.time.set(json.getInt("id"), time);
							SendData send = new SendData();
							
							Data.state.set(json.getInt("id"), 4);
							send.state(json.getInt("id"), 4);
							send.time();
							Toast.makeText(
									getApplicationContext(),
									Data.mRemoteUser.get(json.getInt("id") - 1).name
											+ " win ", Toast.LENGTH_LONG)
									.show();
							if(intent==null){
								intent = new Intent(GdxApplication.this,
										ResultActivity.class);
								startActivity(intent);
								Destroy();
							}	
							if(sendtimer!=null){
								sendtimer.cancel();
							}
							Gdx.app.exit();
						}
						
						
					} else {
						if (json.getInt("id") == 0) {
							dialog_mydead();
							Data.time.set(0, time);
							Data.state.set(0, 3);
							SendData send = new SendData();
							send.myState();
							
							tBoard0.setTransform(250.0f, 1000.0f, 0);

							Toast.makeText(getApplicationContext(),
									"you are dead ", Toast.LENGTH_LONG).show();
						} else {
							int id = json.getInt("id");
							Data.time.set(id, time);
							Data.state.set(id, 3);
							SendData send = new SendData();
							send.state(id, 3);
							PolygonShape shapeRect;
							switch (id) {
							case 1:
								
								tBoard1.setTransform(250.0f, 1000.0f, 0);
								break;
							case 2:
								tBoard2.setTransform(250.0f,  1000.0f, 0);
								break;
							case 3:
								tBoard3.setTransform(250.0f,  1000.0f, 0);
								break;
							default:
							}
							Toast.makeText(
									getApplicationContext(),
									Data.mRemoteUser.get(json.getInt("id") - 1).name
											+ " is dead ", Toast.LENGTH_LONG)
									.show();

						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

		}
	};
	
	
	private void dialog_offline() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("警告");
		builder.setMessage("您的小伙伴离开了游戏，游戏可能无法正常进行，请退出游戏");
		CharSequence cs = "确定";
		builder.setPositiveButton(cs, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
					
					arg0.dismiss();
					Data.mGameShare.quitGame();
					Gdx.app.exit();
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

	private void dialog2() {
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
		builder.setPositiveButton("否", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.dismiss();
				onResume();
			}
		});
		builder.setNegativeButton("是", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.dismiss();
				Data.mGameShare.quitGame();
				Gdx.app.exit();
				System.exit(0);
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
				arg0.dismiss();
			}
		});
		builder.create().show();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 道具监听
		po = new PropsObservable();
		po.addObserver(new PropsObserver());

		context = this;
		intent=null;
		
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
		
		
		time=0;
		timer.schedule(task, 10, 10);

		switch (mode) {
		case 1:
			initialize(new SoloMode(windowHandler, po), false);
			break;
		case 2:
			if (inviter) {
				initialize(new TwoMode(windowHandler, po), false);
			}

			else {
				initialize(new TwoModeClient(windowHandler, po), false);
			}

			break;
		case 3:
			if (inviter)
				initialize(new ThreeMode(windowHandler,po), false);
			else
				initialize(new ThreeModeClient(windowHandler, po), false);
			break;
		case 4:
			if (inviter)
				initialize(new FourMode(windowHandler,po), false);
			else
				initialize(new FourModeClient(windowHandler, po), false);
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
			Data.mLocalUser = Data.mGameShare.getLocalUser();
			Data.mRemoteUser = Data.mGameShare.getRemoteUsers();

		}
	};

	private GameUserListener mUserListener = new GameUserListener() {

		@Override
		public void onLocalUserChanged(UserEventType type, GameUserInfo user) {
			Data.mLocalUser = user;
		}

		@Override
		public void onRemoteUserChanged(UserEventType type, GameUserInfo user) {
			
			switch (type) {
            case OFFLINE:
                    mHandler.sendEmptyMessage(102);
               
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
				} else if (message.getType().equals(
						GameMessages.TYPE_GAME_RESULT)) {

					mHandler.sendMessage(mHandler.obtainMessage(
							RECEIVED_GAME_RESULT, message.getMessage()));
				} else if (message.getType().equals(
						GameMessages.TYPE_BOARD_SIZE)) {

					mHandler.sendMessage(mHandler.obtainMessage(
							RECEIVED_BOARD_SIZE, message.getMessage()));
				} else if (message.getType()
						.equals(GameMessages.TYPE_BALL_SIZE)) {

					mHandler.sendMessage(mHandler.obtainMessage(
							RECEIVED_BALL_SIZE, message.getMessage()));
				} else if (message.getType().equals(GameMessages.TYPE_PROPS)) {

					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_PROPS,
							message.getMessage()));
				} else if (message.getType().equals(
						GameMessages.TYPE_PROPS_IMAGE)) {

					mHandler.sendMessage(mHandler.obtainMessage(
							RECEIVED_PROPS_IMAGE, message.getMessage()));
				} else if (message.getType()
						.equals(GameMessages.TYPE_EAT_BLOCK)) {

					mHandler.sendMessage(mHandler.obtainMessage(
							RECEIVED_EAT_BLOCK, message.getMessage()));
				} else if (message.getType().equals(GameMessages.TYPE_TIME)) {
					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_TIME,
							message.getMessage()));
				} else if (message.getType()
						.equals(GameMessages.TYPE_ANY_STATE)) {
					mHandler.sendMessage(mHandler.obtainMessage(
							RECEIVED_ANY_STATE, message.getMessage()));
				} else if (message.getType()
						.equals(GameMessages.TYPE_CONTROLID)) {
					mHandler.sendMessage(mHandler.obtainMessage(
							RECEIVED_CONTROLID, message.getMessage()));
				} else if (message.getType().equals(
						GameMessages.TYPE_PROPS_ACTIVITY)) {
					mHandler.sendMessage(mHandler.obtainMessage(
							RECEIVED_PROPS_ACTIVITY, message.getMessage()));
				}
			} catch (JSONException e) {
				return;
			}
		}
	};
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			runOnUiThread(new Runnable() { // UI thread
				@Override
				public void run() {
					time++;
				}
			});
		}
	};
	
	protected void Destroy() {
		Data.mGameShare.removeMessageListener(mMessageListener);
		Data.mGameShare.removeUserListener(mUserListener);
		Data.mGameShare.unbind(mBindlistener);
	
	}

}