package com.edu.seu.crazyball2;

import com.badlogic.gdx.physics.box2d.Body;

public class Constant {
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static float bound_width = 1.0f;
	
	public static float board_halfheight = 1.5f; // board的一半
	public static float board_halfwidth = 5f; // board的一半
	public static float board_halfwidth0 = 5f;
	public static float board_halfwidth1 = 5f;
	public static float board_halfwidth2 = 5f;
	public static float board_halfwidth3 = 5f;
	
	public static Body tBoard0;
	public static Body tBoard1;
	public static Body tBoard2;
	public static Body tBoard3;
	
	public static Body tBall;
	
	public static float circle_radius_standard = 2.0f;
	public static float circle_radius = 2.0f;
	
	public static float boardrate = 1f / 8f;
	public static float set_x;
	public static float set_y;
	public static int BALL_HIT = 0;
	public static int ball_temp_post = 0;
//	public static int BLOCK_HIT = 0;
	public static int BOARD_HIT = 0;
	public static float block_width = 1.0f;
	
	public static final int SHOW_DIALOG = 0;
	public static final int SHOW_TOAST = 1;
	public static final int SHOW_MYDEAD_DIALOG = 2;
	public static final boolean isUpdate = false;
}
