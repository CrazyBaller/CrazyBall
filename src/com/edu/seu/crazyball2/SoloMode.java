package com.edu.seu.crazyball2;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.edu.seu.message.Data;
import com.edu.seu.message.SendData;
import com.edu.seu.props.PropsObservable;
import com.edu.seu.tool.Tool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import static com.edu.seu.crazyball2.Constant.*;

public class SoloMode implements ApplicationListener, ContactListener,
		InputProcessor {

	private Handler windowHandler;
	private CreateWorld mCreateWorld;
	private World mworld;
	private GL10 gl;

	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;

	private List<Body> blockList = new ArrayList<Body>();
	private Body[] slipe = new Body[2];
	private Body headTitle;
	private Body blockTitle;
	private Body Express;
	
	private Fixture m_sensor;

	private Mesh board_mesh;

	private boolean firstTouch = true;
	private boolean touchingSensor = false;
	private PropsObservable po;
	private float mLastTime = 0;
	private SpriteBatch batch;
	private boolean backReleased = false;
	private Vector2 oldVector;
	
	Music music;
	Music backmusic;
	Sound sound;

	int flagend0 = 0;

	public SoloMode(Handler h, PropsObservable po) {
		this.windowHandler = h;
		this.po = po;
	}

	@Override
	public void create() {
		Log.d("debug", "create");

		board_halfwidth0 = SCREEN_WIDTH * boardrate;
		board_halfwidth = SCREEN_WIDTH * boardrate;
		board_halfheight = board_halfwidth / 5;
		circle_radius_standard = board_halfheight;
		circle_radius = circle_radius_standard;
		block_width = board_halfwidth/4f;
		offset_center = (5*SCREEN_WIDTH)/7-(3*SCREEN_HEIGHT)/14-board_halfheight;

		// init color
		initColor();
		
		//初始化声音
		initSound();
		music.play();
		music.setLooping(true);
		music.setVolume(15);

		// 镜头下的世界
		camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		camera.position.set(0, offset_center, 0);
		System.out.println("the sub is "+(SCREEN_HEIGHT-SCREEN_WIDTH));
		System.out.println("the sub is "+(SCREEN_HEIGHT-SCREEN_WIDTH)/7*2);
		System.out.println("the offset_center is "+offset_center);

		gl = Gdx.graphics.getGL10();
		renderer = new Box2DDebugRenderer();

		// 创建背景世界
		mCreateWorld = new CreateWorld();
		mworld = mCreateWorld.getWorld();

		board_mesh = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));

		// 创建球和板
		createBallBoard();
		setBallBoardColor();

		// 初始化主动道具
		initBlock();
		initMyblock();
		
		//初始化title
		initTitle();

		// 创建感应区
		tSensor = B2Util.createSensor(mworld, base_width * 2, m_sensor, 0f,
				SCREEN_WIDTH / 2, new BodyData(BodyData.BODY_SENSOR), null);
		m_sensor = tSensor.getFixtureList().get(0);

		// 设置碰撞监听
		mworld.setContactListener(this);

		// 设置输入监听
		InputMultiplexer inputmultiplexer = new InputMultiplexer();
		inputmultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputmultiplexer);
		Gdx.input.setCatchBackKey(true);

	}
	
	private void initTitle(){
		headTitle = B2Util.createRectangle(mworld, (SCREEN_WIDTH*3)/8,base_width, 
				-(SCREEN_WIDTH / 8),
				SCREEN_WIDTH-board_halfheight+base_width, BodyType.StaticBody, 0,
				0, 0, 0, new BodyData(BodyData.BODY_BLOCK, 10), null);
		blockTitle = B2Util.createRectangle(mworld,  SCREEN_WIDTH / 8,base_width,
				((SCREEN_WIDTH *3)/ 8),
				-board_halfheight-base_width/2-base_width, BodyType.StaticBody, 0,
				0, 0, 0, new BodyData(BodyData.BODY_BLOCK, 10), null);
		Express = B2Util.createRectangle(mworld,  SCREEN_WIDTH / 8,base_width,
				((SCREEN_WIDTH *3)/ 8),
				SCREEN_WIDTH-board_halfheight+base_width, BodyType.StaticBody, 0,
				0, 0, 0, new BodyData(BodyData.BODY_BLOCK, 10), null);
	}

	private void initSound(){
		music = Gdx.audio.newMusic(Gdx.files.internal("sound/2ways.mp3"));
		//backmusic = Gdx.audio.newMusic(Gdx.files.internal("data/"));
		sound = Gdx.audio.newSound(Gdx.files.internal("sound/CountDown.mp3"));
	}
	private void initColor() {
		colors[0] = Color.valueOf("4db6af");
		colors[1] = Color.valueOf("f26d6e");
		colors[2] = Color.valueOf("6fcda8");
		colors[3] = Color.valueOf("fd987a");
		bgcolor = Color.valueOf("34495E");
		/*System.out.println(" r:" + bgcolor.r + " g:" + bgcolor.g + " b:"
				+ bgcolor.b);*/
	}

	private void initMyblock() {
		slipe[0] = B2Util.createRectangle(mworld, base_width, base_width,
				-SCREEN_WIDTH / 4f,
				-(board_halfheight + base_width * (2.5f+1.25f)), BodyType.StaticBody, 0,
				0, 0, 0, new BodyData(BodyData.BODY_BLOCK, 10), null);
		slipe[1] = B2Util.createRectangle(mworld, base_width, base_width,
				SCREEN_WIDTH / 4f,
				-(board_halfheight + base_width * (2.5f+1.25f)), BodyType.StaticBody, 0,
				0, 0, 0, new BodyData(BodyData.BODY_BLOCK, 11), null);
	}

	private void initBlock() {
		Set<Integer> nums = new HashSet<Integer>();
		Random rd = new Random();
		while (nums.size() < 6) {
			nums.add((int) (rd.nextInt(100)));
		}

		Iterator<Integer> iter = nums.iterator();

		blockList.clear();
		for (int i = 0; i < 2 ; i++) {
			Integer temp = iter.next();
			float x = (temp % 10 - 5) * (block_width * 2.4f);
			float y = (1.5f + (temp / 10)) * block_width * 2.4f;
			int type = rd.nextInt(4) + 31;
			Body tB = B2Util.createRectangle(mworld, block_width / 1.2f,
					block_width / 1.2f, x, y, BodyType.StaticBody, 0, 0, 0, 0,
					new BodyData(BodyData.BODY_BLOCK, type), null);
			blockList.add(tB);
		}
		while (iter.hasNext()) {
			Integer temp = iter.next();
			float x = (temp % 10 - 5) * (block_width * 2.4f);
			float y = (1.5f + temp / 10) * block_width * 2.4f;
			int type = rd.nextInt(6) + 21;
			Body tB = B2Util.createRectangle(mworld, block_width / 1.2f,
					block_width / 1.2f, x, y, BodyType.StaticBody, 0, 0, 0, 0,
					new BodyData(BodyData.BODY_BLOCK, type), null);
			blockList.add(tB);
		}

	}

	private void createBallBoard() {
		// 创建球
		tBall = B2Util.createCircle(mworld, circle_radius_standard, 0,
				board_halfheight + circle_radius_standard,
				BodyType.DynamicBody, 0, 2, 1, 0, new BodyData(
						BodyData.BODY_BALL), null);

		// 创建挡板
		tBoard0 = B2Util.createRectangle(mworld, board_halfwidth0,
				board_halfheight, 0, 0, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BOARD), null);

	}

	private void setBallBoardColor() {
		float x = tBoard0.getPosition().x;
		float y = tBoard0.getPosition().y;
		/*
		 * System.out.println(" r:"+colors[0].r+" g:"+colors[0].g+" b:"+colors[0]
		 * .b);
		 * System.out.println(" r:"+colors[1].r+" g:"+colors[1].g+" b:"+colors
		 * [1].b);
		 * System.out.println(" r:"+colors[2].r+" g:"+colors[2].g+" b:"+colors
		 * [2].b);
		 * System.out.println(" r:"+colors[3].r+" g:"+colors[3].g+" b:"+colors
		 * [3].b);
		 */
		int i = Data.myID;
		/* System.out.println("myID is "+i); */
		board_mesh.setVertices(new float[] { x - board_halfwidth0,
				y + board_halfheight, 0, colors[i].toFloatBits(),
				x - board_halfwidth0, y - board_halfheight, 0,
				colors[i].toFloatBits(), x + board_halfwidth0,
				y + board_halfheight, 0, colors[i].toFloatBits(),
				x + board_halfwidth0, y - board_halfheight, 0,
				colors[i].toFloatBits() });

	}

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();
		mLastTime += dt;
		if (mLastTime >= 1.0 / 60) {
			mLastTime = 0;
		} else
			return;

		mworld.step(Gdx.graphics.getDeltaTime(), 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glClearColor(1f, 1f, 1f, 0f);

		mCreateWorld.getScreen().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBackground().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		board_mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_one().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_two().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_three().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_four().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getControlBackground()
				.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		//mCreateWorld.getSlipeBackground().render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		setBallBoardColor();
		//board_mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		// 反力场处理
		if (touchingSensor == true) {
			//System.out.println("touchingSensor is " + touchingSensor);
			if (canTouching == true) {
				m_sensor = tSensor.getFixtureList().get(0);
				Body ground = m_sensor.getBody();
				CircleShape circle = (CircleShape) m_sensor.getShape();
				/*System.out.println("circle.getRadius() is "
						+ circle.getRadius());
				System.out.println("circle.getRadius() is "
						+ circle.getPosition().toString());*/
				Vector2 center = ground.getWorldPoint(circle.getPosition());
				Vector2 position = tBall.getPosition();
				Vector2 d = center.sub(position);
				// d.notifyAll();
				Vector2 F = d.mul(50.0f);
				tBall.applyForce(F, position);
				//System.out.println("get f");
			}
		}
		batch = mCreateWorld.getBatch();

		batch.begin();

		// 画柱子
		mCreateWorld.setBoundCircle();

		// 画反力场黑洞
		if (canTouching == true) {
			batch.draw(mCreateWorld.getBlockTexture(541), set_x
					+ (0 - base_width * 2) * 10f, set_y - offset_center*10f
					+ (SCREEN_WIDTH / 2 - base_width * 2) * 10f,
					40 * base_width, 40 * base_width);
		}
		float x = tBall.getPosition().x;
		float y = tBall.getPosition().y;
		circle_radius = tBall.getFixtureList().get(0).getShape().getRadius();
		batch.draw(mCreateWorld.getTexture2(),
				set_x + (x - circle_radius) * 10, set_y - offset_center*10f
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
				batch.draw(
						mCreateWorld.getBlockTexture(400 + bd.getchangeType()),
						set_x + (x - block_width / 1.2f) * 10, set_y - offset_center*10f
								+ (y - block_width / 1.2f) * 10,
						10 * block_width / 0.6f, 10 * block_width / 0.6f);
			}
		}
		if (blockList.size() == 0) {
			initBlock();
		}
