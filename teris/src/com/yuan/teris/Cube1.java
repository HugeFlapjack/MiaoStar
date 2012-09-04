package com.yuan.teris;

/*
 * 　■
 * ■■■
 */
public class Cube1 extends Cube {
	static int color;

	// Cube1() {
	// super(color);
	// setPosByStyle(style, x, y);
	// }

	Cube1(CubeStyle style) {
		super(color, style);
	}

	// 根据第一个方块的位置和style设置所有方块的位置
	protected void setPosByStyle(CubeStyle s, int x, int y) {

		// 设置第一个方块的位置
		squares[0].setPosition(x, y);

		// 根据style算出其它方块的位置
		switch (s) {
		case UP:
			squares[1].setPosition(x + 1, y);
			squares[2].setPosition(x + 2, y);
			squares[3].setPosition(x + 1, y + 1);
			break;
		case DOWN:
			squares[1].setPosition(x - 1, y + 1);
			squares[2].setPosition(x, y + 1);
			squares[3].setPosition(x + 1, y + 1);
			break;
		case RIGHT:
			squares[1].setPosition(x - 1, y + 1);
			squares[2].setPosition(x, y + 1);
			squares[3].setPosition(x, y + 2);
			break;
		case LEFT:
			squares[1].setPosition(x, y + 1);
			squares[2].setPosition(x + 1, y + 1);
			squares[3].setPosition(x, y + 2);
			break;
		default:
			break;
		}
	}

	// 计算出旋转后第一个方块的位置和旋转后的style
	protected void caculateRotatedPosAndStyle(RotateDir dir) {
		// TODO Auto-generated method stub
		if (RotateDir.CLOCKWISE == dir) {

			switch (style) {
			case LEFT:
				y++;
				style = CubeStyle.UP;
				break;

			case RIGHT:
				x--;
				y++;
				style = CubeStyle.DOWN;
				break;

			case UP:
				x += 2;
				y--;
				style = CubeStyle.RIGHT;
				break;

			case DOWN:
				x--;
				y--;
				style = CubeStyle.LEFT;
				break;

			default:
				break;
			}
		} else if (RotateDir.ANTICLOCKWISE == dir) {

			switch (style) {
			case LEFT:
				x++;
				y++;
				style = CubeStyle.DOWN;
				break;

			case RIGHT:
				x -= 2;
				y++;
				style = CubeStyle.UP;
				break;

			case UP:
				y--;
				style = CubeStyle.LEFT;
				break;

			case DOWN:
				x++;
				y--;
				style = CubeStyle.RIGHT;
				break;

			default:
				break;
			}
		} else {

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
