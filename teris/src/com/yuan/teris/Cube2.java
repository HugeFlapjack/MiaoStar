package com.yuan.teris;

/*
 * ■■
 * ■■
 */
public class Cube2 extends Cube {

	static int color;

	// Cube2() {
	// super(color);
	// setPosByStyle(style, x, y);
	// }

	Cube2(CubeStyle style) {
		super(color, style);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void caculateRotatedPosAndStyle(RotateDir dir) {
		// TODO Auto-generated method stub
		// 这个cube不会旋转，此方法为空
	}

	@Override
	protected void setPosByStyle(CubeStyle s, int x, int y) {
		// TODO Auto-generated method stub

		squares[0].setPosition(x, y);
		squares[1].setPosition(x + 1, y);
		squares[2].setPosition(x, y + 1);
		squares[3].setPosition(x + 1, y + 1);
	}

	public void changeColor(int c) {
		color = c;

		for (int i = 0; i < SQUARE_NUM; i++) {
			squares[i].setColor(color);
		}
	}

	public int getColor() {
		return color;
	}
}
