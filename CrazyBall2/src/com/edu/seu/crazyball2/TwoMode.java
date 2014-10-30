package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.edu.seu.message.Data;
import com.edu.seu.message.SendData;
import com.edu.seu.tool.Tool;

public class TwoMode implements ApplicationListener, ContactListener,
		InputProcessor {

	private Handler windowHandler;
	private CreateWorld mCreateWorld;
	private World mworld;
	private GL10 gl;

	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;

	private Body tBoard0;
	private Body tBoard1;
	private Body tBall;

	private Mesh board_mesh;
	private Mesh board_mesh1;

	private boolean firstTouch = true;

	float tboard1_x;

	float old_board_x = 0;
	float old_ball_x = 0;
	float old_ball_y = 0;

	SendData send;

	public TwoMode(Handler h) {
		this.windowHandler = h;
	}

	public void create() {
		Message m=new Message();
		m.what=SHOW_TOAST;
		windowHandler.sendMessage(m);
		
		Log.d("debug", "create");
		send = new SendData();

		board_halfwidth = SCREEN_WIDTH * boardrate;
		board_halfheight = board_halfwidth / 5;

		// 镜头下的世界
		camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		camera.position.set(0, 10, 0);

		gl = Gdx.graphics.getGL10();
		renderer = new Box2DDebugRenderer();

		// 创建背景世界
		mCreateWorld = new CreateWorld();
		mworld = mCreateWorld.getWorld();

		// 创建球和板
		createBallBoard();
		setBallBoardColor();

		// 设置碰撞监听
		mworld.setContactListener(this);

		// 设置输入监听
		InputMultiplexer inputmultiplexer = new InputMultiplexer();
		inputmultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputmultiplexer);
	}

	private void createBallBoard() {
		// 创建球
		tBall = B2Util.createCircle(mworld, circle_radius, 0, board_halfheight
				+ circle_radius, BodyType.DynamicBody, 0, 2, 1, 0,
				new BodyData(BodyData.BODY_BALL), null);
		// 创建挡板
		tBoard0 = B2Util.createRectangle(mworld, SCREEN_WIDTH * boardrate,
				board_halfheight, 0, 0, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BOARD), null);
		tBoard1 = B2Util.createRectangle(mworld, SCREEN_WIDTH * boardrate,
				board_halfheight, 0, SCREEN_WIDTH - 2 * board_halfheight,
				BodyType.StaticBody, 0, 0, 0, 0, new BodyData(
						BodyData.BODY_BOARD), null);

	}

	private void setBallBoardColor() {
		float x = tBoard0.getPosition().x;
		float y = tBoard0.getPosition().y;

		board_mesh = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		board_mesh.setVertices(new float[] { x - board_halfwidth,
				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				x - board_halfwidth, y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255), x + board_halfwidth,
				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				x + board_halfwidth, y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255) });
		board_mesh.setIndices(new short[] { 0, 1, 2, 3 });

		x = tBoard1.getPosition().x;
		y = tBoard1.getPosition().y;

		board_mesh1 = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		board_mesh1.setVertices(new float[] { x - board_halfwidth,
				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				x - board_halfwidth, y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255), x + board_halfwidth,
				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				x + board_halfwidth, y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255) });
		board_mesh1.setIndices(new short[] { 0, 1, 2, 3 });

	}

	private float mLastTime = 0;
	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();
		mLastTime += dt;
		if (mLastTime >= 1.0/60)
		{
			mLastTime = 0;
		}
		else return;

		
		tboard1_x = Data.location.get(1) * SCREEN_WIDTH / 2;
		tBoard1.setTransform(tboard1_x, tBoard1.getWorldCenter().y, 0);
		if(Data.boardsize.get(0)==1){
			ChangeBlock tT = new ChangeBlock(tBoard0);
			tT.start(((BodyData) tBoard0.getUserData()).getchangeType());
		}		
		if(Data.boardsize.get(1)==1){
			ChangeBlock tT = new ChangeBlock(tBoard1);
			tT.start(((BodyData) tBoard1.getUserData()).getchangeType());
		}		

//		mworld.step(Gdx.graphics.getDeltaTime(), 1, 1);
		mworld.step(1.0f/6.0f, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glClearColor(1f, 1f, 1f, 0f);

		mCreateWorld.getBound_one().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_two().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_three().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_four().render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		setBallBoardColor();
		board_mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		board_mesh1.render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		SpriteBatch batch = mCreateWorld.getBatch();

		batch.begin();
		float x = tBall.getPosition().x;
		float y = tBall.getPosition().y;
		batch.draw(mCreateWorld.getTexture2(), set_x - 20f + x * 10, set_y
				- 120f + y * 10, 40f, 40f);
		batch.end();

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
			firstTouch = false;
			tBall.setLinearVelocity(40f, 60f);
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
			tBoard0.setTransform(touchV.x,0, 0);
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
		if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BORDER_BOTTOM)   // 0号死
				|| (dA.getType() == BodyData.BODY_BORDER_BOTTOM && dB.getType() == BodyData.BODY_BALL)) {
			tBall.setLinearVelocity(0, 0);
			int result= new Tool().judge(0);
			
			SendData send = new SendData();
			send.sendresult(0, result);
			
			
			JSONObject json = new JSONObject();
	    	try {
				
				json.put("id", 0);
				json.put("result", result);
				
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
	    	
	    	Message m=new Message();
			m.what=SHOW_MYDEAD_DIALOG;
			m.obj= json.toString();
			windowHandler.sendMessage(m);
			
			
		}else if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BORDER_UP) // 1号死
				|| (dA.getType() == BodyData.BODY_BORDER_UP && dB.getType() == BodyData.BODY_BALL)){
			int result= new Tool().judge(1);
			
			SendData send = new SendData();
			send.sendresult(1, result);
			
			JSONObject json = new JSONObject();
	    	try {
				
				json.put("id", 1);
				json.put("result", result);
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
	    	
	    	Message m=new Message();
			m.what=SHOW_MYDEAD_DIALOG;
			m.obj= json.toString();
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

	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		return false;
	}

}
