// DrawFibnacciLine.cpp : 定义应用程序的入口点。
//

#include "stdafx.h"
#include "DrawFibnacciLine.h"

#define MAX_LOADSTRING 100

// 全局变量:
HINSTANCE hInst;								// 当前实例
TCHAR szTitle[MAX_LOADSTRING];					// 标题栏文本
TCHAR szWindowClass[MAX_LOADSTRING];			// 主窗口类名
int fibnacciNum[FIBNACCIARRAY_MAXNUM];			// 斐波那契数列

// 此代码模块中包含的函数的前向声明:
ATOM				MyRegisterClass(HINSTANCE hInstance);
BOOL				InitInstance(HINSTANCE, int);
LRESULT CALLBACK	WndProc(HWND, UINT, WPARAM, LPARAM);
INT_PTR CALLBACK	About(HWND, UINT, WPARAM, LPARAM);

int APIENTRY _tWinMain(HINSTANCE hInstance,
                     HINSTANCE hPrevInstance,
                     LPTSTR    lpCmdLine,
                     int       nCmdShow)
{
	UNREFERENCED_PARAMETER(hPrevInstance);
	UNREFERENCED_PARAMETER(lpCmdLine);

 	// TODO: 在此放置代码。
	MSG msg;
	HACCEL hAccelTable;

	// 初始化全局字符串
	LoadString(hInstance, IDS_APP_TITLE, szTitle, MAX_LOADSTRING);
	LoadString(hInstance, IDC_DRAWFIBNACCILINE, szWindowClass, MAX_LOADSTRING);
	MyRegisterClass(hInstance);

	// 执行应用程序初始化:
	if (!InitInstance (hInstance, nCmdShow))
	{
		return FALSE;
	}

	hAccelTable = LoadAccelerators(hInstance, MAKEINTRESOURCE(IDC_DRAWFIBNACCILINE));

	// 主消息循环:
	while (GetMessage(&msg, NULL, 0, 0))
	{
		if (!TranslateAccelerator(msg.hwnd, hAccelTable, &msg))
		{
			TranslateMessage(&msg);
			DispatchMessage(&msg);
		}
	}

	return (int) msg.wParam;
}



//
//  函数: MyRegisterClass()
//
//  目的: 注册窗口类。
//
//  注释:
//
//    仅当希望
//    此代码与添加到 Windows 95 中的“RegisterClassEx”
//    函数之前的 Win32 系统兼容时，才需要此函数及其用法。调用此函数十分重要，
//    这样应用程序就可以获得关联的
//    “格式正确的”小图标。
//
ATOM MyRegisterClass(HINSTANCE hInstance)
{
	WNDCLASSEX wcex;

	wcex.cbSize = sizeof(WNDCLASSEX);

	wcex.style			= CS_HREDRAW | CS_VREDRAW;
	wcex.lpfnWndProc	= WndProc;
	wcex.cbClsExtra		= 0;
	wcex.cbWndExtra		= 0;
	wcex.hInstance		= hInstance;
	wcex.hIcon			= LoadIcon(hInstance, MAKEINTRESOURCE(IDI_DRAWFIBNACCILINE));
	wcex.hCursor		= LoadCursor(NULL, IDC_ARROW);
	wcex.hbrBackground	= (HBRUSH)(COLOR_WINDOW+1);
	wcex.lpszMenuName	= MAKEINTRESOURCE(IDC_DRAWFIBNACCILINE);
	wcex.lpszClassName	= szWindowClass;
	wcex.hIconSm		= LoadIcon(wcex.hInstance, MAKEINTRESOURCE(IDI_SMALL));

	return RegisterClassEx(&wcex);
}

