package com.edu.seu.props;

import java.util.ArrayList;
import java.util.Observable;
import com.edu.seu.message.Data;


public class PropsObservable extends Observable {

	public void setChange(int type,int id){
			if(type>20&&type<25){
				ArrayList<Integer> data=new ArrayList<Integer>();
				data.add(type);
				data.add(id);
				setChanged();
			    notifyObservers(data);
			}else if(Data.state.get(id)==2){
				ArrayList<Integer> data=new ArrayList<Integer>();
				data.add(type);
				data.add(id);
				setChanged();
			    notifyObservers(data);
			}
	}
}
