package com.edu.seu.crazyball2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import static com.edu.seu.crazyball2.Constant.*;

public class SoloMode implements ApplicationListener, ContactListener,
		InputProcessor {

	private Handler windowHandler;
	private CreateWorld mCreateWorld;
	private World mworld;
	private GL10 gl;

	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;

	private Body tBoard0;
	private Body tBall;
	
	//生成的方块
	private List<Body> ballList = new ArrayList<Body>();
	private List<Body> blockList = new ArrayList<Body>();

	private Mesh board_mesh;

	private boolean firstTouch = true;

	public SoloMode(Handler h) {
		this.windowHandler = h;
	}

	@Override
	public void create() {
		Log.d("debug", "create");

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

		// 创建球和板
		createBallBoard();
		setBallBoardColor();

		// 设置碰撞监听
		mworld.setContactListener(this);

		// 设置输入监听
		InputMultiplexer inputmultiplexer = new InputMultiplexer();
		inputmultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputmultiplexer);
		
		// 创建砖块
		initBlock();
/*  		final Timer ts = new Timer();
		TimerTask bt = new TimerTask(){
			@Override

            public void run() {
				while(true)
            	initBlock();
            	//ts.cancel();
            	}                
		};
		ts.schedule(bt, 60000,60000);  */
		
		// 设置碰撞监听
		mworld.setContactListener(this);
	}

	

	//创建砖块
	private void initBlock() {
		//获取随机的十个坐标点
		Set<Integer> nums = new HashSet<Integer>();
		Random rd = new Random();
		while(nums.size()<6){
			//利用nextFloat()生成[0，99)范围内的随机整数序列
			nums.add((int) (rd.nextFloat()*100));
		}
		
		Iterator<Integer> iter = nums.iterator();
		
		//设置具有被动属性的砖块(改变板的状态的属性)
		blockList.clear();
		for(int i=0 ;i<3;i++){
			Integer temp  = iter.next();
			float x = (temp%10-5)*(block_width*2.4f);
			float y = (10+(temp/10)*block_width*2.4f);
			System.out.println("x:"+x+" y:"+y);
			int type = rd.nextInt(4)+31;
			Body tB = B2Util.createRectangle(mworld, block_width, block_width, x,
					y, BodyType.StaticBody, 0, 0, 0, 0,
					new BodyData(BodyData.BODY_BLOCK,type), null);
			blockList.add(tB);
		}
		//设置具有主动属性的砖块(改变板的状态的属性)
		ballList.clear();
		while(iter.hasNext()){
			Integer temp  = iter.next();
			float x = (temp%10-5)*(block_width*2.4f);
			float y = (10+(temp/10)*block_width*2.4f);
			int type = rd.nextInt(4)+21;
			Body tB = B2Util.createRectangle(mworld, block_width, block_width, x,
					y, BodyType.StaticBody, 0, 0, 0, 0,
					new BodyData(BodyData.BODY_BLOCK,type), null);
			System.out.println("x:"+x+" y:"+y);
			ballList.add(tB);
		}
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

	}

/*	绘制图片、上色*/
	//绘制球的图片和板的图片
	private void setBallBoardColor() {
		float x = tBoard0.getPosition().x;
		float y = tBoard0.getPosition().y;

		board_mesh.setVertices(new float[] { x - board_halfwidth,
				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				x - board_halfwidth, y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255), x + board_halfwidth,
				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
				x + board_halfwidth, y - board_halfheight, 0,
				Color.toFloatBits(0, 0, 0, 255) });

	}
	//绘制砖块的图片
	@Override
	public void render() {
		mworld.step(Gdx.graphics.getDeltaTime(), 10, 8);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glClearColor(1f, 1f, 1f, 0f);

		mCreateWorld.getBound_one().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_two().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_three().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_four().render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		setBallBoardColor();
		board_mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		SpriteBatch batch = mCreateWorld.getBatch();

		batch.begin();
		float x = tBall.getPosition().x;
		float y = tBall.getPosition().y;
		System.out.println("draw x:"+x+"  draw y:"+y);
		batch.draw(mCreateWorld.getTexture2(), set_x - 20f + x * 10, set_y
				- 120f + y * 10, 40f, 40f);
		// 销毁处理
		for (int i = 0; i < ballList.size(); i++) {
			Body b = ballList.get(i);
			BodyData bd = (BodyData) b.getUserData();
			if (bd.health == 0) {
				mworld.destroyBody(b);
				ballList.remove(i);
				i--;
			}
			else{
				System.out.println("draw x:"+b.getPosition().x+"  draw y:"+b.getPosition().y);
				batch.draw(mCreateWorld.getBlockTexture(12),i*20f,i*20f, 20f,20f);
			}
		}
		for (int i = 0; i < blockList.size(); i++) {
			Body b = blockList.get(i);
			BodyData bd = (BodyData) b.getUserData();
			if (bd.health == 0) {
				mworld.destroyBody(b);
				blockList.remove(i);
				i--;
			}
			else{
				System.out.println("succeed in the draw method1");
				batch.draw(mCreateWorld.getBlockTexture(12),i*20f,i*20f, 20f,20f);
			}
		}
		if(ballList.size()==0&&blockList.size()==0){
			initBlock();
		}
		batch.end();
		camera.update();
		camera.apply(gl);
		renderer.render(mworld, camera.combined);
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		Vector3 touchV = new Vector3(arg0, arg1, 0);
		camera.unproject(touchV);
		if (firstTouch) {
			firstTouch = false;
			tBall.setLinearVelocity(60f, 80f);
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
		if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BORDER_BOTTOM)
				|| (dA.getType() == BodyData.BODY_BORDER_BOTTOM && dB.getType() == BodyData.BODY_BALL)) {
			/*tBall.setLinearVelocity(0, 0);
			Message m=new Message();
			m.what=SHOW_DIALOG;
			windowHandler.sendMessage(m);*/
		}
		if (dA.getType() == BodyData.BODY_BLOCK) {
			dA.health = 0;
			if(dA.getchangeType()<30){
				System.out.println("bump! "+dA.getchangeType());
				ChangeBall tT = new ChangeBall(tBall);
				tT.start(dA.getchangeType());
			}
			else{
				System.out.println("bump! "+dA.getchangeType());
				ChangeBoard tT = new ChangeBoard(tBoard0);
				tT.start(dA.getchangeType());
			}
		}
		if (dB.getType() == BodyData.BODY_BLOCK) {
			dB.health = 0;
			if(dA.getchangeType()<30){
				System.out.println("bump! "+dA.getchangeType());
				ChangeBall tT = new ChangeBall(tBall);
				tT.start(dA.getchangeType());
			}
			else{
				System.out.println("bump! "+dA.getchangeType());
				ChangeBoard tT = new ChangeBoard(tBoard0);
				tT.start(dA.getchangeType());
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