//
//   函数: InitInstance(HINSTANCE, int)
//
//   目的: 保存实例句柄并创建主窗口
//
//   注释:
//
//        在此函数中，我们在全局变量中保存实例句柄并
//        创建和显示主程序窗口。
//
BOOL InitInstance(HINSTANCE hInstance, int nCmdShow)
{
   HWND hWnd;

   hInst = hInstance; // 将实例句柄存储在全局变量中

   hWnd = CreateWindow(szWindowClass, szTitle, WS_OVERLAPPEDWINDOW,
      CW_USEDEFAULT, 0, CW_USEDEFAULT, 0, NULL, NULL, hInstance, NULL);

   if (!hWnd)
   {
      return FALSE;
   }

   // 算出斐波那契数列，存入数组
   fibnacciNum[0] = 1;
   fibnacciNum[1] = 1;
   for (int i = 2; i < FIBNACCIARRAY_MAXNUM; ++i)
   {
		fibnacciNum[i] = fibnacciNum[i - 1] + fibnacciNum[i - 2];
   }

   ShowWindow(hWnd, nCmdShow);
   UpdateWindow(hWnd);

   return TRUE;
}

//
//  函数: WndProc(HWND, UINT, WPARAM, LPARAM)
//
//  目的: 处理主窗口的消息。
//
//  WM_COMMAND	- 处理应用程序菜单
//  WM_PAINT	- 绘制主窗口
//  WM_DESTROY	- 发送退出消息并返回
//
//
LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
	int wmId, wmEvent;
	PAINTSTRUCT ps;
	HDC hdc;
	POINT startPoint;
	Quadrant quadrant = SECOND_Q;
	Direction direction = ANTICLOCKWISE;

	startPoint.x = X_CENTER;
	startPoint.y = Y_CENTER;

	switch (message)
	{
	case WM_COMMAND:
		wmId    = LOWORD(wParam);
		wmEvent = HIWORD(wParam);
		// 分析菜单选择:
		switch (wmId)
		{
		case IDM_ABOUT:
			DialogBox(hInst, MAKEINTRESOURCE(IDD_ABOUTBOX), hWnd, About);
			break;
		case IDM_EXIT:
			DestroyWindow(hWnd);
			break;
		default:
			return DefWindowProc(hWnd, message, wParam, lParam);
		}
		break;
	case WM_PAINT:
		hdc = BeginPaint(hWnd, &ps);
		// TODO: 在此添加任意绘图代码...
		DrawFLine(hdc, startPoint, quadrant, direction);
		EndPaint(hWnd, &ps);
		break;
	case WM_DESTROY:
		PostQuitMessage(0);
		break;
	default:
		return DefWindowProc(hWnd, message, wParam, lParam);
	}
	return 0;
}

// “关于”框的消息处理程序。
INT_PTR CALLBACK About(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
	UNREFERENCED_PARAMETER(lParam);
	switch (message)
	{
	case WM_INITDIALOG:
		return (INT_PTR)TRUE;

	case WM_COMMAND:
		if (LOWORD(wParam) == IDOK || LOWORD(wParam) == IDCANCEL)
		{
			EndDialog(hDlg, LOWORD(wParam));
			return (INT_PTR)TRUE;
		}
		break;
	}
	return (INT_PTR)FALSE;
}

// 画出斐波那契螺旋线
void DrawFLine(HDC hdc, POINT startPoint, Quadrant quadrant, Direction direction)
{
	int radius = 0;

	for (int i = 0; i < FIBNACCIARRAY_MAXNUM; i++)
	{
		radius = fibnacciNum[i];

		startPoint = DrawQuarterArc(hdc, startPoint, radius, quadrant, direction);
		if (CLOCKWISE == direction)
		{
			quadrant = (Quadrant)((quadrant + 3) % 4);
		}
		else
		{
			quadrant = (Quadrant)((quadrant + 1) % 4);
		}
	}

}

