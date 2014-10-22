package com.edu.seu.message;

import org.json.JSONException;
import org.json.JSONObject;

import com.lenovo.game.GameMessage;

public class GameMessages {
    public static final String MSG_TYPE_SAY_HELLO = "say_hello";
    
    public static final String MSC_TYPE_TWO_SEND = "two_send";
    
    public static final String TYPE_Remote_Location = "Remote_Location";
    
    public static abstract class AbstractGameMessage {
    	AbstractGameMessage(String type) {
    		mType = type;
    	}
    	
    	public void setFrom(String from) {
    		mFrom = from;
    	}
    	
    	public void setTo(String to) {
    		mTo = to;
    	}
    	
    	public String getFrom() {
    		return mFrom;
    	}
    	
    	public String getTo() {
    		return mTo;
    	}
    	
    	public String getType() {
    		return mType;
    	}
    	
    	public GameMessage toGameMessage() {
    		try {
    			GameMessage gameMsg = new GameMessage(mType, mFrom, mTo);
				gameMsg.setMessage(toJSON().toString());
				return gameMsg;
			} catch (JSONException e) {}
    		return null;
    	}
    	
    	abstract public JSONObject toJSON() throws JSONException;
    	
    	abstract public void fromJSON(JSONObject json) throws JSONException;
    	
    	private String mType;
    	private String mFrom;
    	private String mTo;
    }

    public static class HelloMessage extends AbstractGameMessage {
    	String mMessage;
    	
    	public HelloMessage() {
        	super(MSG_TYPE_SAY_HELLO);
        }

        public HelloMessage(String from, String to, String message) {
            super(MSG_TYPE_SAY_HELLO);
            
            setFrom(from);
            setTo(to);
            mMessage = message;
        }
        
        public String getMessage() {
            return mMessage;
        }
        
        public JSONObject toJSON() throws JSONException {
        	JSONObject json = new JSONObject();
        	json.put("type", getType());
        	json.put("message", mMessage);
        	return json;
        }
        
        public void fromJSON(JSONObject json) throws JSONException {
        	mMessage = json.getString("message");
        }
    }
    
    
    public static class TwoMessage extends AbstractGameMessage {
    	int mMessage;
    	
    	public TwoMessage() {
        	super(MSC_TYPE_TWO_SEND);
        }

        public TwoMessage(String from, String to, int message) {
            super(MSC_TYPE_TWO_SEND);
            
            setFrom(from);
            setTo(to);
            mMessage = message;
        }
        
        public int getMessage() {
            return mMessage;
        }
        
        public JSONObject toJSON() throws JSONException {
        	JSONObject json = new JSONObject();
        	json.put("type", getType());
        	json.put("message", mMessage);
        	return json;
        }
        
        public void fromJSON(JSONObject json) throws JSONException {
        	mMessage = json.getInt("message");
        }
    }
    
    
    
    
    public static class RemoteLocationMessage extends AbstractGameMessage {
    	String mMessage;
    	
    	public RemoteLocationMessage() {
        	super(TYPE_Remote_Location);
        }

        public RemoteLocationMessage(String from, String to, String message) {
            super(TYPE_Remote_Location);
            
            setFrom(from);
            setTo(to);
            mMessage = message;
        }
        
        public String getMessage() {
            return mMessage;
        }
        
        public JSONObject toJSON() throws JSONException {
        	JSONObject json = new JSONObject();
        	json.put("type", getType());
        	json.put("message", mMessage);
        	return json;
        }
        
        public void fromJSON(JSONObject json) throws JSONException {
        	mMessage = json.getString("message");
        }
    }
    
    
    
    
    
    

    public static AbstractGameMessage createAbstractGameMessage (String type, String message) throws JSONException {
    	AbstractGameMessage gameMessage = null;
    	JSONObject json = new JSONObject(message);
    	if (type.equalsIgnoreCase(MSG_TYPE_SAY_HELLO))
    		gameMessage = new HelloMessage();
    	else if(type.equalsIgnoreCase(MSC_TYPE_TWO_SEND))
    		gameMessage = new TwoMessage();
    	else if(type.equalsIgnoreCase(TYPE_Remote_Location))
    		gameMessage = new RemoteLocationMessage();
    	if (gameMessage == null)
    		return null;
    	
    	gameMessage.fromJSON(json);
    	return gameMessage;
    }
}
