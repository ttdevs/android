#include <jni.h>
// #include <string>

using namespace std;

#include "utils/log.h"
#include "base64.h"

#include "com_ttdevs_jniutils_CipherUtils.h"

JNIEXPORT jint JNICALL Java_com_ttdevs_jniutils_CipherUtils_add(JNIEnv *env, jclass type, jint x, jint y) {
    std::string input_str("\"{\\\"bucket\\\":\\\"onetest\\\",\\\"keys\\\":[\\\"/one/2016-08-09/160402b4-0470-49e0-b408-b580675cf910.png\\\",\\\"/bingo/2015-12-09/160402b4-0470-49e0-b408-b580675cf910.png\\\",\\\"/status/2014-10-09/160402b4-0470-49e0-b408-b580675cf910.png\\\",\\\"/food/2014-10-09/160402b4-0470-49e0-b408-b580675cf910.png\\\"]}\"");
    std::string base64_str, output_str;

    LOGE("%s", input_str.c_str());
    printf("input_strinput_str");

    Base64Encode(input_str, &base64_str);
    LOGE("%s", base64_str.c_str());
    Base64Decode(base64_str, &output_str);
    LOGE("%s", output_str.c_str());
    return x + y;
}
