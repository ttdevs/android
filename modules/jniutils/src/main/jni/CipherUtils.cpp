#include <jni.h>
#include <string.h>

#include "utils/log.h"
#include "ios_base64/NSDataMKBase64.h"
#include "CipherUtils.h"
#include "CipherConfig.h"

bool mAuth = false;
jint ttdevs = 1765215831;

JNIEXPORT jstring JNICALL
Java_com_ttdevs_jniutils_CipherUtils_base64Encode(JNIEnv *env, jclass type,
                                                  jstring dataString_) {
    if (!mAuth) {
        return env->NewStringUTF("haha");
    }

    const char *dataString = env->GetStringUTFChars(dataString_, 0);
    LOGE("Input from java: %s", dataString);

    int szOutString = 0;
    char *outputBuffer = mk_NewBase64Encode(dataString,
                                            strlen(dataString),
                                            false,
                                            &szOutString);
    LOGE("output String: %s", outputBuffer);
    LOGE("szOutString lenght: %d", szOutString);

    env->ReleaseStringUTFChars(dataString_, dataString);

    return env->NewStringUTF(dataString);
}

JNIEXPORT jboolean JNICALL
Java_com_ttdevs_jniutils_CipherUtils_authenticate(JNIEnv *env, jclass type,
                                                  jobject context) {

    CipherConfig::checkAppInfo(env, context);

    return CipherConfig::isAuth();
}

JNIEXPORT jstring JNICALL
Java_com_ttdevs_jniutils_CipherUtils_createCipherKey(JNIEnv *env, jclass) {

}