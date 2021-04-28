# Android Demo By ttdevs

这是一个关于Android的Demo，主要用于记录日常使用的代码。仅供参考！

## 项目结构

### Android

``` shell
.
├── app
│   ├── README.md
│   ├── app.iml
│   ├── build
│   ├── build.gradle
│   ├── proguard-rules.pro
│   └── src
├── apps
│   ├── air
│   ├── apps.iml
│   └── dagger
├── modules
│   ├── annotations
│   ├── guava
│   ├── hybrid
│   ├── indicator
│   ├── jniutils
│   ├── markdown
│   ├── modules.iml
│   ├── reactive
│   ├── retrofit
│   └── webscoket
├── build.gradle
├── config.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
├── CONTRIBUTING.md
├── LICENSE
├── README.md
├── android.iml
├── android.jks
├── doc
│   └── image
├── gradle
│   └── wrapper
└── settings.gradle
```

## Application

### [air][air_md]

## Module

### [indicator][indicator_md]

一个自定义View，展示效果如下：

![indicator_line](modules/indicator/indicator_line.png)

![indicator_progress](modules/indicator/indicator_progress.png)

### [jniutils][jniutils_md]

### [markdown][markdown_md]

### [reactive][reactive_md]

## Tools

### [Conceal][conceal_md]

[Conceal][conceal_src]，一个本地文件加密工具类。

### [Endless][endless_md]

自己实现的 `RecycleView` 的onLoadMore。

------
[air_md]:apps/air/README.md
[indicator_md]:modules/indicator/README.md
[rxjava_src]:https://github.com/ReactiveX/RxJava
[sqlcipher_md]:apps/sqlcipher/README.md
[sqlcipher_src]:https://github.com/sqlcipher/android-database-sqlcipher
[sqlcipher_blog]:http://blog.csdn.net/ttdevs/article/details/50700630
[conceal_md]:app/src/main/java/com/ttdevs/android/conceal/README.md
[conceal_src]:https://github.com/facebook/conceal
[endless_md]:app/src/main/java/com/ttdevs/android/endless/README.md
[endless_blog]:http://blog.csdn.net/ttdevs/article/details/48194885
[jniutils_md]:modules/jniutils/README.md
[markdown_md]:modules/markdown/README.md
[reactive_md]:modules/reactive/README.md

