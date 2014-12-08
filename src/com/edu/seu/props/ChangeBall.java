package com.edu.seu.props;

import static com.edu.seu.crazyball2.Constant.*;

import java.util.Timer;
import java.util.TimerTask;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.edu.seu.crazyball2.BodyData;
import com.edu.seu.message.Data;

public class ChangeBall {
	private CircleShape shapeCircle;
	private int changeType;

	// 执行change
	public void start(int changeType) {
		this.changeType = changeType;
		if (changeType == BodyData.BALL_TOBIGGER) {
			this.toBigger();
			//warningSound.play(30);
		} else if (changeType == BodyData.BALL_TOSMALLER) {
			this.toSmaller();
			//warningSound.play(30);
		} else if (changeType == BodyData.BALL_TOFASTTER) {
			this.toFaster();
			//warningSound.play(30);
		} else if (changeType == BodyData.BALL_TOSLOWER) {
			this.toSlower();
			//warningSound.play(30);
		}
		timeVoid();
	}

	public void timeVoid() {

		final Timer timer = new Timer();
		BALL_HIT++;
		final int i_local = BALL_HIT;

		TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				if (i_local == BALL_HIT) {
					if (changeType == BodyData.BALL_TOBIGGER
							|| changeType == BodyData.BALL_TOSMALLER) {
						shapetoNormal();
					} else {
						speedtoNormal();
					}

				}

				timer.cancel();
			}

		};
		timer.schedule(tt, 5000);
	}

	// 属性改变函数
	// 主动性能改变（改变小球的属性）
	public void toBigger() {
		shapeCircle = (CircleShape) tBall.getFixtureList().get(0).getShape();
		float OriRadius = shapeCircle.getRadius();
		if (OriRadius > circle_radius) {
			shapeCircle.setRadius((float) (OriRadius * 1.2));
		} else {
			shapeCircle.setRadius((float) (circle_radius * 1.2));
		}
	}

	public void toSmaller() {
		shapeCircle = (CircleShape) tBall.getFixtureList().get(0).getShape();
		float OriRadius = shapeCircle.getRadius();
		if (OriRadius < circle_radius) {
			shapeCircle.setRadius((float) (OriRadius / 1.1));
		} else {
			shapeCircle.setRadius((float) (circle_radius / 1.1));
		}
	}

	public void toFaster() {
		if (Data.myID == 0) {
			float x = tBall.getLinearVelocity().x;
			float y = tBall.getLinearVelocity().y;
			tBall.setLinearVelocity(x * 2.0f, y * 2.0f);
		}
	}

	public void toSlower() {
		if (Data.myID == 0) {
			float x = tBall.getLinearVelocity().x;
			float y = tBall.getLinearVelocity().y;
			tBall.setLinearVelocity(x / 1.5f, y / 1.5f);
		}

	}

	public void shapetoNormal() {
		shapeCircle = (CircleShape) tBall.getFixtureList().get(0).getShape();
		shapeCircle.setRadius(circle_radius_standard);
	}

	public void speedtoNormal() {
		if (Data.myID == 0) {
			float x = tBall.getLinearVelocity().x;
			float y = tBall.getLinearVelocity().y;
			float multiple=(float) (SCREEN_WIDTH/(Math.sqrt(x*x+y*y)));
			tBall.setLinearVelocity(multiple*x, multiple*y);
		}
	}
}
