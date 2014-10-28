package com.edu.seu.crazyball2;

public class BodyData {

	// ����body����������
	public static final int BODY_BORDER_UP = 1;
	public static final int BODY_BALL = 2;
	public static final int BODY_BLOCK = 3;
	public static final int BODY_SENSOR = 4;
	public static final int BODY_BORDER_LEFT = 5;
	public static final int BODY_BORDER_RIGHT = 6;
	public static final int BODY_BORDER_BOTTOM = 7;
	public static final int BODY_BOARD =6;

	// ����ש��ı�������(�ı�������)
	public static final int BLOCK_TOLONGER = 31;
	public static final int BLOCK_TOSHORTER = 32;
	public static final int BLOCK_TOSPRING = 33;
	public static final int BLOCK_TOWOOD = 34;

	// ����ש�����������(�ı��������)
	public static final int BALL_TOBIGGER = 21;
	public static final int BALL_TOSMALLER = 22;
	public static final int BALL_TOFASTTER = 23;
	public static final int BALL_TOSLOWER = 24;

	private int _type;
	private int _changeType;
	public int health = 100;// ������ʾ״̬
	public int color;// ������ɫ

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
