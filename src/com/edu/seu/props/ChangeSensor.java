package com.edu.seu.props;

import static com.edu.seu.crazyball2.Constant.*;

import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ChangeSensor {
	ImageButton addButton;
	public void start() {
		canTouching = true;		
		addButton=new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/blackhole.png")))));
    	addButton.setPosition(set_x+ (0 - base_width * 3) * 10f, set_y - offset_center*10f+ (SCREEN_WIDTH / 2 - base_width * 3) * 10f);
    	addButton.setSize(base_width * 40, base_width * 40);
    	RepeatAction rotateToAction = Actions.repeat(5, Actions.rotateTo(720f, 5.0f));
    	addButton.getImage().setOrigin(addButton.getWidth()/2, addButton.getHeight()/2); 
    	addButton.getImage().addAction(rotateToAction);
    	addButton.getImage().setFillParent(true);
    	stage.addActor(addButton);
		//warningSound.play(30);
		timeVoid();
	}

	public void timeVoid() {

		final Timer timer = new Timer();
		TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				canTouching = false;
				stage.getRoot().removeActor(addButton);
				timer.cancel();
			}
		};
		timer.schedule(tt, 5000);
	}
}
