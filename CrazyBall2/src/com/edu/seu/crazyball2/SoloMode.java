package com.edu.seu.crazyball2;

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
import com.edu.seu.props.PropsObservable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static com.edu.seu.crazyball2.Constant.*;

public class SoloMode implements ApplicationListener, ContactListener,
		InputProcessor {

	private Handler windowHandler;
	private CreateWorld mCreateWorld;
	private World mworld;
	private GL10 gl;

	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;

	private List<Body> ballList = new ArrayList<Body>();
	private List<Body> blockList = new ArrayList<Body>();

	private Mesh board_mesh;

	private boolean firstTouch = true;
	private PropsObservable po;
	
	public SoloMode(Handler h,PropsObservable po) {
		this.windowHandler = h;
		this.po=po;
	}
	@Override
	public void create() {
		Log.d("debug", "create");
		
		board_halfwidth0 = SCREEN_WIDTH * boardrate;
		board_halfwidth = SCREEN_WIDTH * boardrate;
		board_halfheight = board_halfwidth / 5;
		circle_radius_standard=board_halfheight;
		circle_radius=circle_radius_standard;
		block_width = 1f*circle_radius;

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

		initBlock();
	}

	private void initBlock() {
		Set<Integer> nums = new HashSet<Integer>();
		Random rd = new Random();
		while (nums.size() < 7) {
			nums.add((int) (rd.nextInt(100)));
		}

		Iterator<Integer> iter = nums.iterator();

		blockList.clear();
		for (int i = 0; i < 1; i++) {
			Integer temp = iter.next();
			float x = (temp %10 - 5) * block_width*2.4f;
			float y = (temp /10 + 3)* block_width*2.4f;
			int type = rd.nextInt(4) + 31;
			Body tB = B2Util.createRectangle(mworld, block_width/2, block_width/2,
					x, y, BodyType.StaticBody, 0, 0, 0, 0, new BodyData(
							BodyData.BODY_BLOCK, type), null);
			blockList.add(tB);
		}
		ballList.clear();
		while (iter.hasNext()) {
			Integer temp = iter.next();
			float x = (temp %10 - 5) * block_width*2.4f;
			float y = (temp /10 + 3)* block_width*2.4f;
			int type = rd.nextInt(2) + 21;
			Body tB = B2Util.createRectangle(mworld, block_width/2, block_width/2,
					x, y, BodyType.StaticBody, 0, 0, 0, 0, new BodyData(
							BodyData.BODY_BLOCK, type), null);
			ballList.add(tB);
		}
	}

	private void createBallBoard() {
		// 创建球
		tBall = B2Util.createCircle(mworld, circle_radius_standard, 0, board_halfheight
				+ circle_radius_standard, BodyType.DynamicBody, 0, 2, 1, 0,
				new BodyData(BodyData.BODY_BALL), null);
		// 创建挡板
		tBoard0 = B2Util.createRectangle(mworld, board_halfwidth0,
				board_halfheight, 0, 0, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BOARD), null);

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

	}

	@Override
	public void render() {
		mworld.step(Gdx.graphics.getDeltaTime(), 1, 1);
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
		batch.draw(mCreateWorld.getTexture2(), set_x +( x-circle_radius )* 10, set_y
				-100f + (y- circle_radius )* 10, 20*circle_radius, 20*circle_radius);
		
		for (int i = 0; i < ballList.size(); i++) {
			Body b = ballList.get(i);
			BodyData bd = (BodyData) b.getUserData();
			if (bd.health == 0) {
				mworld.destroyBody(b);
				ballList.remove(i);
				i--;
			} else {
				batch.draw(mCreateWorld.getBlockTexture(12), i * 20f, i * 20f,
						20f, 20f);
			}
		}
		for (int i = 0; i < blockList.size(); i++) {
			Body b = blockList.get(i);
			BodyData bd = (BodyData) b.getUserData();
			if (bd.health == 0) {
				mworld.destroyBody(b);
				blockList.remove(i);
				i--;
			} else {
				batch.draw(mCreateWorld.getBlockTexture(12), i * 20f, i * 20f,
						20f, 20f);
			}
		}
		if (ballList.size() == 0 && blockList.size() == 0) {
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
			tBall.setLinearVelocity(0, 0);
			Message m=new Message();
			m.what=SHOW_DIALOG;
			windowHandler.sendMessage(m);
		}
		if (dA.getType() == BodyData.BODY_BLOCK) {		
			dA.health = 0;			
			ball_temp_post++;
			System.out.println("Begin to listener,ballhit:"+ ball_temp_post);
			po.setChange(dA.getchangeType(),0);
		}	
		if (dB.getType() == BodyData.BODY_BLOCK) {						
			dB.health = 0;		
			ball_temp_post++;
			System.out.println("Begin to listener,ballhit:"+ ball_temp_post);
			po.setChange(dA.getchangeType(),0);
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
