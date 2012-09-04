package com.yuan.teris;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static int screenW;
	public static int screenH;

	private Button btnMoveLeft;
	private Button btnMoveRight;
	private Button btnMoveDown;
//	private Button btnRotateClockwise;
	private Button btnRotateAnticlockwise;
	public static Button btnStartPause;
	public static TextView scoreView;
	private TerisView tesrisView;
	private CubeView nextCubeView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenW = metrics.widthPixels;
		screenH = metrics.heightPixels;
		getCubeColor();

		setContentView(R.layout.activity_main);

		btnMoveLeft = (Button) findViewById(R.id.btn_move_left);
		btnMoveRight = (Button) findViewById(R.id.btn_move_right);
		btnMoveDown = (Button) findViewById(R.id.btn_move_down);
//		btnRotateClockwise = (Button) findViewById(R.id.btn_rotate_clolckwise);
		btnRotateAnticlockwise = (Button) findViewById(R.id.btn_rotate_anticlolckwise);
		btnStartPause = (Button) findViewById(R.id.btn_start_pause);

		scoreView = (TextView) findViewById(R.id.tv_score);
		tesrisView = (TerisView) findViewById(R.id.terisView);
		nextCubeView= (CubeView) findViewById(R.id.nextCubeView);
		
		TerisView.setCubeView(nextCubeView);
		
		btnMoveLeft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tesrisView.onButtonDown(MoveDir.LEFT, null);
			}
		});

		btnMoveRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tesrisView.onButtonDown(MoveDir.RIGHT, null);
			}
		});

		btnMoveDown.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tesrisView.onButtonDown(MoveDir.DOWN_TO_END, null);
			}
		});

//		btnRotateClockwise.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				view.onButtonDown(null, RotateDir.CLOCKWISE);
//			}
//		});

		btnRotateAnticlockwise.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tesrisView.onButtonDown(null, RotateDir.ANTICLOCKWISE);
			}
		});

		btnStartPause.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TerisView.GAME_STATUS_PLAYING == TerisView.gameStatus) {
					TerisView.handler
							.sendEmptyMessage(TerisView.MSG_PAUSE_GAME);
					btnStartPause.setText("¿ªÊ¼");
				} else if (TerisView.GAME_STATUS_PAUSE == TerisView.gameStatus) {
					TerisView.handler
							.sendEmptyMessage(TerisView.MSG_RESUME_GAME);
					btnStartPause.setText("ÔÝÍ£");
				} else if (TerisView.GAME_STATUS_READY == TerisView.gameStatus) {
					TerisView.handler
							.sendEmptyMessage(TerisView.MSG_START_GAME);
					btnStartPause.setText("ÔÝÍ£");
				} else if (TerisView.GAME_STATUS_LOST == TerisView.gameStatus) {
					TerisView.handler
							.sendEmptyMessage(TerisView.MSG_START_GAME);
					btnStartPause.setText("ÔÝÍ£");
				}
			}
		});
	}

	@Override
	protected void onPause() {
		if (TerisView.GAME_STATUS_PLAYING == TerisView.gameStatus) {
			TerisView.handler.sendEmptyMessage(TerisView.MSG_PAUSE_GAME);
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_setcolor:
			Intent i = new Intent();
			i.setClass(MainActivity.this, SetColorActivity.class);
			startActivity(i);
			break;
		case R.id.menu_showRecord:
			showRecord();
			break;
		default:
			break;
		}
		return true;
	}

	public static void showScore(int score) {
		scoreView.setText("score: " + score);
	}

	private void getCubeColor() {
		SharedPreferences colors = getSharedPreferences("gameInfo", MODE_PRIVATE);
		Cube1.color = colors.getInt("color_cube1", 0xFF000000);
		Cube2.color = colors.getInt("color_cube2", 0xFF000080);
		Cube3.color = colors.getInt("color_cube3", 0xFF008000);
		Cube4.color = colors.getInt("color_cube4", 0xFF800000);
		Cube5.color = colors.getInt("color_cube5", 0xFFFF8000);
		Cube6.color = colors.getInt("color_cube6", 0xFF8000FF);
		Cube7.color = colors.getInt("color_cube7", 0xFFFF0080);
	}

	private void showRecord() {
		SharedPreferences record = getSharedPreferences("gameInfo", Activity.MODE_PRIVATE);
		int recordNo1 = record.getInt("recordScoreNo1", 0);
		int recordNo2 = record.getInt("recordScoreNo2", 0);
		int recordNo3 = record.getInt("recordScoreNo3", 0);
		
		Toast.makeText(this, "No.1:\t\t\t" + recordNo1 + "\nNo.2:\t\t\t" + recordNo2 + "\nNo.3:\t\t\t" + recordNo3, Toast.LENGTH_LONG).show();
			
	}
}
