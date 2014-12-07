package com.edu.seu.crazyball2;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.edu.seu.message.Data;
import com.edu.seu.props.PropsObservable;
import static com.edu.seu.crazyball2.Constant.base_width;

public class PropsBar {

	Texture texture;
	ArrayList<Button> buttonarray = new ArrayList<Button>();
	ArrayList<Integer> buttonType = new ArrayList<Integer>();
	ImageButton addButton;
	ImageButton selectpeople;
	ImageButton button;

	SequenceAction sequencemove;
	ParallelAction parallelAction;
	MoveToAction move1;
	MoveToAction move2;
	MoveToAction move3;
	MoveToAction move4;
	MoveToAction movetempAction;
	MoveToAction delete1;
	MoveToAction delete2;
	MoveToAction delete3;
	ScaleToAction scaleToAction;

	float standy;
	float buttonbuttom;
	float buttonup;
	float buttonsize;
	float deltaX;	
	
	static int touchnumber = 3;

	Action endAction;
	Stage stage;
	private PropsObservable po;
	
	private int clock = 0;

	public Stage getStage() {
		return stage;
	}

	public PropsBar(PropsObservable p) {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				true);
		this.po=p;
		
		standy=(Gdx.graphics.getHeight()-Gdx.graphics.getWidth())/7f;
		buttonbuttom = standy*1.97f;
		buttonup = buttonbuttom+20f;
		buttonsize=standy*1.7f;
		deltaX=standy*0.4f;
		
