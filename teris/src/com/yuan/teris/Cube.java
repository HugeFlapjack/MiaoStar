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

		// ֱ���ƶ�������
		if (MoveDir.DOWN_TO_END == dir) {
			boolean isMoveAgain = true;
			while (isMoveAgain) {
				isMoveAgain = moveDownOneStep();
			}
			return;
		}

		// �����ƶ�
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

	// �ж��ܷ������ƶ�
	private boolean isMove(MoveDir dir) {
		boolean isMove = true;

		// �ж��Ƿ��ܹ�����
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
		// �ж��Ƿ��ܹ�����
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

	// ����ֵΪ��ʾ�ܷ��������
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

			// �����Ѿ������ټ������ƣ����ҷ���δ�����ϱ߽�
			if (false == isOutOfUpBorder()) {
				TerisView.storeCube(); // �������������squareMatrix��
				TerisView.removeLines();
				TerisView.regetCube(); // �����µķ���
			}
			// �����Ѿ������ټ������ƣ����ҷ��鳬���ϱ߽�,��Ϸ����
			else {
				TerisView.storeCube(); // �������������squareMatrix��
				TerisView.handler.sendEmptyMessage(TerisView.MSG_GAMEOVER);
			}
		}

		return isMoveAgain;
	}

	private boolean isMoveDown() {

		// �ж����ƺ��Ƿ񳬹��±߽�
		for (Square s : squares) {
			if (s.getPosition().y >= TerisView.YCOUNT - 1) {
				return false;
			}
		}

		// �ж����ƺ��Ƿ�������������,����ж�Ӧ���ڡ��Ƿ񳬹��±߽硱���ж�֮��
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

		// ��¼��תǰ��һ�������λ�õ�style
		int oldX = x, oldY = y;
		CubeStyle oldStyle = style;

		// ��ת���Ƿ񳬳��߽�/�����������غ�
		boolean isReset = false;

		// �������ת���һ�������λ�ú���ת���style
		caculateRotatedPosAndStyle(dir);

		// ������ת���һ�������λ�ú�style�������з����λ��
		setPosByStyle(style, x, y);

		for (Square s : squares) {

			// �ж���ת���Ƿ��з������
			if (s.getPosition().x < 0
					|| s.getPosition().x > TerisView.XCOUNT - 1
					|| s.getPosition().y > TerisView.YCOUNT - 1) {
				isReset = true;
				break;
			}

			// �ж���ת���Ƿ������������غ�
			if (s.getPosition().y >= 0
					&& null != TerisView.squareMatrix[s.getPosition().x][s
							.getPosition().y]) {
				isReset = true;
				break;
			}
		}

		// �����ת�����/�غϣ�������Ϊ��תǰ��λ�ú�style
		if (isReset) {
			x = oldX;
			y = oldY;
			style = oldStyle;
			setPosByStyle(style, x, y);
		}
	}

	// ���ݵ�һ�������λ�ú�style�������з����λ��
	protected abstract void setPosByStyle(CubeStyle s, int x, int y);

	// �жϷ����Ƿ񳬳��ϱ߽�
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
