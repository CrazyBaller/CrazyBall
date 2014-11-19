package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.edu.seu.message.Data;
import com.edu.seu.message.SendData;
import com.edu.seu.tool.Tool;

public class FourMode implements ApplicationListener, ContactListener,
		InputProcessor {

	private Handler windowHandler;
	private CreateWorld mCreateWorld;
	private World mworld;
	private GL10 gl;

	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;

	private List<Body> blockList = new ArrayList<Body>();

	private Mesh board_mesh;
	private Mesh board_mesh1;
	private Mesh board_mesh2;
	private Mesh board_mesh3;

	private boolean firstTouch = true;

	float tboard1_x;
	float tboard2_y;
	float tboard3_y;

	float old_board_x = 0;
	float old_ball_x = 0;
	float old_ball_y = 0;

	SendData send;
	private float mLastTime = 0;
	private SpriteBatch batch;

	private boolean backReleased = false;
	private Vector2 oldVector;

	public FourMode(Handler h) {
		this.windowHandler = h;
	}

	@Override
	public void create() {
		Log.d("debug", "create");

		board_halfwidth0 = SCREEN_WIDTH * boardrate;
		board_halfwidth1 = SCREEN_WIDTH * boardrate;
		board_halfwidth2 = SCREEN_WIDTH * boardrate;
		board_halfwidth3 = SCREEN_WIDTH * boardrate;
		board_halfwidth = SCREEN_WIDTH * boardrate;
		board_halfheight = board_halfwidth / 5;
		circle_radius_standard = board_halfheight;
		circle_radius = circle_radius_standard;
		block_width = circle_radius;

		send = new SendData();
		// 镜头下的世界
		camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		camera.position.set(0, 10, 0);

		gl = Gdx.graphics.getGL10();
		renderer = new Box2DDebugRenderer();

		// 创建背景世界
		mCreateWorld = new CreateWorld();
		mworld = mCreateWorld.getWorld();

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

		batch = mCreateWorld.getBatch();
		// 创建球和板
		createBallBoard();
		setBallBoardColor();

		initBlock();

		// 设置碰撞监听
		mworld.setContactListener(this);

		// 设置输入监听
		InputMultiplexer inputmultiplexer = new InputMultiplexer();
		inputmultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputmultiplexer);
		Gdx.input.setCatchBackKey(true);
	}

	private void initBlock() {
		blockList.clear();
		for (int i = 0; i < Data.propsimageid.size(); i++) {
			int type = Data.propsimageid.get(i);
			float x = Data.propsimagex.get(i) * SCREEN_WIDTH / 2;
			float y = SCREEN_WIDTH - 2 * board_halfheight
					- Data.propsimagey.get(i) * SCREEN_WIDTH / 2;
			Body t = B2Util.createRectangle(mworld, block_width / 1.6f,
					block_width / 1.6f, x, y, BodyType.StaticBody, 0, 0, 0, 0,
					new BodyData(BodyData.BODY_BLOCK, type), null);
			blockList.add(t);
		}
	}

	private void createBallBoard() {
		// 创建球
		tBall = B2Util.createCircle(mworld, circle_radius_standard, 0,
				SCREEN_WIDTH / 2 - board_halfheight, BodyType.DynamicBody, 0,
				2, 1, 0, new BodyData(BodyData.BODY_BALL), null);
		// 创建挡板
		tBoard0 = B2Util.createRectangle(mworld, board_halfwidth0,
				board_halfheight, 0, 0, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BOARD), null);
		tBoard1 = B2Util.createRectangle(mworld, board_halfwidth1,
				board_halfheight, 0, SCREEN_WIDTH - 2 * board_halfheight,
				BodyType.StaticBody, 0, 0, 0, 0, new BodyData(
						BodyData.BODY_BOARD), null);
		tBoard2 = B2Util.createRectangle(mworld, board_halfheight,
				board_halfwidth2, -SCREEN_WIDTH / 2 + board_halfheight,
				-board_halfheight + SCREEN_WIDTH / 2, BodyType.StaticBody, 0,
				0, 0, 0, new BodyData(BodyData.BODY_BOARD), null);
		tBoard3 = B2Util.createRectangle(mworld, board_halfheight,
				board_halfwidth3, SCREEN_WIDTH / 2 - board_halfheight,
				-board_halfheight + SCREEN_WIDTH / 2, BodyType.StaticBody, 0,
				0, 0, 0, new BodyData(BodyData.BODY_BOARD), null);
	}

	private void setBallBoardColor() {
		float x = tBoard0.getPosition().x;
		float y = tBoard0.getPosition().y;

		board_mesh.setVertices(new float[] { x - board_halfwidth0,
				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				x - board_halfwidth0, y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255), x + board_halfwidth0,
				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				x + board_halfwidth0, y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255) });

		x = tBoard1.getPosition().x;
		y = tBoard1.getPosition().y;

		board_mesh1.setVertices(new float[] { x - board_halfwidth1,
				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				x - board_halfwidth1, y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255), x + board_halfwidth1,
				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				x + board_halfwidth1, y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255) });

		x = tBoard2.getPosition().x;
		y = tBoard2.getPosition().y;

		board_mesh2.setVertices(new float[] { x - board_halfheight,
				y + board_halfwidth2, 0, Color.toFloatBits(0, 0, 0, 255),
				x - board_halfheight, y - board_halfwidth2, 0,
				Color.toFloatBits(0, 0, 0, 255), x + board_halfheight,
				y + board_halfwidth2, 0, Color.toFloatBits(0, 0, 0, 255),
				x + board_halfheight, y - board_halfwidth2, 0,
				Color.toFloatBits(0, 0, 0, 255) });

		x = tBoard3.getPosition().x;
		y = tBoard3.getPosition().y;

		board_mesh3.setVertices(new float[] { x - board_halfheight,
				y + board_halfwidth3, 0, Color.toFloatBits(0, 0, 0, 255),
				x - board_halfheight, y - board_halfwidth3, 0,
				Color.toFloatBits(0, 0, 0, 255), x + board_halfheight,
				y + board_halfwidth3, 0, Color.toFloatBits(0, 0, 0, 255),
				x + board_halfheight, y - board_halfwidth3, 0,
				Color.toFloatBits(0, 0, 0, 255) });

	}

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();
		mLastTime += dt;
		if (mLastTime >= 1.0 / 60) {
			mLastTime = 0;
		} else
			return;

		mworld.step(1.0f / 6.0f, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glClearColor(1f, 1f, 1f, 0f);

		mCreateWorld.getBound_one().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_two().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_three().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_four().render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		tBoard1.setTransform(Data.location.get(1) * SCREEN_WIDTH / 2,
				tBoard1.getWorldCenter().y, 0);
		tboard2_y = SCREEN_WIDTH / 2 - board_halfheight - Data.location.get(2)
				* SCREEN_WIDTH / 2;
		tBoard2.setTransform(tBoard2.getWorldCenter().x, tboard2_y, 0);
		tboard3_y = SCREEN_WIDTH / 2 - board_halfheight - Data.location.get(3)
				* SCREEN_WIDTH / 2;
		tBoard3.setTransform(tBoard2.getWorldCenter().x, tboard3_y, 0);

		setBallBoardColor();
		board_mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		board_mesh1.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		board_mesh2.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		board_mesh3.render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		batch.begin();
		float x = tBall.getPosition().x;
		float y = tBall.getPosition().y;
		circle_radius = tBall.getFixtureList().get(0).getShape().getRadius();
		batch.draw(mCreateWorld.getTexture2(),
				set_x + (x - circle_radius) * 10, set_y - 100f
						+ (y - circle_radius) * 10, 20 * circle_radius,
				20 * circle_radius);
		for (int i = 0; i < blockList.size(); i++) {
			Body b = blockList.get(i);
			BodyData bd = (BodyData) b.getUserData();
			if (bd.health == 0) {
				mworld.destroyBody(b);
				blockList.remove(i);
				i--;
			} else {
				x = b.getPosition().x;
				y = b.getPosition().y;
				batch.draw(mCreateWorld.getBlockTexture(12), set_x
						+ (x - block_width / 2) * 10, set_y - 100f
						+ (y - block_width / 2) * 10, 10 * block_width / 0.8f,
						10 * block_width / 0.8f);
			}
		}
		batch.end();

		if (Gdx.input.isKeyPressed(Keys.BACK) && !backReleased) {
			backReleased = true;
			Message m = new Message();
			m.what = SHOW_DIALOG2;
			windowHandler.sendMessage(m);
			pause();
		}

		camera.update();
		camera.apply(gl);
		renderer.render(mworld, camera.combined);

		if (old_ball_x == tBall.getWorldCenter().x
				& old_ball_y == tBall.getWorldCenter().y) {

		} else {
			Data.ball.set(0, tBall.getWorldCenter().x / (SCREEN_WIDTH / 2));
			Data.ball.set(1, tBall.getWorldCenter().y / (SCREEN_WIDTH / 2));

			send.ball();
			old_ball_x = tBall.getWorldCenter().x;
			old_ball_y = tBall.getWorldCenter().y;
		}

		if (old_board_x != tBoard0.getWorldCenter().x) {
			send.myboard();
			old_board_x = tBoard0.getWorldCenter().x;
		}

	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		Vector3 touchV = new Vector3(arg0, arg1, 0);
		camera.unproject(touchV);
		if (firstTouch) {
			Random r = new Random();
			float xv = r.nextFloat() * 20;
			float yv = 40 - xv;
			if (r.nextInt(2) == 0)
				xv = -xv;
			if (r.nextInt(2) == 0)
				yv = -yv;
			firstTouch = false;
			tBall.setLinearVelocity(xv, yv);
		}

		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		Vector3 touchV = new Vector3(arg0, arg1, 0);
		camera.unproject(touchV);
		// 设置移动坐标
		if (touchV.x <= SCREEN_WIDTH / 2 - board_halfheight * 2
				- board_halfwidth
				&& touchV.x >= -SCREEN_WIDTH / 2 + board_halfheight * 2
						+ board_halfwidth) {
			tBoard0.setTransform(touchV.x, tBoard0.getWorldCenter().y, 0);
			Data.location.set(Data.myID, 2 * tBoard0.getWorldCenter().x
					/ SCREEN_WIDTH);
		}
		return false;
	}

	// 碰撞检测
	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {
		Body cA = arg0.getFixtureA().getBody();
		Body cB = arg0.getFixtureB().getBody();

		BodyData dA = (BodyData) cA.getUserData();
		BodyData dB = (BodyData) cB.getUserData();
		if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BORDER_BOTTOM) // 0号死
				|| (dA.getType() == BodyData.BODY_BORDER_BOTTOM && dB.getType() == BodyData.BODY_BALL)) {
			tBall.setLinearVelocity(0, 0);
			int result = new Tool().judge(0);

			SendData send = new SendData();
			send.sendresult(0, result);

			JSONObject json = new JSONObject();
			try {

				json.put("id", 0);
				json.put("result", result);

			} catch (JSONException e) {

				e.printStackTrace();
			}

			Message m = new Message();
			m.what = SHOW_MYDEAD_DIALOG;
			m.obj = json.toString();
			windowHandler.sendMessage(m);

		} else if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BORDER_UP) // 1号死
				|| (dA.getType() == BodyData.BODY_BORDER_UP && dB.getType() == BodyData.BODY_BALL)) {
			int result = new Tool().judge(1);

			SendData send = new SendData();
			send.sendresult(1, result);

			JSONObject json = new JSONObject();
			try {

				json.put("id", 1);
				json.put("result", result);

			} catch (JSONException e) {

				e.printStackTrace();
			}

			Message m = new Message();
			m.what = SHOW_MYDEAD_DIALOG;
			m.obj = json.toString();
			windowHandler.sendMessage(m);
		} else if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BORDER_LEFT) // 2号死
				|| (dA.getType() == BodyData.BODY_BORDER_LEFT && dB.getType() == BodyData.BODY_BALL)) {

			int result = new Tool().judge(2);

			SendData send = new SendData();
			send.sendresult(2, result);

			JSONObject json = new JSONObject();
			try {

				json.put("id", 2);
				json.put("result", result);

			} catch (JSONException e) {

				e.printStackTrace();
			}

			Message m = new Message();
			m.what = SHOW_MYDEAD_DIALOG;
			m.obj = json;
			windowHandler.sendMessage(m);
		} else if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BORDER_RIGHT) // 3号死
				|| (dA.getType() == BodyData.BODY_BORDER_RIGHT && dB.getType() == BodyData.BODY_BALL)) {

			int result = new Tool().judge(3);

			SendData send = new SendData();
			send.sendresult(3, result);

			JSONObject json = new JSONObject();
			try {

				json.put("id", 3);
				json.put("result", result);

			} catch (JSONException e) {

				e.printStackTrace();
			}

			Message m = new Message();
			m.what = SHOW_MYDEAD_DIALOG;
			m.obj = json;
			windowHandler.sendMessage(m);
		}
	}

	@Override
	public void dispose() {
		Log.d("debug", "dispose");

		if (mworld != null) {
			mworld.dispose();
			mworld = null;
		}
		if (renderer != null) {
			renderer.dispose();
			renderer = null;
		}
		if (mCreateWorld.getTexture2() != null) {
			mCreateWorld.getTexture2().dispose();
		}
		if (mCreateWorld.getBatch() != null) {
			mCreateWorld.getBatch().dispose();
		}
	}

	@Override
	public void pause() {
		Log.d("debug", "pause");
		oldVector = tBall.getLinearVelocity();
		tBall.setLinearVelocity(0, 0);
	}

	@Override
	public void resize(int arg0, int arg1) {
		Log.d("debug", "resize");
	}

	@Override
	public void resume() {
		Log.d("debug", "resume");
		tBall.setLinearVelocity(oldVector);
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
		backReleased = false;
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

	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		return false;
	}

}
