package com.edu.seu.props;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.edu.seu.crazyball2.ChangeBall;
import com.edu.seu.crazyball2.ChangeBoard;
import com.edu.seu.crazyball2.ChangeSensor;

public class PropsObserver implements Observer {
	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable arg0, Object data) {
		System.out
				.println("                                       observer updata");
		// TODO Auto-generated method stub
		/*
		 * 31 boardtolonger 32 boardtoshoeter 33 boardtospring 34 boardtowood 35
		 * boardtonormal20 balltonormaal 21 balltotobigger 22 balltosmaller 23
		 * balltofaster 24 balltoslower 25 balltonormal
		 */
		int type = ((ArrayList<Integer>) data).get(0);
		int id = ((ArrayList<Integer>) data).get(1);
		if (type > 30 && type < 35) {
			ChangeBoard cb = new ChangeBoard(id);
			cb.start(type);
		}
		if (type > 20 && type < 25) {
			ChangeBall cbl = new ChangeBall();
			cbl.start(type);
		}
		if (type == 41) {
			ChangeSensor cs = new ChangeSensor();
			cs.start();
		}
	}

}
