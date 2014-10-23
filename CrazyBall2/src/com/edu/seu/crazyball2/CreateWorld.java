package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.SCREEN_WIDTH;
import static com.edu.seu.crazyball2.Constant.board_halfheight;
import static com.edu.seu.crazyball2.Constant.boardrate;
import static com.edu.seu.crazyball2.Constant.bound_width;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CreateWorld {

	private World world;
	private Body tBound1;
	private Body tBound2;
	private Body tBound3;
	private Body tBound4;
	private Body tBound_circle1;
	private Body tBound_circle2;
	private Body tBound_circle3;
	private Body tBound_circle4;
	private Mesh bound_one;
	private Mesh bound_two;
	private Mesh bound_three;
	private Mesh bound_four;

	private SpriteBatch batch;
	private Texture texture;
	private Texture texture2;
	private ImageButton Btn_A_OK;
	private ImageButton Btn_B_Cancel;
	private Stage stage;
	private BitmapFont font;
	private Window dialogWindow;

	float board_halfwidth = SCREEN_WIDTH * boardrate;

	public CreateWorld() {
		world = new World(new Vector2(0, 0f), true);
		tBound1 = B2Util.createRectangle(world, SCREEN_WIDTH / 2,
				bound_width / 2, 0, -board_halfheight + SCREEN_WIDTH
						- bound_width / 2, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BORDER), null); // up
		tBound2 = B2Util.createRectangle(world, bound_width / 2,
				SCREEN_WIDTH / 2, -SCREEN_WIDTH / 2, -board_halfheight
						+ SCREEN_WIDTH / 2, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BORDER), null); // left
		tBound3 = B2Util.createRectangle(world, bound_width / 2,
				SCREEN_WIDTH / 2, SCREEN_WIDTH / 2, -board_halfheight
						+ SCREEN_WIDTH / 2, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BORDER), null); // right
		tBound4 = B2Util.createRectangle(world, SCREEN_WIDTH / 2,
				bound_width / 2, 0, -board_halfheight + bound_width / 2,
				BodyType.StaticBody, 0, 0, 0, 0, new BodyData(
						BodyData.BODY_BOTTOM), null); // down
		tBound_circle1= B2Util.createCircle(world,board_halfheight*2,-SCREEN_WIDTH/2,SCREEN_WIDTH-board_halfheight, BodyType.StaticBody, 0, 0,0, 0,
				new BodyData(BodyData.BODY_BALL), null);
		tBound_circle2= B2Util.createCircle(world,board_halfheight*2,+SCREEN_WIDTH/2,SCREEN_WIDTH-board_halfheight, BodyType.StaticBody, 0, 0,0, 0,
				new BodyData(BodyData.BODY_BALL), null);
		tBound_circle3= B2Util.createCircle(world,board_halfheight*2,-SCREEN_WIDTH/2, -board_halfheight, BodyType.StaticBody, 0, 0,0, 0,
				new BodyData(BodyData.BODY_BALL), null);
		tBound_circle4= B2Util.createCircle(world,board_halfheight*2,+SCREEN_WIDTH/2, -board_halfheight, BodyType.StaticBody, 0, 0,0, 0,
				new BodyData(BodyData.BODY_BALL), null);
		

		setBoundColor();
		stage = new Stage();
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("data/potato.fnt"),
				Gdx.files.internal("data/potato.png"), false);
		texture2 = new Texture(Gdx.files.internal("data/ball.png"));

		setButton();
		setWindow();
		setBtnListener();
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

	       TextureRegionDrawable Btn_A_UP = new TextureRegionDrawable(regionBtn[2]);
	       TextureRegionDrawable Btn_A_DOWN = new TextureRegionDrawable(
	               regionBtn[3]);

	       TextureRegionDrawable Btn_B_UP = new TextureRegionDrawable(regionBtn[4]);
	       TextureRegionDrawable Btn_B_DOWN = new TextureRegionDrawable(
	               regionBtn[5]);


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
	               return super.touchDown(event, x, y, pointer, button);
	           }
	           
	       });
	   }
	public World getWorld() {
		return world;
	}

	public Mesh getBound_one() {
		return bound_one;
	}

	public Mesh getBound_two() {
		return bound_two;
	}

	public Mesh getBound_three() {
		return bound_three;
	}

	public Mesh getBound_four() {
		return bound_four;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public Texture getTexture() {
		return texture;
	}

	public Texture getTexture2() {
		return texture2;
	}

	public ImageButton getBtn_A_OK() {
		return Btn_A_OK;
	}

	public ImageButton getBtn_B_Cancel() {
		return Btn_B_Cancel;
	}

	public Stage getStage() {
		return stage;
	}

	public BitmapFont getFont() {
		return font;
	}

	public Window getDialogWindow() {
		return dialogWindow;
	}

}
