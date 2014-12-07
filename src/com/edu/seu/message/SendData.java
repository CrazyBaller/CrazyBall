package com.edu.seu.message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.lenovo.game.GameMessage;
import com.edu.seu.message.GameMessages.StringMessage;
import static com.edu.seu.crazyball2.Constant.*;

public class SendData {

	public void unfity() {
		// if (Data.mRemoteUser.size() == 0)
		// return;
		Data.myID = 0;
		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			JSONObject json = new JSONObject();
			try {
				json.put("myid", i + 1);
				json.put("hostid", Data.mLocalUser.id);
			} catch (JSONException e) {

				e.printStackTrace();
			}
			StringMessage helloMsg = new StringMessage(
					GameMessages.TYPE_UNIFY_ID, Data.mLocalUser.id,
					Data.mRemoteUser.get(i).id, json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}

	}

	public void answerunfity() {
		// if (Data.mRemoteUser.size() == 0)
		// return;

		JSONObject json = new JSONObject();
		try {
			json.put("id", Data.myID);

		} catch (JSONException e) {

			e.printStackTrace();
		}
		StringMessage helloMsg = new StringMessage(
				GameMessages.TYPE_ANSWER_UNIFY, Data.mLocalUser.id,
				Data.hostID, json.toString());
		// convert to interface message
		GameMessage gameMsg = helloMsg.toGameMessage();
		if (gameMsg == null)
			return;

		// send message
		Data.mGameShare.sendMessage(gameMsg);

	}

	public void myboard() {
		if (Data.mRemoteUser.size() == 0)
			return;

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			JSONObject json = new JSONObject();
			try {
				json.put("id", Data.myID);
				json.put("x", Data.location.get(Data.myID));

			} catch (JSONException e) {

				e.printStackTrace();
			}
			StringMessage helloMsg = new StringMessage(
					GameMessages.TYPE_BOARD_LOCATION, Data.mLocalUser.id,
					Data.mRemoteUser.get(i).id, json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}

	}

	public void boardsize(int id, int size) {
		Data.boardsize.set(id, size);

		if (Data.mRemoteUser.size() == 0)
			return;

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			JSONObject json = new JSONObject();
			try {
				json.put("id", id);
				json.put("size", size);

			} catch (JSONException e) {

				e.printStackTrace();
			}
			StringMessage helloMsg = new StringMessage(
					GameMessages.TYPE_BOARD_SIZE, Data.mLocalUser.id,
					Data.mRemoteUser.get(i).id, json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}

	}

	public void ball() {
		if (Data.mRemoteUser.size() == 0)
			return;

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			JSONObject json = new JSONObject();
			try {

				json.put("x", Data.ball.get(0));
				json.put("y", Data.ball.get(1));
			} catch (JSONException e) {

				e.printStackTrace();
			}
			StringMessage helloMsg = new StringMessage(
					GameMessages.TYPE_BALL_LOCATION, Data.mLocalUser.id,
					Data.mRemoteUser.get(i).id, json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}

	}

	public void ballsize(int size) {
		Data.ballsize = size;

		if (Data.mRemoteUser.size() == 0)
			return;

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			JSONObject json = new JSONObject();
			try {

				json.put("size", size);

			} catch (JSONException e) {

				e.printStackTrace();
			}
			StringMessage helloMsg = new StringMessage(
					GameMessages.TYPE_BALL_SIZE, Data.mLocalUser.id,
					Data.mRemoteUser.get(i).id, json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}

	}

	public void myState() {
		if(Data.mRemoteUser==null||Data.mRemoteUser.size() == 0)
			return;

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			JSONObject json = new JSONObject();
			try {

				json.put("id", Data.myID);
				json.put("state", Data.state.get(Data.myID));
			} catch (JSONException e) {

				e.printStackTrace();
			}
			StringMessage helloMsg = new StringMessage(GameMessages.TYPE_STATE,
					Data.mLocalUser.id, Data.mRemoteUser.get(i).id,
					json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}
	}

	public void state(int id, int state) {
		if (Data.mRemoteUser.size() == 0)
			return;

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			JSONObject json = new JSONObject();
			try {

				json.put("id", id);
				json.put("state", state);
			} catch (JSONException e) {

				e.printStackTrace();
			}
			StringMessage helloMsg = new StringMessage(
					GameMessages.TYPE_ANY_STATE, Data.mLocalUser.id,
					Data.mRemoteUser.get(i).id, json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}
	}

	public void start() {
		if (Data.mRemoteUser.size() == 0)
			return;

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			JSONObject json = new JSONObject();
			try {

				json.put("mode", Data.mode);

			} catch (JSONException e) {

				e.printStackTrace();
			}
			StringMessage helloMsg = new StringMessage(
					GameMessages.TYPE_START_GAME, Data.mLocalUser.id,
					Data.mRemoteUser.get(i).id, json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}
	}

