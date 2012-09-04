package com.yuan.teris;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;

public class Square {
	// public static final int STEP_LENGTH = MainActivity.screenW /
	// TerisView.XCOUNT;
	public static int squareRealSize;
	public static int squareDisplaySize;
	public static int squareOffsetX;
	public static int squareOffsetY;

	private int color;
	private int posX;
	private int posY;

	public Square(int color, int posX, int posY) {
		this.color = color;
		this.posX = posX;
		this.posY = posY;
	}

	public void drawSquare(Canvas canvas) {
		Paint p = new Paint();
		p.setColor(color);
		p.setStrokeWidth(2.0f);
		p.setStyle(Style.STROKE);
		canvas.drawRect(new Rect(posX * squareRealSize + (squareRealSize - squareDisplaySize)/2 + squareOffsetX, 
				posY * squareRealSize + (squareRealSize - squareDisplaySize)/2 + squareOffsetY, 
				posX * squareRealSize + (squareRealSize - squareDisplaySize)/2 + squareOffsetX + squareDisplaySize, 
				posY * squareRealSize + (squareRealSize - squareDisplaySize)/2 + squareOffsetY + squareDisplaySize), p);
	}

	public void moveDownOneStep() {
		if (posY < TerisView.YCOUNT - 1) {
			posY++;
		}
	}

	public Point getPosition() {
		Point p = new Point();
		p.set(posX, posY);

		return p;
	}

	public void setPosition(int x, int y) {
		posX = x;
		posY = y;
	}

	public void moveLeft() {
		posX--;
	}

	public void moveRight() {
		posX++;
	}

	public void setColor(int color) {
		this.color = color;
	}
}