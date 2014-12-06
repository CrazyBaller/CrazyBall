package com.edu.seu.props;

import java.util.ArrayList;
import java.util.Observable;
import com.edu.seu.message.Data;

public class PropsObservable extends Observable {

	public void setChange(int type, int id) {
		if (type > 30 && type < 35) {
			ArrayList<Integer> data = new ArrayList<Integer>();
			data.add(type);
			data.add(id);
			setChanged();
			notifyObservers(data);
		} else if (Data.state.get(id) == 2) {          //活着才能有主动道具
			ArrayList<Integer> data = new ArrayList<Integer>();
			data.add(type);
			data.add(id);
			setChanged();
			notifyObservers(data);
		}
	}
}
