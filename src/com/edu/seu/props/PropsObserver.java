package com.edu.seu.props;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import static com.edu.seu.crazyball2.Constant.isUpdate;
import com.edu.seu.message.Data;

public class PropsObserver implements Observer {
	@SuppressWarnings("unchecked")
	
//	public static final int BALL_TOBIGGER = 31;
//	public static final int BALL_TOSMALLER = 32;
//	public static final int PROPS_FRESH = 33;
//
//	// 设置主动道具
//	public static final int BALL_TOFASTTER = 21;
//	public static final int BALL_TOSLOWER = 22;
//	public static final int BOARD_TOLONGER = 23;
//	public static final int BOARD_TOSHORTER = 24;
//	public static final int BOARD_DISAPPEAR = 25;
//	public static final int BOARD_NOCONTROL = 26;
		
	@Override
	public void update(Observable arg0, Object data) {

		int type = ((ArrayList<Integer>) data).get(0);
		int id = ((ArrayList<Integer>) data).get(1);
		if(type==31||type==32||type == 21 || type==22){   
			ChangeBall cbl = new ChangeBall();
			cbl.start(type);
		}
		if(type==33){
			// 刷新道具
			if(Data.mode!=0&&Data.myID==1){
				isUpdate = true;
			}
		}
		if(type==34){
			ChangeSensor cs = new ChangeSensor();
			cs.start();
		}
		if (type>=23&&type<=26) {
			ChangeBoard cbd = new ChangeBoard(id);
			cbd.start(type);
		}
	}
}
