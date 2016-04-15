#include <stdlib.h>
#include <string>

using namespace std;

#include "com_ttdevs_jniutils_CipherUtils.h"
#include "utils/log.h"

JNIEXPORT jint JNICALL Java_com_ttdevs_jniutils_CipherUtils_add(JNIEnv *, jclass, jint x, jint y) {
    std::string input_str("MoreWindows-http://blog.csdn.net/morewindows");
    std::string base64_str, output_str;

    LOGE("%s", input_str.c_str());
    printf("input_strinput_str");

    // __android_log_print(ANDROID_LOG_INFO,LOG_TAG, "aaa");
    // Base64Encode(input_str, &base64_str);
    // cout<<"encode: \n"<<base64_str<<endl;
    // __android_log_print(ANDROID_LOG_INFO,LOG_TAG,base64_str.c_str());
    // Base64Decode(base64_str, &output_str);
    // cout<<"decode: \n"<<output_str<<endl;
    // __android_log_print(ANDROID_LOG_INFO,LOG_TAG,"Call stringFromJNI!\n");
    return x + y;
}