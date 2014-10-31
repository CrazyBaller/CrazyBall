package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;

import java.util.ArrayList;
import java.util.List;

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
import com.badlogic.gdx.graphics.Texture;
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
import com.edu.seu.message.Data;
import com.edu.seu.message.SendData;
import com.edu.seu.props.PropsObservable;

public class TwoModeClient implements ApplicationListener, ContactListener,
		InputProcessor {

	private GL10 gl;
	private Handler windowHandler;
	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;
	private CreateWorld mCreateWorld;
	private World mworld;

	private Body lastboard;  //上一个碰的板
	Body tB;

	private Mesh board_mesh;
	private Mesh board_mesh1;

	private float board1_x = 0;
	private float board1_y = 0;

	private float board_x = 0;
	private float board_y = SCREEN_WIDTH - 2 * board_halfheight;
	private float ball_x = 0;
	private float ball_y = circle_radius + board_halfheight;
	
	SendData send = null;
	private float old_board_x = 0;
	
	PropsObservable po;
	
	public TwoModeClient(Handler h,PropsObservable po) {
		this.windowHandler = h;
		this.po=po;
	}
	@Override
	public void create() {
		Message m=new Message();
		m.what=SHOW_TOAST;
		windowHandler.sendMessage(m);
		
		Log.d("debug", "create");
		send = new SendData();
		
		board_halfwidth0 = SCREEN_WIDTH * boardrate;
		board_halfwidth1 = SCREEN_WIDTH * boardrate;
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
		
		board_mesh = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		board_mesh1 = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		
		createBallBoard();
		setBallBoardColor();
		
		// 设置输入监听
		InputMultiplexer inputmultiplexer = new InputMultiplexer();
		inputmultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputmultiplexer);
		// 创建砖块
		initBlock();
		
		// 设置碰撞监听
		mworld.setContactListener(this);
	}
	
	private void createBallBoard() {
		// 创建球
		tBall = B2Util.createCircle(mworld, circle_radius, 0, board_halfheight
				+ circle_radius, BodyType.DynamicBody, 0, 2, 1, 0,
				new BodyData(BodyData.BODY_BALL), null);
		// 创建挡板
		tBoard1 = B2Util.createRectangle(mworld, board_halfwidth1,
				board_halfheight, 0, 0, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BOARD), null);
		tBoard0 = B2Util.createRectangle(mworld, board_halfwidth0,
				board_halfheight, 0, SCREEN_WIDTH - 2 * board_halfheight,
				BodyType.StaticBody, 0, 0, 0, 0, new BodyData(
						BodyData.BODY_BOARD), null);

	}
	private void setBallBoardColor() {
		
		board_x = Data.location.get(0) * SCREEN_WIDTH / 2;
		tBoard0.setTransform(board_x, tBoard0.getWorldCenter().y, 0);
		
		board_mesh.setVertices(new float[] { board_x - board_halfwidth0,
				board_y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				board_x - board_halfwidth0, board_y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255), board_x + board_halfwidth0,
				board_y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				board_x + board_halfwidth0, board_y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255) });
				
		board_mesh1.setVertices(new float[] { board1_x - board_halfwidth1,
						board1_y + board_halfheight, 0,
						Color.toFloatBits(0, 0, 0, 255),
						board1_x - board_halfwidth1,
						board1_y - board_halfheight, 0,
						Color.toFloatBits(0, 0, 0, 255),
						board1_x + board_halfwidth1,
						board1_y + board_halfheight, 0,
						Color.toFloatBits(0, 0, 0, 255),
						board1_x + board_halfwidth1,
						board1_y - board_halfheight, 0,
						Color.toFloatBits(0, 0, 0, 255) });

	}

	
	private List<Body> ballList = null;
	private List<Body> blockList = null;

	//创建砖块
	private void initBlock() {
		//设置具有主动属性的砖块(改变球的状态的属性)
//		ballList = new ArrayList<Body>();
//		for (int i = 0; i < 3; i++) {
//			for (int j = 0; j < 3; j++) {
//				int type = 21+j;
//				Body tB = B2Util.createRectangle(world, 2, 1, i * 5 + 15,
//						j * 4 + 15, BodyType.StaticBody, 0, 0, 0, 0,
//						new BodyData(BodyData.BODY_BLOCK,type), null);
//				ballList.add(tB);
//			}
//		}
		
		//设置具有被动属性的砖块(改变板的状态的属性)
//		blockList = new ArrayList<Body>();
//		for (int i = 3; i < 5; i++) {
//			for (int j = 0; j < 3; j++) {
//				int type = 31+j;
//				Body tB = B2Util.createRectangle(world, 2, 1, i * 5 + 15,
//						j * 4 + 15, BodyType.StaticBody, 0, 0, 0, 0,
//						new BodyData(BodyData.BODY_BLOCK,type), null);
//				blockList.add(tB);
//			}
//		}
		tB = B2Util.createRectangle(mworld, 2, 1, 0,
				SCREEN_WIDTH/2, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BLOCK,31), null);
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
		ball_x = Data.ball.get(0) * SCREEN_WIDTH / 2;
		ball_y = SCREEN_WIDTH - 2 * board_halfheight - Data.ball.get(1)
				* SCREEN_WIDTH / 2;
		tBall.setTransform(ball_x, ball_y, 0);
		batch.draw(mCreateWorld.getTexture2(), set_x - 20f + ball_x * 10, set_y - 120f + ball_y
				* 10, 40f, 40f);
		batch.end();

		camera.update();
		camera.apply(gl);
		renderer.render(mworld, camera.combined);

		if (old_board_x != board1_x) {
			send.myboard();
			old_board_x = board1_x;
		}
		
	
