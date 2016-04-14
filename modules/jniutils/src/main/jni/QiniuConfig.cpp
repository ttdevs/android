
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "QiniuConfig.h"
#include "FrogBase64.h"
#include "FrogSha1.h"

#include <algorithm>
using namespace std;

#ifndef BYTE
typedef unsigned char       BYTE;
#endif

const int MaxJsonLen = 4096;

const char* DEV_KEY = "d154bc1a7f6e4581";
const char* PRO_KEY = "e9735690d8fedf34";

const char* DEV_SECRET = "14588f1bdda13a05510e15a8c2b657c13ed979f2";
const char* PRO_SECRET = "1c34b98ff74f965de7ab4b614078c6b5c50d1562";

const char* QA_BUCKET_DEFAULT       = "onetest";
const char* QA_BUCKET_SITE          = "boheqa-site";
const char* PRO_BUCKET_DEFAULT      = "bohe-one";

const std::string CASE_FOOD 	    = "food";

const char* HMAC_SHA1 = "HmacSHA1";

bool mDebug = 1;

CQiniuConfig::CQiniuConfig()
{

}

CQiniuConfig::~CQiniuConfig()
{

}

int getPosition(const char *array, size_t size, char c)
{
  const char* end = array + size;
  const char* match = std::find(array, end, c);
  return (end == match)? -1 : (match-array);
}

void CQiniuConfig::setDebug(bool isDebug)
{
	mDebug = isDebug;
}

bool CQiniuConfig::isDebug()
{
	return mDebug;
}

const char* CQiniuConfig::getBucketKey(const char* szKey)
{
    std::string strKey(szKey);
	std::string strType = strKey.substr(0, strKey.find_first_of('\\'));
    if (mDebug)
    {
        if (strType == CASE_FOOD)
        {
            return QA_BUCKET_SITE;
        }
        else
        {
            return QA_BUCKET_DEFAULT;
        }
    }
    return PRO_BUCKET_DEFAULT;
}

const char* CQiniuConfig::GetAppKey()
{
	return mDebug ? DEV_KEY : PRO_KEY;
}

const char* CQiniuConfig::getSecretKey()
{
	return mDebug ? DEV_SECRET : PRO_SECRET;
}


void CQiniuConfig::MakeContextJson(const char* szKey, char* szContextJson)
{
	sprintf(szContextJson, "{\"bucket\":\"%s\",\"key\":\"%s\"}", getBucketKey(szKey), szKey);
}

void CQiniuConfig::getRequestBody(const char* szKey, char* bodyString)
{
	char szContextJson[MaxJsonLen] = { 0 };
	char szContextParams[MaxJsonLen] = { 0 };
	char szSign[MaxJsonLen] = { 0 };
	MakeContextJson(szKey, szContextJson);
	contextParams((const char*)szContextJson, szContextParams);
	signature(szContextParams, szSign);
	sprintf(bodyString, "{\"context_params\":\"%s\",\"sign\":\"%s\"}", szContextParams, szSign);
}

void CQiniuConfig::contextParams(const char* json, char* szContextParams)
{
	unsigned long nLen = MaxJsonLen;
	CFrogBase64::Encode((const unsigned char*)json, strlen(json), (unsigned char*)szContextParams, &nLen);
}

void CQiniuConfig::signature(const char* strContextParams, char* szSign)
{
	char szSrc[MaxJsonLen] = { 0 };
	sprintf((char*)szSrc, "%s%s", GetAppKey(), strContextParams);
	encryptHMAC((const char*)szSrc, getSecretKey(), szSign);
}


void CQiniuConfig::encryptHMAC(const char* strContextParams, const char* key, char* szSign)
{
	const char* strAppKey = getSecretKey();
	char szHmacSha1[21];

	hmac_sha1((unsigned char*)strAppKey, strlen(strAppKey), (unsigned char*)strContextParams, strlen(strContextParams), (BYTE*)szHmacSha1);
	unsigned long nLen = MaxJsonLen;
	CFrogBase64::Encode((const unsigned char*)szHmacSha1, 20, (unsigned char*)szSign, &nLen);
}