package com.edu.seu.props;

import static com.edu.seu.crazyball2.Constant.*;
import java.util.Timer;
import java.util.TimerTask;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.edu.seu.crazyball2.BodyData;
import com.edu.seu.message.Data;

public class ChangeBoard {
	private PolygonShape shapeRect;
	private int id;
	private Vector2 vertex;
	private Fixture boardFixture;
	private int changeType;

	public ChangeBoard(int num) {
		this.id = num;
	}

	public boolean ifvalidate(int n) {
		if (Data.state.get(n) != 3)
			return true;
		else
			return false;
	}

	// 执行change
	public void start(int changeType) {
		this.changeType = changeType;
		if (ifvalidate(this.id)) {
			if (changeType == BodyData.BOARD_TOLONGER) {
				this.toLonger();
			} else if (changeType == BodyData.BOARD_TOSHORTER) {
				this.toShorter();
			} else if (changeType == BodyData.BOARD_DISAPPEAR) {
				this.toDisappear();
			} else if (changeType == BodyData.BOARD_NOCONTROL) {
				this.noControl();
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
					if (changeType == BodyData.BOARD_DISAPPEAR) {
						for (int i = 0; i < 4; i++) {
							showBoard[i] = 1;
						}
					} else if (changeType == BodyData.BOARD_NOCONTROL) {
						move_board = true;
					} else {
						toNormal(id);
					}
				}
				timer.cancel();
			}
		};
		timer.schedule(tt, 3000);
	}

	protected void toNormal(int i) {
		// TODO Auto-generated method stub
		switch (i) {
		case 0:
			boardFixture = tBoard0.getFixtureList().get(0);
			shapeRect = (PolygonShape) boardFixture.getShape();
			shapeRect.setAsBox(board_halfwidth, board_halfheight);
			board_halfwidth0 = board_halfwidth;
			break;
		case 1:
			boardFixture = tBoard1.getFixtureList().get(0);
			shapeRect = (PolygonShape) boardFixture.getShape();
			shapeRect.setAsBox(board_halfwidth, board_halfheight);
			board_halfwidth1 = board_halfwidth;
			break;
		case 2:
			boardFixture = tBoard2.getFixtureList().get(0);
			shapeRect = (PolygonShape) boardFixture.getShape();
			shapeRect.setAsBox(board_halfwidth, board_halfheight);
			board_halfwidth2 = board_halfwidth;
			break;
		case 3:
			boardFixture = tBoard3.getFixtureList().get(0);
			shapeRect = (PolygonShape) boardFixture.getShape();
			shapeRect.setAsBox(board_halfwidth, board_halfheight);
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

	public void toDisappear() {
		switch (id) {
		case 0:
			showBoard[0] = 0;
			break;
		case 1:
			showBoard[1] = 0;
			break;
		case 2:
			showBoard[2] = 0;
			break;
		case 3:
			showBoard[3] = 0;
			break;
		default:
			return;
		}
	}

	public void noControl() {
		move_board = false;
	}

}
