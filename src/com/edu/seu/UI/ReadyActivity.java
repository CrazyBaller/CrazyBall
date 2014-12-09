package com.edu.seu.UI;

import static com.edu.seu.message.Data.state;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.edu.seu.crazyball2.GdxApplication;
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
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReadyActivity extends Activity {

	private static final int RECEIVED_MYSTATE = 8;
	private static final int RECEIVED_START = 9;
	private static final int RECEIVED_SYSTEM_ID = 10;
	private int readyflag = 0;
	Button readybtn = null;
	Button explainbtn = null;
	Button exitbtn = null;
	TextView modetextview = null;
	TextView gametitle = null;
	Tool tool = new Tool();
	
	Typeface fontFace = null;
	private Intent intent = null;
	
	boolean clock =true; 

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			JSONObject json;
			switch (msg.what) {
			
			case 101:
				
				dialog_offline();
				break;
			
			case RECEIVED_MYSTATE:

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
						if(clock){
						clock=false;
						startgame(Data.mode);
						}
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

			case RECEIVED_SYSTEM_ID:

				try {
					json = new JSONObject((String) msg.obj);

					JSONArray array = json.getJSONArray("systemid");

					for (int i = 0; i < Data.mode; i++) {
						Data.systemid.set(i, array.getString(i));
					}

					Toast.makeText(getApplicationContext(), array.getString(0),
							Toast.LENGTH_LONG).show();

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
		
		intent = null;
		clock =true;
		
		
		
		
		Data.mGameShare = new GameShare(getApplicationContext());
		Data.mGameShare.bind(mBindlistener);
		Data.mGameShare.addUserListener(mUserListener);
		Data.mGameShare.addMessageListener(mMessageListener);

		fontFace = Typeface.createFromAsset(getAssets(), "fonts/font1.TTF");
		readybtn = (Button) findViewById(R.id.ready_ready);
		readybtn.setTypeface(fontFace);
		readybtn.setOnClickListener(mClickListener);
		
		explainbtn = (Button) findViewById(R.id.ready_explain);
		explainbtn.setTypeface(fontFace);
		explainbtn.setOnClickListener(mClickListener);
		
		exitbtn = (Button) findViewById(R.id.ready_exit);
		exitbtn.setTypeface(fontFace);
		exitbtn.setOnClickListener(mClickListener);
		
		gametitle = (TextView) findViewById(R.id.ready_titlename);
		gametitle.setTypeface(fontFace);
		
		
		fontFace = Typeface.createFromAsset(getAssets(), "fonts/font1.TTF");
		modetextview = (TextView) findViewById(R.id.ready_mode);
		modetextview.setTypeface(fontFace);

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
	
	private void dialog_offline() {
		AlertDialog.Builder builder = new Builder(ReadyActivity.this);
		builder.setTitle("警告");
		builder.setMessage("您的小伙伴离开了游戏，游戏可能无法正常进行，请退出游戏");
		CharSequence cs = "确定";
		builder.setPositiveButton(cs, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
					
					arg0.dismiss();
					Data.mGameShare.quitGame();
					ReadyActivity.this.finish();
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

	private Bindlistener mBindlistener = new Bindlistener() {
		@Override
		public void onBind(boolean success) {
			// Log.v(TAG, "onBind, is bind success : " + success);
			// get local user and remote users in this game

			if (success) {

				SendData send = new SendData();
				if(Data.state.size()!=0){
					Data.state.set(Data.myID, 0);
					send.myState();
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
			
			
			switch (type) {
            case OFFLINE:
           
                    mHandler.sendEmptyMessage(101);
               
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
				if (message.getType().equals(GameMessages.TYPE_STATE)) {
					mHandler.sendMessage(mHandler.obtainMessage(
							RECEIVED_MYSTATE, message.getMessage()));
				} else if (message.getType().equals(
						GameMessages.TYPE_START_GAME)) {
					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_START,
							message.getMessage()));
				} else if (message.getType()
						.equals(GameMessages.TYPE_SYSTEM_ID)) {
					mHandler.sendMessage(mHandler.obtainMessage(
							RECEIVED_SYSTEM_ID, message.getMessage()));
				}

			} catch (JSONException e) {
				return;
			}
		}
	};

	private void startgame(int mode) {
		for (int i = 0; i < Data.mode; i++)
			Data.state.set(i, 2);

		
		if(intent==null){
			intent = new Intent(ReadyActivity.this, GdxApplication.class);
			startActivity(intent);
			onDestroy();
			finish();
		}
		
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
					ReadyActivity.this.finish();
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

	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ready_ready:
				if (readyflag == 0) {
					readyflag = 1;
					if(Data.state==null||Data.state.isEmpty()){
						Data.state=new ArrayList<Integer>();
						Data.state.add(0);
						Data.state.add(0);
						Data.state.add(0);
						Data.state.add(0);
					}
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
				ReadyActivity.this.finish();
				break;
				
			case R.id.ready_explain:
				Intent in=new Intent(ReadyActivity.this,GuideActivity.class);
				startActivity(in);
				break;

			default:
				break;
			}
		}
	};

}
