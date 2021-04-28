#include "CipherConfig.h"

const static int BOOHEE_HASH_CODE = -2997946;

static bool mDebug = true;
static bool mAuth = false;
static jstring pkg;

CipherConfig::CipherConfig() {
}

CipherConfig::~CipherConfig() {
}

void CipherConfig::setDebug(bool isDebug) {
    mDebug = isDebug;
}

bool CipherConfig::isDebug() {
    return mDebug;
}

bool CipherConfig::isAuth() {
    return mAuth;
}

int CipherConfig::getHashCode() {
    return BOOHEE_HASH_CODE;
}

void CipherConfig::checkAppInfo(JNIEnv *env, jobject context) {
    jclass jclazz = env->GetObjectClass(context);// 获得Context类
    jmethodID mid = env->GetMethodID(jclazz, "getPackageName",
                                     "()Ljava/lang/String;"); // 得到getPackageName方法的ID
    jstring packageName = (jstring) env->CallObjectMethod(context,
                                                          mid);// 获得当前应用包名
    pkg = packageName;

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
//    LOGI("Signatures signature: %s", env->GetStringUTFChars(signature, 0));

    mid = env->GetMethodID(jclazz, "hashCode", "()I");
    jint hashCode = env->CallIntMethod(sign, mid);

    if (CipherConfig::isDebug()) {
        LOGI("Signatures hashCode: %d", hashCode);
    }

    mAuth = (hashCode == CipherConfig::getHashCode());
}
