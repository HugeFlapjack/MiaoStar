package com.yuan.teris;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

public class TerisView extends View {

	public static final int CUBE_STYLE_NUM = CubeStyle.values().length;
	public static final int XCOUNT = 10;
	public static final int YCOUNT = 15;
	public static int VIEW_SIZE_W;
	public static int VIEW_SIZE_H;
	public static int VIEW_LOCATION_X;
	public static int VIEW_LOCATION_Y;

	public static final int MSG_START_GAME = 1;
	public static final int MSG_RESUME_GAME = 2;
	public static final int MSG_PAUSE_GAME = 4;
	public static final int MSG_GAMEOVER = 5;
	public static final int MSG_CUBE_MOVEDOWN = 6;
	public static final int MSG_CREATE_NEW_CUBE = 7;

	public static final int GAME_STATUS_PLAYING = 1;
	public static final int GAME_STATUS_READY = 2;
	public static final int GAME_STATUS_PAUSE = 3;
	public static final int GAME_STATUS_LOST = 4;

	public static Random rand = new Random();
	public static Square[][] squareMatrix = new Square[XCOUNT][YCOUNT];
	public static RefreshHandler handler;
	public static int score = 0; // 游戏得分
	public static int gameStatus = GAME_STATUS_READY;

	private static long moveDownDelay = 1000; // 向下移动的时间间隔
	private static Cube cube = null;
	private static Cube nextCube = null;

	private static int level = 1;
	
	private static CubeView cubeView = null;
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		for (int x = 0; x < XCOUNT; x++) {
			for (int y = 0; y < YCOUNT; y++) {
				if (null != squareMatrix[x][y]) {
					squareMatrix[x][y].drawSquare(canvas);
				}
			}
		}

		if (null != cube) {
			cube.draw(canvas);
		}

		Paint p = new Paint();
		p.setColor(Color.BLACK);
		p.setStrokeWidth(2.0f);
		p.setStyle(Style.STROKE);
		canvas.drawRect(Square.squareOffsetX - 2, Square.squareOffsetY - 2,
				Square.squareOffsetX + Square.squareRealSize * XCOUNT + 2,
				Square.squareOffsetY + Square.squareRealSize * YCOUNT + 2, p);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		VIEW_SIZE_W = w;
		VIEW_SIZE_H = h;
		int[] viewLocation = new int[2];
		this.getLocationOnScreen(viewLocation);
		VIEW_LOCATION_X = viewLocation[0];
		VIEW_LOCATION_Y = viewLocation[1];

		// 根据view的高和宽计算出每个小方块占的宽度，使能够显示出XCOUT * YCOUNT个小方块
		if ((float) VIEW_SIZE_H / (float) VIEW_SIZE_W > (float) YCOUNT
				/ (float) XCOUNT) {
			Square.squareRealSize = VIEW_SIZE_W / XCOUNT;
		} else {
			Square.squareRealSize = VIEW_SIZE_H / YCOUNT;
		}

		// 小方块的大小
		Square.squareDisplaySize = Square.squareRealSize * 9 / 10;

		Square.squareOffsetX = VIEW_LOCATION_X
				+ (VIEW_SIZE_W - Square.squareRealSize * XCOUNT) / 2;
		Square.squareOffsetY = 4;
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// int x = (int) event.getX();
	// int y = (int) event.getY();
	// if (GAME_STATUS_PLAYING == gameStatus) {
	// MoveDir moveDir = judgeMoveDir(x, y);
	// RotateDir rotateDir = judgeRotateDir(x, y);
	//
	// cube.move(moveDir);
	// cube.rotate(rotateDir);
	//
	// invalidate();
	// }
	//
	// return super.onTouchEvent(event);
	// }

	public void onButtonDown(MoveDir moveDir, RotateDir rotateDir) {
		if (GAME_STATUS_PLAYING == gameStatus) {
			cube.move(moveDir);
			cube.rotate(rotateDir);

			invalidate();
		}
	}

	public TerisView(Context context) {
		super(context);
		handler = new RefreshHandler();
		handler.sendEmptyMessage(MSG_CUBE_MOVEDOWN);
	}

	public TerisView(Context context, AttributeSet attrs) {
		super(context, attrs);
		handler = new RefreshHandler();
		handler.sendEmptyMessage(MSG_CUBE_MOVEDOWN);
	}

	// private MoveDir judgeMoveDir(int x, int y) {
	// if (y < MainActivity.screenH / 5) {
	// return null;
	// }
	// if (y > MainActivity.screenH * 4 / 5) {
	// return MoveDir.DOWN_TO_END;
	// }
	// if (x < MainActivity.screenW * 2 / 5) {
	// return MoveDir.LEFT;
	// } else if (x > MainActivity.screenW * 3 / 5) {
	// return MoveDir.RIGHT;
	// } else {
	// return null;
	// }
	// }
	//
	// private RotateDir judgeRotateDir(int x, int y) {
	// if (y < MainActivity.screenH / 5) {
	// if (x < MainActivity.screenW * 2 / 5) {
	// return RotateDir.ANTICLOCKWISE;
	// } else if (x > MainActivity.screenW * 3 / 5) {
	// return RotateDir.CLOCKWISE;
	// } else {
	// return null;
	// }
	// } else {
	// return null;
	// }
	// }

