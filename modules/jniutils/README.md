# Android Studio 2.0 NDK开发

## 0x00 前情回顾

前不久写了一篇环境搭建，虽然可以跑一个demo，但是如果你跟着做的话就会发现，仅仅是可以跑一个Demo，真做起来很是蛋疼。编写源码，生成so，跑App，再改代码，再生成so，再跑app。而且编写代码没有提示，不能格式化，各种不能，就是最最基本的文本开发，过度依赖IDE的我们怎么受得了。经过一段时间折腾和Android Studio 2.0的release，发现了更完美的配置方式，而且不用编写Android.mk和Application.mk。看完这篇，你会发现JNI的开发也是如此的so easy。

## 0x01 环境搭建

所需环境：

1. Android Studio 2.0
2. Gradle 2.10（最低要求）
3. NDK：ndk-r11c（目前最新版：2016-04-16）
4. LLDB：2.1（目前最新版：2016-04-16）

下面开始正式操作：

- 安装Android Studio 2.0

	这个就不细说了。
	
- 配置Gradle

	最低要求Gradle版本为2.10，目前最新版本为2.12。修改方法：
	
	`项目根目录/gradle/wrapper/gradle-wrapper.properties`
	
	将最下面的一行改成你要的版本，如：
	
	`distributionUrl=https\://services.gradle.org/distributions/gradle-2.10-all.zip`
	
- 安装NDK

	安装NDK有两种方式，一种是直接下载最新的NDK安装文件进行安装，另一种是通过SDK安装：
	
	1. NDK安装文件
	
		下载参考[这里](http://developer.android.com/ndk/downloads/index.html)。根据你的操作系统下载不同版本。Mac系统[android-ndk-r10e-linux-x86.bin](http://dl.google.com/android/ndk/android-ndk-r10e-linux-x86.bin)的安装如下：
		
        ``` shell
        ➜  ndk ls
        android-ndk-r10e-darwin-x86_64.bin
        ➜  ndk chmod a+x android-ndk-r10e-darwin-x86_64.bin
        ➜  ndk ./android-ndk-r10e-darwin-x86_64.bin
        ```
	
	2. 通过SDK安装
	
		`Android Studio > Preferences > Appearance & Behavior > Syetem Settings > Android SDK > SDK Tools`
		
		在这个Tab中找到 `Android NDK`，选中安装即可。安装之后我们可以在 Android SDK 的目录下看到一个 `ndk-bundle` 目录，这个就是我们的NDK目录。
	
- LLDB

	和 GDB 类似，[LLDB](http://lldb.llvm.org/)  是一款调试器，可以调试我们的JNI代码。同上打开SDK设置界面：
	
	`Android Studio > Preferences > Appearance & Behavior > Syetem Settings > Android SDK > SDK Tools`
	
	在这个Tab中找到 `LLDB`，选中安装即可。安装之后我们可以在 Android SDK 的目录下看到一个 `lldb` 目录，这个就是我们的 LLDB 目录。LLDB的更多信息可参考[这里](http://lldb.llvm.org/)。
	
通过上面的配置，接下来就可以开始我们的JNI的开发了。 可能你会问之前我有介绍过一篇开发环境的搭建，这个有什么不同呢？如果你使用之前的方法，你需要配置 Android.mk、Application.mk，无代码自动完成，不能代码个时候，运行调试麻烦，等等。这些问题在这里都不存在了，让我们把更多的精力放到代码编写中。

## 0x02 测试Demo

新建代码CipherUtils.java：

``` java
public class CipherUtils {
    static {
        System.loadLibrary("CipherUtils");
    }
    public static native int add(int x, int y);
}
```

然后可以通过javah命令生成对应的 `.h` 文件。令人兴奋的是，Android Studio可以帮我们自动生成 C 代码。点击 CipherUtils 中的`add` 方法，Option ＋ Return 即可生成一个 `CipherUtils.c`文件，由于我打算使用C++，因此将扩展名改为 `cpp` ，同时添加头文件，最终如下：

``` c++
#include <jni.h>
#include <string>
#include "utils/log.h"

JNIEXPORT jint JNICALL Java_com_ttdevs_jniutils_CipherUtils_add(JNIEnv *env, jclass type, jint x, jint y) {
    std::string input_str("Test add two number!");
    LOGE("%s", input_str.c_str());
    return x + y;
}
```

`PS：如果报错，可是将错误的 cpp文件拷贝出来再拷贝回去。`

build.gradle 配置

``` gradle
android {
    ......
    defaultConfig {
        ......
        ndk {
            moduleName "CipherUtils"
            stl "gnustl_static" // stlport_static
            // cFlags "-std=c++11 -Wall"
            abiFilters "armeabi", "armeabi-v7a"
            ldLibs "log", "android"
        }
    }
    sourceSets.main {
        // 如果不写(jni.srcDirs = ['src/main/jni/'])
        // Android展示模式没有jni目录
        // this file has been added after the last project sync with gradle
        // 必须有值,如: 'src/main/jni/'
        jni.srcDirs = ['src/main/jni/']
        // jniLibs.srcDir "libs"
    }
    buildTypes {
        debug {
            jniDebuggable = true
        }
        ......
    }
    ......
}
```

直接点击运行按钮，即可跑我们的测试程序了。就这么简单。

## 0x03 JNI层的Debug

这个同样简单。首先需要在Application的build.gradle中配置一下：

``` gradle
android {
    ......
    buildTypes {
        debug {
            jniDebuggable = true
        }
        ......
    }
}
```

在 `C/C++` 源文件上添加断点，点击debug，就可以像debug我们的java代码一样调试我们的原生代码了。
