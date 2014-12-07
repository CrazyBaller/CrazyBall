package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;

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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.edu.seu.message.Data;
import com.edu.seu.message.SendData;
import com.edu.seu.props.PropsObservable;
import com.edu.seu.tool.Tool;

public class FourMode implements ApplicationListener, ContactListener,
		InputProcessor {

	private Handler windowHandler;
	private CreateWorld mCreateWorld;
	private GL10 gl;

	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;

	private Body[] slipe = new Body[2];
	private Body headTitle;
	private Body blockTitle;
	private Body Express;
	private Fixture m_sensor;

	private PropsObservable po;

	private Mesh board_mesh;
	private Mesh board_mesh1;
	private Mesh board_mesh2;
	private Mesh board_mesh3;

	private boolean firstTouch = true;
	private boolean touchingSensor = false;

	float tboard1_x;
	float tboard2_y;
	float tboard3_y;
	float x;
	float y;

	float old_board_x = 0;
	float old_ball_x = 0;
	float old_ball_y = 0;

	SendData send;
	private float mLastTime = 0;
	private SpriteBatch batch;

	int flagend0 = 0;
	int flagend1 = 0;
	int flagend2 = 0;
	int flagend3 = 0;

	private boolean backReleased = false;
	private Vector2 oldVector;
	
	Music music;
	Music backmusic;
	Sound sound;
	
	private PropsBar propsbar;

	public FourMode(Handler h, PropsObservable po) {
		this.windowHandler = h;
		this.po = po;
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
		block_width = board_halfwidth/4f;
		offset_center = (5*SCREEN_WIDTH)/7-(3*SCREEN_HEIGHT)/14-board_halfheight;
		showBoard[0]=1;
		showBoard[1]=1;
		showBoard[2]=1;
		showBoard[3]=1;
		move_board=true;    
		isUpdate = false;

		send = new SendData();

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
		
		

		gl = Gdx.graphics.getGL10();
		renderer = new Box2DDebugRenderer();

		// 创建背景世界
		mCreateWorld = new CreateWorld();
		mworld = mCreateWorld.getWorld();
		
		propsbar = new PropsBar(po); 

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
		inputmultiplexer.addProcessor(propsbar.getStage());
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

	private void initBlock() {
		Data.blockList.clear();
		// Data.blockliststate.clear();
		for (int i = 0; i < Data.propsimageid.size(); i++) {
			int type = Data.propsimageid.get(i);
			float x = -Data.propsimagex.get(i) * SCREEN_WIDTH / 2;
			float y = SCREEN_WIDTH - 2 * board_halfheight
					- Data.propsimagey.get(i) * SCREEN_WIDTH / 2;
			Body t = B2Util.createRectangle(mworld, block_width / 1.6f,
					block_width / 1.6f, x, y, BodyType.StaticBody, 0, 0, 0, 0,
					new BodyData(BodyData.BODY_BLOCK, type, i), null);
			Data.blockList.add(t);
			// Data.blockliststate.add(1);

		}
		Data.propsimageid.clear();
		Data.propsimagex.clear();
		Data.propsimagey.clear();
	}

	private void initColor() {
		colors[0] = Color.valueOf("4db6af");
		colors[1] = Color.valueOf("f26d6e");
		colors[2] = Color.valueOf("6fcda8");
		colors[3] = Color.valueOf("fd987a");
		bgcolor = Color.valueOf("34495E");

	}
	
	private void initSound(){
		music = Gdx.audio.newMusic(Gdx.files.internal("sound/2ways.mp3"));
		//backmusic = Gdx.audio.newMusic(Gdx.files.internal("data/"));
		sound = Gdx.audio.newSound(Gdx.files.internal("sound/CountDown.mp3"));
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

	private void createBallBoard() {
		// 创建球
		tBall = B2Util.createCircle(mworld, circle_radius_standard, 0,
				SCREEN_WIDTH / 2 - board_halfheight, BodyType.DynamicBody, 0,
				2, 1, 0, new BodyData(BodyData.BODY_BALL), null);
		// 创建挡板
		tBoard0 = B2Util.createRectangle(mworld, board_halfwidth0,
				board_halfheight, 0, 0, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BOARD0), null);
		tBoard1 = B2Util.createRectangle(mworld, board_halfwidth1,
				board_halfheight, 0, SCREEN_WIDTH - 2 * board_halfheight,
				BodyType.StaticBody, 0, 0, 0, 0, new BodyData(
						BodyData.BODY_BOARD1), null);
		tBoard2 = B2Util.createRectangle(mworld, board_halfheight,
				board_halfwidth2, -SCREEN_WIDTH / 2 + board_halfheight,
				-board_halfheight + SCREEN_WIDTH / 2, BodyType.StaticBody, 0,
				0, 0, 0, new BodyData(BodyData.BODY_BOARD2), null);
		tBoard3 = B2Util.createRectangle(mworld, board_halfheight,
				board_halfwidth3, SCREEN_WIDTH / 2 - board_halfheight,
				-board_halfheight + SCREEN_WIDTH / 2, BodyType.StaticBody, 0,
				0, 0, 0, new BodyData(BodyData.BODY_BOARD3), null);
	}

	private void setBallBoardColor() {

		x = tBoard0.getPosition().x;
		y = tBoard0.getPosition().y;

		board_mesh.setVertices(new float[] { x - board_halfwidth0,
				y + board_halfheight, 0, colors[0].toFloatBits(),
				x - board_halfwidth0, y - board_halfheight, 0,
				colors[0].toFloatBits(), x + board_halfwidth0,
				y + board_halfheight, 0, colors[0].toFloatBits(),
				x + board_halfwidth0, y - board_halfheight, 0,
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

		x = tBoard2.getPosition().x;
		y = tBoard2.getPosition().y;

		board_mesh2.setVertices(new float[] { x - board_halfheight,
				y + board_halfwidth2, 0, colors[2].toFloatBits(),
				x - board_halfheight, y - board_halfwidth2, 0,
				colors[2].toFloatBits(), x + board_halfheight,
				y + board_halfwidth2, 0, colors[2].toFloatBits(),
				x + board_halfheight, y - board_halfwidth2, 0,
				colors[2].toFloatBits() });
		
		x = tBoard3.getPosition().x;
		y = tBoard3.getPosition().y;

		board_mesh3.setVertices(new float[] { x - board_halfheight,
				y + board_halfwidth3, 0, colors[3].toFloatBits(),
				x - board_halfheight, y - board_halfwidth3, 0,
				colors[3].toFloatBits(), x + board_halfheight,
				y + board_halfwidth3, 0, colors[3].toFloatBits(),
				x + board_halfheight, y - board_halfwidth3, 0,
				colors[3].toFloatBits() });
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

		mCreateWorld.getScreen().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBackground().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		board_mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_one().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_two().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_three().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_four().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getControlBackground()
				.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getTimeBackGround().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		//mCreateWorld.getSlipeBackground().render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		tBoard1.setTransform(-Data.location.get(1) * SCREEN_WIDTH / 2,
				tBoard1.getWorldCenter().y, 0);

		tboard2_y = SCREEN_WIDTH / 2 - board_halfheight - Data.location.get(2)
				* SCREEN_WIDTH / 2;
		tBoard2.setTransform(tBoard2.getWorldCenter().x, tboard2_y, 0);
		tboard3_y = SCREEN_WIDTH / 2 - board_halfheight + Data.location.get(3)           
				* SCREEN_WIDTH / 2;
		tBoard3.setTransform(tBoard3.getWorldCenter().x, tboard3_y, 0);

		// 反力场处理
		if (touchingSensor == true) {

			if (canTouching == true) {
				m_sensor = tSensor.getFixtureList().get(0);
				Body ground = m_sensor.getBody();
				CircleShape circle = (CircleShape) m_sensor.getShape();
				Vector2 center = ground.getWorldPoint(circle.getPosition());
				Vector2 position = tBall.getPosition();
				Vector2 d = center.sub(position);
				// d.notifyAll();
				Vector2 F = d.mul(200.0f);
				tBall.applyForce(F, position);
			}
		}

		setBallBoardColor();
		
		if(showBoard[0]==1){
			board_mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		}
		if(showBoard[1]==1){
			board_mesh1.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		}
		if(showBoard[2]==1){
			board_mesh2.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		}
		if(showBoard[3]==1){
			board_mesh3.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		}

		batch.begin();
		float x = tBall.getPosition().x;
		float y = tBall.getPosition().y;
		circle_radius = tBall.getFixtureList().get(0).getShape().getRadius();
		// 画柱子
		mCreateWorld.setBoundCircle();

		// 画反力场黑洞
		if (canTouching == true) {
			batch.draw(mCreateWorld.getBlockTexture(541), set_x
					+ (0 - base_width * 2) * 10f, set_y - offset_center*10f
					+ (SCREEN_WIDTH / 2 - base_width * 2) * 10f,
					40 * base_width, 40 * base_width);
		}
		batch.draw(mCreateWorld.getTexture2(),
				set_x + (x - circle_radius) * 10, set_y - offset_center*10f
						+ (y - circle_radius) * 10, 20 * circle_radius,
				20 * circle_radius);
		
		//画title
		x = headTitle.getPosition().x;
		y = headTitle.getPosition().y;
		batch.draw(mCreateWorld.getTiltleTex(),
				set_x + (x - (SCREEN_WIDTH*3)/8) * 10, set_y - offset_center*10f
						+ (y - base_width) * 10, 60 * SCREEN_WIDTH / 8,
						20 * base_width);
		System.out.println("the title x:"+(set_x + (x - (0.75f * SCREEN_WIDTH)/2) * 10));
		System.out.println("the title y:"+(set_y - offset_center*10f
				+ (y - base_width) * 10));
		//
		//画”道具“title
		x = blockTitle.getPosition().x;
		y = blockTitle.getPosition().y;
		batch.draw(mCreateWorld.getBlockTiltleTex(),
				set_x + (x - SCREEN_WIDTH / 8) * 10, set_y - offset_center*10f
				+ (y - base_width) * 10, 20 * SCREEN_WIDTH / 8,
						20 * base_width);

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
						set_x + (x - block_width / 1.2f) * 10, set_y - offset_center*10f
								+ (y - block_width / 1.2f) * 10,
						10 * block_width / 0.6f, 10 * block_width / 0.6f);
			}
		}
		if (Data.blockList.size() == 0) {
			initBlock();
		}
		if(isUpdate){
			initBlock();
		}
		// 画滑动提示
		for (int i = 0; i < 2; i++) {
			Body b = slipe[i];
			float mBx = b.getPosition().x;
			float mBy = b.getPosition().y;
			batch.draw(mCreateWorld.getBlockTexture(10 + i), set_x
					+ (mBx - base_width) * 10f, set_y - offset_center*10f
					+ (mBy - base_width / 2) * 10.6f, 10 * base_width / 0.6f,
					10 * base_width / 0.6f);
		}
		//写时间
		x = Express.getPosition().x;
		y = Express.getPosition().y;
		mCreateWorld.getFont().draw(batch, "00:00,00'", set_x + (x - (SCREEN_WIDTH / 8)*0.9f) * 10, set_y - offset_center*10f
				+ (y +base_width*0.2f) * 10);
		batch.end();

		if (Gdx.input.isKeyPressed(Keys.BACK) && !backReleased) {
			backReleased = true;
			Message m = new Message();
			m.what = SHOW_DIALOG2;
			windowHandler.sendMessage(m);
			pause();
		}

		propsbar.showselectpeople();
		propsbar.getStage().act(Gdx.graphics.getDeltaTime());
		propsbar.getStage().draw();

		
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
		arg1 = SCREEN_HEIGHT * 5 - arg1;
		arg0 = arg0 - SCREEN_WIDTH * 5;
/*		if (arg1 > 10 * (mB[0].getPosition().y - base_width - offset_center*1)
				&& arg1 < 10 * (mB[0].getPosition().y + base_width - offset_center*1)) {
			System.out.println("right");
			if (arg0 > 10 * (mB[0].getPosition().x - base_width)
					&& arg0 < 10 * (mB[0].getPosition().x + base_width)) {
				if (myBlock[0] != 0) {
					sound.play(30);
					send.propsactivity(21);
					po.setChange(21, 0);
					myBlock[0]--;
				}
			} else if (arg0 > 10 * (mB[1].getPosition().x - base_width)
					&& arg0 < 10 * (mB[1].getPosition().x + base_width)) {
				if (myBlock[1] != 0) {
					sound.play(30);
					send.propsactivity(22);
					po.setChange(22, 0);
					myBlock[1]--;
				}
			} else if (arg0 > 10 * (mB[2].getPosition().x - base_width)
					&& arg0 < 10 * (mB[2].getPosition().x + base_width)) {
				if (myBlock[2] != 0) {
					sound.play(30);
					send.propsactivity(23);
					po.setChange(23, 0);
					myBlock[2]--;
				}
			} else if (arg0 > 10 * (mB[3].getPosition().x - base_width)
					&& arg0 < 10 * (mB[3].getPosition().x + base_width)) {
				if (myBlock[3] != 0) {
					sound.play(30);
					send.propsactivity(24);
					po.setChange(24, 0);
					myBlock[3]--;
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
		if(move_board){
			if (touchV.x <= SCREEN_WIDTH / 2 - board_halfheight * 2
					- board_halfwidth
					&& touchV.x >= -SCREEN_WIDTH / 2 + board_halfheight * 2
							+ board_halfwidth) {
				tBoard0.setTransform(touchV.x, tBoard0.getWorldCenter().y, 0);
				Data.location.set(Data.myID, 2 * tBoard0.getWorldCenter().x
						/ SCREEN_WIDTH);
			}
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
			//tBall.setLinearVelocity(0, 0);
			
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

		} else if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BORDER_UP) // 1号死
				|| (dA.getType() == BodyData.BODY_BORDER_UP && dB.getType() == BodyData.BODY_BALL)) {
			if (flagend1 == 0) {
				flagend1 = 1;
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
			}
		} else if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BORDER_LEFT) // 2号死
				|| (dA.getType() == BodyData.BODY_BORDER_LEFT && dB.getType() == BodyData.BODY_BALL)) {

			if(flagend2 == 0){
				flagend2 = 1;
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
			}
		}else if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BORDER_RIGHT) // 3号死
				|| (dA.getType() == BodyData.BODY_BORDER_RIGHT && dB.getType() == BodyData.BODY_BALL)) {

			if(flagend3 == 0){
				flagend3 = 1;
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
		Fixture fixtureA = arg0.getFixtureA();
		Fixture fixtureB = arg0.getFixtureB();
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
	public void endContact(Contact arg0) {
		// m_sensor = tSensor.getFixtureList().get(0);
		Fixture fixtureA = arg0.getFixtureA();
		Fixture fixtureB = arg0.getFixtureB();
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
		if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BOARD0) // 0号碰
				|| (dA.getType() == BodyData.BODY_BOARD0 && dB.getType() == BodyData.BODY_BALL)) {
			CONTROL_ID = 0;
			send.controlId();
		} else if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BOARD1) // 1号碰
				|| (dA.getType() == BodyData.BODY_BOARD1 && dB.getType() == BodyData.BODY_BALL)) {
			CONTROL_ID = 1;
			send.controlId();
		} else if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BOARD2) // 2号碰
				|| (dA.getType() == BodyData.BODY_BOARD2 && dB.getType() == BodyData.BODY_BALL)) {
			CONTROL_ID = 2;
			send.controlId();
		}else if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BOARD3) // 2号碰
					|| (dA.getType() == BodyData.BODY_BOARD3 && dB.getType() == BodyData.BODY_BALL)) {
			CONTROL_ID = 3;
			send.controlId();
		}
		if (dA.getType() == BodyData.BODY_BLOCK) {
			sound.play(30);
			dA.health = 0;
			int i = dA.getchangeType();
			// if (tBall.getLinearVelocity().y < 0) {
			if (CONTROL_ID == 0) {
				send.eatblock(dA.getId());
				send.props(i, 0);
				if (i > 30 && i < 35) { //被动
					po.setChange(i, 0);
				} else {
					propsbar.addbutton(i);
				}
			} else if (CONTROL_ID == 1) {
				send.eatblock(dA.getId());
				send.props(i, 1);
				if (i > 30 && i < 35) { //被动
					po.setChange(i, 1);
				} 
			} else if (CONTROL_ID == 2) {
				send.eatblock(dA.getId());
				send.props(i, 2);
				if (i > 30 && i < 35) { //被动
					po.setChange(i, 2);
				} 
			}else if (CONTROL_ID ==3) {
				send.eatblock(dA.getId());
				send.props(i, 3);
				if (i > 30 && i < 35) { //被动
					po.setChange(i, 3);
				} 
			}
		}
		if (dB.getType() == BodyData.BODY_BLOCK) {
			sound.play(30);
			dB.health = 0;
			int i = dB.getchangeType();
			if (CONTROL_ID == 0) {
				send.eatblock(dA.getId());
				send.props(i, 0);
				if (i > 30 && i < 35) { //被动
					po.setChange(i, 0);
				} else {
					propsbar.addbutton(i);
				}
			} else if (CONTROL_ID == 1) {
				send.eatblock(dA.getId());
				send.props(i, 1);
				if (i > 30 && i < 35) { //被动
					po.setChange(i, 1);
				} 
			} else if (CONTROL_ID == 2) {
				send.eatblock(dA.getId());
				send.props(i, 2);
				if (i > 30 && i < 35) { //被动
					po.setChange(i, 2);
				} 
			}else if (CONTROL_ID ==3) {
				send.eatblock(dA.getId());
				send.props(i, 3);
				if (i > 30 && i < 35) { //被动
					po.setChange(i, 3);
				} 
			}
		}
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		return false;
	}

}
