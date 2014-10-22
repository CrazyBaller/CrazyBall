package com.edu.seu.crazyball2;

import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static com.edu.seu.crazyball2.Constant.*;

public class SoloMode implements ApplicationListener, ContactListener,
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
	
	private SpriteBatch batch;
	private Texture texture;
	private Texture texture2;
	private ImageButton Btn_A_OK;
	private ImageButton Btn_B_Cancel;
	private	Stage stage;
	private BitmapFont font;
	private Window dialogWindow;

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
		
		//弹窗
		 stage = new Stage();
		 batch = new SpriteBatch();
		 font = new BitmapFont(Gdx.files.internal("data/potato.fnt"),
	               Gdx.files.internal("data/potato.png"), false);
		 texture2 = new Texture(Gdx.files.internal("data/ball.png"));
		 
	     setButton();
	     setWindow();
	     setBtnListener();
	     
	     
	     
		// 设置输入监听
	     InputMultiplexer inputmultiplexer=new  InputMultiplexer();
	     inputmultiplexer.addProcessor(stage);
	     inputmultiplexer.addProcessor(this);
	     Gdx.input.setInputProcessor(inputmultiplexer);
		
		
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
		tBound1 = B2Util.createRectangle(world, SCREEN_WIDTH/2,
				bound_width/2, 0, -board_halfheight+SCREEN_WIDTH- bound_width / 2, BodyType.StaticBody,
				0, 0, 0, 0, new BodyData(BodyData.BODY_BORDER), null); // up
		tBound2 = B2Util.createRectangle(world, bound_width / 2,
				SCREEN_WIDTH / 2, -SCREEN_WIDTH / 2, -board_halfheight + SCREEN_WIDTH / 2, BodyType.StaticBody,
				0, 0, 0, 0, new BodyData(BodyData.BODY_BORDER), null); // left
		tBound3 = B2Util.createRectangle(world, bound_width / 2,
				SCREEN_WIDTH / 2, SCREEN_WIDTH / 2, -board_halfheight + SCREEN_WIDTH / 2, BodyType.StaticBody,
				0, 0, 0, 0, new BodyData(BodyData.BODY_BORDER), null); // right
		tBound4 = B2Util.createRectangle(world, SCREEN_WIDTH / 2,
				bound_width / 2, 0, -board_halfheight + bound_width / 2,
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
	
	public void setButton() {
	       texture = new Texture(Gdx.files.internal("data/control.png"));

	       TextureRegion[][] spilt = TextureRegion.split(texture, 64, 64);

	       TextureRegion[] regionBtn = new TextureRegion[6];
	       // 显示
	       regionBtn[0] = spilt[0][0];
	       regionBtn[1] = spilt[0][1];
	       // 确认
	       regionBtn[2] = spilt[0][2];
	       regionBtn[3] = spilt[0][3];
	       // 取消
	       regionBtn[4] = spilt[1][0];
	       regionBtn[5] = spilt[1][1];

	       TextureRegionDrawable Btn_SHOW_UP = new TextureRegionDrawable(
	               regionBtn[0]);
	       TextureRegionDrawable Btn_SHOW_DOWN = new TextureRegionDrawable(
	               regionBtn[1]);

	       TextureRegionDrawable Btn_A_UP = new TextureRegionDrawable(regionBtn[2]);
	       TextureRegionDrawable Btn_A_DOWN = new TextureRegionDrawable(
	               regionBtn[3]);

	       TextureRegionDrawable Btn_B_UP = new TextureRegionDrawable(regionBtn[4]);
	       TextureRegionDrawable Btn_B_DOWN = new TextureRegionDrawable(
	               regionBtn[5]);

//	       Btn_SHOW = new ImageButton(Btn_SHOW_UP, Btn_SHOW_DOWN);

	       Btn_A_OK = new ImageButton(Btn_A_UP, Btn_A_DOWN);

	       Btn_B_Cancel = new ImageButton(Btn_B_UP, Btn_B_DOWN);

	   }
	
	public void setWindow() {
	       TextureRegionDrawable WindowDrable = new TextureRegionDrawable(
	               new TextureRegion(new Texture(
	                       Gdx.files.internal("data/dialog.png"))));
	       
	       WindowStyle style = new WindowStyle(font, Color.RED, WindowDrable);
	       
	       dialogWindow = new Window("Game", style);
	       
	       dialogWindow.setWidth(Gdx.graphics.getWidth()/1.5f);
	       dialogWindow.setHeight(Gdx.graphics.getHeight()/4f);
	       
	       dialogWindow.setPosition(Gdx.graphics.getWidth()/6f,3*Gdx.graphics.getWidth()/8f);
	       
	       dialogWindow.setMovable(true);
	       
	               
	       Btn_A_OK.setPosition(Gdx.graphics.getWidth()/10, 0);
	       
	       Btn_B_Cancel.setPosition(Gdx.graphics.getWidth()/3, 0);
	       
	       
	       dialogWindow.addActor(Btn_A_OK);
	       
	       dialogWindow.addActor(Btn_B_Cancel);
	       
	   }
	
	 public void setBtnListener() {
//	       Btn_SHOW.addListener(new InputListener(){
//
//	           @Override
//	           public boolean touchDown(InputEvent event, float x, float y,
//	                   int pointer, int button) {
//	               
//	               stage.addActor(dialogWindow);
//	               
//	               return true;
//	           }
//	           
//	       });
//	       
	       Btn_A_OK.addListener(new InputListener(){
	           
	           @Override
	           public boolean touchDown(InputEvent event, float x, float y,
	                   int pointer, int button) {
	               System.out.println("sdfjksfdjslajgkldsgjkldgj");
	               Gdx.app.exit();
	               
	               return true;
	           }
	           
	       }); 
	       
	       Btn_B_Cancel.addListener(new InputListener(){

	           @Override
	           public boolean touchDown(InputEvent event, float x, float y,
	                   int pointer, int button) {
	               
	               dialogWindow.remove();
	               tBall.setLinearVelocity(50f, 70f);
	               return super.touchDown(event, x, y, pointer, button);
	           }
	           
	       });
	   }
	   

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
		
		batch.begin();
	    font.draw(batch, "Touch The Button!!!!", 100, 200);
		float x=tBall.getPosition().x;
		float y=tBall.getPosition().y;
		System.out.println("x:"+x+"  "+"y:"+y+" "+"set_x:"+set_x+" set_y:"+set_y);
	    batch.draw(texture2,set_x-20f+x*10,set_y-120f+y*10,40f,40f);
	    batch.end();
	    stage.act();
	    stage.draw(); 
		
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
	 stage.addActor(dialogWindow);
		tBall.setLinearVelocity(0, 0);	
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
		if ( texture!= null) {
			texture.dispose();
			texture = null;
		}
		if ( batch!= null) {
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
