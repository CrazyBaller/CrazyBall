package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.SCREEN_HEIGHT;
import static com.edu.seu.crazyball2.Constant.SCREEN_WIDTH;
import static com.edu.seu.crazyball2.Constant.board_halfheight;
import static com.edu.seu.crazyball2.Constant.board_halfwidth;
import static com.edu.seu.crazyball2.Constant.boardrate;
import static com.edu.seu.crazyball2.Constant.bound_width;
import static com.edu.seu.crazyball2.Constant.CONTROL_ID;
import static com.edu.seu.crazyball2.Constant.base_width;
import static com.edu.seu.crazyball2.Constant.set_x;
import static com.edu.seu.crazyball2.Constant.set_y;
import static com.edu.seu.crazyball2.Constant.offset_center;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

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
	private Mesh screen;
	private Mesh background;
	private Mesh controlBackground;
	private Mesh slipeBackground;
	private Mesh bound_one;
	private Mesh bound_two;
	private Mesh bound_three;
	private Mesh bound_four;
	private Mesh head;

	private SpriteBatch batch;
	private Texture textureup;
	private Texture texturedown;
	private Texture texture2;
	private TextureAtlas atlas;
	private TextureRegion blockRegion;


	public CreateWorld() {
		board_halfwidth = SCREEN_WIDTH * boardrate;
		base_width = (SCREEN_HEIGHT-SCREEN_WIDTH)/7;// half base width
		world = new World(new Vector2(0, 0f), true);
		tBound1 = B2Util.createRectangle(world, SCREEN_WIDTH / 2,
				bound_width / 2, 0, -board_halfheight + SCREEN_WIDTH
						- bound_width / 2, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BORDER_UP), null); // up
		tBound2 = B2Util.createRectangle(world, bound_width / 2,
				SCREEN_WIDTH / 2, -SCREEN_WIDTH / 2, -board_halfheight
						+ SCREEN_WIDTH / 2, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BORDER_LEFT), null); // left
		tBound3 = B2Util.createRectangle(world, bound_width / 2,
				SCREEN_WIDTH / 2, SCREEN_WIDTH / 2, -board_halfheight
						+ SCREEN_WIDTH / 2, BodyType.StaticBody, 0, 0, 0, 0,
				new BodyData(BodyData.BODY_BORDER_RIGHT), null); // right
		tBound4 = B2Util.createRectangle(world, SCREEN_WIDTH / 2,
				bound_width / 2, 0, -board_halfheight + bound_width / 2,
				BodyType.StaticBody, 0, 0, 0, 0, new BodyData(
						BodyData.BODY_BORDER_BOTTOM), null); // down
		tBound_circle1 = B2Util.createCircle(world, board_halfheight * 2,
				-SCREEN_WIDTH / 2, SCREEN_WIDTH - board_halfheight,
				BodyType.StaticBody, 0, 0, 0, 0, new BodyData(
						BodyData.BODY_BALL), null);//up
		tBound_circle2 = B2Util.createCircle(world, board_halfheight * 2,
				+SCREEN_WIDTH / 2, SCREEN_WIDTH - board_halfheight,
				BodyType.StaticBody, 0, 0, 0, 0, new BodyData(
						BodyData.BODY_BALL), null);//up
		tBound_circle3 = B2Util.createCircle(world, board_halfheight * 2,
				-SCREEN_WIDTH / 2, -board_halfheight, BodyType.StaticBody, 0,
				0, 0, 0, new BodyData(BodyData.BODY_BALL), null);//down
		tBound_circle4 = B2Util.createCircle(world, board_halfheight * 2,
				+SCREEN_WIDTH / 2, -board_halfheight, BodyType.StaticBody, 0,
				0, 0, 0, new BodyData(BodyData.BODY_BALL), null);//down
		
		setScreenColor();
		setBackgroundColor();
		setBoundColor();
		setControlBackground();
		setSlipeBackground();
		setHead();
		batch = new SpriteBatch();
		texture2 = new Texture(Gdx.files.internal("ball.png"));
		textureup = new Texture(Gdx.files.internal("balldown.png"));
		texturedown = new Texture(Gdx.files.internal("ballup.png"));
		atlas = new TextureAtlas(Gdx.files.internal("data/pack"));
		blockRegion = new TextureRegion(atlas.findRegion("11"));

	}
	private void setScreenColor(){
		float halfwidth = SCREEN_HEIGHT / 2;
		float halfheight = SCREEN_WIDTH / 2;
		float x = tBound4.getPosition().x;
		float y = offset_center;
		screen = new Mesh(true, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		screen.setVertices(new float[] { x - halfheight, y + halfwidth, 0,
				Color.toFloatBits(250, 248, 239, 255), x - halfheight,
				y - halfwidth, 0, Color.toFloatBits(250, 248, 239, 255),
				x + halfheight, y + halfwidth, 0,
				Color.toFloatBits(250, 248, 239, 255), x + halfheight,
				y - halfwidth, 0, Color.toFloatBits(250, 248, 239, 255) });
	}

	private void setBackgroundColor() {
		float halfwidth = SCREEN_WIDTH / 2;
		float halfheight = SCREEN_WIDTH / 2;
		float x = tBound1.getPosition().x;
		float y = tBound2.getPosition().y;
		background = new Mesh(true, 4, 4, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		background.setVertices(new float[] { x - halfheight, y + halfwidth, 0,
				Color.toFloatBits(248, 246, 223, 255), x - halfheight,
				y - halfwidth, 0, Color.toFloatBits(248, 246, 223, 255),
				x + halfheight, y + halfwidth, 0,
				Color.toFloatBits(248, 246, 223, 255), x + halfheight,
				y - halfwidth, 0, Color.toFloatBits(248, 246, 223, 255) });
	}

	private void setControlBackground() {
		float halfwidth = base_width;
		float halfheight =(SCREEN_WIDTH *3)/ 8;
		float x = -SCREEN_WIDTH/8;
		float y = -board_halfheight-base_width/2-base_width;
		controlBackground = new Mesh(true, 4, 4, new VertexAttribute(
				Usage.Position, 3, "a_position"), new VertexAttribute(
				Usage.ColorPacked, 4, "a_color"));
		controlBackground.setVertices(new float[] { x - halfheight*1.2f,
				y + halfwidth, 0, Color.toFloatBits(138, 117, 97, 255),
				x - halfheight*1.2f, y - halfwidth, 0,
				Color.toFloatBits(138, 117, 97, 255), x + halfheight,
				y + halfwidth, 0, Color.toFloatBits(138, 117, 97, 255),
				x + halfheight, y - halfwidth, 0,
				Color.toFloatBits(138, 117, 97, 255) });
	}

	private void setSlipeBackground() {
		float halfwidth = base_width * 3.25f;
		float halfheight = SCREEN_WIDTH / 2;
		float x = tBound1.getPosition().x;
		float y = tBound4.getPosition().y - base_width * 3.25f;
		slipeBackground = new Mesh(true, 4, 4, new VertexAttribute(
				Usage.Position, 3, "a_position"), new VertexAttribute(
				Usage.ColorPacked, 4, "a_color"));
		slipeBackground.setVertices(new float[] { x - halfheight,
				y + halfwidth, 0, Color.toFloatBits(85, 85, 85, 255),
				x - halfheight, y - halfwidth, 0,
				Color.toFloatBits(85, 85, 85, 255), x + halfheight,
				y + halfwidth, 0, Color.toFloatBits(85, 85, 85, 255),
				x + halfheight, y - halfwidth, 0,
				Color.toFloatBits(85, 85, 85, 255) });
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
					0, Color.toFloatBits(187, 173, 160, 255), x - halfheight,
					y - halfwidth, 0, Color.toFloatBits(187, 173, 160, 255),
					x + halfheight, y + halfwidth, 0,
					Color.toFloatBits(187, 173, 160, 255), x + halfheight,
					y - halfwidth, 0, Color.toFloatBits(187, 173, 160, 255) });
		}

		x = tBound2.getPosition().x;
		y = tBound2.getPosition().y;

		if (bound_two == null) {
			bound_two = new Mesh(true, 4, 4, new VertexAttribute(
					Usage.Position, 3, "a_position"), new VertexAttribute(
					Usage.ColorPacked, 4, "a_color"));
			bound_two.setVertices(new float[] { x - halfwidth, y + halfheight,
					0, Color.toFloatBits(187, 173, 160, 255), x - halfwidth,
					y - halfheight, 0, Color.toFloatBits(187, 173, 160, 255),
					x + halfwidth, y + halfheight, 0,
					Color.toFloatBits(187, 173, 160, 255), x + halfwidth,
					y - halfheight, 0, Color.toFloatBits(187, 173, 160, 255) });
		}

		x = tBound3.getPosition().x;
		y = tBound3.getPosition().y;

		if (bound_three == null) {
			bound_three = new Mesh(true, 4, 4, new VertexAttribute(
					Usage.Position, 3, "a_position"), new VertexAttribute(
					Usage.ColorPacked, 4, "a_color"));
			bound_three.setVertices(new float[] { x - halfwidth,
					y + halfheight, 0, Color.toFloatBits(187, 173, 160, 255),
					x - halfwidth, y - halfheight, 0,
					Color.toFloatBits(187, 173, 160, 255), x + halfwidth,
					y + halfheight, 0, Color.toFloatBits(187, 173, 160, 255),
					x + halfwidth, y - halfheight, 0,
					Color.toFloatBits(187, 173, 160, 255) });
		}

		x = tBound4.getPosition().x;
		y = tBound4.getPosition().y;

		if (bound_four == null) {
			bound_four = new Mesh(true, 4, 4, new VertexAttribute(
					Usage.Position, 3, "a_position"), new VertexAttribute(
					Usage.ColorPacked, 4, "a_color"));
			bound_four.setVertices(new float[] { x - halfheight, y + halfwidth,
					0, Color.toFloatBits(187, 173, 160, 255), x - halfheight,
					y - halfwidth, 0, Color.toFloatBits(187, 173, 160, 255),
					x + halfheight, y + halfwidth, 0,
					Color.toFloatBits(187, 173, 160, 255), x + halfheight,
					y - halfwidth, 0, Color.toFloatBits(187, 173, 160, 255) });
		}

	}

	public void setBoundCircle() {
		float x = tBound_circle1.getPosition().x;
		float y = tBound_circle1.getPosition().y;
		batch.draw(this.getTextureUp(), set_x + (x - board_halfheight * 2) * 10,
				set_y - offset_center*10f + (y - board_halfheight * 2) * 10,
				20 * board_halfheight * 2, 20 * board_halfheight * 2);

		x = tBound_circle2.getPosition().x;
		y = tBound_circle2.getPosition().y;
		batch.draw(this.getTextureUp(), set_x + (x - board_halfheight * 2) * 10,
				set_y - offset_center*10f + (y - board_halfheight * 2) * 10,
				20 * board_halfheight * 2, 20 * board_halfheight * 2);

		x = tBound_circle3.getPosition().x;
		y = tBound_circle3.getPosition().y;
		batch.draw(this.getTextureDown(), set_x + (x - board_halfheight * 2) * 10,
				set_y - offset_center*10f + (y - board_halfheight * 2) * 10,
				20 * board_halfheight * 2, 20 * board_halfheight * 2);

		x = tBound_circle4.getPosition().x;
		y = tBound_circle4.getPosition().y;
		batch.draw(this.getTextureDown(), set_x + (x - board_halfheight * 2) * 10,
				set_y - offset_center*10f + (y - board_halfheight * 2) * 10,
				20 * board_halfheight * 2, 20 * board_halfheight * 2);
	}

	public void setHead() {
		float x = tBound1.getPosition().x;
		float y = SCREEN_WIDTH-base_width+board_halfheight;
		float halfwidth = base_width;
		float halfheight = SCREEN_WIDTH / 2;
		head = new Mesh(true, 4, 4, new VertexAttribute(Usage.Position, 3,
				"a_position"), new VertexAttribute(Usage.ColorPacked, 4,
				"a_color"));
		head.setVertices(new float[] { x - halfheight, y + halfwidth, 0,
				Color.toFloatBits(250, 248, 239, 255), x - halfheight,
				y - halfwidth, 0, Color.toFloatBits(250, 248, 239, 255),
				x + halfheight, y + halfwidth, 0,
				Color.toFloatBits(250, 248, 239, 255), x + halfheight,
				y - halfwidth, 0, Color.toFloatBits(250, 248, 239, 255) });

	}

	public World getWorld() {
		return world;
	}
	
	public Mesh getScreen() {
		return screen;
	}

	public Mesh getBackground() {
		return background;
	}

	public Mesh getControlBackground() {
		return controlBackground;
	}

	public Mesh getSlipeBackground() {
		return slipeBackground;
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

	public Mesh getHead() {
		return head;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public Texture getTextureUp() {
		return textureup;
	}
	
	public Texture getTextureDown() {
		return texturedown;
	}
	
	public Texture getTexture2() {
		return texture2;
	}

	public TextureRegion getBlockTexture(int type) {
		if (type < 430 && type > 420) {
			blockRegion = new TextureRegion(atlas.findRegion(String
					.valueOf(type)));
		} else if (type < 450 && type > 430) {
			//System.out.println(CONTROL_ID);
			//System.out.println(CONTROL_ID * 100 + type - 400);
			if((CONTROL_ID * 100 + type - 400)==241){
				blockRegion = new TextureRegion(atlas.findRegion(String
						.valueOf(100 + type - 400)));
			}
			else{
			blockRegion = new TextureRegion(atlas.findRegion(String
					.valueOf(CONTROL_ID * 100 + type - 400)));
			}
			
		} else {
			blockRegion = new TextureRegion(atlas.findRegion(String
					.valueOf(type)));
		}
		return blockRegion;
	}

}