// 根据半径、中心点和类型画四分之一圆弧
void DrawQuarterArc(HDC hdc, int radius, int xPoint, int yPoint, int style)
{
	POINT p1, p2, p3, p4, pStart, pEnd;
	p1.x = xPoint + radius;
	p1.y = yPoint;
	p2.x = xPoint;
	p2.y = yPoint - radius;
	p3.x = xPoint - radius;
	p3.y = yPoint;
	p4.x = xPoint;
	p4.y = yPoint + radius;
	switch (style)
	{
	case 0:
		pStart = p1;
		pEnd = p2;
		break;
	case 1:
		pStart = p2;
		pEnd = p3;
		break;
	case 2:
		pStart  = p3;
		pEnd = p4;
		break;
	case 3:
		pStart = p4;
		pEnd = p1;
		break;
	default:
		return;
	}

	Arc(hdc, xPoint - radius, yPoint - radius, xPoint + radius, yPoint + radius, pStart.x, pStart.y, pEnd.x, pEnd.y);
}

/************************************************************
*函数名：	DrawQuarterArc
*功能：		根据起始点、半径、象限和顺/逆时针方向画四分之一圆弧
*返回值：	圆弧的结束点
*参数：		startPoint	圆弧开始点
			radius		圆弧所在圆的半径
			quadrant	以圆弧开始点为原点，圆弧所在的象限
			direction	圆弧从开始点到结束点的方向（顺时针/逆时针）
*时间：		2012/6/7
*************************************************************/
POINT DrawQuarterArc(HDC hdc, POINT startPoint, int radius, Quadrant quadrant, Direction direction)
{
	POINT endPoint;		// 圆弧结束点
	POINT tlPoint;		// 圆弧所在矩形的左上角点
	POINT brPoint;		// 圆弧所在矩形的右下角点

	if (CLOCKWISE == direction)
	{
		if (FIRST_Q == quadrant)
		{
			endPoint.x = startPoint.x + radius;
			endPoint.y = startPoint.y - radius;
			tlPoint.x = startPoint.x;
			tlPoint.y = startPoint.y - radius;
		}
		else if(SECOND_Q == quadrant)
		{
			endPoint.x = startPoint.x - radius;
			endPoint.y = startPoint.y - radius;
			tlPoint.x = startPoint.x - radius;
			tlPoint.y = startPoint.y -  2 * radius;
		}
		else if(THIRD_Q == quadrant)
		{
			endPoint.x = startPoint.x - radius;
			endPoint.y = startPoint.y + radius;
			tlPoint.x = startPoint.x - 2 * radius;
			tlPoint.y = startPoint.y -  radius;
		}
		else
		{
			endPoint.x = startPoint.x + radius;
			endPoint.y = startPoint.y + radius;
			tlPoint.x = startPoint.x - radius;
			tlPoint.y = startPoint.y;
		}
		brPoint.x = tlPoint.x + 2 * radius;
		brPoint.y = tlPoint.y + 2 * radius;

		Arc(hdc,tlPoint.x, tlPoint.y, brPoint.x, brPoint.y, endPoint.x, endPoint.y, startPoint.x, startPoint.y);
	}
	else
	{
		if (FIRST_Q == quadrant)
		{
			endPoint.x = startPoint.x + radius;
			endPoint.y = startPoint.y - radius;
			tlPoint.x = startPoint.x - radius;
			tlPoint.y = startPoint.y -  2 * radius;
		}
		else if(SECOND_Q == quadrant)
		{
			endPoint.x = startPoint.x - radius;
			endPoint.y = startPoint.y - radius;
			tlPoint.x = startPoint.x - 2 * radius;
			tlPoint.y = startPoint.y -  radius;
		}
		else if(THIRD_Q == quadrant)
		{
			endPoint.x = startPoint.x - radius;
			endPoint.y = startPoint.y + radius;
			tlPoint.x = startPoint.x - radius;
			tlPoint.y = startPoint.y;
		}
		else
		{
			endPoint.x = startPoint.x + radius;
			endPoint.y = startPoint.y + radius;
			tlPoint.x = startPoint.x;
			tlPoint.y = startPoint.y - radius;
		}
		brPoint.x = tlPoint.x + 2 * radius;
		brPoint.y = tlPoint.y + 2 * radius;

		Arc(hdc,tlPoint.x, tlPoint.y, brPoint.x, brPoint.y, startPoint.x, startPoint.y, endPoint.x, endPoint.y);
	}
	return endPoint;	
}