		defmove();
		if(Data.mode!=1)
		defselectpeople();
		
		
	}
	
	public void showselectpeople(){
		if(touchnumber!=3){
			stage.addActor(selectpeople);
		}else {
			selectpeople.remove();
		}
	}

	   public void defselectpeople(){
       	if(Data.mode==2){
       		selectpeople=new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("people2.png")))));
       	}else if(Data.mode==3){
       		selectpeople=new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("people3.png")))));
       	}else if(Data.mode==4){
       		
       		selectpeople=new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("people4.png")))));
       	}
 
       	selectpeople.setPosition(Gdx.graphics.getWidth()/4,buttonbuttom+60f);

       	selectpeople.addListener(new InputListener() {
       		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
       			
       			if(Data.mode==2){
       				
       				if(x<selectpeople.getWidth()/2-deltaX){
           				selectnotify(0);
           				
           			}else{
           				selectnotify(1);
           			}
       			}else if(Data.mode==3){
       				if(x<selectpeople.getWidth()/4-deltaX){
           				selectnotify(0);
           				
           			}else if(x<selectpeople.getWidth()/2-deltaX){
           				selectnotify(1);
           			}else if(x<3*selectpeople.getWidth()/4-deltaX){
           				selectnotify(2);
           			}
       				
       			}else if(Data.mode==4){
       				
       				if(x<selectpeople.getWidth()/4-deltaX){
           				selectnotify(0);
           				
           			}else if(x<selectpeople.getWidth()/2-deltaX){
           				selectnotify(1);
           			}else if(x<3*selectpeople.getWidth()/4-deltaX){
           				selectnotify(2);
           			}else {
           				selectnotify(3);
           				
           			}
       			}
       			return true;
       		}

			});
       }
       
       void selectnotify(int id){
       	int type=0;
       	if(touchnumber==0){
       		type=buttonType.get(0);
       		po.setChange(buttonType.get(0), id);
				deletebutton(0);
				touchnumber=3;
			}else if(touchnumber==1){
				type=buttonType.get(1);
				po.setChange(buttonType.get(1), id);
				deletebutton(1);
				touchnumber=3;
			}else if(touchnumber==2){
				type=buttonType.get(2);
				po.setChange(buttonType.get(2), id);
				deletebutton(2);
				touchnumber=3;
			}
       }
       
       public void defmove(){
       	
       	move1=Actions.moveTo(Gdx.graphics.getWidth(), buttonbuttom,0.3f);
			move2=Actions.moveTo(Gdx.graphics.getWidth()/2f-deltaX, buttonbuttom,0.3f);			
			move3=Actions.moveTo(Gdx.graphics.getWidth()/4f-deltaX, buttonbuttom,0.3f);
			move4=Actions.moveTo(-deltaX, buttonbuttom,0.3f);		
			
			delete1 = Actions.moveTo(Gdx.graphics.getWidth()/2f-deltaX, Gdx.graphics.getWidth()/2,0.3f);
			delete2 = Actions.moveTo(Gdx.graphics.getWidth()/4f-deltaX, Gdx.graphics.getWidth()/2,0.3f);
			delete3 = Actions.moveTo(0, Gdx.graphics.getWidth()/2-deltaX,0.3f);
			
			scaleToAction =Actions.scaleTo(0.1f, 0.1f,0.2f);
			
       }
       
       public void addbutton(int type)
       {
    	  if(clock==1)
    		  return;
    		   button=new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("p1.png")))));
    	   
       	
       	button.setSize(buttonsize,buttonsize);
			button.setPosition(-Gdx.graphics.getWidth()/4,buttonbuttom);
			//button.getImage().scale(Gdx.graphics.getWidth()/256);
			button.getImage().setFillParent(true);
		
			button.addListener(new InputListener() {
       		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
       			
       			System.out.println("x="+event.getStageX()+"world"+Gdx.graphics.getWidth()/4);
       			if(event.getStageX() < Gdx.graphics.getWidth()/4-deltaX)
       			{
       				if(Data.mode==1){
       					
       					po.setChange(buttonType.get(2), 0);
       					deletebutton(2);
       					
       				}else
       				//deletebutton(2);
       				{
       					if(touchnumber!=2)
       				{
       					touchnumber=2;
       					buttonarray.get(0).setPosition(Gdx.graphics.getWidth()/2-deltaX, buttonbuttom);
       					buttonarray.get(1).setPosition(Gdx.graphics.getWidth()/4-deltaX, buttonbuttom);
       					buttonarray.get(2).setPosition(-deltaX, buttonup);
       					
       				}else{
       					touchnumber=3;
       					buttonarray.get(2).setPosition(-deltaX, buttonbuttom);
       				}
       				}
       			}else if(event.getStageX()<Gdx.graphics.getWidth()/2-deltaX){
       				//deletebutton(1);
       				if(Data.mode==1){
       					
       					po.setChange(buttonType.get(1), 0);
       					deletebutton(1);
       					
       				}
       				else{
       				if(touchnumber!=1){
       					touchnumber=1;
       				if(buttonarray.size()==2){
       					buttonarray.get(0).setPosition(Gdx.graphics.getWidth()/2-deltaX, buttonbuttom);
       					buttonarray.get(1).setPosition(Gdx.graphics.getWidth()/4-deltaX, buttonup);
       				}else if(buttonarray.size()==3){
       					buttonarray.get(0).setPosition(Gdx.graphics.getWidth()/2-deltaX, buttonbuttom);
       					buttonarray.get(1).setPosition(Gdx.graphics.getWidth()/4-deltaX, buttonup);
       					buttonarray.get(2).setPosition(-deltaX, buttonbuttom);
       				}}else{
       					touchnumber=3;
       					buttonarray.get(1).setPosition(Gdx.graphics.getWidth()/4-deltaX, buttonbuttom);
       				}}
       			}else if(event.getStageX()<3*Gdx.graphics.getWidth()/4-deltaX){
       				
       				//deletebutton(0);
       				if(Data.mode==1){
       					
       					po.setChange(buttonType.get(0), 0);
       					deletebutton(0);
       				}
       				else{
       				if(touchnumber!=0){
       					touchnumber=0;
       				if(buttonarray.size()==3){
       					buttonarray.get(0).setPosition(Gdx.graphics.getWidth()/2-deltaX, buttonup);
       					buttonarray.get(1).setPosition(Gdx.graphics.getWidth()/4-deltaX, buttonbuttom);
       					buttonarray.get(2).setPosition(-deltaX, Gdx.graphics.getWidth()/4);
       				}else if(buttonarray.size()==2){
       					buttonarray.get(0).setPosition(Gdx.graphics.getWidth()/2-deltaX, buttonup);
       					buttonarray.get(1).setPosition(Gdx.graphics.getWidth()/4-deltaX, buttonbuttom);
       					
       				}else if(buttonarray.size()==1){
       					buttonarray.get(0).setPosition(Gdx.graphics.getWidth()/2-deltaX, buttonup);
       				
       				}}else{
       					touchnumber=3;
       					buttonarray.get(0).setPosition(Gdx.graphics.getWidth()/2-deltaX, buttonbuttom);
       				}}
       				
       			}
       			return true;
       		}

			});
			
			
			
			//Ôö¼Óbutton¶¯»­
       	if(buttonarray.size()<3){
       		
   			if(buttonarray.size()==0)
   			{
   				buttonarray.add(button);
   				buttonType.add(type);
   				buttonarray.get(0).addAction(move2);
   				stage.addActor(buttonarray.get(0));
   			}else if(buttonarray.size()==1){

   				buttonarray.add(button);
   				buttonType.add(type);
   				buttonarray.get(1).addAction(move3);
   				stage.addActor(buttonarray.get(1));
   			}else if(buttonarray.size()==2){

   				buttonarray.add(button);
   				buttonType.add(type);
   				buttonarray.get(2).addAction(move4);
   				stage.addActor(buttonarray.get(2));
   			}
       	}else if(buttonarray.size()==3){
       		buttonarray.add(button);
				buttonType.add(type);
				stage.addActor(buttonarray.get(3));
				Action endActiontemp = Actions.run(new Runnable() {
					
					@Override
					public void run() {
						//buttonarray.get(0).remove();
						Button tempbtn = buttonarray.get(0);
						tempbtn.remove();
						buttonarray.remove(0);
						buttonType.remove(0);

						touchnumber=3;
						buttonarray.get(0).addAction(move2);
						buttonarray.get(1).addAction(move3);
						buttonarray.get(2).addAction(move4);
						
						}
				});
				
				SequenceAction sequencemovetemp = Actions.sequence(move1,endActiontemp);
				buttonarray.get(0).addAction(sequencemovetemp);
				
				
				
			
       	}
       	
       	
       }
       
      int deletebutton(int index)
       {
    	  clock=1;
   	   int temp = 0;
   	   if(index==0){
   		   temp=buttonType.get(index);
   		 
   		   
   		   
   		   endAction = Actions.run(new Runnable() {
					
					@Override
					public void run() {
						Button tbtn= buttonarray.get(0);
						tbtn.remove();
						buttonarray.remove(0);
						buttonType.remove(0);
						clock=0;
						if(buttonarray.size()==2){
						buttonarray.get(0).addAction(move2);
						buttonarray.get(1).addAction(move3);
						}
						else if(buttonarray.size()==1){
							buttonarray.get(0).addAction(move2);
						}
												
						}
				});
   		   sequencemove = Actions.sequence(delete1,scaleToAction,endAction);
   		   buttonarray.get(index).addAction(sequencemove);
   		   
   	   }else if(index==1){
   		   temp=buttonType.get(index);
   		 
   		   endAction = Actions.run(new Runnable() {
					
					@Override
					public void run() {
						Button tbtn= buttonarray.get(1);
						tbtn.remove();
						buttonarray.remove(1);
						buttonType.remove(1);
						clock=0;
						if(buttonarray.size()==2){
							buttonarray.get(1).addAction(move3);
						}
						
												
						}
				});
  		   sequencemove = Actions.sequence(delete2,scaleToAction,endAction);
  		   buttonarray.get(index).addAction(sequencemove);
   	   }else if(index==2){
   		   temp=buttonType.get(index);
   		 
   		   endAction = Actions.run(new Runnable() {
					
					@Override
					public void run() {
						Button tbtn= buttonarray.get(2);
						tbtn.remove();
						buttonarray.remove(2);
						buttonType.remove(2);
						clock=0;
						
						}
				});
  		   sequencemove = Actions.sequence(delete3,scaleToAction,endAction);
  		   buttonarray.get(index).addAction(sequencemove);
   	   }
       	return temp;
       }
}