	public void sendresult(int id, int result) {

		Data.state.set(id, result);

		if (Data.mRemoteUser.size() == 0)
			return;

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			JSONObject json = new JSONObject();
			try {

				json.put("id", id);
				if (id == 0)
					json.put("name", Data.mLocalUser.name);
				else
					json.put("name", Data.mRemoteUser.get(id - 1).name);
				json.put("result", result);

			} catch (JSONException e) {

				e.printStackTrace();
			}
			StringMessage helloMsg = new StringMessage(
					GameMessages.TYPE_GAME_RESULT, Data.mLocalUser.id,
					Data.mRemoteUser.get(i).id, json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}
	}

	public void props(int type, int id) {
		if (Data.mRemoteUser.size() == 0)
			return;

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			JSONObject json = new JSONObject();
			try {

				json.put("type", type);
				json.put("id", id);

			} catch (JSONException e) {

				e.printStackTrace();
			}
			StringMessage helloMsg = new StringMessage(GameMessages.TYPE_PROPS,
					Data.mLocalUser.id, Data.mRemoteUser.get(i).id,
					json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}
	}

	public void propsactivity(int type) {
		if (Data.mRemoteUser.size() == 0)
			return;

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			JSONObject json = new JSONObject();
			try {

				json.put("type", type);

			} catch (JSONException e) {

				e.printStackTrace();
			}
			StringMessage helloMsg = new StringMessage(
					GameMessages.TYPE_PROPS_ACTIVITY, Data.mLocalUser.id,
					Data.mRemoteUser.get(i).id, json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}
	}

	public void propsimage() {
		if (Data.mRemoteUser.size() == 0)
			return;

		JSONObject json = new JSONObject();
		try {
			JSONArray array = new JSONArray(Data.propsimageid);
			json.put("propsimageid", array);
			array = new JSONArray(Data.propsimagex);
			json.put("propsimagex", array);
			array = new JSONArray(Data.propsimagey);
			json.put("propsimagey", array);

		} catch (JSONException e) {

			e.printStackTrace();
		}

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			StringMessage helloMsg = new StringMessage(
					GameMessages.TYPE_PROPS_IMAGE, Data.mLocalUser.id,
					Data.mRemoteUser.get(i).id, json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}
	}

	public void eatblock(int id) {
		if (Data.mRemoteUser.size() == 0)
			return;

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			JSONObject json = new JSONObject();
			try {

				json.put("id", id);

			} catch (JSONException e) {

				e.printStackTrace();
			}
			StringMessage helloMsg = new StringMessage(
					GameMessages.TYPE_EAT_BLOCK, Data.mLocalUser.id,
					Data.mRemoteUser.get(i).id, json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}
	}

	public void systemid() {

		JSONObject json = new JSONObject();
		Data.systemid.set(0, Data.mLocalUser.id);

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {

			Data.systemid.set(i + 1, Data.mRemoteUser.get(i).id);

		}

		try {

			json.put("systemid", Data.systemid);

		} catch (JSONException e) {

			e.printStackTrace();
		}

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			StringMessage helloMsg = new StringMessage(
					GameMessages.TYPE_SYSTEM_ID, Data.mLocalUser.id,
					Data.mRemoteUser.get(i).id, json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}
	}

	public void controlId() {
		if (Data.mRemoteUser.size() == 0)
			return;

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			JSONObject json = new JSONObject();
			try {

				json.put("controlId", CONTROL_ID);

			} catch (JSONException e) {

				e.printStackTrace();
			}
			StringMessage helloMsg = new StringMessage(
					GameMessages.TYPE_CONTROLID, Data.mLocalUser.id,
					Data.mRemoteUser.get(i).id, json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}
	}

	public void time() {
		Data.name.set(0, Data.mLocalUser.name);

		for (int i = 0; i < Data.mRemoteUser.size(); i++) {

			Data.name.set(i + 1, Data.mRemoteUser.get(i).name);

		}

		if (Data.mRemoteUser.size() == 0)
			return;

		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray(Data.time);
		JSONArray arrayname = new JSONArray(Data.name);
		try {

			json.put("time", array);
			json.put("name", arrayname);

		} catch (JSONException e) {

			e.printStackTrace();
		}
		for (int i = 0; i < Data.mRemoteUser.size(); i++) {
			StringMessage helloMsg = new StringMessage(GameMessages.TYPE_TIME,
					Data.mLocalUser.id, Data.mRemoteUser.get(i).id,
					json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg == null)
				return;

			// send message
			Data.mGameShare.sendMessage(gameMsg);
		}
	}

}