//		BodyData bd = (BodyData)tB.getUserData();
//		if (bd.health == 0) {
//			world.destroyBody(tB);
//		}
		
//		// 销毁处理
//		for (int i = 0; i < ballList.size(); i++) {
//			Body b = ballList.get(i);
//			BodyData bd = (BodyData) b.getUserData();
//			if (bd.health == 0) {
//				world.destroyBody(b);
//				ballList.remove(i);
//				i--;
//			}
//		}
//		for (int i = 0; i < blockList.size(); i++) {
//			Body b = blockList.get(i);
//			BodyData bd = (BodyData) b.getUserData();
//			if (bd.health == 0) {
//				world.destroyBody(b);
//				blockList.remove(i);
//				i--;
//			}
//		}
		BodyData bd=(BodyData)tB.getUserData();
		if(bd.health==0)
			mworld.destroyBody(tB);
		
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

		if (touchV.x <= SCREEN_WIDTH / 2 - board_halfheight * 2
				- board_halfwidth1
				&& touchV.x >= -SCREEN_WIDTH / 2 + board_halfheight * 2
						+ board_halfwidth1) {
			board1_x=touchV.x;
			tBoard1.setTransform(touchV.x, 0, 0);
			Data.location.set(Data.myID, 2 *board1_x
					/ SCREEN_WIDTH);
		}
		System.out.println("touch drag");

		return false;
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
		Body cA = arg0.getFixtureA().getBody();
		Body cB = arg0.getFixtureB().getBody();

		BodyData dA = (BodyData) cA.getUserData();
		BodyData dB = (BodyData) cB.getUserData();
		if (dA.getType() == BodyData.BODY_BLOCK) {
			dA.health = 0;
			int i=dA.getchangeType();
			if(i<30){
				Message m=new Message();
				m.what=SHOW_TOAST;
				windowHandler.sendMessage(m);
				if(tBall.getLinearVelocity().y<0){    //0号板碰的
					send.props(i,0);
					po.setChange(i,0);
				}else{
					send.props(i,1);			//1号板碰的
					po.setChange(i,1);
				}
			}
			else{
				Message m=new Message();
				m.what=SHOW_TOAST;
				windowHandler.sendMessage(m);
				if(tBall.getLinearVelocity().y<0){    //0号板碰的
					send.props(i,0);
					po.setChange(i,0);
				}else{								//1号板碰的
					send.props(i,1);
					po.setChange(i,1);
				}
			}
		}
		if (dB.getType() == BodyData.BODY_BLOCK) {
			dB.health = 0;
			int i=dA.getchangeType();
			if(i<30){
				System.out.println(dA.getchangeType());
				Message m=new Message();
				m.what=SHOW_TOAST;
				windowHandler.sendMessage(m);
				if(tBall.getLinearVelocity().y<0){    //1号板碰的
					send.props(i,1);
					po.setChange(i,1);
				}else{								//0号板碰的
					send.props(i,0);
					po.setChange(i,0);
				}
			}
			else{
				System.out.println(dA.getchangeType());
				Message m=new Message();
				m.what=SHOW_TOAST;
				windowHandler.sendMessage(m);
				if(tBall.getLinearVelocity().y<0){    //1号板碰的
					send.props(i,1);
					po.setChange(i,1);
				}else{								//0号板碰的
					send.props(i,0);
					po.setChange(i,0);
				}
			}
		}
	}

}
