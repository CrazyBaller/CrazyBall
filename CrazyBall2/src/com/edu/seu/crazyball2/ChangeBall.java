package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;
import java.util.Timer;
import java.util.TimerTask;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.edu.seu.message.Data;


public class ChangeBall {
	/*private Fixture fdDef = new Fixture(null, 0);*/
	private CircleShape shapeCircle;
	
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
		shapeCircle = (CircleShape) tBall.getFixtureList().get(0).getShape();
		float OriRadius = shapeCircle.getRadius();
		if(OriRadius>circle_radius_standard){
			shapeCircle.setRadius((float) (OriRadius*1.1));
			circle_radius=(float) (OriRadius*1.1);
			System.out.println("radius:"+circle_radius);
		}
		else{
			shapeCircle.setRadius((float) (circle_radius_standard*1.1));
			circle_radius=(float) (circle_radius_standard*1.1);
			System.out.println("radius:"+circle_radius);
		}	
	}
	public void toSmaller(){
		shapeCircle = (CircleShape) tBall.getFixtureList().get(0).getShape();
		float OriRadius = shapeCircle.getRadius();
		if(OriRadius<circle_radius_standard){
			shapeCircle.setRadius((float) (OriRadius/1.1));
			circle_radius=(float) (OriRadius/1.1);
			System.out.println("radius:"+circle_radius);
		}
		else{
			shapeCircle.setRadius((float) (circle_radius_standard/1.1));
			circle_radius=(float) (circle_radius_standard/1.1);
			System.out.println("radius:"+circle_radius);
		}
	}
  	public void toFaster(){
  		if(Data.myID==0){
  			float x=tBall.getLinearVelocity().x;
  			float y=tBall.getLinearVelocity().y;
  			tBall.setLinearVelocity(x*1.1f, y*1.1f);			
  		}
	}
	public void toSlower(){
		if(Data.myID==0){
  			float x=tBall.getLinearVelocity().x;
  			float y=tBall.getLinearVelocity().y;
  			tBall.setLinearVelocity(x/1.1f, y/1.1f);			
  		}
		
	}  
	public void toNormal(){
		shapeCircle = (CircleShape) tBall.getFixtureList().get(0).getShape();
		shapeCircle.setRadius(circle_radius_standard);
		if(Data.myID==0){
  			float x=tBall.getLinearVelocity().x;
  			float y=tBall.getLinearVelocity().y;
  			tBall.setLinearVelocity(20f, 20f*y/x);			
  		}
	}
}
