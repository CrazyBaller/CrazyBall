package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;
import java.util.Timer;
import java.util.TimerTask;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.edu.seu.message.Data;

public class ChangeBoard {
	private PolygonShape shapeRect;
	private int id;
	private Vector2 vertex;
	private Fixture boardFixture;

	public ChangeBoard(int num) {
		this.id = num;
	}
	public boolean ifvalidate(int n){
		if(Data.state.get(n)!=3)
			return true;
		else 
			return false;
	}

	// 执行change
	public void start(int changeType) {
		if(ifvalidate(this.id)){
			if (changeType == 31) {
				this.toLonger();
			} else if (changeType == 32) {
				this.toShorter();
			} else if (changeType == 33) {
				this.toSpring();
			} else if (changeType == 34) {
				this.toWood();
			} else {
			}
			timeVoid();
		}
	}

	public void timeVoid() {

		final Timer timer = new Timer();
		BOARD_HIT++;
		final int i_local = BOARD_HIT;

		TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				if (i_local == BOARD_HIT) {
					toNormal(id);

				}

				timer.cancel();

			}

		};
		timer.schedule(tt, 1000);
	}

	protected void toNormal(int i) {
		// TODO Auto-generated method stub
		switch (i) {
		case 0:
			boardFixture = tBoard0.getFixtureList().get(0);
			shapeRect = (PolygonShape) boardFixture.getShape();
			shapeRect.setAsBox(board_halfwidth, board_halfheight);
			boardFixture.setRestitution(1.0f);
			board_halfwidth0 = board_halfwidth;
			break;
		case 1:
			boardFixture = tBoard1.getFixtureList().get(0);
			shapeRect = (PolygonShape) boardFixture.getShape();
			shapeRect.setAsBox(board_halfwidth, board_halfheight);
			boardFixture.setRestitution(1.0f);
			board_halfwidth1 = board_halfwidth;
			break;
		case 2:
			boardFixture = tBoard2.getFixtureList().get(0);
			shapeRect = (PolygonShape) boardFixture.getShape();
			shapeRect.setAsBox(board_halfwidth, board_halfheight);
			boardFixture.setRestitution(1.0f);
			board_halfwidth2 = board_halfwidth;
			break;
		case 3:
			boardFixture = tBoard3.getFixtureList().get(0);
			shapeRect = (PolygonShape) boardFixture.getShape();
			shapeRect.setAsBox(board_halfwidth, board_halfheight);
			boardFixture.setRestitution(1.0f);
			board_halfwidth3 = board_halfwidth;
			break;
		default:
			return;
		}
	}

	// 被动性能改变（板的属性发生改变）
	public void toLonger() {
		switch (id) {
		case 0:
			shapeRect = (PolygonShape) tBoard0.getFixtureList().get(0)
					.getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if (vertex.x > board_halfwidth) {
				vertex.x = (float) (vertex.x * 1.2);
				shapeRect.setAsBox((float) (vertex.x * 1.2), board_halfheight);
				board_halfwidth0 = (float) (vertex.x * 1.2);
			} else {
				shapeRect.setAsBox((float) (board_halfwidth * 1.2),
						board_halfheight);
				board_halfwidth0 = (float) (board_halfwidth * 1.2);
			}
			break;
		case 1:
			shapeRect = (PolygonShape) tBoard1.getFixtureList().get(0)
					.getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if (vertex.x > board_halfwidth) {
				vertex.x = (float) (vertex.x * 1.2);
				shapeRect.setAsBox((float) (vertex.x * 1.2), board_halfheight);
				board_halfwidth1 = (float) (vertex.x * 1.2);
			} else {
				shapeRect.setAsBox((float) (board_halfwidth * 1.2),
						board_halfheight);
				board_halfwidth1 = (float) (board_halfwidth * 1.2);
			}
			break;
		case 2:
			shapeRect = (PolygonShape) tBoard2.getFixtureList().get(0)
					.getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if (vertex.x > board_halfwidth) {
				vertex.x = (float) (vertex.x * 1.2);
				shapeRect.setAsBox((float) (vertex.x * 1.2), board_halfheight);
				board_halfwidth2 = (float) (vertex.x * 1.2);
			} else {
				shapeRect.setAsBox((float) (board_halfwidth * 1.2),
						board_halfheight);
				board_halfwidth2 = (float) (board_halfwidth * 1.2);
			}
			break;
		case 3:
			shapeRect = (PolygonShape) tBoard3.getFixtureList().get(0)
					.getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if (vertex.x > board_halfwidth) {
				vertex.x = (float) (vertex.x * 1.2);
				shapeRect.setAsBox((float) (vertex.x * 1.2), board_halfheight);
				board_halfwidth3 = (float) (vertex.x * 1.2);
			} else {
				shapeRect.setAsBox((float) (board_halfwidth * 1.2),
						board_halfheight);
				board_halfwidth3 = (float) (board_halfwidth * 1.2);
			}
			break;
		default:
			return;
		}
	}

	public void toShorter() {
		switch (id) {
		case 0:
			shapeRect = (PolygonShape) tBoard0.getFixtureList().get(0)
					.getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if (vertex.x < board_halfwidth) {
				vertex.x = (float) (vertex.x / 1.2);
				shapeRect.setAsBox((float) (vertex.x / 1.2), board_halfheight);
				board_halfwidth0 = (float) (vertex.x / 1.2);
			} else {
				shapeRect.setAsBox((float) (board_halfwidth / 1.2),
						board_halfheight);
				board_halfwidth0 = (float) (board_halfwidth / 1.2);
			}
			break;
		case 1:
			shapeRect = (PolygonShape) tBoard1.getFixtureList().get(0)
					.getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if (vertex.x < board_halfwidth) {
				vertex.x = (float) (vertex.x / 1.2);
				shapeRect.setAsBox((float) (vertex.x / 1.2), board_halfheight);
				board_halfwidth1 = (float) (vertex.x / 1.2);
			} else {
				shapeRect.setAsBox((float) (board_halfwidth / 1.2),
						board_halfheight);
				board_halfwidth1 = (float) (board_halfwidth / 1.2);
			}
			break;
		case 2:
			shapeRect = (PolygonShape) tBoard2.getFixtureList().get(0)
					.getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if (vertex.x < board_halfwidth) {
				vertex.x = (float) (vertex.x / 1.2);
				shapeRect.setAsBox((float) (vertex.x / 1.2), board_halfheight);
				board_halfwidth0 = (float) (vertex.x / 1.2);
			} else {
				shapeRect.setAsBox((float) (board_halfwidth / 1.2),
						board_halfheight);
				board_halfwidth2 = (float) (board_halfwidth / 1.2);
			}
			break;
		case 3:
			shapeRect = (PolygonShape) tBoard3.getFixtureList().get(0)
					.getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if (vertex.x < board_halfwidth) {
				vertex.x = (float) (vertex.x / 1.2);
				shapeRect.setAsBox((float) (vertex.x / 1.2), board_halfheight);
				board_halfwidth3 = (float) (vertex.x / 1.2);
			} else {
				shapeRect.setAsBox((float) (board_halfwidth / 1.2),
						board_halfheight);
				board_halfwidth3 = (float) (board_halfwidth / 1.2);
			}
			break;
		default:
			return;
		}
	}

	public void toSpring() {
		switch (id) {
		case 0:
			tBoard0.getFixtureList().get(0).setRestitution(1.0f); // 回复力==1
			break;
		case 1:
			tBoard1.getFixtureList().get(0).setRestitution(1.0f); // 回复力==1
			break;
		case 2:
			tBoard2.getFixtureList().get(0).setRestitution(1.0f); // 回复力==1
			break;
		case 3:
			tBoard3.getFixtureList().get(0).setRestitution(1.0f); // 回复力==1
			break;
		default:
			return;
		}
	}

	public void toWood() {
		switch (id) {
		case 0:
			tBoard0.getFixtureList().get(0).setRestitution(0.5f);
			break;
		case 1:
			tBoard1.getFixtureList().get(0).setRestitution(0.5f);
			break;
		case 2:
			tBoard2.getFixtureList().get(0).setRestitution(0.5f);
			break;
		case 3:
			tBoard3.getFixtureList().get(0).setRestitution(0.5f);
			break;
		default:
			return;
		}
	}
}