/*		// 画道具框
		for (int i = 0; i < 4; i++) {
			Body b = mB[i];
			float mBx = b.getPosition().x;
			float mBy = b.getPosition().y;
			if (myBlock[i] == 0) {
				//System.out.println("set_x: " + set_x + "set_y: " + set_y);
				batch.draw(mCreateWorld.getBlockTexture(0), set_x
						+ (mBx - base_width) * 10f, set_y - offset_center*10f
						+ (mBy - base_width / 2) * 10.6f,
						10 * base_width / 0.6f, 10 * base_width / 0.6f);
			} else {
				batch.draw(
						mCreateWorld.getBlockTexture(Data.myID * 100 + 21 + i),
						set_x + (mBx - base_width) * 10f, set_y - offset_center*10f
								+ (mBy - base_width / 2) * 10.6f,
						10 * base_width / 0.6f, 10 * base_width / 0.6f);
			}
		}*/
		// 画滑动提示
		for (int i = 0; i < 2; i++) {
			Body b = slipe[i];
			float mBx = b.getPosition().x;
			float mBy = b.getPosition().y;
			//System.out.println("slipe_x: " + set_x + "slipe_y: " + set_y);
			batch.draw(mCreateWorld.getBlockTexture(10 + i), set_x
					+ (mBx - base_width) * 10f, set_y - offset_center*10f
					+ (mBy - base_width / 2) * 10.6f, 10 * base_width / 0.6f,
					10 * base_width / 0.6f);
		}
		//System.out.println("end batch");
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
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		Vector3 touchV = new Vector3(arg0, arg1, 0);
		camera.unproject(touchV);
		if (firstTouch) {
			Random r = new Random();
			float xv = r.nextFloat() * SCREEN_WIDTH;
			float yv = SCREEN_WIDTH - xv;
			if (r.nextInt(2) == 0)
				xv = -xv;
			if (r.nextInt(2) == 0)
				yv = -yv;
			firstTouch = false;
			tBall.setLinearVelocity(xv, yv);
		}
		arg1 = SCREEN_HEIGHT * 5 - arg1;
		arg0 = arg0 - SCREEN_WIDTH * 5;
