package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.SCREEN_WIDTH;
import static com.edu.seu.crazyball2.Constant.board_halfheight;
import static com.edu.seu.crazyball2.Constant.boardrate;
import static com.edu.seu.crazyball2.Constant.bound_width;
import android.content.Context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
	private Texture texture2;
	private Texture textureBlock;
	private TextureAtlas atlas;		
	private TextureRegion blockRegion;		

	float board_halfwidth = SCREEN_WIDTH * boardrate;

	public CreateWorld() {
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
		tBound_circle1= B2Util.createCircle(world,board_halfheight*2,-SCREEN_WIDTH/2,SCREEN_WIDTH-board_halfheight, BodyType.StaticBody, 0, 0,0, 0,
				new BodyData(BodyData.BODY_BALL), null);
		tBound_circle2= B2Util.createCircle(world,board_halfheight*2,+SCREEN_WIDTH/2,SCREEN_WIDTH-board_halfheight, BodyType.StaticBody, 0, 0,0, 0,
				new BodyData(BodyData.BODY_BALL), null);
		tBound_circle3= B2Util.createCircle(world,board_halfheight*2,-SCREEN_WIDTH/2, -board_halfheight, BodyType.StaticBody, 0, 0,0, 0,
				new BodyData(BodyData.BODY_BALL), null);
		tBound_circle4= B2Util.createCircle(world,board_halfheight*2,+SCREEN_WIDTH/2, -board_halfheight, BodyType.StaticBody, 0, 0,0, 0,
				new BodyData(BodyData.BODY_BALL), null);
		

		setBoundColor();
		batch = new SpriteBatch();
		texture2 = new Texture(Gdx.files.internal("ball.png"));
		atlas = new TextureAtlas(Gdx.files.internal("data/pack"));		
		blockRegion= new TextureRegion(atlas.findRegion("11"));

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
		}

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
	public Texture getTexture2() {
		return texture2;
	}
	public TextureRegion getBlockTexture(int type){		
		return blockRegion;		
	}

}
