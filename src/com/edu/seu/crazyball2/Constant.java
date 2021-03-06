package com.edu.seu.crazyball2;

import java.util.Timer;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

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
	public static float base_width = 10f;
	public static float offset_center = 12f;
	
	public static World mworld;

	public static Body tBoard0;
	public static Body tBoard1;
	public static Body tBoard2;
	public static Body tBoard3;

	public static Body tBall;

	// 感应区
	public static Body tSensor;
	public static boolean canTouching = false;

	public static float circle_radius_standard = 2.0f;
	public static float circle_radius = 2.0f;

	public static float boardrate = 1f / 8f;
	public static float set_x;
	public static float set_y;
	public static int BALL_HIT = 0;
	public static int BOARD_HIT = 0;
	public static float block_width = 1.0f;

	public static final int SHOW_MYDEAD_DIALOG = 2;
	public static final int SHOW_DIALOG2 = 3;

	public static int CONTROL_ID = 0;
	public static final Color[] colors = { new Color(77, 182, 175, 255),
			new Color(242, 109, 110, 255), new Color(111, 205, 168, 255),
			new Color(253, 152, 122, 255) };
	public static Color bgcolor = new Color();

	//public static int[] myBlock = new int[4];
	
	public static int[] showBoard={1,1,1,1};   ///board显示状态，为1则显示，为0则隐藏
	public static boolean move_board=true;     ///board可以移动
	public static boolean isUpdate = false;
	
	//设置警告声音
	public static Sound warningSound;
    
	//设置定时器
	public static Timer sendtimer;  //发送定时器
<<<<<<< HEAD
	
	//public static Stage stage;

=======
	//设置舞台
	public static Stage stage;
>>>>>>> 797c9de4e3e95ae95391bc227a12d6015633be60
}
