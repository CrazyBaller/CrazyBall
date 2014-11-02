package com.edu.seu.props;

import java.util.ArrayList;
import java.util.Observable;

import com.edu.seu.message.Data;

import static com.edu.seu.crazyball2.Constant.*;

public class PropsObservable extends Observable {

	public void setChange(int type,int id){
		ArrayList<Integer> data=new ArrayList<Integer>();
		data.add(type);
		data.add(id);
		setChanged();
	    notifyObservers(data);
	}
}
