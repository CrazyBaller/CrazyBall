package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import android.util.Log;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.edu.seu.message.Data;
import com.edu.seu.message.SendData;
import com.edu.seu.message.GameMessages.RemoteLocationMessage;
import com.lenovo.game.GameMessage;
import com.lenovo.game.GameUserInfo;

public class FourModeClient implements ApplicationListener, ContactListener,
		InputProcessor {

	private GL10 gl;
	private Handler windowHandler;
	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;

	private Mesh bound_one;
	private Mesh bound_two;
	private Mesh bound_three;
	private Mesh bound_four;
	private Mesh board_mesh;
	private Mesh board_mesh1;
	private Mesh board_mesh2;
	private Mesh board_mesh3;

	private SpriteBatch batch;
	private Texture texture2;

	private float board_x = 0;
	private float board_y = 0;
	private float board1_x = 0;
	private float board1_y = 0;
	private float board2_x = 0;
	private float board2_y = 0;
	private float board3_x = 0;
	private float board3_y = 0;
	private float ball_x = 0;
	private float ball_y = circle_radius + board_halfheight;

	private int type = 1;

	float board_halfwidth = SCREEN_WIDTH * boardrate;

	SendData send = null;
	private float old_board_x = 0;

	public FourModeClient(Handler h) {
		this.windowHandler = h;
	}

	@Override
	public void create() {
		Log.d("debug", "create");

		send = new SendData();

		board_halfheight = board_halfwidth * 1 / 5;
		// 镜头下的世界
		camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		camera.position.set(0, 10, 0);

		gl = Gdx.graphics.getGL10();

		board_mesh = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		board_mesh1 = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		board_mesh2 = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		board_mesh3 = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));

		setBoundColor();
		setBallBoardColor();

		batch = new SpriteBatch();
		texture2 = new Texture(Gdx.files.internal("data/ball.png"));

		// 设置输入监听
		InputMultiplexer inputmultiplexer = new InputMultiplexer();
		inputmultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputmultiplexer);

	}

	private void setBoundColor() {
		float halfwidth = bound_width / 2;
		float halfheight = SCREEN_WIDTH / 2;

		float x = 0;
		float y = -board_halfheight + SCREEN_WIDTH - bound_width / 2;

		if (bound_one == null) {
			bound_one = new Mesh(true, 4, 4, new VertexAttribute(
					Usage.Position, 3, "a_position"), new VertexAttribute(
					Usage.ColorPacked, 4, "a_color"));
			bound_one.setVertices(new float[] { x - halfheight, y + halfwidth,
					0, Color.toFloatBits(192, 0, 0, 255), x - halfheight,
					y - halfwidth, 0, Color.toFloatBits(192, 0, 0, 255),
					x + halfheight, y + halfwidth, 0,
					Color.toFloatBits(192, 0, 0, 255), x + halfheight,
					y - halfwidth, 0, Color.toFloatBits(192, 0, 0, 255) });
			bound_one.setIndices(new short[] { 0, 1, 2, 3 });
		}

		x = -SCREEN_WIDTH / 2;
		y = -board_halfheight + SCREEN_WIDTH / 2;

		if (bound_two == null) {
			bound_two = new Mesh(true, 4, 4, new VertexAttribute(
					Usage.Position, 3, "a_position"), new VertexAttribute(
					Usage.ColorPacked, 4, "a_color"));
			bound_two.setVertices(new float[] { x - halfwidth, y + halfheight,
					0, Color.toFloatBits(192, 0, 0, 255), x - halfwidth,
					y - halfheight, 0, Color.toFloatBits(192, 0, 0, 255),
					x + halfwidth, y + halfheight, 0,
					Color.toFloatBits(192, 0, 0, 255), x + halfwidth,
					y - halfheight, 0, Color.toFloatBits(192, 0, 0, 255) });
			bound_two.setIndices(new short[] { 0, 1, 2, 3 });
		}

		x = SCREEN_WIDTH / 2;
		y = -board_halfheight + SCREEN_WIDTH / 2;

		if (bound_three == null) {
			bound_three = new Mesh(true, 4, 4, new VertexAttribute(
					Usage.Position, 3, "a_position"), new VertexAttribute(
					Usage.ColorPacked, 4, "a_color"));
			bound_three.setVertices(new float[] { x - halfwidth,
					y + halfheight, 0, Color.toFloatBits(192, 0, 0, 255),
					x - halfwidth, y - halfheight, 0,
					Color.toFloatBits(192, 0, 0, 255), x + halfwidth,
					y + halfheight, 0, Color.toFloatBits(192, 0, 0, 255),
					x + halfwidth, y - halfheight, 0,
					Color.toFloatBits(192, 0, 0, 255) });
			bound_three.setIndices(new short[] { 0, 1, 2, 3 });
		}

		x = 0;
		y = -board_halfheight + bound_width / 2;

		if (bound_four == null) {
			bound_four = new Mesh(true, 4, 4, new VertexAttribute(
					Usage.Position, 3, "a_position"), new VertexAttribute(
					Usage.ColorPacked, 4, "a_color"));
			bound_four.setVertices(new float[] { x - halfheight, y + halfwidth,
					0, Color.toFloatBits(192, 0, 0, 255), x - halfheight,
					y - halfwidth, 0, Color.toFloatBits(192, 0, 0, 255),
					x + halfheight, y + halfwidth, 0,
					Color.toFloatBits(192, 0, 0, 255), x + halfheight,
					y - halfwidth, 0, Color.toFloatBits(192, 0, 0, 255) });
			bound_four.setIndices(new short[] { 0, 1, 2, 3 });
		}

	}

	private void setBallBoardColor() {
		float x = board_x;
		float y = board_y;

		board_mesh.setVertices(new float[] { x - board_halfwidth,
				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				x - board_halfwidth, y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255), x + board_halfwidth,
				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				x + board_halfwidth, y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255) });

		if (Data.myID == 1) {
			type = 1;
			board1_x = Data.location.get(0) * SCREEN_WIDTH / 2;
			board1_y = SCREEN_WIDTH - 2 * board_halfheight;

			board2_x = -SCREEN_WIDTH / 2 + board_halfheight;
			board2_y = SCREEN_WIDTH / 2 - board_halfheight
					- Data.location.get(2) * SCREEN_WIDTH / 2;

			board3_x = SCREEN_WIDTH / 2 - board_halfheight;
			board3_y = SCREEN_WIDTH / 2 - board_halfheight
					- Data.location.get(3) * SCREEN_WIDTH / 2;
		} else if (Data.myID == 2) {
			type = 2;

			board1_x = Data.location.get(3) * SCREEN_WIDTH / 2;
			board1_y = SCREEN_WIDTH - 2 * board_halfheight;

			board2_x = -SCREEN_WIDTH / 2 + board_halfheight;
			board2_y = SCREEN_WIDTH / 2 - board_halfheight
					- Data.location.get(1) * SCREEN_WIDTH / 2;

			board3_x = SCREEN_WIDTH / 2 - board_halfheight;
			board3_y = SCREEN_WIDTH / 2 - board_halfheight
					- Data.location.get(0) * SCREEN_WIDTH / 2;

		} else {
			type = 3;
			board1_x = Data.location.get(2) * SCREEN_WIDTH / 2;
			board1_y = SCREEN_WIDTH - 2 * board_halfheight;

			board2_x = -SCREEN_WIDTH / 2 + board_halfheight;
			board2_y = SCREEN_WIDTH / 2 - board_halfheight
					- Data.location.get(0) * SCREEN_WIDTH / 2;

			board3_x = SCREEN_WIDTH / 2 - board_halfheight;
			board3_y = SCREEN_WIDTH / 2 - board_halfheight
					- Data.location.get(1) * SCREEN_WIDTH / 2;
		}
		board_mesh1
				.setVertices(new float[] { board1_x - board_halfwidth,
						board1_y + board_halfheight, 0,
						Color.toFloatBits(0, 0, 0, 255),
						board1_x - board_halfwidth,
						board1_y - board_halfheight, 0,
						Color.toFloatBits(0, 0, 0, 255),
						board1_x + board_halfwidth,
						board1_y + board_halfheight, 0,
						Color.toFloatBits(0, 0, 0, 255),
						board1_x + board_halfwidth,
						board1_y - board_halfheight, 0,
						Color.toFloatBits(0, 0, 0, 255) });

		board_mesh2.setVertices(new float[] { board2_x - board_halfheight,
				board2_y + board_halfwidth, 0, Color.toFloatBits(0, 0, 0, 255),
				board2_x - board_halfheight, board2_y - board_halfwidth, 0,
				Color.toFloatBits(0, 0, 0, 255), board2_x + board_halfheight,
				board2_y + board_halfwidth, 0, Color.toFloatBits(0, 0, 0, 255),
				board2_x + board_halfheight, board2_y - board_halfwidth, 0,
				Color.toFloatBits(0, 0, 0, 255) });

		board_mesh3.setVertices(new float[] { board3_x - board_halfheight,
				board3_y + board_halfwidth, 0, Color.toFloatBits(0, 0, 0, 255),
				board3_x - board_halfheight, board3_y - board_halfwidth, 0,
				Color.toFloatBits(0, 0, 0, 255), board3_x + board_halfheight,
				board3_y + board_halfwidth, 0, Color.toFloatBits(0, 0, 0, 255),
				board3_x + board_halfheight, board3_y - board_halfwidth, 0,
				Color.toFloatBits(0, 0, 0, 255) });

	}

	@Override
	public void render() {
		// if() //dead
		// windowHandler.sendMessage(windowHandler.obtainMessage());

		if (Data.mRemoteUser.size() != 0) {
			GameUserInfo remoteUser = Data.mRemoteUser.get(0);

			JSONObject json = new JSONObject();
			try {
				json.put("board_x", board_x);
				json.put("board_y", board_y);
			} catch (JSONException e) {

				e.printStackTrace();
			}

			RemoteLocationMessage helloMsg = new RemoteLocationMessage(
					Data.mLocalUser.id, remoteUser.id, json.toString());
			// convert to interface message
			GameMessage gameMsg = helloMsg.toGameMessage();
			if (gameMsg != null)
				Data.mGameShare.sendMessage(gameMsg);

		}

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glClearColor(1f, 1f, 1f, 0f);

		bound_one.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		bound_two.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		bound_three.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		bound_four.render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		setBallBoardColor();
		board_mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		board_mesh1.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		board_mesh2.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		board_mesh3.render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		batch.begin();
		if (type == 1) {
			ball_x = Data.ball.get(0) * SCREEN_WIDTH / 2;
			ball_y = SCREEN_WIDTH - 2 * board_halfheight - Data.ball.get(1)
					* SCREEN_WIDTH / 2;
			batch.draw(texture2, set_x - 20f + ball_x * 10, set_y - 120f
					+ ball_y * 10, 40f, 40f);
		} else if (type == 2) {
			ball_x = (SCREEN_WIDTH / 2) * (1 - Data.ball.get(1));
			ball_y = (SCREEN_WIDTH / 2) * (1 - Data.ball.get(0))
					- board_halfheight;
			batch.draw(texture2, set_x - 20f + ball_x * 10, set_y - 120f
					+ ball_y * 10, 40f, 40f);
		} else {
			ball_x = (SCREEN_WIDTH / 2) * (Data.ball.get(1) - 1)
					+ board_halfheight;
			ball_y = (SCREEN_WIDTH / 2) * (1 - Data.ball.get(0));
			batch.draw(texture2, set_x - 20f + ball_x * 10, set_y - 120f
					+ ball_y * 10, 40f, 40f);
		}
		batch.end();

		camera.update();
		camera.apply(gl);

		if (old_board_x != board_x) {
			send.myboard();
			old_board_x = board_x;
		}

	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		Vector3 vTouch = new Vector3(arg0, arg1, 0);
		// 像素坐标转换为world坐标
		camera.unproject(vTouch);

		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		Vector3 touchV = new Vector3(arg0, arg1, 0);
		camera.unproject(touchV);

		board_x = touchV.x;
		System.out.println("touch drag");

		return false;
	}

	@Override
	public void dispose() {
		Log.d("debug", "dispose");

		if (renderer != null) {
			renderer.dispose();
			renderer = null;
		}
		if (batch != null) {
			batch.dispose();
			batch = null;
		}
	}

	@Override
	public void pause() {
		Log.d("debug", "pause");
	}

	@Override
	public void resize(int arg0, int arg1) {
		Log.d("debug", "resize");
	}

	@Override
	public void resume() {
		Log.d("debug", "resume");
	}

	@Override
	public boolean keyDown(int arg0) {
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		return false;
	}

	@Override
	public void beginContact(Contact arg0) {

	}

	@Override
	public void endContact(Contact arg0) {

	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {
		// TODO Auto-generated method stub

	}

}
