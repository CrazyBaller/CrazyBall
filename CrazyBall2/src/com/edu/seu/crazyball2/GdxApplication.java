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
import com.edu.seu.message.SendData;
import com.lenovo.game.GameMessage;
import com.lenovo.game.GameMessageListener;
import com.lenovo.game.GameUserInfo;
import com.lenovo.game.GameUserListener;
import com.lenovo.gamesdk.GameShare;
import com.lenovo.gamesdk.GameShare.Bindlistener;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;



public class GdxApplication extends AndroidApplication {
    /** Called when the activity is first created. */
	
	private static final int MSG_RECEIVED_MESSAGE = 1;
    private static final int MSG_RECEIVED_TWO = 2;
    private static final int RECEIVED_Remote_Location=3;
    private static final int RECEIVED_BALL = 4;
    private static final int RECEIVED_BOARD = 5;
    private static final int RECEIVED_STATE = 6;
    private static final int RECEIVED_UNFITY_ID = 7;
	    //private GameShare mGameShare;
	    //private GameUserInfo mLocalUser;
	    //private List<GameUserInfo> mRemoteUser;
	
	 private Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				  Data datadef = new Data();
			      datadef.def();
				
				switch (msg.what) {
				case MSG_RECEIVED_MESSAGE:
					if(Data.inviter==false)
					System.out.println((String)msg.obj);
					break;
				case RECEIVED_Remote_Location:
					if(Data.inviter==true)
					{
						System.out.println((String)msg.obj);
					
					JSONObject json;
					try {
						json = new JSONObject((String)msg.obj);
						board2_x=(float) json.getDouble("board_x");
						board2_y=(float) json.getDouble("board_y");
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
					break;
				case RECEIVED_UNFITY_ID:
					if(Data.inviter==false){
					System.out.println("8888888888888"+(String)msg.obj);
					
					JSONObject json;
					try {
						json = new JSONObject((String)msg.obj);
						//Data.myID= json.getInt("myid");
						Data.myID=1;
						Data.hostID=json.getString("hostid");
							
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					break;
				case RECEIVED_BALL:
					if(Data.inviter==false)
					{
						System.out.println((String)msg.obj);
					
					JSONObject json;
					try {
						json = new JSONObject((String)msg.obj);
						float f=(float) json.getDouble("x");
						Data.ball.set(0, f);
						f=(float) json.getDouble("y");
						Data.ball.set(1, f);
							
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
					
					break;
				case RECEIVED_BOARD:
					JSONObject json;
					try {
						
						System.out.println("8888888888888"+(String)msg.obj);
						json = new JSONObject((String)msg.obj);
						float f=(float) json.getDouble("x");
						Data.location.set(json.getInt("id"), f);
						//System.out.println("222222222222"+Data.ball.get(json.getInt("id")));
						} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
					
					
				}
			}

	 };
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Data datadef = new Data();
        datadef.def();
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
        
   
        
        Data.mGameShare = new GameShare(getApplicationContext());
        // bind SHAREIt application
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
		SCREEN_WIDTH/=10;
		SCREEN_HEIGHT/=10;
		set_x=SCREEN_WIDTH*5;
		set_y=SCREEN_HEIGHT*5;
		

	    
	//	mode=2;
	//	inviter=false;
		switch(mode){
			case 1:
				initialize(new SoloMode(), false);
				break;
			case 2:
				if(inviter)
					initialize(new TwoMode(),false);
				else
					initialize(new TwoModeClient(),false);
				break;
			case 3:
				if(inviter)
					initialize(new ThreeMode(),false);
				else
					initialize(new ThreeModeClient(),false);
				break;
			case 4:
				if(inviter)
					initialize(new FourMode(),false);
				else
					initialize(new FourModeClient(),false);
				break;
			default:
				Gdx.app.exit();
		}
			
		
		if(mode==1)
			initialize(new SoloMode(), false);
		else if(mode==2){
			if(inviter==true)
				initialize(new TwoMode(),false);
			else
				initialize(new TwoModeClient(),false);
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
        	
        	
        	 if(Data.inviter==true)
 	        {
 	        	SendData send =new SendData();
 	        	send.unfity();
 	        }
        }
    };

    private GameUserListener mUserListener = new GameUserListener() {

        @Override
        public void onLocalUserChanged(UserEventType type, GameUserInfo user) {
           // Log.v(TAG, "onLocalUserChanged, eventType : " + type + ", userInfo : " + user);
        	System.out.println( "onLocalUserChanged, eventType : " + type + ", userInfo : " + user);
        	Data.mLocalUser = user;
        }

        @Override
        public void onRemoteUserChanged(UserEventType type, GameUserInfo user) {
          //  Log.v(TAG, "onRemoteUserChanged, eventType : " + type + ", userInfo : " + user);
        	System.out.println("onRemoteUserChanged, eventType : " + type + ", userInfo : " + user);
        }
    };
    
    private GameMessageListener mMessageListener = new GameMessageListener() {

    	// received the message from others in the game.
        @Override
        public void onMessage(GameMessage gameMessage) {
           // Log.v(TAG, "onMessage, message : " + gameMessage.toString());
            try {
				AbstractGameMessage message = GameMessages.createAbstractGameMessage(gameMessage.getType(), gameMessage.getMessage());
				System.out.println("11111111111111"+message.getType());
				if (message.getType().equals( GameMessages.MSG_TYPE_SAY_HELLO)) {
					HelloMessage hello = (HelloMessage)message;
					mHandler.sendMessage(mHandler.obtainMessage(MSG_RECEIVED_MESSAGE, hello.getMessage()));
				}else if(message.getType() .equals( GameMessages.TYPE_Remote_Location))
				{
					System.out.println("999999999999"+message.getMessage());
					RemoteLocationMessage test = (RemoteLocationMessage)message;
					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_Remote_Location, test.getMessage()));
				}else if(message.getType() .equals( GameMessages.TYPE_BOARD_LOCATION))
				{
					
					System.out.println("1111999999板板板9999991111"+message.getMessage());
					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_BOARD, message.getMessage()));
				}
				else if(message.getType() .equals( GameMessages.TYPE_BALL_LOCATION))
					{
					System.out.println("9999999球球球99999"+message.getMessage());
					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_BALL, message.getMessage()));
					}
				else if(message.getType().equals( GameMessages.TYPE_UNIFY_ID))
					{
					System.out.println("999999999999"+message.getMessage());
					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_UNFITY_ID, message.getMessage()));
					}
				else if(message.getType() .equals( GameMessages.TYPE_STATE))
					{
					System.out.println("999999999999"+message.getMessage());
					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_STATE, message.getMessage()));
					}
				
				System.out.println("99999999999944444444444444");
				
					
			} catch (JSONException e) {
				return;
			}
        }
    };
    
    
}