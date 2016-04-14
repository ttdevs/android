# NDK

## 下载NDK

### 下载

[NDK Download](http://developer.android.com/ndk/downloads/index.html)

[mac: android-ndk-r10e-linux-x86.bin](http://dl.google.com/android/ndk/android-ndk-r10e-linux-x86.bin)

### 安装

```shell
➜  ndk ls
android-ndk-r10e-darwin-x86_64.bin
➜  ndk chmod a+x android-ndk-r10e-darwin-x86_64.bin
➜  ndk ./android-ndk-r10e-darwin-x86_64.bin
```

## 配置环境

### gradle.properties

在末尾添加一行： `android.useDeprecatedNdk=true`

### 添加build工具

AS > Preferences > Tools > External Tools

#### add javah

- Name: javah
- Group: default(External Tools)
- Description: generate C header
- Options: select all
- Show in: select all
- Tools Settings:
	- Program: $JDKPath$/bin/javah
	- Parameters: -v -d $ModuleFileDir$/src/main/jni -jni $FileClass$
	- Working directory: $SourcepathEntry$ (PS:PS:right click on the class)

![项目结构](image-javah.png)

``` shell
➜  ~ javah
用法:
  javah [options] <classes>
其中, [options] 包括:
  -o <file>                输出文件 (只能使用 -d 或 -o 之一)
  -d <dir>                 输出目录
  -v  -verbose             启用详细输出
  -h  --help  -?           输出此消息
  -version                 输出版本信息
  -jni                     生成 JNI 样式的标头文件 (默认值)
  -force                   始终写入输出文件
  -classpath <path>        从中加载类的路径
  -bootclasspath <path>    从中加载引导类的路径
<classes> 是使用其全限定名称指定的
(例如, java.lang.Object)。
```

#### add ndkBuild

- Name: ndkBuild
- Group: default(External Tools)
- Description: ndk build
- Options: select all
- Show in: select all
- Tools Settings:
	- Program: /Users/ttdevs/android/android-ndk-r10e/ndk-build
	- Parameters: NDK_LIBS_OUT=$ModuleFileDir$/libs
	- Working directory: $ProjectFileDir$/ndkutil/src/main
	- Working directory: $ModuleFileDir$/src/main (PS:right click on the module name)

![项目结构](image-ndkbuild.png)

## 编写代码

### 新建一个Module：ndkutil

### 修改ndkutil的build.gradle

``` python
android {
    ...
    defaultConfig {
        ...
        ndk {
            moduleName "ndkutil"
//            abiFilters "armeabi-v7a", "x86"
//            stl "gnustl_static"
//            cFlags "-std=c++11 -Wall"
//            ldLibs "log", "jnigraphics", "EGL", "GLESv3"
        }
    }

    sourceSets.main {
        jni.srcDirs = []
        jniLibs.srcDir "libs"
    }
}
```

### 创建工具类CipherUtil

``` java
package com.ttdevs.ndk;
public class CipherUtil {
    static {
        System.loadLibrary("Cipher");
    }
    public static native String getCipherKey();
}
```

### 生成头文件com_ttdevs_ndk_CipherUtil.h

在刚才创建的CipherUtil.java上点击右键 > External Tools > javah
此时会在main目录下生成: jni/com_ttdevs_ndk_CipherUtil.h

### 编写代码
都是在ndkutil/src/main/jni目录下

- 创建C源码:Cipher.cpp

``` C++
#include "com_ttdevs_ndk_CipherUtil.h"

JNIEXPORT jstring JNICALL Java_com_ttdevs_ndk_CipherUtil_getCipherKey(JNIEnv *env, jclass){
    return (*env).NewStringUTF("Hello World!");
}
```
- 创建Android.mk

``` shell
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := Cipher
LOCAL_SRC_FILES := Cipher.cpp

include $(BUILD_SHARED_LIBRARY)
```

- 创建Application.mk

``` shell
APP_MODULE := Cipher
APP_ABI := all
```

### 编译生成so文件

在module（ndkutil）上点击右键 > External Tools > ndkBuild ，可以看到如下log：

``` log
/Users/ttdevs/android/android-ndk-r10d/ndk-build NDK_LIBS_OUT=/Users/ttdevs/android/workspace/Demo/ndkutil/libs
[arm64-v8a] Compile++      : Cipher <= Cipher.cpp
[arm64-v8a] SharedLibrary  : libCipher.so
[arm64-v8a] Install        : libCipher.so => /Users/ttdevs/android/workspace/Demo/ndkutil/libs/arm64-v8a/libCipher.so
[x86_64] Compile++      : Cipher <= Cipher.cpp
[x86_64] SharedLibrary  : libCipher.so
[x86_64] Install        : libCipher.so => /Users/ttdevs/android/workspace/Demo/ndkutil/libs/x86_64/libCipher.so
[mips64] Compile++      : Cipher <= Cipher.cpp
[mips64] SharedLibrary  : libCipher.so
[mips64] Install        : libCipher.so => /Users/ttdevs/android/workspace/Demo/ndkutil/libs/mips64/libCipher.so
[armeabi-v7a] Compile++ thumb: Cipher <= Cipher.cpp
[armeabi-v7a] SharedLibrary  : libCipher.so
[armeabi-v7a] Install        : libCipher.so => /Users/ttdevs/android/workspace/Demo/ndkutil/libs/armeabi-v7a/libCipher.so
[armeabi] Compile++ thumb: Cipher <= Cipher.cpp
[armeabi] SharedLibrary  : libCipher.so
[armeabi] Install        : libCipher.so => /Users/ttdevs/android/workspace/Demo/ndkutil/libs/armeabi/libCipher.so
[x86] Compile++      : Cipher <= Cipher.cpp
[x86] SharedLibrary  : libCipher.so
[x86] Install        : libCipher.so => /Users/ttdevs/android/workspace/Demo/ndkutil/libs/x86/libCipher.so
[mips] Compile++      : Cipher <= Cipher.cpp
[mips] SharedLibrary  : libCipher.so
[mips] Install        : libCipher.so => /Users/ttdevs/android/workspace/Demo/ndkutil/libs/mips/libCipher.so

Process finished with exit code 0
```

此时我们可以发现在ndkutil/libs/目录下生成的so文件。

### 测试代码

``` java
String cipher = CipherUtil.getCipherKey();
```

## 项目结构

![项目结构](image-dir.png)

