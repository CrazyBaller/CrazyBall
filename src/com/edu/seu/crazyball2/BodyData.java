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
	public static final int BODY_BOARD = 8;
	public static final int BODY_BOARD0 = 9;
	public static final int BODY_BOARD1 = 10;
	public static final int BODY_BOARD2 = 11;
	public static final int BODY_BOARD3 = 12;

	public static final int left = 10;
	public static final int right = 11;

	// 设置被动道具
	public static final int BALL_TOBIGGER = 31;
	public static final int BALL_TOSMALLER = 32;
	public static final int PROPS_FRESH = 33;
	public static final int DEFY_GRAVITY = 34;
	

	// 设置主动道具
	public static final int BALL_TOFASTTER = 21;
	public static final int BALL_TOSLOWER = 22;
	public static final int BOARD_TOLONGER = 23;
	public static final int BOARD_TOSHORTER = 24;
	public static final int BOARD_DISAPPEAR = 25;
	public static final int BOARD_NOCONTROL = 26;

	private int id;
	private int _type;
	private int _changeType;
	public int health = 100;// 设置显示状态
	public int color;// 设置颜色

	public BodyData(int type) {
		this._type = type;
	}

	// //为道具设计
	public BodyData(int type, int changeType, int id) {
		this._type = type;
		this._changeType = changeType;
		this.id = id;
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

	public int getId() {
		return this.id;
	}
}
