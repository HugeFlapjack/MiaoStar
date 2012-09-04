package com.yuan.teris;

import android.graphics.Canvas;

public abstract class Cube {
	public static final int SQUARE_NUM = 4;

	protected int x, y;
	protected static int color;
	protected CubeStyle style;
	protected Square[] squares = new Square[SQUARE_NUM];

	// Cube(int c) {
	// color = c;
	// this.style = CubeStyle.values()[TerisView.rand
	// .nextInt(TerisView.CUBE_STYLE_NUM)];
	// this.x = 4;
	// this.y = -1;
	// for (int i = 0; i < SQUARE_NUM; i++) {
	// squares[i] = new Square(color, 0, 0);
	// }
	// }

	
	Cube(int c, CubeStyle style) {
		color = c;
		this.x = 4;
		this.y = -1;
		this.style = style;

		for (int i = 0; i < SQUARE_NUM; i++) {
			squares[i] = new Square(color, 0, 0);
		}

		setPosByStyle(this.style, x, y);
	}

	public void move(MoveDir dir) {

		// 直接移动到最下
		if (MoveDir.DOWN_TO_END == dir) {
			boolean isMoveAgain = true;
			while (isMoveAgain) {
				isMoveAgain = moveDownOneStep();
			}
			return;
		}

		// 左右移动
		if (isMove(dir)) {
			if (MoveDir.LEFT == dir) {
				for (Square s : squares) {
					s.moveLeft();
				}
				x--;
			} else {
				for (Square s : squares) {
					s.moveRight();
				}
				x++;
			}
		}
	}

	// 判断能否左右移动
	private boolean isMove(MoveDir dir) {
		boolean isMove = true;

		// 判断是否能够左移
		if (MoveDir.LEFT == dir) {
			for (Square s : squares) {
				if (0 == s.getPosition().x
						|| (s.getPosition().y >= 0 && null != TerisView.squareMatrix[s
								.getPosition().x - 1][s.getPosition().y])) {
					isMove = false;
					break;
				}
			}
		}
		// 判断是否能够右移
		else if (MoveDir.RIGHT == dir) {
			for (Square s : squares) {
				if (TerisView.XCOUNT - 1 == s.getPosition().x
						|| (s.getPosition().y >= 0 && null != TerisView.squareMatrix[s
								.getPosition().x + 1][s.getPosition().y])) {
					isMove = false;
					break;
				}
			}
		} else {
			isMove = false;
		}

		return isMove;
	}

	// 返回值为表示能否继续下移
	public boolean moveDownOneStep() {
		boolean isMoveAgain = false;

		if (isMoveDown()) {
			for (Square s : squares) {
				s.moveDownOneStep();
			}
			isMoveAgain = true;
			y++;
		} else {
			isMoveAgain = false;

			// 方块已经不能再继续下移，并且方块未超出上边界
			if (false == isOutOfUpBorder()) {
				TerisView.storeCube(); // 将方块存入数组squareMatrix中
				TerisView.removeLines();
				TerisView.regetCube(); // 产生新的方块
			}
			// 方块已经不能再继续下移，并且方块超出上边界,游戏结束
			else {
				TerisView.storeCube(); // 将方块存入数组squareMatrix中
				TerisView.handler.sendEmptyMessage(TerisView.MSG_GAMEOVER);
			}
		}

		return isMoveAgain;
	}

	private boolean isMoveDown() {

		// 判断下移后是否超过下边界
		for (Square s : squares) {
			if (s.getPosition().y >= TerisView.YCOUNT - 1) {
				return false;
			}
		}

		// 判断下移后是否碰到其它方块,这个判断应该在“是否超过下边界”的判断之后
		for (Square s : squares) {
			if (s.getPosition().y + 1 >= 0
					&& null != TerisView.squareMatrix[s.getPosition().x][s
							.getPosition().y + 1]) {
				return false;
			}
		}

		return true;
	}

	protected abstract void caculateRotatedPosAndStyle(RotateDir dir);

	public void rotate(RotateDir dir) {

		// 记录旋转前第一个方块的位置的style
		int oldX = x, oldY = y;
		CubeStyle oldStyle = style;

		// 旋转后是否超出边界/与其它方块重合
		boolean isReset = false;

		// 计算出旋转后第一个方块的位置和旋转后的style
		caculateRotatedPosAndStyle(dir);

		// 根据旋转后第一个方块的位置和style设置所有方块的位置
		setPosByStyle(style, x, y);

		for (Square s : squares) {

			// 判断旋转后是否有方块出界
			if (s.getPosition().x < 0
					|| s.getPosition().x > TerisView.XCOUNT - 1
					|| s.getPosition().y > TerisView.YCOUNT - 1) {
				isReset = true;
				break;
			}

			// 判断旋转后是否与其它方块重合
			if (s.getPosition().y >= 0
					&& null != TerisView.squareMatrix[s.getPosition().x][s
							.getPosition().y]) {
				isReset = true;
				break;
			}
		}

		// 如果旋转后出界/重合，则重置为旋转前的位置和style
		if (isReset) {
			x = oldX;
			y = oldY;
			style = oldStyle;
			setPosByStyle(style, x, y);
		}
	}

	// 根据第一个方块的位置和style设置所有方块的位置
	protected abstract void setPosByStyle(CubeStyle s, int x, int y);

	// 判断方块是否超出上边界
	private boolean isOutOfUpBorder() {
		boolean isOutOfUpBorder = false;

		for (Square s : squares) {
			if (s.getPosition().y < 0) {
				isOutOfUpBorder = true;
				break;
			}
		}

		return isOutOfUpBorder;
	}

	public Square[] getSquares() {
		return squares;
	}

	public void draw(Canvas canvas) {
		for (Square s : squares) {
			s.drawSquare(canvas);
		}
	}

	public void changePostion(int x, int y) {
		this.x = x;
		this.y = y;
		setPosByStyle(this.style, x, y);
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
