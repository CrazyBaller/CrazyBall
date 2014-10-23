package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;
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

public class ThreeMode implements ApplicationListener, ContactListener,
		InputProcessor {

	private CreateWorld mCreateWorld;
	private World mworld;
	private GL10 gl;

	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;

	private Body tBoard0;
	private Body tBoard1;
	private Body tBoard2;
	private Body tBall;
	
	private Mesh board_mesh;
	private Mesh board_mesh1;
	private Mesh board_mesh2;
	
	private Stage stage;
	
	float board_halfwidth = SCREEN_WIDTH * boardrate;
	private boolean firstTouch = true;

	@Override
	public void create() {
		Log.d("debug", "create");

		// 镜头下的世界
		camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		camera.position.set(0, 10, 0);

		gl = Gdx.graphics.getGL10();
		renderer = new Box2DDebugRenderer();
		
		//创建背景世界
		mCreateWorld=new CreateWorld();
		mworld=mCreateWorld.getWorld();
		stage=mCreateWorld.getStage();
		
		//创建球和板
		createBallBoard();
		setBallBoardColor();
		
		// 设置碰撞监听
		mworld.setContactListener(this); 
		
		// 设置输入监听
	    InputMultiplexer inputmultiplexer=new  InputMultiplexer();
	    inputmultiplexer.addProcessor(stage);
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
		tBoard1 = B2Util.createRectangle(mworld,SCREEN_WIDTH * boardrate,board_halfheight, 
				 0, SCREEN_WIDTH-2*board_halfheight, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BOARD), null);
		tBoard2 = B2Util.createRectangle(mworld,board_halfheight,  SCREEN_WIDTH * boardrate,
				-SCREEN_WIDTH/2+board_halfheight, -board_halfheight+SCREEN_WIDTH / 2, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BOARD), null);
		
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
		
		x = tBoard2.getPosition().x;
		y = tBoard2.getPosition().y;
		
		board_mesh2 = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		board_mesh2.setVertices(new float[] { x - board_halfheight,
				y + board_halfwidth, 0, Color.toFloatBits(0, 0, 0, 255),
				x - board_halfheight, y - board_halfwidth, 0,
				Color.toFloatBits(0, 0, 0, 255), x + board_halfheight,
				y + board_halfwidth, 0, Color.toFloatBits(0, 0, 0, 255),
				x + board_halfheight, y - board_halfwidth, 0,
				Color.toFloatBits(0, 0, 0, 255) });
		board_mesh2.setIndices(new short[] { 0, 1, 2, 3 });
		
	}	   

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
		board_mesh1.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		board_mesh2.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		SpriteBatch batch=mCreateWorld.getBatch();
			
		batch.begin();
		float x=tBall.getPosition().x;
		float y=tBall.getPosition().y;
	    batch.draw(mCreateWorld.getTexture2(),set_x-20f+x*10,set_y-120f+y*10,40f,40f);
	    batch.end();
	    stage.act();
	    stage.draw(); 
		
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
		tBoard0.setTransform(touchV.x, tBoard0.getWorldCenter().y, 0);
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
		stage.addActor(mCreateWorld.getDialogWindow());
		tBall.setLinearVelocity(0, 0);	
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
		if (mCreateWorld.getTexture()!= null) {
			mCreateWorld.getTexture().dispose();
		}
		if ( mCreateWorld.getTexture2()!= null) {
			mCreateWorld.getTexture2().dispose();
		}
		if ( mCreateWorld.getBatch()!= null) {
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
