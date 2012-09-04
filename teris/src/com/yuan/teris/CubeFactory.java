package com.yuan.teris;


public class CubeFactory {

	static Cube getRandomCube() {
		Cube c = null;
		int index = TerisView.rand.nextInt(7);
		CubeStyle style = CubeStyle.values()[TerisView.rand.nextInt(4)];
		
		c = getCube(index, style);
		
		return c;
	}

	static Cube getCube(int index, CubeStyle style) {
		Cube c = null;
		
		switch (index) {
		case 0:
			c = new Cube1(style);
			break;
		case 1:
			c = new Cube2(style);
			break;
		case 2:
			c = new Cube3(style);
			break;
		case 3:
			c = new Cube4(style);
			break;
		case 4:
			c = new Cube5(style);
			break;
		case 5:
			c = new Cube6(style);
			break;
		case 6:
			c = new Cube7(style);
			break;
		default:
			break;
		}
		
		return c;
	}
}
