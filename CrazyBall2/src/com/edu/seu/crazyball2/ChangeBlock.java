package com.edu.seu.crazyball2;

import static com.edu.seu.crazyball2.Constant.*;
import java.util.Timer;
import java.util.TimerTask;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class ChangeBlock {
	private PolygonShape shapeRect;
	private int id;
	private Vector2 vertex;
	private Fixture boardFixture;

	public ChangeBlock(int num){
		this.id=num;
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
            		toNormal(id);
            		System.out.println("到点啦！");
            	}

                timer.cancel();

            }

        };
        timer.schedule(tt, 10000);
    }
	protected void toNormal(int i) {
		// TODO Auto-generated method stub
		switch(i){
		case 0:
			boardFixture=tBoard0.getFixtureList().get(0);
			shapeRect = (PolygonShape) boardFixture.getShape();
			shapeRect.setAsBox(board_halfwidth, board_halfheight);
			boardFixture.setRestitution(1.0f);	
		case 1:
			boardFixture=tBoard1.getFixtureList().get(0);
			shapeRect = (PolygonShape) boardFixture.getShape();
			shapeRect.setAsBox(board_halfwidth, board_halfheight);
			boardFixture.setRestitution(1.0f);	
		case 2:
			boardFixture=tBoard2.getFixtureList().get(0);
			shapeRect = (PolygonShape) boardFixture.getShape();
			shapeRect.setAsBox(board_halfwidth, board_halfheight);
			boardFixture.setRestitution(1.0f);	
		case 3:
			boardFixture=tBoard3.getFixtureList().get(0);
			shapeRect = (PolygonShape) boardFixture.getShape();
			shapeRect.setAsBox(board_halfwidth, board_halfheight);
			boardFixture.setRestitution(1.0f);	
		default:
			return;
		}		
	}

	//被动性能改变（板的属性发生改变）
	public void toLonger(){
		switch(id){
		case 0:
			shapeRect = (PolygonShape) tBoard0.getFixtureList().get(0).getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if(vertex.x>board_halfwidth){
				vertex.x = (float) (vertex.x*1.2);
				shapeRect.setAsBox((float) (vertex.x*1.2), board_halfheight);
				board_halfwidth0=(float) (vertex.x*1.2);
			}
			else{
				shapeRect.setAsBox((float) (board_halfwidth*1.2), board_halfheight);
				board_halfwidth0=(float) (board_halfwidth*1.2);
			}
			break;
		case 1:
			shapeRect = (PolygonShape) tBoard1.getFixtureList().get(0).getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if(vertex.x>board_halfwidth){
				vertex.x = (float) (vertex.x*1.2);
				shapeRect.setAsBox((float) (vertex.x*1.2), board_halfheight);
				board_halfwidth1=(float) (vertex.x*1.2);
			}
			else{
				shapeRect.setAsBox((float) (board_halfwidth*1.2), board_halfheight);
				board_halfwidth1=(float) (board_halfwidth*1.2);
			}
			break;
		case 2:
			shapeRect = (PolygonShape) tBoard2.getFixtureList().get(0).getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if(vertex.x>board_halfwidth){
				vertex.x = (float) (vertex.x*1.2);
				shapeRect.setAsBox((float) (vertex.x*1.2), board_halfheight);
				board_halfwidth2=(float) (vertex.x*1.2);
			}
			else{
				shapeRect.setAsBox((float) (board_halfwidth*1.2), board_halfheight);
				board_halfwidth2=(float) (board_halfwidth*1.2);
			}
			break;
		case 3:
			shapeRect = (PolygonShape) tBoard3.getFixtureList().get(0).getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if(vertex.x>board_halfwidth){
				vertex.x = (float) (vertex.x*1.2);
				shapeRect.setAsBox((float) (vertex.x*1.2), board_halfheight);
				board_halfwidth3=(float) (vertex.x*1.2);
			}
			else{
				shapeRect.setAsBox((float) (board_halfwidth*1.2), board_halfheight);
				board_halfwidth3=(float) (board_halfwidth*1.2);
			}
			break;
		default:
			return;
		}
	}
	public void toShorter(){
		switch(id){
		case 0:
			shapeRect = (PolygonShape) tBoard0.getFixtureList().get(0).getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if(vertex.x<board_halfwidth){
				vertex.x = (float) (vertex.x/1.2);
				shapeRect.setAsBox((float) (vertex.x/1.2), board_halfheight);
				board_halfwidth0=(float) (vertex.x*1.2);
			}
			else{
				shapeRect.setAsBox((float) (board_halfwidth/1.2), board_halfheight);
				board_halfwidth0=(float) (board_halfwidth*1.2);
			}
			break;
		case 1:
			shapeRect = (PolygonShape) tBoard1.getFixtureList().get(0).getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if(vertex.x<board_halfwidth){
				vertex.x = (float) (vertex.x/1.2);
				shapeRect.setAsBox((float) (vertex.x/1.2), board_halfheight);
				board_halfwidth1=(float) (vertex.x*1.2);
			}
			else{
				shapeRect.setAsBox((float) (board_halfwidth/1.2), board_halfheight);
				board_halfwidth1=(float) (board_halfwidth*1.2);
			}
			break;
		case 2:
			shapeRect = (PolygonShape) tBoard2.getFixtureList().get(0).getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if(vertex.x<board_halfwidth){
				vertex.x = (float) (vertex.x/1.2);
				shapeRect.setAsBox((float) (vertex.x/1.2), board_halfheight);
				board_halfwidth0=(float) (vertex.x*1.2);
			}
			else{
				shapeRect.setAsBox((float) (board_halfwidth/1.2), board_halfheight);
				board_halfwidth2=(float) (board_halfwidth*1.2);
			}
			break;
		case 3:
			shapeRect = (PolygonShape) tBoard3.getFixtureList().get(0).getShape();
			vertex = new Vector2();
			shapeRect.getVertex(2, vertex);
			if(vertex.x<board_halfwidth){
				vertex.x = (float) (vertex.x/1.2);
				shapeRect.setAsBox((float) (vertex.x/1.2), board_halfheight);
				board_halfwidth3=(float) (vertex.x*1.2);
			}
			else{
				shapeRect.setAsBox((float) (board_halfwidth/1.2), board_halfheight);
				board_halfwidth3=(float) (board_halfwidth*1.2);
			}
			break;
		default:
			return;
		}
		board_halfwidth0=(float) (board_halfwidth/1.2);
		Vector2 vertex = new Vector2();
		shapeRect.getVertex(2, vertex);
		if(vertex.x<board_halfwidth){
			vertex.x = (float) (vertex.x/1.2);
			shapeRect.setAsBox((float) (vertex.x/1.2), board_halfheight);
		}
		else{
			shapeRect.setAsBox((float) (board_halfwidth/1.2), board_halfheight);
		}
	}
	public void toSpring(){
		switch(id){
		case 0:
			 tBoard0.getFixtureList().get(0).setRestitution(1.0f);  //回复力==1
			 break;
		case 1:
			 tBoard1.getFixtureList().get(0).setRestitution(1.0f);  //回复力==1
			 break;
		case 2:
			 tBoard2.getFixtureList().get(0).setRestitution(1.0f);  //回复力==1
			 break;
		case 3:
			 tBoard3.getFixtureList().get(0).setRestitution(1.0f);  //回复力==1
			 break;
		default:
			return;
		}	
	}
	public void toWood(){
		switch(id){
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

