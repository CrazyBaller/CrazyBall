package com.edu.seu.tool;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.R.integer;
import android.R.string;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.edu.seu.message.Data;

public class Tool {

	public int judge(int id) {

		int number = 0;
		for (int i = 0; i < Data.mode; i++) {
			if (Data.state.get(i) == 3)
				number++;
		}
		if (number + 1 == Data.mode)
			return 4;
		else
			return 3;
	}

	public ArrayList<Integer> timeorder() {
		ArrayList<Integer> order = new ArrayList<Integer>();
		for (int i = 0; i < Data.mode; i++)
			order.add(i);

		for (int i = 0; i < Data.mode; i++) {
			for (int j = Data.mode - 1; j > i; j--) {
				if (Data.time.get(order.get(j)) < Data.time.get(order
						.get(j - 1))) {
					int temp = order.get(j);
					order.set(j, order.get(j - 1));
					order.set(j - 1, temp);
				}
			}
		}

		return order;
	}

	public String changetimetoString(int time) {
		int ms = 0;
		int second = 0;
	

		ms = time % 100;
		second = time / 100;


		String result = second+"s" ;
		return result;
	}
	
	public String changetimetoshow(int time){
		int ms = 0;
		int second = 0;
		int minute = 0;

		ms = time % 100;
		time = time / 100;
		second = time % 60;
		time = time / 60;
		minute = time;
		
		String result =null;
		
		if(minute==0)
		{
			result="00:";
		}else if(minute<10){
			result="0"+minute+":";
		}else {
			result=minute+":";
		}
		
		if(second==0)
		{
			result+="00,";
		}else if(second<10){
			result+="0"+second+",";
		}else{
			result+=second+",";
		}
		
		if(ms==0){
			result+="00'";
		}else if(ms<10){
			result+="0"+ms+"'";
		}else {
			result+=ms+"'";
		}
		
		return result;
		
	}

	public Bitmap stringtoBitmap(String imgStr) {
		// 将字符串转换成Bitmap类型

		Bitmap bitmap = null;

		try {

			byte[] bitmapArray;

			bitmapArray = Base64.decode(imgStr, Base64.DEFAULT);

			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,

			bitmapArray.length);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return bitmap;
	}

	public String bitmaptoString(Bitmap bitmap) {

		// 将Bitmap转换成字符串

		String string = null;

		ByteArrayOutputStream bStream = new ByteArrayOutputStream();

		bitmap.compress(CompressFormat.PNG, 100, bStream);

		byte[] bytes = bStream.toByteArray();

		string = Base64.encodeToString(bytes, Base64.DEFAULT);

		return string;

	}

	public String getname(String systemid) {
		if (systemid.equals(Data.mLocalUser.id)) {
			return Data.mLocalUser.name;
		} else {
			for (int i = 0; i < Data.mRemoteUser.size(); i++) {
				if (systemid.equals(Data.mRemoteUser.get(i).id))
					return Data.mRemoteUser.get(i).name;
			}
		}

		return null;
	}

	public int maxtime() {
		ArrayList<Integer> order = timeorder();

		return Data.time.get(0);
	}

	public int mintime() {
		ArrayList<Integer> order = timeorder();

		return Data.time.get(order.size() - 1);
	}

}
