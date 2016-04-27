#include <jni.h>
#include "string"
#include "utils/log.h"

class CipherConfig {

public:

    CipherConfig();

    ~CipherConfig();


public:

    void static setDebug(bool isDebug);

    bool static isDebug();

    bool static isAuth();

    int static getHashCode();

    void static checkAppInfo(JNIEnv *env, jobject context);
};

