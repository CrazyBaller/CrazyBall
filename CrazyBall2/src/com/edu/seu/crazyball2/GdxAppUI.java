package com.edu.seu.crazyball2;

import java.util.ArrayList;
import java.util.List;

import android.util.DisplayMetrics;
import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
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

public class GdxAppUI implements ApplicationListener, ContactListener,
		InputProcessor {

	private World world;
	private GL10 gl;

	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;

	@Override
	public void create() {
		Log.d("debug", "create");
		


		// 镜头下的世界
		camera = new OrthographicCamera(48, 32);
		camera.position.set(24, 16, 0);

		gl = Gdx.graphics.getGL10();
		renderer = new Box2DDebugRenderer();
		this.createWorld();
		// 设置输入监听
		Gdx.input.setInputProcessor(this);
	}

	private Body tBlock;
	private Body tBall;

	// 创建世界
	private void createWorld() {
		world = new World(new Vector2(0, 0f), true);
		// 创建边框
		B2Util.createEdge(world, 1, 30, 47, 30, BodyType.StaticBody, 0.3f, 1,
				0, new BodyData(BodyData.BODY_BORDER), null);
		B2Util.createEdge(world, 1, 1, 1, 30, BodyType.StaticBody, 0.3f, 1, 0,
				new BodyData(BodyData.BODY_BORDER), null);
		B2Util.createEdge(world, 47, 1, 47, 30, BodyType.StaticBody, 0.3f, 1,
				0, new BodyData(BodyData.BODY_BORDER), null);
//		B2Util.createEdge(world, 1, 47, 30, 47, BodyType.StaticBody, 0.3f, 1,
//				0, new BodyData(BodyData.BODY_BORDER), null);
		
		// 创建球
		tBall = B2Util.createCircle(world, 1, 15, 4, BodyType.DynamicBody, 0,
				0, 1, 0, new BodyData(BodyData.BODY_BALL), null);
		// 创建挡板
		tBlock = B2Util.createRectangle(world, 5, 1, 15, 2,
				BodyType.StaticBody, 0, 0, 0, 0, new BodyData(
						BodyData.BODY_BORDER), null);
		// 创建砖块
		initBlock();
		// 设置碰撞监听
		world.setContactListener(this);
	}

	private List<Body> ballList = null;

	//创建砖块
	private void initBlock() {
		ballList = new ArrayList<Body>();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				Body tB = B2Util.createRectangle(world, 2, 1, i * 5 + 15,
						j * 4 + 15, BodyType.StaticBody, 0, 0, 0, 0,
						new BodyData(BodyData.BODY_BLOCK), null);
				ballList.add(tB);
			}
		}
	}

	@Override
	public void render() {
		// 重要的几句代码
		/*****************************************/
		world.step(Gdx.graphics.getDeltaTime(), 8, 8);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glClearColor(1f, 1f, 1f, 0f);

		camera.update();
		camera.apply(gl);
		renderer.render(world);
		/****************************/

		// 销毁处理
		for (int i = 0; i < ballList.size(); i++) {
			Body b = ballList.get(i);
			BodyData bd = (BodyData) b.getUserData();
			if (bd.health == 0) {
				world.destroyBody(b);
				ballList.remove(i);
				i--;
			}
		}
	}

	private boolean firstTouch = true;
	private boolean isTouching = false;

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		Vector3 vTouch = new Vector3(arg0, arg1, 0);
		// 像素坐标转换为world坐标
		camera.unproject(vTouch);

//		Vector2 tbv = tBlock.getWorldCenter();
//		if (touchInBody(tbv, vTouch)) {
//			isTouching = true;
//			System.out.println("touch downs");
//		} else {
//			isTouching = false;
//		}
		isTouching = true;
		if (isTouching && firstTouch) {
			firstTouch = false;
			// 初始给球体一个力量
			tBall.applyLinearImpulse(new Vector2(20, 10),
					tBall.getWorldCenter());
		}

		return false;
	}

	private boolean touchInBody(Vector2 v1, Vector3 v2) {
		if (v2.x > v1.x - 2 && v2.x < v1.x + 2 && v2.y > v1.y - .5
				&& v2.y < v1.y + .5) {
			return true;
		}

		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		Vector3 touchV = new Vector3(arg0, arg1, 0);
		camera.unproject(touchV);

		if (isTouching) {
			// 设置移动坐标
			tBlock.setTransform(touchV.x, tBlock.getWorldCenter().y, 0);
			System.out.println("touch drag");
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
		if (dA.getType() == BodyData.BODY_BLOCK) {
			dA.health = 0;
		}
		if (dB.getType() == BodyData.BODY_BLOCK) {
			dB.health = 0;
		}
	}

	@Override
	public void dispose() {
		Log.d("debug", "dispose");

		if (world != null) {
			world.dispose();
			world = null;
		}
		if (renderer != null) {
			renderer.dispose();
			renderer = null;
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
	public boolean touchMoved(int arg0, int arg1) {
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		isTouching = false;
		return false;
	}

	@Override
	public void beginContact(Contact arg0) {

	}

	@Override
	public void endContact(Contact arg0) {

	}

	public class BodyData {
		public static final int BODY_BORDER = 1;
		public static final int BODY_BALL = 2;
		public static final int BODY_BLOCK = 3;

		private int _type;
		public int health = 100;

		public BodyData(int type) {
			this._type = type;
		}

		public int getType() {
			return this._type;
		}
	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1) {
		// TODO Auto-generated method stub

	}

}
