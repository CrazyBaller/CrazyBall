package com.edu.seu.message;

import org.json.JSONException;
import org.json.JSONObject;

import com.lenovo.game.GameMessage;

public class GameMessages {
	public static final String MSG_TYPE_SAY_HELLO = "say_hello";

	public static final String MSC_TYPE_TWO_SEND = "two_send";

	public static final String TYPE_Remote_Location = "Remote_Location";

	public static final String TYPE_BOARD_LOCATION = "board_location";

	public static final String TYPE_BOARD_SIZE = "board_size";

	public static final String TYPE_BALL_LOCATION = "ball_location";

	public static final String TYPE_BALL_SIZE = "ball_size";

	public static final String TYPE_UNIFY_ID = "unify_id";

	public static final String TYPE_STATE = "my_state";

	public static final String TYPE_ANY_STATE = "any_state";

	public static final String TYPE_START_GAME = "startgame";

	public static final String TYPE_GAME_RESULT = "game_result";

	public static final String TYPE_PROPS = "props";

	public static final String TYPE_PROPS_ACTIVITY = "props_activity";

	public static final String TYPE_PROPS_IMAGE = "props_image";

	public static final String TYPE_EAT_BLOCK = "eat_block";

	public static final String TYPE_SYSTEM_ID = "system_id";

	public static final String TYPE_TIME = "time";

	public static final String TYPR_STRING = "string";

	public static final String TYPE_CONTROLID = "controlid";

	public static final String TYPE_ANSWER_UNIFY = "answer_unify";

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
			} catch (JSONException e) {
			}
			return null;
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

		private String mType;
		private String mFrom;
		private String mTo;
		String mMessage;
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

	public static class StringMessage extends AbstractGameMessage {

		public StringMessage() {
			super(TYPR_STRING);
		}

		public StringMessage(String type) {
			super(type);
		}

		public StringMessage(String type, String from, String to, String message) {
			super(type);

			setFrom(from);
			setTo(to);
			mMessage = message;
		}
	}

	public static AbstractGameMessage createAbstractGameMessage(String type,
			String message) throws JSONException {
		AbstractGameMessage gameMessage = null;
		JSONObject json = new JSONObject(message);
		if (type.equalsIgnoreCase(MSG_TYPE_SAY_HELLO))
			gameMessage = new HelloMessage();
		else if (type.equalsIgnoreCase(TYPE_Remote_Location))
			gameMessage = new RemoteLocationMessage();
		else {
			gameMessage = new StringMessage(type);

		}
		if (gameMessage == null)
			return null;

		gameMessage.fromJSON(json);
		return gameMessage;
	}
}
