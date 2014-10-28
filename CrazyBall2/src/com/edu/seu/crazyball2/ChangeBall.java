package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;
import java.util.Timer;
import java.util.TimerTask;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;


public class ChangeBall {
	/*private Fixture fdDef = new Fixture(null, 0);*/
	private CircleShape shapeCircle = new CircleShape();
		
	
	ChangeBall(Body tBall){
		/*fdDef = tBall.getFixtureList().get(0);*/
		//获得小球shape
		shapeCircle = (CircleShape) tBall.getFixtureList().get(0).getShape();
	}
	//执行change
    public void start(int changeType) { 
    	if(changeType==21){
    		this.toBigger();
    	}
    	else if(changeType==22){
    		this.toSmaller();
    	}
    	else if(changeType==23){
    		this.toFaster();
    	}
    	else if(changeType==24){
    		this.toSlower();
    	}
    	else{
    		System.out.println(changeType);
    	}
    	   	timeVoid();

    }

    public void timeVoid(){

        final Timer timer = new Timer();
        BALL_HIT++;
        final int i_local = BALL_HIT;

        TimerTask tt=new TimerTask() { 

            @Override

            public void run() {
            	if(i_local==BALL_HIT){
            		toNormal();
            		System.out.println("到点啦！");
            	}

                timer.cancel();
            }

        };
        timer.schedule(tt, 10000);
    }

	
	//属性改变函数
	//主动性能改变（改变小球的属性）
	public void toBigger(){
		float OriRadius = shapeCircle.getRadius();
		if(OriRadius>circle_radius){
			shapeCircle.setRadius((float) (OriRadius*1.2));
		}
		else{
			shapeCircle.setRadius((float) (circle_radius*1.2));
		}
		
	}
	public void toSmaller(){
		float OriRadius = shapeCircle.getRadius();
		if(OriRadius<circle_radius){
			shapeCircle.setRadius((float) (OriRadius/1.1));
		}
		else{
			shapeCircle.setRadius((float) (circle_radius/1.1));
		}
	}
  	public void toFaster(){

	}
	public void toSlower(){
		
	}  
	public void toNormal(){
		shapeCircle.setRadius((float) (circle_radius/1.1));
	}
}