/*		if (arg1 > 10 * (mB[0].getPosition().y - base_width - offset_center)
				&& arg1 < 10 * (mB[0].getPosition().y + base_width - offset_center)) {
			//System.out.println("right");
			if (arg0 > 10 * (mB[0].getPosition().x - base_width)
					&& arg0 < 10 * (mB[0].getPosition().x + base_width)) {
				if (myBlock[0] != 0) {
					music.pause();
					sound.play(30);
					//sound.setLooping(false);
					po.setChange(21, 0);
					myBlock[0]--;
					music.play();
				}
			} else if (arg0 > 10 * (mB[1].getPosition().x - base_width)
					&& arg0 < 10 * (mB[1].getPosition().x + base_width)) {
				if (myBlock[1] != 0) {
					music.pause();
					sound.play(30);
					po.setChange(22, 0);
					myBlock[1]--;
					music.play();
				}
			} else if (arg0 > 10 * (mB[2].getPosition().x - base_width)
					&& arg0 < 10 * (mB[2].getPosition().x + base_width)) {
				if (myBlock[2] != 0) {
					music.pause();
					sound.play(30);
					po.setChange(23, 0);
					myBlock[2]--;
					music.play();
				}
			} else if (arg0 > 10 * (mB[3].getPosition().x - base_width)
					&& arg0 < 10 * (mB[3].getPosition().x + base_width)) {
				if (myBlock[3] != 0) {
					music.pause();
					sound.play(30);
					po.setChange(24, 0);
					myBlock[3]--;
					music.play();
				}
			}
		}*/
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		Vector3 touchV = new Vector3(arg0, arg1, 0);
		camera.unproject(touchV);
		// 设置移动坐标
		if (touchV.x <= SCREEN_WIDTH / 2 - board_halfheight * 2
				- board_halfwidth0
				&& touchV.x >= -SCREEN_WIDTH / 2 + board_halfheight * 2
						+ board_halfwidth0) {
			tBoard0.setTransform(touchV.x, tBoard0.getWorldCenter().y, 0);
		}

		return false;
	}

	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {
		Body cA = arg0.getFixtureA().getBody();
		Body cB = arg0.getFixtureB().getBody();

		BodyData dA = (BodyData) cA.getUserData();
		BodyData dB = (BodyData) cB.getUserData();
		if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BORDER_BOTTOM)
				|| (dA.getType() == BodyData.BODY_BORDER_BOTTOM && dB.getType() == BodyData.BODY_BALL)) {
			tBall.setLinearVelocity(0, 0);
			// Message m=new Message();
			// m.what=SHOW_DIALOG;
			// windowHandler.sendMessage(m);

			if (flagend0 == 0)

			{
				flagend0 = 1;

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
			}

		}
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
		if (board_mesh != null) {
			board_mesh.dispose();
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
		//System.out.println("succeed in begin");
		// m_sensor = tSensor.getFixtureList().get(0);
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		/*System.out.println("tSensor radius is :"
				+ tSensor.getFixtureList().get(0).getShape().getRadius());*/
		/* System.out.println(m_sensor); */
		if (fixtureA == m_sensor) {
			/*
			 * System.out.println((fixtureB.getBody().getUserData().toString()));
			 */
			if (fixtureB.getBody().getUserData() != null) {
				touchingSensor = true;

			}
			//System.out.println("touchingSensor is" + touchingSensor);
		}
		// System.out.println(fixtureB);
		/*System.out.println("m_sensor radius is :"
				+ m_sensor.getShape().getRadius());*/
		if (fixtureB == m_sensor) {
			/*
			 * System.out.println((fixtureA.getBody().getUserData().toString()));
			 */
			if (fixtureA.getBody().getUserData() != null) {
				touchingSensor = true;
			}
			//System.out.println("touchingSensor is" + touchingSensor);
		}
	}

	@Override
	public void endContact(Contact contact) {
		//System.out.println("succeed in end");
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
		Body cA = arg0.getFixtureA().getBody();
		Body cB = arg0.getFixtureB().getBody();

		BodyData dA = (BodyData) cA.getUserData();
		BodyData dB = (BodyData) cB.getUserData();

		if (dA.getType() == BodyData.BODY_BLOCK) {
			//music.pause();
			sound.play(30);
			dA.health = 0;
			po.setChange(dA.getchangeType(), 0);
			if (dA.getchangeType() > 20 & dA.getchangeType() < 30) {
				myBlock[dA.getchangeType() - 21]++;
			}
		}
		if (dB.getType() == BodyData.BODY_BLOCK) {
			//music.pause();
			sound.play(30);
			dB.health = 0;
			po.setChange(dA.getchangeType(), 0);
			if (dA.getchangeType() > 20 & dA.getchangeType() < 30) {
				myBlock[dA.getchangeType() - 21]++;
			}
		}
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		return false;
	}

}
