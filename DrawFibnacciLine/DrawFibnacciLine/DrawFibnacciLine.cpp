// DrawFibnacciLine.cpp : ����Ӧ�ó������ڵ㡣
//

#include "stdafx.h"
#include "DrawFibnacciLine.h"

#define MAX_LOADSTRING 100

// ȫ�ֱ���:
HINSTANCE hInst;								// ��ǰʵ��
TCHAR szTitle[MAX_LOADSTRING];					// �������ı�
TCHAR szWindowClass[MAX_LOADSTRING];			// ����������
int fibnacciNum[FIBNACCIARRAY_MAXNUM];			// 쳲���������

// �˴���ģ���а����ĺ�����ǰ������:
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

 	// TODO: �ڴ˷��ô��롣
	MSG msg;
	HACCEL hAccelTable;

	// ��ʼ��ȫ���ַ���
	LoadString(hInstance, IDS_APP_TITLE, szTitle, MAX_LOADSTRING);
	LoadString(hInstance, IDC_DRAWFIBNACCILINE, szWindowClass, MAX_LOADSTRING);
	MyRegisterClass(hInstance);

	// ִ��Ӧ�ó����ʼ��:
	if (!InitInstance (hInstance, nCmdShow))
	{
		return FALSE;
	}

	hAccelTable = LoadAccelerators(hInstance, MAKEINTRESOURCE(IDC_DRAWFIBNACCILINE));

	// ����Ϣѭ��:
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
//  ����: MyRegisterClass()
//
//  Ŀ��: ע�ᴰ���ࡣ
//
//  ע��:
//
//    ����ϣ��
//    �˴�������ӵ� Windows 95 �еġ�RegisterClassEx��
//    ����֮ǰ�� Win32 ϵͳ����ʱ������Ҫ�˺��������÷������ô˺���ʮ����Ҫ��
//    ����Ӧ�ó���Ϳ��Ի�ù�����
//    ����ʽ��ȷ�ġ�Сͼ�ꡣ
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
//   ����: InitInstance(HINSTANCE, int)
//
//   Ŀ��: ����ʵ�����������������
//
//   ע��:
//
//        �ڴ˺����У�������ȫ�ֱ����б���ʵ�������
//        ��������ʾ�����򴰿ڡ�
//
BOOL InitInstance(HINSTANCE hInstance, int nCmdShow)
{
   HWND hWnd;

   hInst = hInstance; // ��ʵ������洢��ȫ�ֱ�����

   hWnd = CreateWindow(szWindowClass, szTitle, WS_OVERLAPPEDWINDOW,
      CW_USEDEFAULT, 0, CW_USEDEFAULT, 0, NULL, NULL, hInstance, NULL);

   if (!hWnd)
   {
      return FALSE;
   }

   // ���쳲��������У���������
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
//  ����: WndProc(HWND, UINT, WPARAM, LPARAM)
//
//  Ŀ��: ���������ڵ���Ϣ��
//
//  WM_COMMAND	- ����Ӧ�ó���˵�
//  WM_PAINT	- ����������
//  WM_DESTROY	- �����˳���Ϣ������
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
		// �����˵�ѡ��:
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
		// TODO: �ڴ���������ͼ����...
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

// �����ڡ������Ϣ�������
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

// ����쳲�����������
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

// ���ݰ뾶�����ĵ�����ͻ��ķ�֮һԲ��
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
*��������	DrawQuarterArc
*���ܣ�		������ʼ�㡢�뾶�����޺�˳/��ʱ�뷽���ķ�֮һԲ��
*����ֵ��	Բ���Ľ�����
*������		startPoint	Բ����ʼ��
			radius		Բ������Բ�İ뾶
			quadrant	��Բ����ʼ��Ϊԭ�㣬Բ�����ڵ�����
			direction	Բ���ӿ�ʼ�㵽������ķ���˳ʱ��/��ʱ�룩
*ʱ�䣺		2012/6/7
*************************************************************/
POINT DrawQuarterArc(HDC hdc, POINT startPoint, int radius, Quadrant quadrant, Direction direction)
{
	POINT endPoint;		// Բ��������
	POINT tlPoint;		// Բ�����ھ��ε����Ͻǵ�
	POINT brPoint;		// Բ�����ھ��ε����½ǵ�

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
