package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
import com.edu.seu.message.GameMessages.RemoteLocationMessage;
import com.edu.seu.message.SendData;
import com.edu.seu.props.PropsObservable;
import com.lenovo.game.GameMessage;
import com.lenovo.game.GameUserInfo;

public class ThreeModeClient implements ApplicationListener, ContactListener,
		InputProcessor {

	private GL10 gl;
	private Handler windowHandler;
	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;
	private CreateWorld mCreateWorld;
	private World mworld;

	private List<Body> blockList = new ArrayList<Body>();
	private PropsObservable po;
	private Mesh board_mesh;
	private Mesh board_mesh1;
	private Mesh board_mesh2;

	private SpriteBatch batch;
	private Texture texture2;

	private float board_x = 0;
	private float board_y = 0;
	private float board1_x = 0;
	private float board1_y = 0;
	private float board2_x = 0;
	private float board2_y = 0;
	private float ball_x = 0;
	private float ball_y = 0;

	private int type = 1;
	float x;
	float y;
	private int lasttouch=0;
	
	SendData send = null;
	private float old_board_x = 0;
	private float mLastTime = 0;
	
	private boolean backReleased=false;
	private Vector2 oldVector;

	public ThreeModeClient(Handler h, PropsObservable po) {
		this.windowHandler = h;
		this.po = po;
	}

	@Override
	public void create() {
		Log.d("debug", "create");

		board_halfwidth0 = SCREEN_WIDTH * boardrate;
		board_halfwidth1 = SCREEN_WIDTH * boardrate;
		board_halfwidth2 = SCREEN_WIDTH * boardrate;
		board_halfwidth = SCREEN_WIDTH * boardrate;
		board_halfheight = board_halfwidth / 5;
		circle_radius_standard = board_halfheight;
		circle_radius = circle_radius_standard;
		block_width = 1f * circle_radius;

		send = new SendData();

		// 镜头下的世界
		camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		camera.position.set(0, 10, 0);

		gl = Gdx.graphics.getGL10();

		// 创建背景世界
		mCreateWorld = new CreateWorld();
		mworld = mCreateWorld.getWorld();
		board_mesh = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		board_mesh1 = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		board_mesh2 = new Mesh(false, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));

		createBallBoard();
		setBallBoardColor();
		if (type == 1)
			initBlock();
		else {
			initBlockClient();
		}

		mworld.setContactListener(this);

		// 设置输入监听
		InputMultiplexer inputmultiplexer = new InputMultiplexer();
		inputmultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputmultiplexer);
		Gdx.input.setCatchBackKey(true);

	}

	private void initBlock() {
		Set<Integer> nums = new HashSet<Integer>();
		Random rd = new Random();
		while (nums.size() < 9) {
			nums.add((int) (rd.nextInt(100)));
		}

		Iterator<Integer> iter = nums.iterator();

		Data.propsimageid.clear();
		Data.propsimagex.clear();
		Data.propsimagey.clear();
		blockList.clear();
		int id=0;
		for (int i = 0; i < 6; i++) {
			Integer temp = iter.next();
			float x = (temp % 10 - 5) * (block_width * 2.4f);
			float y = (3 + (temp / 10)) * block_width * 2.4f;
			int type = rd.nextInt(4) + 31;
			Body tB = B2Util.createRectangle(mworld, block_width / 1.6f,
					block_width / 1.6f, x, y, BodyType.StaticBody, 0, 0, 0, 0,
					new BodyData(BodyData.BODY_BLOCK, type,id), null);
			blockList.add(tB);
			id++;
			Data.propsimageid.add(type);
			Data.propsimagex.add(x/(SCREEN_WIDTH/2));
			Data.propsimagey.add(y/(SCREEN_WIDTH/2));
		}
		while (iter.hasNext()) {
			Integer temp = iter.next();
			float x = (temp % 10 - 5) * (block_width * 2.4f);
			float y = (3 + temp / 10) * block_width * 2.4f;
			int type = rd.nextInt(4) + 21;
			Body tB = B2Util.createRectangle(mworld, block_width / 1.6f,
					block_width / 1.6f, x, y, BodyType.StaticBody, 0, 0, 0, 0,
					new BodyData(BodyData.BODY_BLOCK, type,id), null);
			blockList.add(tB);
			id++;
			Data.propsimageid.add(type);
			Data.propsimagex.add(x/(SCREEN_WIDTH/2));
			Data.propsimagey.add(y/(SCREEN_WIDTH/2));
		}

		send.propsimage(); // /发送道具位置

	}

	private void initBlockClient() {
		blockList.clear();
		for (int i = 0; i < Data.propsimageid.size(); i++) {
			int type = Data.propsimageid.get(i);
			float x = Data.propsimagex.get(i)* SCREEN_WIDTH / 2;
			float y = SCREEN_WIDTH - 2 * board_halfheight
					- Data.propsimagey.get(i) * SCREEN_WIDTH / 2;
			Body t = B2Util.createRectangle(mworld, block_width / 1.6f,
					block_width / 1.6f, x, y, BodyType.StaticBody, 0, 0, 0, 0,
					new BodyData(BodyData.BODY_BLOCK, type), null);
			blockList.add(t);
		}
	}

	private void createBallBoard() {
		// 创建球
		tBall = B2Util.createCircle(mworld, circle_radius_standard, 0,
				SCREEN_WIDTH / 2 - board_halfheight, BodyType.DynamicBody, 0,
				2, 1, 0, new BodyData(BodyData.BODY_BALL), null);
		// 创建挡板
		if (Data.myID == 1) {
			type = 1;
			tBoard1 = B2Util.createRectangle(mworld, board_halfwidth1,
					board_halfheight, 0, 0, BodyType.StaticBody, 0, 0, 0, 0,
					new BodyData(BodyData.BODY_BOARD1), null);
			tBoard0 = B2Util.createRectangle(mworld, board_halfwidth0,
					board_halfheight, 0, SCREEN_WIDTH - 2 * board_halfheight,
					BodyType.StaticBody, 0, 0, 0, 0, new BodyData(
							BodyData.BODY_BOARD0), null);
			tBoard2 = B2Util.createRectangle(mworld, board_halfheight,
					board_halfwidth2, SCREEN_WIDTH / 2 - board_halfheight,
					-board_halfheight + SCREEN_WIDTH / 2, BodyType.StaticBody,
					0, 0, 0, 0, new BodyData(BodyData.BODY_BOARD2), null);
		} else if (Data.myID == 2) {
			type = 2;
			tBoard1 = B2Util.createRectangle(mworld, board_halfheight,
					board_halfwidth1, -SCREEN_WIDTH / 2 + board_halfheight,
					-board_halfheight + SCREEN_WIDTH / 2, BodyType.StaticBody,
					0, 0, 0, 0, new BodyData(BodyData.BODY_BOARD1), null);
			tBoard0 = B2Util.createRectangle(mworld, board_halfheight,
					board_halfwidth0, SCREEN_WIDTH / 2 - board_halfheight,
					-board_halfheight + SCREEN_WIDTH / 2, BodyType.StaticBody,
					0, 0, 0, 0, new BodyData(BodyData.BODY_BOARD0), null);
			tBoard2 = B2Util.createRectangle(mworld, board_halfwidth2,
					board_halfheight, 0, 0, BodyType.StaticBody, 0, 0, 0, 0,
					new BodyData(BodyData.BODY_BOARD2), null);

		}

	}

	private void setBallBoardColor() {
		board_x = tBoard0.getPosition().x;
		board_y = tBoard0.getPosition().y;
		board1_x = tBoard1.getPosition().x;
		board1_y = tBoard1.getPosition().y;
		board2_x = tBoard2.getPosition().x;
		board2_y = tBoard2.getPosition().y;
		if (type == 1) {
			board_mesh.setVertices(new float[] { board_x - board_halfwidth0,
					board_y + board_halfheight, 0,
					Color.toFloatBits(0, 0, 0, 255),
					board_x - board_halfwidth0, board_y - board_halfheight, 0,
					Color.toFloatBits(0, 0, 0, 255),
					board_x + board_halfwidth0, board_y + board_halfheight, 0,
					Color.toFloatBits(0, 0, 0, 255),
					board_x + board_halfwidth0, board_y - board_halfheight, 0,
					Color.toFloatBits(0, 0, 0, 255) });
			board_mesh1.setVertices(new float[] { board1_x - board_halfwidth1,
					board1_y + board_halfheight, 0,
					Color.toFloatBits(0, 0, 0, 255),
					board1_x - board_halfwidth1, board1_y - board_halfheight,
					0, Color.toFloatBits(0, 0, 0, 255),
					board1_x + board_halfwidth1, board1_y + board_halfheight,
					0, Color.toFloatBits(0, 0, 0, 255),
					board1_x + board_halfwidth1, board1_y - board_halfheight,
					0, Color.toFloatBits(0, 0, 0, 255) });
			board_mesh2.setVertices(new float[] { board2_x - board_halfheight,
					board2_y + board_halfwidth2, 0,
					Color.toFloatBits(0, 0, 0, 255),
					board2_x - board_halfheight, board2_y - board_halfwidth2,
					0, Color.toFloatBits(0, 0, 0, 255),
					board2_x + board_halfheight, board2_y + board_halfwidth2,
					0, Color.toFloatBits(0, 0, 0, 255),
					board2_x + board_halfheight, board2_y - board_halfwidth2,
					0, Color.toFloatBits(0, 0, 0, 255) });
		} else if (type == 2) {
			board_mesh.setVertices(new float[] { board_x - board_halfheight,
					board_y + board_halfwidth0, 0,
					Color.toFloatBits(0, 0, 0, 255),
					board_x - board_halfheight, board_y - board_halfwidth0, 0,
					Color.toFloatBits(0, 0, 0, 255),
					board_x + board_halfheight, board_y + board_halfwidth0, 0,
					Color.toFloatBits(0, 0, 0, 255),
					board_x + board_halfheight, board_y - board_halfwidth0, 0,
					Color.toFloatBits(0, 0, 0, 255) });
			board_mesh1.setVertices(new float[] { board1_x - board_halfheight,
					board1_y + board_halfwidth1, 0,
					Color.toFloatBits(0, 0, 0, 255),
					board1_x - board_halfheight, board1_y - board_halfwidth1,
					0, Color.toFloatBits(0, 0, 0, 255),
					board1_x + board_halfheight, board1_y + board_halfwidth1,
					0, Color.toFloatBits(0, 0, 0, 255),
					board1_x + board_halfheight, board1_y - board_halfwidth1,
					0, Color.toFloatBits(0, 0, 0, 255) });
			board_mesh2.setVertices(new float[] { board2_x - board_halfwidth2,
					board2_y + board_halfheight, 0,
					Color.toFloatBits(0, 0, 0, 255),
					board2_x - board_halfwidth2, board2_y - board_halfheight,
					0, Color.toFloatBits(0, 0, 0, 255),
					board2_x + board_halfwidth2, board2_y + board_halfheight,
					0, Color.toFloatBits(0, 0, 0, 255),
					board2_x + board_halfwidth2, board2_y - board_halfheight,
					0, Color.toFloatBits(0, 0, 0, 255) });
		}

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

		mCreateWorld.getBound_one().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_two().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_three().render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		mCreateWorld.getBound_four().render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		setBallBoardColor();
		board_mesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		board_mesh1.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
		board_mesh2.render(GL10.GL_TRIANGLE_STRIP, 0, 4);

		batch.begin();
		if (type == 1) {
			tBoard0.setTransform(Data.location.get(0) * SCREEN_WIDTH / 2,
					tBoard0.getWorldCenter().y, 0);
			tBoard2.setTransform(tBoard2.getWorldCenter().x, SCREEN_WIDTH / 2
					- board_halfheight - Data.location.get(2) * SCREEN_WIDTH
					/ 2, 0);
			ball_x = Data.ball.get(0) * SCREEN_WIDTH / 2;
			ball_y = SCREEN_WIDTH - 2 * board_halfheight - Data.ball.get(1)
					* SCREEN_WIDTH / 2;
			circle_radius=tBall.getFixtureList().get(0).getShape().getRadius();
			batch.draw(mCreateWorld.getTexture2(), set_x +( ball_x-circle_radius )* 10, set_y
					-100f + (ball_y- circle_radius )* 10, 20*circle_radius, 20*circle_radius);
		} else {
			tBoard0.setTransform(tBoard0.getWorldCenter().x, SCREEN_WIDTH / 2
					- board_halfheight - Data.location.get(0) * SCREEN_WIDTH
					/ 2, 0);
			tBoard1.setTransform(tBoard1.getWorldCenter().x, SCREEN_WIDTH / 2
					- board_halfheight - Data.location.get(1) * SCREEN_WIDTH
					/ 2, 0);
			ball_x = (SCREEN_WIDTH / 2) * (1 - Data.ball.get(1));
			ball_y = (SCREEN_WIDTH / 2) * (1 - Data.ball.get(0))
					- board_halfheight;
			circle_radius=tBall.getFixtureList().get(0).getShape().getRadius();
			batch.draw(mCreateWorld.getTexture2(), set_x +( ball_x-circle_radius )* 10, set_y
					-100f + (ball_y- circle_radius )* 10, 20*circle_radius, 20*circle_radius);
		}
		
		for (int i = 0; i < blockList.size(); i++) {
			Body b = blockList.get(i);
			BodyData bd = (BodyData) b.getUserData();
			if (bd.health == 0) {
				mworld.destroyBody(b);
				blockList.remove(i);
				i--;
			} else {
				x = b.getPosition().x;
				y = b.getPosition().y;
				batch.draw(mCreateWorld.getBlockTexture(12), set_x
						+ (x - block_width / 2) * 10, set_y - 100f
						+ (y - block_width / 2) * 10, 10 * block_width / 0.8f,
						10 * block_width / 0.8f);
			}
		}
		if (blockList.size() == 0 && type == 1) {
			initBlock();
		}
		batch.end();
		
		if (Gdx.input.isKeyPressed(Keys.BACK)&&!backReleased) 
		{   
			backReleased=true;
			Message m=new Message();
		    m.what=SHOW_DIALOG2;
	     	windowHandler.sendMessage(m);
		    pause();
		} 
		camera.update();
		camera.apply(gl);

		if ((old_board_x != tBoard1.getPosition().x)&&type==1) {
			send.myboard();
			old_board_x = tBoard1.getPosition().x;
		}
		if ((old_board_x != tBoard2.getPosition().x)&&type==2) {
			send.myboard();
			old_board_x = tBoard2.getPosition().x;
		}
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
//		Vector3 vTouch = new Vector3(arg0, arg1, 0);
//		// 像素坐标转换为world坐标
//		camera.unproject(vTouch);

		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		Vector3 touchV = new Vector3(arg0, arg1, 0);
		camera.unproject(touchV);
		if(type==1){
			if (touchV.x <= SCREEN_WIDTH / 2 - board_halfheight * 2
					- board_halfwidth1
					&& touchV.x >= -SCREEN_WIDTH / 2 + board_halfheight * 2
							+ board_halfwidth1) {
				tBoard1.setTransform(touchV.x, 0, 0);
				Data.location.set(Data.myID, 2 * touchV.x / SCREEN_WIDTH);
			}
		}else{
			if (touchV.x <= SCREEN_WIDTH / 2 - board_halfheight * 2
					- board_halfwidth2
					&& touchV.x >= -SCREEN_WIDTH / 2 + board_halfheight * 2
							+ board_halfwidth2) {
				tBoard2.setTransform(touchV.x, 0, 0);
				Data.location.set(Data.myID, 2 * touchV.x / SCREEN_WIDTH);
			}
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
		oldVector=tBall.getLinearVelocity();
		tBall.setLinearVelocity(0,0);
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
		backReleased=false;
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
		Body cA = arg0.getFixtureA().getBody();
		Body cB = arg0.getFixtureB().getBody();

		BodyData dA = (BodyData) cA.getUserData();
		BodyData dB = (BodyData) cB.getUserData();
		if (dA.getType() == BodyData.BODY_BLOCK) {
			dA.health = 0;
			int i = dA.getchangeType();
				send.props(i, lasttouch);
				po.setChange(i, lasttouch);
		}
		if (dB.getType() == BodyData.BODY_BLOCK) {
			dB.health = 0;
			int i = dA.getchangeType();
				send.props(i, lasttouch);
				po.setChange(i, lasttouch);
		}
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
		
		if ((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BOARD0)       //0号碰
				|| (dA.getType() == BodyData.BODY_BOARD0 && dB.getType() == BodyData.BODY_BALL)){
			lasttouch=0;
		}else if((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BOARD1)       //1号碰
				|| (dA.getType() == BodyData.BODY_BOARD1 && dB.getType() == BodyData.BODY_BALL)){
			lasttouch=1;
		}else if((dA.getType() == BodyData.BODY_BALL && dB.getType() == BodyData.BODY_BOARD2)       //2号碰
				|| (dA.getType() == BodyData.BODY_BOARD2 && dB.getType() == BodyData.BODY_BALL)){
			lasttouch=2;
		}
	}

}
