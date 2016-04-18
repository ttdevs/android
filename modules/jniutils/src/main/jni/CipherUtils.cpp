#include <jni.h>
#include <string>

#include "utils/log.h"
#include "ios_base64/NSDataMKBase64.h"
#include "com_ttdevs_jniutils_CipherUtils.h"

JNIEXPORT void JNICALL
Java_com_ttdevs_jniutils_CipherUtils_base64Encode(JNIEnv *env, jclass type, jstring dataString_) {
    const char *dataString = env->GetStringUTFChars(dataString_, 0);
    LOGE("Input from java: %s", dataString);
    env->ReleaseStringUTFChars(dataString_, dataString);

    int szOutString = 0;
    char *outputBuffer = mk_NewBase64Encode(dataString,
                                            strlen(dataString),
                                            false,
                                            &szOutString);
    LOGE("output String: %s", outputBuffer);
    LOGE("szOutString lenght: %d", szOutString);
}