package com.edu.seu.crazyball2;

import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

	private Mesh bound_one;
	private Mesh bound_two;
	private Mesh bound_three;
	private Mesh bound_four;
	private Mesh ball_mesh;
	private Mesh board_mesh;

	private Body tBound1;
	private Body tBound2;
	private Body tBound3;
	private Body tBound4;
	private Body tBoard;
	private Body tBall;

	float board_halfwidth = SCREEN_WIDTH * boardrate;

	@Override
	public void create() {
		Log.d("debug", "create");

		// 镜头下的世界
		camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		camera.position.set(0, 10, 0);

		gl = Gdx.graphics.getGL10();
		renderer = new Box2DDebugRenderer();
		this.createWorld();

		setBoundColor();
		setBallBoardColor();

		// 设置输入监听
		Gdx.input.setInputProcessor(this);
	}

	private void setBoundColor() {
		float halfwidth = bound_width / 2;
		float halfheight = SCREEN_WIDTH / 2;

		float x = tBound1.getPosition().x;
		float y = tBound1.getPosition().y;

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

		x = tBound2.getPosition().x;
		y = tBound2.getPosition().y;

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

		x = tBound3.getPosition().x;
		y = tBound3.getPosition().y;

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

		x = tBound4.getPosition().x;
		y = tBound4.getPosition().y;

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
		float x = tBoard.getPosition().x;
		float y = tBoard.getPosition().y;

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
		
		x=tBall.getPosition().x;
		y=tBall.getPosition().y;
		
//		board_mesh = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
//				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
//				"a_color"));
//		board_mesh.setVertices(new float[] { x - board_halfwidth,
//				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
//				x - board_halfwidth, y - board_halfheight, 0,
//				Color.toFloatBits(0, 0, 0, 255), x + board_halfwidth,
//				y + board_halfheight, 0, Color.toFloatBits(0, 0, 0, 255),
//				x + board_halfwidth, y - board_halfheight, 0,
//				Color.toFloatBits(0, 0, 0, 255) });
//		board_mesh.setIndices(new short[] { 0, 1, 2, 3 });
	}

	// 创建世界
	private void createWorld() {
		world = new World(new Vector2(0, 0f), true);

		// 创建边框
		// B2Util.createEdge(world, 1, 30, 20, 30, BodyType.StaticBody, 0.3f, 1,
		// 0, new BodyData(BodyData.BODY_BORDER), null);
		// B2Util.createEdge(world, 1, 1, 1, 30, BodyType.StaticBody, 0.3f, 1,
		// 0,
		// new BodyData(BodyData.BODY_BORDER), null);
		// B2Util.createEdge(world, 47, 1, 47, 30, BodyType.StaticBody, 0.3f, 1,
		// 0, new BodyData(BodyData.BODY_BORDER), null);
		// B2Util.createEdge(world, 1, 47, 30, 47, BodyType.StaticBody, 0.3f, 1,
		// 0, new BodyData(BodyData.BODY_BORDER), null);
		tBound1 = B2Util.createRectangle(world, SCREEN_WIDTH / 2,
				bound_width / 2, 0, -board_halfheight - bound_width
						+ SCREEN_WIDTH - bound_width / 2, BodyType.StaticBody,
				0, 0, 0, 0, new BodyData(BodyData.BODY_BORDER), null); // up
		tBound2 = B2Util.createRectangle(world, bound_width / 2,
				SCREEN_WIDTH / 2, -SCREEN_WIDTH / 2, -board_halfheight
						- bound_width + SCREEN_WIDTH / 2, BodyType.StaticBody,
				0, 0, 0, 0, new BodyData(BodyData.BODY_BORDER), null); // left
		tBound3 = B2Util.createRectangle(world, bound_width / 2,
				SCREEN_WIDTH / 2, SCREEN_WIDTH / 2, -board_halfheight
						- bound_width + SCREEN_WIDTH / 2, BodyType.StaticBody,
				0, 0, 0, 0, new BodyData(BodyData.BODY_BORDER), null); // right
		tBound4 = B2Util.createRectangle(world, SCREEN_WIDTH / 2,
				bound_width / 2, 0, -board_halfheight - bound_width / 2,
				BodyType.StaticBody, 0, 0, 0, 0, new BodyData(
						BodyData.BODY_BOTTOM), null); // down

		// 创建球
		tBall = B2Util.createCircle(world, circle_radius, 0, board_halfheight
				+ circle_radius, BodyType.DynamicBody, 0, 2, 1, 0,
				new BodyData(BodyData.BODY_BALL), null);
		// 创建挡板
		tBoard = B2Util.createRectangle(world, SCREEN_WIDTH * boardrate,
				board_halfheight, 0, 0, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BOARD), null);

		Vector2 tbv = tBoard.getWorldCenter();
		// 创建砖块
		// initBoard();
		// 设置碰撞监听
		world.setContactListener(this);
	}

	// private List<Body> ballList = null;

	// //创建砖块
	// private void initBoard() {
	// ballList = new ArrayList<Body>();
	// for (int i = 0; i < 5; i++) {
	// for (int j = 0; j < 3; j++) {
	// Body tB = B2Util.createRectangle(world, 2, 1, i * 5 + 15,
	// j * 4 + 15, BodyType.StaticBody, 0, 0, 0, 0,
	// new BodyData(BodyData.BODY_BOARD), null);
	// ballList.add(tB);
	// }
	// }
	// }

	@Override
	public void render() {
		// 重要的几句代码
		/*****************************************/
		world.step(Gdx.graphics.getDeltaTime(), 10, 8);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glClearColor(1f, 1f, 1f, 0f);

		bound_one.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		bound_two.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		bound_three.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		bound_four.render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		setBallBoardColor();
		board_mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		camera.update();
		camera.apply(gl);
		renderer.render(world, camera.combined);
		/****************************/

		// 销毁处理
		// for (int i = 0; i < ballList.size(); i++) {
		// Body b = ballList.get(i);
		// BodyData bd = (BodyData) b.getUserData();
		// if (bd.health == 0) {
		// world.destroyBody(b);
		// ballList.remove(i);
		// i--;
		// }
		// }
	}

	private boolean firstTouch = true;
	private boolean isTouching = false;

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		Vector3 vTouch = new Vector3(arg0, arg1, 0);
		// 像素坐标转换为world坐标
		camera.unproject(vTouch);

		// Vector2 tbv = tBoard.getWorldCenter();
		// if (touchInBody(tbv, vTouch)) {
		// isTouching = true;
		// System.out.println("touch downs");
		// } else {
		// isTouching = false;
		// }
		isTouching = true;
		if (isTouching && firstTouch) {
			firstTouch = false;
			// 初始给球体一个力量
			// tBall.applyLinearImpulse(new Vector2(2000, 1000),
			// tBall.getWorldCenter());
			tBall.setLinearVelocity(50f, 70f);
		}

		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		Vector3 touchV = new Vector3(arg0, arg1, 0);
		camera.unproject(touchV);

		if (isTouching) {
			// 设置移动坐标
			tBoard.setTransform(touchV.x, tBoard.getWorldCenter().y, 0);
			System.out.println("touch drag");

			float x = tBoard.getPosition().x;
			float y = tBoard.getPosition().y;
			System.out.println("x:" + x + "   " + "y:" + y);
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
		if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BOTTOM)||(dA.getType() == BodyData.BODY_BOTTOM && dB.getType() == BodyData.BODY_BALL) ) {
//			dA.health = 0;
			System.out.println("你输了");
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
		public static final int BODY_BOTTOM = 2;
		public static final int BODY_BALL = 3;
		public static final int BODY_BOARD = 4;

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

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
