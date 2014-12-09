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
<<<<<<< HEAD
import com.edu.seu.crazyball2.CreateWorld;
import com.edu.seu.crazyball2.FourMode;
import com.edu.seu.crazyball2.FourModeClient;
import com.edu.seu.crazyball2.SoloMode;
import com.edu.seu.crazyball2.ThreeMode;
import com.edu.seu.crazyball2.ThreeModeClient;
import com.edu.seu.crazyball2.TwoMode;
import com.edu.seu.crazyball2.TwoModeClient;
import com.edu.seu.message.Data;
=======
>>>>>>> 797c9de4e3e95ae95391bc227a12d6015633be60

public class ChangeSensor {
	ImageButton addButton;
	public void start() {
<<<<<<< HEAD
		canTouching = true;
		addButton=new ImageButton(new TextureRegionDrawable(CreateWorld.getBlockHole()));
    	addButton.setPosition(set_x+ (0 - base_width * 3) * 10f, set_y - offset_center*10f+ (SCREEN_WIDTH / 2 - base_width * 3) * 10f);
    	addButton.setSize(base_width * 40, base_width * 40);
    	RepeatAction rotateToAction = Actions.repeat(5, Actions.rotateTo(1440f, 10.0f));
    	addButton.getImage().setOrigin(addButton.getWidth()/2, addButton.getHeight()/2); 
    	addButton.getImage().addAction(rotateToAction);
    	addButton.getImage().setFillParent(true);
    	if(Data.mode == 1){
    		SoloMode.propsbar.getStage().addActor(addButton);
    	}
    	else if(Data.mode == 2){
    		if(Data.myID == 0){
    			TwoMode.propsbar.getStage().addActor(addButton);
    		}
    		else {
				TwoModeClient.propsbar.getStage().addActor(addButton);
			}
    	}
    	else if (Data.mode == 3) {
    		if(Data.myID == 0){
    			ThreeMode.propsbar.getStage().addActor(addButton);
    		}
    		else {
				ThreeModeClient.propsbar.getStage().addActor(addButton);
			}
		}
    	else {
    		if(Data.myID == 0){
    			FourMode.propsbar.getStage().addActor(addButton);
    		}
    		else {
				FourModeClient.propsbar.getStage().addActor(addButton);
			}
		}
    	//stage.addActor(addButton);
=======
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
>>>>>>> 797c9de4e3e95ae95391bc227a12d6015633be60
		timeVoid();
	}

	public void timeVoid() {

		final Timer timer = new Timer();
		TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				canTouching = false;
<<<<<<< HEAD
		    	if(Data.mode == 1){
		    		SoloMode.propsbar.getStage().getRoot().removeActor(addButton);
		    	}
		    	else if(Data.mode == 2){
		    		if(Data.myID == 0){
		    			TwoMode.propsbar.getStage().getRoot().removeActor(addButton);
		    		}
		    		else {
						TwoModeClient.propsbar.getStage().getRoot().removeActor(addButton);
					}
		    	}
		    	else if (Data.mode == 3) {
		    		if(Data.myID == 0){
		    			ThreeMode.propsbar.getStage().getRoot().removeActor(addButton);
		    		}
		    		else {
						ThreeModeClient.propsbar.getStage().getRoot().removeActor(addButton);
					}
				}
		    	else {
		    		if(Data.myID == 0){
		    			FourMode.propsbar.getStage().getRoot().removeActor(addButton);
		    		}
		    		else {
						FourModeClient.propsbar.getStage().getRoot().removeActor(addButton);
					}
				}
				//stage.getRoot().removeActor(addButton);
=======
				stage.getRoot().removeActor(addButton);
>>>>>>> 797c9de4e3e95ae95391bc227a12d6015633be60
				timer.cancel();
			}
		};
		timer.schedule(tt, 10000);
	}
}
