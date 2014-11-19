package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;
import java.util.Timer;
import java.util.TimerTask;

public class ChangeSensor {
	public void start() {
		canTouching = true;

		timeVoid();
	}

	public void timeVoid() {

		final Timer timer = new Timer();
		TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				canTouching = false;

				timer.cancel();
			}
		};
		timer.schedule(tt, 10000);
	}
}
