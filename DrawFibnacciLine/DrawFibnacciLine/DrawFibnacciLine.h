#pragma once

#include "resource.h"

#define X_CENTER 300
#define Y_CENTER 300

typedef enum{
	CLOCKWISE = 0,
	ANTICLOCKWISE =1
}Direction;

typedef enum{
	FIRST_Q = 0,
	SECOND_Q,
	THIRD_Q,
	FOURTH_Q
}Quadrant;

#define FIBNACCIARRAY_MAXNUM  20


void DrawFLine(HDC hdc, POINT startPoint, Quadrant quadrant, Direction direction);
void DrawQuarterArc(HDC hdc, int radius, int xPoint, int yPoint, int style);
POINT DrawQuarterArc(HDC hdc, POINT startPoint, int radius, Quadrant quadrant, Direction direction);