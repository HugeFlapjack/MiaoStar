package com.yuan.teris;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

@SuppressLint({ "NewApi", "NewApi" })
public class CubeView extends View {

	int squareRealSize;
	int squareDisplaySize;
	int squareOffsetX;
	int squareOffsetY;
	int squareRealSizeBackup;
	int squareDisplaySizeBackup;
	int squareOffsetXBackup;
	int squareOffsetYBackup;
	private boolean selected = false;
	private int x, y, w, h;

	Cube cube = null;

	public CubeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CubeView, 0, 0);
		int index = a.getInteger(R.styleable.CubeView_cubeIndex, 0xFF);
		setCube(index);
		a.recycle();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		int[] viewLocation = new int[2];
		this.getLocationOnScreen(viewLocation);

		if (h > w) {
			squareRealSize = w / 5;
		} else {
			squareRealSize = h / 5;
		}

		// 小方块的大小
		squareDisplaySize = squareRealSize * 9 / 10;

		squareOffsetX = 0;
		squareOffsetY = 0;
		
		// 取得本View的位置和大小
		this.x = (int) getX();
		this.y = (int) getY();
		this.w = getWidth();
		this.h = getHeight();
	}

	@Override
	public void draw(Canvas canvas) {
		squareRealSizeBackup = Square.squareRealSize;
		squareDisplaySizeBackup = Square.squareDisplaySize;
		squareOffsetXBackup = Square.squareOffsetX;
		squareOffsetYBackup = Square.squareOffsetY;

		Square.squareRealSize = squareRealSize;
		Square.squareDisplaySize = squareDisplaySize;
		Square.squareOffsetX = squareOffsetX;
		Square.squareOffsetY = squareOffsetY;

		if (null != cube) {
			cube.draw(canvas);
		}
		
		Paint p = new Paint();
		p.setColor(Color.BLACK);
		p.setStrokeWidth(2.0f);
		p.setStyle(Style.STROKE);
		if (true == selected) {
			canvas.drawRect(2, 2, w - 2, h - 2, p);
		}
		Square.squareRealSize = squareRealSizeBackup;
		Square.squareDisplaySize = squareDisplaySizeBackup;
		Square.squareOffsetX = squareOffsetXBackup;
		Square.squareOffsetY = squareOffsetYBackup;
	}

	public void setCube(int index) {
		if (0xFF == index) {
			return;
		}
		cube = CubeFactory.getCube(index, CubeStyle.UP);
		cube.changePostion(1, 1);
		invalidate();
	}

	public void setCube(Cube cube) {
		this.cube = cube;
		cube.changePostion(1, 1);
		invalidate();
	}

	public void changeColor(int color) {
		if (null != cube) {
			cube.changeColor(color);
			invalidate();
		}
	}

	public int getColor() {
		if (null != cube) {
			return cube.getColor();
		}

		return 0xFF000000;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		invalidate();
	}
}
