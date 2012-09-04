package com.yuan.teris;


/*
 * ■■■■ 
 */
public class Cube7 extends Cube {
	static int color;

	// Cube7() {
	// super(color);
	// setPosByStyle(style, x, y);
	// }

	Cube7(CubeStyle style) {
		super(color, style);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void caculateRotatedPosAndStyle(RotateDir dir) {
		// TODO Auto-generated method stub
		if (null == dir) {
			return;
		}

		switch (style) {
		case LEFT:
		case RIGHT:
			x--;
			y += 3;
			style = CubeStyle.UP;
			break;

		case UP:
		case DOWN:
			x++;
			y -= 3;
			style = CubeStyle.RIGHT;
			break;

		default:
			break;
		}
	}

	@Override
	protected void setPosByStyle(CubeStyle s, int x, int y) {
		// TODO Auto-generated method stub
		// 设置第一个方块的位置
		squares[0].setPosition(x, y);

		// 根据style算出其它方块的位置
		switch (s) {
		case UP:
		case DOWN:
			squares[1].setPosition(x + 1, y);
			squares[2].setPosition(x + 2, y);
			squares[3].setPosition(x + 3, y);
			break;
		case RIGHT:
		case LEFT:
			squares[1].setPosition(x, y + 1);
			squares[2].setPosition(x, y + 2);
			squares[3].setPosition(x, y + 3);
			break;
		default:
			break;
		}
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
