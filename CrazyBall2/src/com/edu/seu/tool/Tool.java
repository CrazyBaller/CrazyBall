package com.edu.seu.tool;

import com.edu.seu.message.Data;

public class Tool {

	public int judge(int id){
		
		int number=0;
		for(int i=0;i<Data.mode;i++){
			if(Data.state.get(i)==3)
				number++;
			}
		if(number+1==Data.mode)
		return 4;
		else
		return 3;
	}

}
