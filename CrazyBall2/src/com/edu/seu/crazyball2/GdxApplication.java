package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;
import static com.edu.seu.message.Data.*;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


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
	    //private GameShare mGameShare;
	    //private GameUserInfo mLocalUser;
	    //private List<GameUserInfo> mRemoteUser;
	
	 private Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
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
				}
			}

	 };
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
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
		
//		mode=2;
//		inviter=false;
		
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
				if (message.getType() == GameMessages.MSG_TYPE_SAY_HELLO) {
					HelloMessage hello = (HelloMessage)message;
					mHandler.sendMessage(mHandler.obtainMessage(MSG_RECEIVED_MESSAGE, hello.getMessage()));
				}else if(message.getType() == GameMessages.TYPE_Remote_Location)
				{
					RemoteLocationMessage test = (RemoteLocationMessage)message;
					mHandler.sendMessage(mHandler.obtainMessage(RECEIVED_Remote_Location, test.getMessage()));
				}
			} catch (JSONException e) {
				return;
			}
        }
    };
    
    
}