// secret_key.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include <string>
#include "QiniuConfig.h"

int _tmain(int argc, _TCHAR* argv[])
{
	char bodyString[4096] = { 0 };
	CQiniuConfig::getRequestBody("a\\/2016\\/03\\/14\\/20c15011-8984-4bc2-a349-623ba5a4c4e3", bodyString);

	return 0;
}

