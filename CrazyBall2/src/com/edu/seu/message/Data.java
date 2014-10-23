package com.edu.seu.message;

import static com.edu.seu.crazyball2.Constant.SCREEN_WIDTH;
import static com.edu.seu.crazyball2.Constant.board_halfheight;

import java.util.List;
import java.util.ArrayList;

import com.lenovo.game.GameUserInfo;
import com.lenovo.gamesdk.GameShare;

public class Data {
	public static int mode=1;
	public static boolean inviter=false;
	public static GameShare mGameShare;
    public static GameUserInfo mLocalUser;
    public static List<GameUserInfo> mRemoteUser;
    
    public static int flag=0;
    public static ArrayList<Float> location = new ArrayList<Float>();
	public static ArrayList<Integer> state = new ArrayList<Integer>();
	public static ArrayList<Float> ball = new ArrayList<Float>();
	public static int myID = 1;
	public static String hostID = null;
    
    
    public static float board2_x=0;
    public static float board2_y=SCREEN_WIDTH-2*board_halfheight;
    
    
    public void def()
    {
    	if(flag==0){
    		flag=1;
   		 float def = 0;
   		for(int i=0;i<4;i++)
   		{
   		
   		location.add(def);
   		state.add(0);
   		}
   		
   		ball.add(def);
   		ball.add(def);
       	}
    }
    
	
	public  void setMode (int mode)
	{
		this.mode=mode;
	}
	
	public int getMode()
	{
		return this.mode;
	}

	public void setInviter(boolean inviter)
	{
		this.inviter=inviter;
	}
	
	public boolean getInviter()
	{
		return this.inviter;
	}

}
