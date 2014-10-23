package com.edu.seu.crazyball2;

public class BodyData {
	public static final int BODY_BORDER = 1;
	public static final int BODY_BOTTOM = 2;
	public static final int BODY_BALL = 3;
	public static final int BODY_BOARD = 4;

	private int _type;

	public BodyData(int type) {
		this._type = type;
	}

	public int getType() {
		return this._type;
	}
}

