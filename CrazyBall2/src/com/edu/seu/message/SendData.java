package com.edu.seu.message;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.lenovo.game.GameMessage;
import com.lenovo.game.GameUserInfo;

import com.edu.seu.message.GameMessages.AbstractGameMessage;
import com.edu.seu.message.GameMessages.StringMessage;

public class SendData {

public void unfity()
{
	//if (Data.mRemoteUser.size() == 0)
	//	return;
	System.out.println("333333333unfity3333333size "+Data.mRemoteUser.size());
	Data.myID=0;
	for(int i=0;i<Data.mRemoteUser.size();i++)
	{ 
		JSONObject json = new JSONObject();
    	try {
			json.put("myid",i+1);
			json.put("hostid", Data.mLocalUser.id);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}	
	StringMessage helloMsg = new StringMessage(GameMessages.TYPE_UNIFY_ID,Data.mLocalUser.id,Data.mRemoteUser.get(i).id, json.toString());
	// convert to interface message
	GameMessage gameMsg = helloMsg.toGameMessage();
	if (gameMsg == null)
		return;
	
	// send message
	Data.mGameShare.sendMessage(gameMsg);
	}

}


public void myboard()
{
	if (Data.mRemoteUser.size() == 0)
		return;

	for(int i=0;i<Data.mRemoteUser.size();i++)
	{	JSONObject json = new JSONObject();
    	try {
			json.put("id",Data.myID);
			json.put("x", Data.location.get(Data.myID));

		} catch (JSONException e) {
			
			e.printStackTrace();
		}	
	StringMessage helloMsg = new StringMessage(GameMessages.TYPE_BOARD_LOCATION,Data.mLocalUser.id,Data.mRemoteUser.get(i).id, json.toString());
	// convert to interface message
	GameMessage gameMsg = helloMsg.toGameMessage();
	if (gameMsg == null)
		return;
	
	// send message
	Data.mGameShare.sendMessage(gameMsg);
	}
	
}


public void ball()
{
	if (Data.mRemoteUser.size() == 0)
		return;

	for(int i=0;i<Data.mRemoteUser.size();i++)
	{	JSONObject json = new JSONObject();
    	try {
			
			json.put("x", Data.ball.get(0));
			json.put("y", Data.ball.get(1));
		} catch (JSONException e) {
			
			e.printStackTrace();
		}	
	StringMessage helloMsg = new StringMessage(GameMessages.TYPE_BALL_LOCATION,Data.mLocalUser.id,Data.mRemoteUser.get(i).id, json.toString());
	// convert to interface message
	GameMessage gameMsg = helloMsg.toGameMessage();
	if (gameMsg == null)
		return;
	
	// send message
	Data.mGameShare.sendMessage(gameMsg);
	}
	
}


public void myState()
{
	if (Data.mRemoteUser.size() == 0)
		return;

	for(int i=0;i<Data.mRemoteUser.size();i++)
	{	JSONObject json = new JSONObject();
    	try {
			
			json.put("id", Data.myID);
			json.put("state", Data.state.get(Data.myID));
		} catch (JSONException e) {
			
			e.printStackTrace();
		}	
	StringMessage helloMsg = new StringMessage(GameMessages.TYPE_STATE,Data.mLocalUser.id,Data.mRemoteUser.get(i).id, json.toString());
	// convert to interface message
	GameMessage gameMsg = helloMsg.toGameMessage();
	if (gameMsg == null)
		return;
	
	// send message
	Data.mGameShare.sendMessage(gameMsg);
	}
}


public void start()
{
	if (Data.mRemoteUser.size() == 0)
		return;

	for(int i=0;i<Data.mRemoteUser.size();i++)
	{	JSONObject json = new JSONObject();
    	try {
			
			json.put("mode", Data.mode);
			
		} catch (JSONException e) {
			
			e.printStackTrace();
		}	
	StringMessage helloMsg = new StringMessage(GameMessages.TYPE_START_GAME,Data.mLocalUser.id,Data.mRemoteUser.get(i).id, json.toString());
	// convert to interface message
	GameMessage gameMsg = helloMsg.toGameMessage();
	if (gameMsg == null)
		return;
	
	// send message
	Data.mGameShare.sendMessage(gameMsg);
	}
}

public void sendresult(int id,int result)
{
	
	Data.state.set(id, result);
	
		
	if (Data.mRemoteUser.size() == 0)
		return;

	for(int i=0;i<Data.mRemoteUser.size();i++)
	{	JSONObject json = new JSONObject();
    	try {
			
			json.put("id", id);
			if(id==0)
				json.put("name", Data.mLocalUser.name);
			else
				json.put("name", Data.mRemoteUser.get(id-1).name);
			json.put("result", result);
			
		} catch (JSONException e) {
			
			e.printStackTrace();
		}	
	StringMessage helloMsg = new StringMessage(GameMessages.TYPE_GAME_RESULT,Data.mLocalUser.id,Data.mRemoteUser.get(i).id, json.toString());
	// convert to interface message
	GameMessage gameMsg = helloMsg.toGameMessage();
	if (gameMsg == null)
		return;
	
	// send message
	Data.mGameShare.sendMessage(gameMsg);
	}
}





}
