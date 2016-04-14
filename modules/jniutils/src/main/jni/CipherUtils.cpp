//
// Created by ttdevs on 16/2/21.
//
//#include <iostream>
#include "base64.h"

#include <algorithm>
using namespace std;

#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include "QiniuConfig.h"
#include <jni.h>
#include <unistd.h>

#include <android/log.h>
#define LOG_TAG "System.out"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

#include "com_ttdevs_jniutils_CipherUtils.h"


jstring getPackname(JNIEnv *env, jobject clazz, jobject obj)
{
    jclass native_class = env->GetObjectClass(obj);
    jmethodID mId = env->GetMethodID(native_class, "getPackageName", "()Ljava/lang/String;");
    jstring packName = static_cast<jstring>(env->CallObjectMethod(obj, mId));
    return packName;
}

JNIEXPORT jstring JNICALL Java_com_ttdevs_jniutils_CipherUtils_getCipherKey(JNIEnv *env, jclass clazz, jobject obj)
{
    // jclass native_class = env->GetObjectClass(obj);
    jmethodID instanceMethodId = env->GetMethodID(clazz, "getName", "()Ljava/lang/String;");

    jstring pkg = getPackname(env, clazz, obj);
    LOGD(">>>>>JNI_日志 body: %s", (char*)pkg);

    // jstring resutl = env->CallObjectMethod(clazz, instanceMethodId);

    // return (*env).NewStringUTF("Hello World! getCipherKey");
    return env->NewStringUTF("Hello World! getCipherKey");
    // return env->NewStringUTF(pkg);
}


JNIEXPORT jstring JNICALL Java_com_ttdevs_jniutils_CipherUtils_signature(JNIEnv *env, jclass, jstring jstr)
{
    char* szKey = (char*)env->GetStringUTFChars(jstr,0);
    LOGD(">>>>>JNI_日志 param: %s", szKey);

    // const char* szKey = "a\\/2016\\/03\\/15\\/8ec9e8e6-4dba-4627-aecf-57cfdf316637";
   	char bodyString[4096] = { 0 };
    CQiniuConfig::getRequestBody(szKey, bodyString);
    LOGD(">>>>>JNI_日志 body: %s", bodyString);

    return (*env).NewStringUTF(bodyString);
}

JNIEXPORT jint JNICALL Java_com_ttdevs_jniutils_CipherUtils_add(JNIEnv *, jclass, jint x, jint y)
{
    string input_str("MoreWindows - http://blog.csdn.net/morewindows");
    string base64_str, output_str;

    LOGD(">>>>>JNI_日志 input_str: %s", "aaaa");
    LOGD(">>>>>JNI_日志 input_str: %s", input_str.c_str());
    base64 tool;
    tool.Base64Encode(input_str, &base64_str);
    // cout<<"encode: \n"<<base64_str<<endl;

    // Base64Decode(base64_str, &output_str);
    // cout<<"decode: \n"<<output_str<<endl;
    return x + y;
}

/*
 * Class:     com_boohee_cipher_BooheeCipher
 * Method:    setModule
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_ttdevs_jniutils_CipherUtils_setModule(JNIEnv *env, jclass, jboolean jbool)
{
    // CQiniuConfig::setDebug(jbool == JNI_TRUE);

    LOGD("*****JNI***** setModule jbool: %s", (jbool == JNI_TRUE ? "true" : "false"));
}

JNIEXPORT void JNICALL Java_com_ttdevs_jniutils_CipherUtils_getAppSign(JNIEnv* env, jclass clazz, jobject obj){
    // 获得Context类
        jclass cls = env->GetObjectClass(obj);
        // 得到getPackageManager方法的ID
        jmethodID mid = env->GetMethodID(cls, "getPackageManager", "()Landroid/content/pm/PackageManager;");
        // 获得应用包的管理器
        jobject pm = env->CallObjectMethod(obj, mid);
        // 得到getPackageName方法的ID
        mid = env->GetMethodID(cls, "getPackageName", "()Ljava/lang/String;");
        // 获得当前应用包名
        jstring packageName = (jstring)env->CallObjectMethod(obj, mid);
        // 获得PackageManager类
        cls = env->GetObjectClass(pm);
        // 得到getPackageInfo方法的ID
        mid  = env->GetMethodID(cls, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
        // 获得应用包的信息
        jobject packageInfo = env->CallObjectMethod(pm, mid, packageName, 0x40); //GET_SIGNATURES = 64;
        // 获得PackageInfo 类
        cls = env->GetObjectClass(packageInfo);
        // 获得签名数组属性的ID
        jfieldID fid = env->GetFieldID(cls, "signatures", "[Landroid/content/pm/Signature;");
        // 得到签名数组
        jobjectArray signatures = (jobjectArray)env->GetObjectField(packageInfo, fid);
        // 得到签名
        jobject sign = env->GetObjectArrayElement(signatures, 0);
        // 获得Signature类
        cls = env->GetObjectClass(sign);
        // 得到toCharsString方法的ID
        mid = env->GetMethodID(cls, "toCharsString", "()Ljava/lang/String;");
        // 返回当前应用签名信息
        jstring signature = (jstring)env->CallObjectMethod(sign, mid);

        LOGD("JNI >>>>> Signatures: %s", env->GetStringUTFChars(signature , 0));
}