package com.edu.seu.crazyball2;

public class BodyData {

	// 设置body的属性类型
	public static final int BODY_BORDER_UP = 1;
	public static final int BODY_BALL = 2;
	public static final int BODY_BLOCK = 3;
	public static final int BODY_SENSOR = 4;
	public static final int BODY_BORDER_LEFT = 5;
	public static final int BODY_BORDER_RIGHT = 6;
	public static final int BODY_BORDER_BOTTOM = 7;
	public static final int BODY_BOARD =6;

	// 设置砖块的被动属性(改变板的属性)
	public static final int BLOCK_TOLONGER = 31;
	public static final int BLOCK_TOSHORTER = 32;
	public static final int BLOCK_TOSPRING = 33;
	public static final int BLOCK_TOWOOD = 34;

	// 设置砖块的主动属性(改变球的属性)
	public static final int BALL_TOBIGGER = 21;
	public static final int BALL_TOSMALLER = 22;
	public static final int BALL_TOFASTTER = 23;
	public static final int BALL_TOSLOWER = 24;

	private int _type;
	private int _changeType;
	public int health = 100;// 设置显示状态
	public int color;// 设置颜色

	public BodyData(int type) {
		this._type = type;
	}

	public BodyData(int type, int changeType) {
		this._type = type;
		this._changeType = changeType;
	}

	public int getType() {
		return this._type;
	}

	public int getchangeType() {
		return this._changeType;
	}
}
