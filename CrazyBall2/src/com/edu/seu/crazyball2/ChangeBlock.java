package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class ChangeBlock {
	private PolygonShape shapeRect = new PolygonShape();

	ChangeBlock(Body tBlock){
		shapeRect = (PolygonShape) tBlock.getFixtureList().get(0).getShape();
	}
	
	//执行change
    public void start(int changeType) { 
    	if(changeType==31){
    		this.toLonger();
    	}
    	else if(changeType==32){
    		this.toShorter();
    	}
    	else if(changeType==33){
    		this.toSpring();
    	}
    	else if(changeType==34){
    		this.toWood();
    	}
    	else{
    		System.out.println(changeType);
    	}
    	   	timeVoid();

    }

    public void timeVoid(){

        final Timer timer = new Timer();
        BLOCK_HIT++;
        final int i_local = BLOCK_HIT;

        TimerTask tt=new TimerTask() { 

            @Override

            public void run() {
            	if(i_local==BLOCK_HIT){
            		toNormal();
            		System.out.println("到点啦！");
            	}

                timer.cancel();

            }

        };
        timer.schedule(tt, 10000);
    }
	protected void toNormal() {
		// TODO Auto-generated method stub
		shapeRect.setAsBox(board_halfwidth, board_halfheight);
	}

	//被动性能改变（板的属性发生改变）
	public void toLonger(){
		System.out.println("in longer");
		Vector2 vertex = new Vector2();
		System.out.println(shapeRect.getVertexCount());
		shapeRect.getVertex(2, vertex);
		System.out.println(vertex.x);
		System.out.println(vertex.y);
		if(vertex.x>board_halfwidth){
			vertex.x = (float) (vertex.x*1.2);
			shapeRect.setAsBox((float) (vertex.x*1.2), board_halfheight);
		}
		else{
			shapeRect.setAsBox((float) (board_halfwidth*1.2), board_halfheight);
		}
		System.out.println("out longer");
	}
	public void toShorter(){
		System.out.println("in shorter");
		Vector2 vertex = new Vector2();
		System.out.println(shapeRect.getVertexCount());
		//shapeRect.getVertexCount();
		shapeRect.getVertex(2, vertex);
		System.out.println(vertex.x);
		System.out.println(vertex.y);
		if(vertex.x<board_halfwidth){
			vertex.x = (float) (vertex.x/1.2);
			shapeRect.setAsBox((float) (vertex.x/1.2), board_halfheight);
		}
		else{
			shapeRect.setAsBox((float) (board_halfwidth/1.2), board_halfheight);
		}
		System.out.println("out longer");
	}
	public void toSpring(){
		System.out.println("in Spring");
	}
	public void toWood(){
		System.out.println("out wood");
	}
}

