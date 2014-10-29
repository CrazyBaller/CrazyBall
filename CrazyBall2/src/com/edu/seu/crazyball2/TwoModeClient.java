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

public class TwoModeClient implements ApplicationListener, ContactListener,
		InputProcessor {

	private GL10 gl;
	private Handler windowHandler;
	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;
	private World world;
	private Body ball;
	private Body lastboard;  //上一个碰的板
	Body tB;

	private Mesh bound_one;
	private Mesh bound_two;
	private Mesh bound_three;
	private Mesh bound_four;
	private Mesh board_mesh;
	private Mesh board_mesh1;

	private SpriteBatch batch;
	private Texture texture2;

	private float board_x = 0;
	private float board_y = 0;

	private float board1_x = 0;
	private float board1_y = SCREEN_WIDTH - 2 * board_halfheight;
	private float ball_x = 0;
	private float ball_y = circle_radius + board_halfheight;

	float board_halfwidth = SCREEN_WIDTH * boardrate;
	float board_halfwidth0;
	float board_halfwidth1;

	SendData send = null;
	private float old_board_x = 0;
	
	
	public TwoModeClient(Handler h){
		this.windowHandler=h;
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
		renderer = new Box2DDebugRenderer();
		
		board_mesh = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		board_mesh1 = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		
		createBody();
		setBoundColor();
		setBallBoardColor();
		
		batch = new SpriteBatch();
		texture2 = new Texture(Gdx.files.internal("data/ball.png"));

		// 设置输入监听
		InputMultiplexer inputmultiplexer = new InputMultiplexer();
		inputmultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputmultiplexer);
		// 创建砖块
		initBlock();
		
		// 设置碰撞监听
		world.setContactListener(this);
	}
	private void createBody(){
		world = new World(new Vector2(0, 0f), true);
		ball = B2Util.createCircle(world, circle_radius, 0, board_halfheight
				+ circle_radius, BodyType.DynamicBody, 0, 2, 1, 0,
				new BodyData(BodyData.BODY_BALL), null);
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
		
		board_halfwidth0=board_halfwidth;
		board_halfwidth1=board_halfwidth;
		if(Data.boardsize.get(0)==1)
			board_halfwidth0*=2;
		if(Data.boardsize.get(1)==1)
			board_halfwidth1*=2;

		System.out.println("wwwwwwwwwwwwwwwwww"+board_halfwidth0);
		board_mesh.setVertices(new float[] { x - board_halfwidth0,
				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				x - board_halfwidth0, y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255), x + board_halfwidth0,
				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				x + board_halfwidth0, y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255) });

		board1_x = Data.location.get(0) * SCREEN_WIDTH / 2;
		
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
		tB = B2Util.createRectangle(world, 2, 1, 0,
				SCREEN_WIDTH/2, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BLOCK,31), null);
	}

	
	@Override
	public void render() {
		world.step(Gdx.graphics.getDeltaTime(), 8, 8);

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glClearColor(1f, 1f, 1f, 0f);

		bound_one.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		bound_two.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		bound_three.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		bound_four.render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		setBallBoardColor();
		board_mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		board_mesh1.render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		batch.begin();
		ball_x = Data.ball.get(0) * SCREEN_WIDTH / 2;
		ball_y = SCREEN_WIDTH - 2 * board_halfheight - Data.ball.get(1)
				* SCREEN_WIDTH / 2;
		ball.setTransform(ball_x, ball_y, 0);
		batch.draw(texture2, set_x - 20f + ball_x * 10, set_y - 120f + ball_y
				* 10, 40f, 40f);
		batch.end();

		camera.update();
		camera.apply(gl);
		renderer.render(world, camera.combined);

		if (old_board_x != board_x) {
			send.myboard();
			old_board_x = board_x;
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
		Data.location.set(Data.myID, 2 * board_x / SCREEN_WIDTH);
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
		Body cA = arg0.getFixtureA().getBody();
		Body cB = arg0.getFixtureB().getBody();

		BodyData dA = (BodyData) cA.getUserData();
		BodyData dB = (BodyData) cB.getUserData();
		if (dA.getType() == BodyData.BODY_BLOCK) {
			dA.health = 0;
			if(dA.getchangeType()<30){
				System.out.println(dA.getchangeType());
				Message m=new Message();
				m.what=SHOW_TOAST;
				windowHandler.sendMessage(m);
				if(ball.getLinearVelocity().y<0){    //1号板碰的
				//	send.ballsize(size)
				}else{								//0号板碰的
					
				}
				/*ChangeBall tT = new ChangeBall(tBall);
				tT.start(dA.getchangeType());*/
			}
			else{
				System.out.println(dA.getchangeType());
				Message m=new Message();
				m.what=SHOW_TOAST;
				windowHandler.sendMessage(m);
				if(ball.getLinearVelocity().y<0){    //1号板碰的
					send.boardsize(1,1);
				}else{								//0号板碰的
					send.boardsize(0,1);
				}
				/*ChangeBlock tT = new ChangeBlock(tBlock);
				tT.start(dA.getchangeType());*/
			}
		}
		if (dB.getType() == BodyData.BODY_BLOCK) {
			dB.health = 0;
			if(dA.getchangeType()<30){
				System.out.println(dA.getchangeType());
				Message m=new Message();
				m.what=SHOW_TOAST;
				windowHandler.sendMessage(m);
				if(ball.getLinearVelocity().y<0){    //0号板碰的
					
				}else{								//1号板碰的
					
				}
				/*ChangeBall tT = new ChangeBall(tBall);
				tT.start(dA.getchangeType());*/
			}
			else{
				System.out.println(dA.getchangeType());
				Message m=new Message();
				m.what=SHOW_TOAST;
				windowHandler.sendMessage(m);
				if(ball.getLinearVelocity().y<0){    //1号板碰的
					send.boardsize(1,1);
				}else{								//0号板碰的
					send.boardsize(0,1);
				}
				/*ChangeBlock tT = new ChangeBlock(tBlock);
				tT.start(dA.getchangeType());*/
			}
		}
	}

}