	public static void regetCube() {
		cube = nextCube;
		cube.changePostion(4, -1);
		nextCube = CubeFactory.getRandomCube();
		cubeView.setCube(nextCube);
	}

	// 将方块存入数组squareMatrix中
	public static void storeCube() {
		for (Square s : cube.getSquares()) {
			if (s.getPosition().y >= 0) {
				TerisView.squareMatrix[s.getPosition().x][s.getPosition().y] = s;
			}
		}
	}

	public static void removeLines() {
		int lineNo = 0; // 要消去第lineNo行
		int nums = 0;

		for (int y = 0; y < YCOUNT; y++) {
			boolean remove = true;
			for (int x = 0; x < XCOUNT; x++) {
				if (null == squareMatrix[x][y]) {
					remove = false;
					break;
				}
			}

			if (true == remove) {
				nums++;
				lineNo = y;

				// 将方块向下移动
				for (int y1 = lineNo - 1; y1 >= 0; y1--) {
					for (int x = 0; x < XCOUNT; x++) {
						squareMatrix[x][y1 + 1] = squareMatrix[x][y1];
						if (null != squareMatrix[x][y1 + 1]) {
							squareMatrix[x][y1 + 1].moveDownOneStep();
						}
						squareMatrix[x][0] = null;
					}
				}
			}
		}
		
		addScore(nums);
	}

	private static void addScore(int lineNum) {

		// 增加得分
		switch (lineNum) {
		case 4:
			score += 8;
		case 3:
			score += 5;
		case 2:
			score += 3;
		case 1:
			score += 2;
			
			// 得分超过40增加方块下落的速度
			if (score >= 40 * level) {
				level++;
				moveDownDelay *= 0.9;
			}
		case 0:
			break;
		}

		// 更新显示的得分
		MainActivity.showScore(score);
		
	}
	
	private void initGame() {
		score = 0;
		level = 1;
		MainActivity.scoreView.setText("score: 0");
		moveDownDelay = 1000;
		for (int x = 0; x < XCOUNT; x++) {
			for (int y = 0; y < YCOUNT; y++) {
				squareMatrix[x][y] = null;
			}
		}
		cube = CubeFactory.getRandomCube();
		nextCube = CubeFactory.getRandomCube();
		cubeView.setCube(nextCube);
	}

	public static void setCubeView(CubeView view) {
		cubeView = view;
	}

	// 保存分数记录
	private void storeRecord() {
		SharedPreferences record = getContext().getSharedPreferences("gameInfo", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = record.edit();
		int recordScoreNo1 = record.getInt("recordScoreNo1", 0);
		int recordScoreNo2 = record.getInt("recordScoreNo2", 0);
		int recordScoreNo3 = record.getInt("recordScoreNo3", 0);
		
		if (TerisView.score > recordScoreNo1) {
			recordScoreNo3 = recordScoreNo2;
			recordScoreNo2 = recordScoreNo1;
			recordScoreNo1 = TerisView.score;
		} else if (TerisView.score > recordScoreNo2) {
			recordScoreNo3 = recordScoreNo2;
			recordScoreNo2 = TerisView.score;			
		} else if (TerisView.score > recordScoreNo3) {
			recordScoreNo3 = TerisView.score;			
		} else {
			
		}
		
		editor.putInt("recordScoreNo1", recordScoreNo1);
		editor.putInt("recordScoreNo2", recordScoreNo2);
		editor.putInt("recordScoreNo3", recordScoreNo3);
		
		editor.commit();
	}
	
	class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_CREATE_NEW_CUBE:
				break;
			case MSG_CUBE_MOVEDOWN:
				if (GAME_STATUS_PLAYING == gameStatus) {
					cube.moveDownOneStep();
					TerisView.this.invalidate();
					sendEmptyMessageDelayed(MSG_CUBE_MOVEDOWN, moveDownDelay);
				}
				break;
			case MSG_GAMEOVER:
				storeRecord();
				Toast.makeText(getContext(), "GAME_OVER", Toast.LENGTH_SHORT)
						.show();
				MainActivity.btnStartPause.setText("再来一次");
				gameStatus = GAME_STATUS_LOST;
				break;
			case MSG_PAUSE_GAME:
				gameStatus = GAME_STATUS_PAUSE;
				break;
			case MSG_RESUME_GAME:
				gameStatus = GAME_STATUS_PLAYING;
				sendEmptyMessageDelayed(MSG_CUBE_MOVEDOWN, moveDownDelay);
				break;
			case MSG_START_GAME:
				initGame();
				gameStatus = GAME_STATUS_PLAYING;
				sendEmptyMessageDelayed(MSG_CUBE_MOVEDOWN, moveDownDelay);
				break;
			default:
				break;
			}
		}
	}
}
