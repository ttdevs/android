#include <jni.h>
#include <string>

#include "utils/log.h"
#include "ios_base64/NSDataMKBase64.h"
#include "CipherUtils.h"

bool mAuth = false;
jint boohee = -2997946;
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

    jclass jclazz = env->GetObjectClass(context);// 获得Context类
    jmethodID mid = env->GetMethodID(jclazz, "getPackageName",
                                     "()Ljava/lang/String;"); // 得到getPackageName方法的ID
    jstring packageName = (jstring) env->CallObjectMethod(context,
                                                          mid);// 获得当前应用包名

    mid = env->GetMethodID(jclazz, "getPackageManager",
                           "()Landroid/content/pm/PackageManager;");// 得到getPackageManager方法的ID
    jobject pm = env->CallObjectMethod(context, mid);// 获得应用包的管理器
    jclazz = env->GetObjectClass(pm);// 获得PackageManager类

    // 得到getPackageInfo方法的ID
    mid = env->GetMethodID(jclazz, "getPackageInfo",
                           "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    // 获得应用包的信息,GET_SIGNATURES = 64;
    jobject packageInfo = env->CallObjectMethod(pm, mid, packageName, 0x40);
    jclazz = env->GetObjectClass(packageInfo);// 获得PackageInfo类
    // 获得签名数组属性的ID
    jfieldID fid = env->GetFieldID(jclazz, "signatures",
                                   "[Landroid/content/pm/Signature;");
    // 得到签名数组
    jobjectArray signatures = (jobjectArray) env->GetObjectField(packageInfo,
                                                                 fid);
    jobject sign = env->GetObjectArrayElement(signatures, 0);// 得到签名
    jclazz = env->GetObjectClass(sign);// 获得Signature类

    mid = env->GetMethodID(jclazz, "toCharsString", "()Ljava/lang/String;");
    jstring signature = (jstring) env->CallObjectMethod(sign, mid);// 返回当前应用签名信息
    LOGI("Signatures: %s", env->GetStringUTFChars(signature, 0));

    mid = env->GetMethodID(jclazz, "hashCode", "()I");
    jint hashCode = env->CallIntMethod(sign, mid);

    LOGI("Signatures: %d", hashCode);

    mAuth = (hashCode == boohee);
    return mAuth;
}

JNIEXPORT jstring JNICALL
Java_com_ttdevs_jniutils_CipherUtils_createCipherKey(JNIEnv *env, jclass) {

}