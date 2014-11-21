package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.edu.seu.message.Data;
import com.edu.seu.message.SendData;
import com.edu.seu.props.PropsObservable;

public class TwoModeClient implements ApplicationListener, ContactListener,
		InputProcessor {

	private GL10 gl;
	private Handler windowHandler;
	private OrthographicCamera camera;
	private CreateWorld mCreateWorld;
	private Body[] mB = new Body[4];
	private Body[] slipe = new Body[2];
	private Fixture m_sensor;

	private boolean touchingSensor = false;

	private PropsObservable po;
	Body tB;
	private Mesh board_mesh;
	private Mesh board_mesh1;

	private float board_x = 0;
	private float board_y = 0;
	private float ball_x = 0;
	private float ball_y = 0;

	SendData send = null;
	private float old_board_x = 0;
	private float mLastTime = 0;
	private SpriteBatch batch;

	float x;
	float y;
	
	private boolean firstTouch = true;
	private boolean backReleased = false;
	private Vector2 oldVector;

	public TwoModeClient(Handler h, PropsObservable po) {
		this.windowHandler = h;
		this.po = po;
	}

	@Override
	public void create() {
		Log.d("debug", "create");
		board_halfwidth0 = SCREEN_WIDTH * boardrate;
		board_halfwidth1 = SCREEN_WIDTH * boardrate;
		board_halfwidth = SCREEN_WIDTH * boardrate;
		board_halfheight = board_halfwidth / 5;
		circle_radius_standard = board_halfheight;
		circle_radius = circle_radius_standard;
		block_width = 1f * circle_radius;

		send = new SendData();

		// init color
		initColor();
		// ��ͷ�µ�����
		camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		camera.position.set(0, 12, 0);

		gl = Gdx.graphics.getGL10();

		// ������������
		mCreateWorld = new CreateWorld();
		mworld = mCreateWorld.getWorld();
		batch = mCreateWorld.getBatch();

		board_mesh = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		board_mesh1 = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));

		createBallBoard();
		setBallBoardColor();

		// ��ʼ����������
		initBlock();
		initMyblock();

		// ������Ӧ��
		tSensor = B2Util.createSensor(mworld, base_width * 2, m_sensor, 0f,
				SCREEN_WIDTH / 2, new BodyData(BodyData.BODY_SENSOR), null);
		m_sensor = tSensor.getFixtureList().get(0);

		// �����������
		InputMultiplexer inputmultiplexer = new InputMultiplexer();
		inputmultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputmultiplexer);
		Gdx.input.setCatchBackKey(true);
		// ������ײ����
		mworld.setContactListener(this);

	}

	private void initColor() {
		colors[0] = Color.valueOf("4db6af");
		colors[1] = Color.valueOf("f26d6e");
		colors[2] = Color.valueOf("6fcda8");
		colors[3] = Color.valueOf("fd987a");
		bgcolor = Color.valueOf("34495E");
	}

	private void initMyblock() {
		for (int i = 0; i < 4; i++) {
			myBlock[i] = 0;
		}
		mB[0] = B2Util.createRectangle(mworld, base_width, base_width,
				(-SCREEN_WIDTH / 2 + base_width * 1.5f),
				-(board_halfheight + base_width * 1.5f), BodyType.StaticBody,
				0, 0, 0, 0, new BodyData(BodyData.BODY_BLOCK, 31), null);
		mB[1] = B2Util.createRectangle(mworld, base_width, base_width,
				(-SCREEN_WIDTH / 2 + base_width * 3.5f),
				-(board_halfheight + base_width * 1.5f), BodyType.StaticBody,
				0, 0, 0, 0, new BodyData(BodyData.BODY_BLOCK, 32), null);
		mB[2] = B2Util.createRectangle(mworld, base_width, base_width,
				(-SCREEN_WIDTH / 2 + base_width * 5.5f),
				-(board_halfheight + base_width * 1.5f), BodyType.StaticBody,
				0, 0, 0, 0, new BodyData(BodyData.BODY_BLOCK, 33), null);
		mB[3] = B2Util.createRectangle(mworld, base_width, base_width,
				(-SCREEN_WIDTH / 2 + base_width * 7.5f),
				-(board_halfheight + base_width * 1.5f), BodyType.StaticBody,
				0, 0, 0, 0, new BodyData(BodyData.BODY_BLOCK, 34), null);
		slipe[0] = B2Util.createRectangle(mworld, base_width, base_width,
				(-SCREEN_WIDTH / 2 + base_width * 2.5f),
				-(board_halfheight + base_width * 4f), BodyType.StaticBody, 0,
				0, 0, 0, new BodyData(BodyData.BODY_BLOCK, 10), null);
		slipe[1] = B2Util.createRectangle(mworld, base_width, base_width,
				(-SCREEN_WIDTH / 2 + base_width * 6.5f),
				-(board_halfheight + base_width * 4f), BodyType.StaticBody, 0,
				0, 0, 0, new BodyData(BodyData.BODY_BLOCK, 11), null);
	}

	private void initBlock() {
		Set<Integer> nums = new HashSet<Integer>();
		Random rd = new Random();
		while (nums.size() < 9) {
			nums.add((int) (rd.nextInt(100)));
		}

		Iterator<Integer> iter = nums.iterator();

		Data.propsimageid.clear();
		Data.propsimagex.clear();
		Data.propsimagey.clear();
		Data.blockList.clear();
		int id = 0;
		for (int i = 0; i < 5; i++) {
			Integer temp = iter.next();
			float x = (temp % 10 - 5) * (block_width * 2.4f);
			float y = (3 + (temp / 10)) * block_width * 2.4f;
			int type = rd.nextInt(4) + 31;
			Body tB = B2Util.createRectangle(mworld, block_width / 1.6f,
					block_width / 1.6f, x, y, BodyType.StaticBody, 0, 0, 0, 0,
					new BodyData(BodyData.BODY_BLOCK, type, id), null);
			Data.blockList.add(tB);
			id++;
			Data.propsimageid.add(type);
			Data.propsimagex.add(x / (SCREEN_WIDTH / 2));
			Data.propsimagey.add(y / (SCREEN_WIDTH / 2));

		}
		for (int i = 0; i < 2; i++) {
			Integer temp = iter.next();
			float x = (temp % 10 - 5) * (block_width * 2.4f);
			float y = (3 + (temp / 10)) * block_width * 2.4f;
			int type = 41;
			Body tB = B2Util.createRectangle(mworld, block_width / 1.6f,
					block_width / 1.6f, x, y, BodyType.StaticBody, 0, 0, 0, 0,
					new BodyData(BodyData.BODY_BLOCK, type, id), null);
			Data.blockList.add(tB);
			id++;
			Data.propsimageid.add(type);
			Data.propsimagex.add(x / (SCREEN_WIDTH / 2));
			Data.propsimagey.add(y / (SCREEN_WIDTH / 2));

		}
		while (iter.hasNext()) {
			Integer temp = iter.next();
			float x = (temp % 10 - 5) * (block_width * 2.4f);
			float y = (3 + temp / 10) * block_width * 2.4f;
			int type = rd.nextInt(4) + 21;
			Body tB = B2Util.createRectangle(mworld, block_width / 1.6f,
					block_width / 1.6f, x, y, BodyType.StaticBody, 0, 0, 0, 0,
					new BodyData(BodyData.BODY_BLOCK, type, id), null);
			Data.blockList.add(tB);
			id++;
			Data.propsimageid.add(type);
			Data.propsimagex.add(x / (SCREEN_WIDTH / 2));
			Data.propsimagey.add(y / (SCREEN_WIDTH / 2));
		}
		send.propsimage(); // /���͵���λ��

	}

	private void createBallBoard() {
		// ������
		tBall = B2Util.createCircle(mworld, circle_radius_standard, 0,
				SCREEN_WIDTH / 2 - board_halfheight, BodyType.DynamicBody, 0,
				2, 1, 0, new BodyData(BodyData.BODY_BALL), null);
		// ��������
		tBoard1 = B2Util.createRectangle(mworld, board_halfwidth1,
				board_halfheight, 0, 0, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BOARD), null);
		tBoard0 = B2Util.createRectangle(mworld, board_halfwidth0,
				board_halfheight, 0, SCREEN_WIDTH - 2 * board_halfheight,
				BodyType.StaticBody, 0, 0, 0, 0, new BodyData(
						BodyData.BODY_BOARD), null);

	}

	private void setBallBoardColor() {
		board_x = tBoard0.getPosition().x;
		board_y = tBoard0.getPosition().y;
		board_mesh.setVertices(new float[] { board_x - board_halfwidth0,
				board_y + board_halfheight, 0, colors[0].toFloatBits(),
				board_x - board_halfwidth0, board_y - board_halfheight, 0,
				colors[0].toFloatBits(), board_x + board_halfwidth0,
				board_y + board_halfheight, 0, colors[0].toFloatBits(),
				board_x + board_halfwidth0, board_y - board_halfheight, 0,
				colors[0].toFloatBits() });

		x = tBoard1.getPosition().x;
		y = tBoard1.getPosition().y;
		board_mesh1.setVertices(new float[] { x - board_halfwidth1,
				y + board_halfheight, 0, colors[1].toFloatBits(),
				x - board_halfwidth1, y - board_halfheight, 0,
				colors[1].toFloatBits(), x + board_halfwidth1,
				y + board_halfheight, 0, colors[1].toFloatBits(),
				x + board_halfwidth1, y - board_halfheight, 0,
				colors[1].toFloatBits() });
	}

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();
		mLastTime += dt;
		if (mLastTime >= 1.0 / 60) {
			mLastTime = 0;
		} else
			return;

		mworld.step(1.0f / 60f, 1, 1);

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glClearColor(1f, 1f, 1f, 0f);

		mCreateWorld.getHead().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBackground().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_one().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_two().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_three().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_four().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getControlBackground()
				.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getSlipeBackground().render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		tBoard0.setTransform(Data.location.get(0) * SCREEN_WIDTH / 2,
				tBoard0.getWorldCenter().y, 0);
		// setBallBoardColor();
		// board_mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		// board_mesh1.render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		// batch.begin();
		ball_x = Data.ball.get(0) * SCREEN_WIDTH / 2;
		ball_y = SCREEN_WIDTH - 2 * board_halfheight - Data.ball.get(1)
				* SCREEN_WIDTH / 2;
		tBall.setTransform(ball_x, ball_y, 0);
		circle_radius = tBall.getFixtureList().get(0).getShape().getRadius();

		setBallBoardColor();
		board_mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		board_mesh1.render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		batch.begin();

		// ������
		mCreateWorld.setBoundCircle();

		// ���������ڶ�
		if (canTouching) {
			batch.draw(mCreateWorld.getBlockTexture(541), set_x
					+ (0 - base_width * 2) * 10f, set_y - 120f
					+ (SCREEN_WIDTH / 2 - base_width * 2) * 10f,
					40 * base_width, 40 * base_width);
		}
		batch.draw(mCreateWorld.getTexture2(), set_x + (ball_x - circle_radius)
				* 10, set_y - 120f + (ball_y - circle_radius) * 10,
				20 * circle_radius, 20 * circle_radius);

		for (int i = 0; i < Data.blockList.size(); i++) {
			Body b = Data.blockList.get(i);
			BodyData bd = (BodyData) b.getUserData();
			if (bd.health == 0) {
				mworld.destroyBody(b);
				Data.blockList.remove(i);
				i--;
			} else {
				x = b.getPosition().x;
				y = b.getPosition().y;
				batch.draw(
						mCreateWorld.getBlockTexture(400 + bd.getchangeType()),
						set_x + (x - block_width / 1.2f) * 10, set_y - 120f
								+ (y - block_width / 1.2f) * 10,
						10 * block_width / 0.6f, 10 * block_width / 0.6f);
			}
		}
		if (Data.blockList.size() == 0) {
			initBlock();
		}
		for (int i = 0; i < 4; i++) {
			Body b = mB[i];
			float mBx = b.getPosition().x;
			float mBy = b.getPosition().y;
			if (myBlock[i] == 0) {
				batch.draw(mCreateWorld.getBlockTexture(0), set_x
						+ (mBx - base_width) * 10f, set_y - 120f
						+ (mBy - base_width / 2) * 10.6f,
						10 * base_width / 0.6f, 10 * base_width / 0.6f);
			} else {
				batch.draw(
						mCreateWorld.getBlockTexture(Data.myID * 100 + 21 + i),
						set_x + (mBx - base_width) * 10f, set_y - 120f
								+ (mBy - base_width / 2) * 10.6f,
						10 * base_width / 0.6f, 10 * base_width / 0.6f);
			}
		}
		// ��������ʾ
		for (int i = 0; i < 2; i++) {
			Body b = slipe[i];
			float mBx = b.getPosition().x;
			float mBy = b.getPosition().y;
			batch.draw(mCreateWorld.getBlockTexture(10 + i), set_x
					+ (mBx - base_width) * 10f, set_y - 120f
					+ (mBy - base_width / 2) * 10.6f, 10 * base_width / 0.6f,
					10 * base_width / 0.6f);
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

		if (old_board_x != tBoard1.getPosition().x) {
			send.myboard();
			old_board_x = tBoard1.getPosition().x;
		}

	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		if(firstTouch){
			firstTouch=false;
			send.propsimage();
		}	
		arg1 = SCREEN_HEIGHT * 5 - arg1;
		arg0 = arg0 - SCREEN_WIDTH * 5;
		if (arg1 > 10 * (mB[0].getPosition().y - base_width - 12)
				&& arg1 < 10 * (mB[0].getPosition().y + base_width - 12)) {
			System.out.println("right");
			if (arg0 > 10 * (mB[0].getPosition().x - base_width)
					&& arg0 < 10 * (mB[0].getPosition().x + base_width)) {
				if (myBlock[0] != 0) {
					send.propsactivity(21);
					po.setChange(21, 0);
					myBlock[0]--;
				}
			} else if (arg0 > 10 * (mB[1].getPosition().x - base_width)
					&& arg0 < 10 * (mB[1].getPosition().x + base_width)) {
				if (myBlock[1] != 0) {
					send.propsactivity(22);
					po.setChange(22, 0);
					myBlock[1]--;
				}
			} else if (arg0 > 10 * (mB[2].getPosition().x - base_width)
					&& arg0 < 10 * (mB[2].getPosition().x + base_width)) {
				if (myBlock[2] != 0) {
					send.propsactivity(23);
					po.setChange(23, 0);
					myBlock[2]--;
				}
			} else if (arg0 > 10 * (mB[3].getPosition().x - base_width)
					&& arg0 < 10 * (mB[3].getPosition().x + base_width)) {
				if (myBlock[3] != 0) {
					send.propsactivity(24);
					po.setChange(24, 0);
					myBlock[3]--;
				}
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		Vector3 touchV = new Vector3(arg0, arg1, 0);
		camera.unproject(touchV);

		if (touchV.x <= SCREEN_WIDTH / 2 - board_halfheight * 2
				- board_halfwidth1
				&& touchV.x >= -SCREEN_WIDTH / 2 + board_halfheight * 2
						+ board_halfwidth1) {
			tBoard1.setTransform(touchV.x, 0, 0);
			Data.location.set(Data.myID, 2 * touchV.x / SCREEN_WIDTH);
		}

		return false;
	}

	@Override
	public void dispose() {
		Log.d("debug", "dispose");
		if (mworld != null) {
			mworld.dispose();
			mworld = null;
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
	public void beginContact(Contact contact) {

		// m_sensor = tSensor.getFixtureList().get(0);
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		/* System.out.println(m_sensor); */
		if (fixtureA == m_sensor) {
			/*
			 * System.out.println((fixtureB.getBody().getUserData().toString()));
			 */
			if (fixtureB.getBody().getUserData() != null) {
				touchingSensor = true;

			}

		}
		// System.out.println(fixtureB);

		if (fixtureB == m_sensor) {
			/*
			 * System.out.println((fixtureA.getBody().getUserData().toString()));
			 */
			if (fixtureA.getBody().getUserData() != null) {
				touchingSensor = true;
			}

		}
	}

	@Override
	public void endContact(Contact contact) {

		// m_sensor = tSensor.getFixtureList().get(0);
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		if (fixtureA == m_sensor) {
			if (fixtureB.getBody().getUserData() != null) {
				touchingSensor = false;
			}
		}
		if (fixtureB == m_sensor) {
			if (fixtureA.getBody().getUserData() != null) {
				touchingSensor = false;
			}
		}
	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1) {
		// TODO Auto-generated method stub
		Body cA = arg0.getFixtureA().getBody();
		Body cB = arg0.getFixtureB().getBody();

		BodyData dA = (BodyData) cA.getUserData();
		BodyData dB = (BodyData) cB.getUserData();
		if (dA.getType() == BodyData.BODY_BLOCK) {
			dA.health = 0;
		}
		if (dB.getType() == BodyData.BODY_BLOCK) {
			dB.health = 0;
		}
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