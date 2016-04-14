#ifndef _FROG_20160309_QINIUCONGIG_H_
#define _FROG_20160309_QINIUCONGIG_H_

#ifdef __cplusplus
extern "C" {
#endif

class CQiniuConfig
{
public:
	CQiniuConfig();
	~CQiniuConfig();

public:

    static void setDebug(bool isDebug);

    static bool isDebug();

	static const char* getBucketKey(const char* szKey);

	static const char* GetAppKey();

	static const char* getSecretKey();

	static void getRequestBody(const char* szKey, char* bodyString);

	static void contextParams(const char* json, char* szContextParams);
	// 
	static void MakeContextJson(const char* szKey, char* szContextJson);

	static void signature(const char* strContextParams, char* szSign);

	static void encryptHMAC(const char* strContextParams, const char* key, char* szSign);

};

#ifdef __cplusplus
}
#endif
#endif // _FROG_20160309_QINIUCONGIG_H_