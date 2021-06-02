# 自定义注解

[TOC]

> 需求：有一组功能模块，每个功能模块负责处理一种具体功能且有一个唯一的标识；这些功能模块随项目迭代会有动态的修改、增加或者删除。

- 如果是你会如何设计实现这个需求？
我可能会这样做：新建一个功能模块管理类，管理类中预加载所有的功能模块；提供一个方法，可以根据标识获取具体的功能模块；然后就可以调用功能模块的具体方法了。

- 这样做有什么问题？
每增加一个功能模块你可能至少需要改两个地方甚至更多：创建具体的功能模块类；在功能模块管理类中加入新增的功能模块（此处可能要改一个地方以上）。

- 有没有更好的实现方式？
答案当然是有，比如只新建一个功能模块类，其他工作自动完成。下面介绍如果通过`编译时注解`的方式解决这个问题。

## 0x01 从一个例子开始

考虑到原理理解的难易，这里先给出解决方案：创建具体的功能模块类，在这些类上添加自定义的注解，注解上标识这个类可以处理哪些具体的功能。在程序编译期根据这些注解自动生成一个功能模块管理类。使用时直接调用此功能管理类即可（此管理类和我们手动创建的一样）。若有新的功能模块加入（或者移除），我们只需要创建（或者删除）对应功能模块类即可，只改这一个地方。

下面以一个具体的例子说明这个问题。

**有一个班级，包含若干个学生，每个学生有姓名和年龄，同时还有一个对应的职责，如班长，语文课代表，数学课代表，体育课代表等。班长负责管理班级，课代表负责收作业等。这个班级可能有同学会退学，也可能有新的同学加入。老师通过一个管理类来管理这个班级的所有学生。**

### 创建项目

创建一个Android项目：`DemoAnnotation`，在`DemoAnnotation`中创建两个`Java Library`: `lib_annotation` 和 `lib_compiler`，然后分别配置其`build.gradle`。

``` shell
➜  DemoAnnotation git:(master) ✗ tree -L 1
.
├── app
├── lib_annotation
└── lib_compiler
```

> 以下全部使用Java 8

#### DemoAnnotation

``` shell
plugins {
    id 'com.android.application'
}
android {
    ...
    defaultConfig {
        applicationId "com.ttdevs.demo.annotation"
        ...
        javaCompileOptions {
            annotationProcessorOptions {
                argument "debug", "true"
                argument "param1", "value1"
            }
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
dependencies {
    ...
    implementation project(path: ':lib_annotation')
    annotationProcessor project(path: ':lib_compiler')
}
```

> 引入两个library，注意一个是implementation，另一个是annotationProcessor。
    
#### lib_annotation

``` shell
plugins {
    id 'java-library'
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
```

#### lib_compiler

``` shell
plugins {
    id 'java-library'
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
dependencies {
    implementation project(path: ':lib_annotation')
    implementation 'com.squareup:javapoet:1.13.0'
    implementation 'com.google.auto.service:auto-service-annotations:1.0'
    annotationProcessor 'com.google.auto.service:auto-service:1.0'
}
```

> 通过`implementation 'com.google.auto.service:auto-service-annotations:1.0'`引入`@AutoService(Processor.class)`。

### lib_annotation中创建注解类

这里创建一个叫`Student`的注解，包含姓名，年龄，职责，如下：

``` shell
package com.ttdevs.demo.lib.annotation;
...
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Student {
    String name();
    int age() default 8;
    /**
     * Duty information, ClassMonitor, Chinese, Math, Sport, Art etc.
     *
     * @return
     */
    String[] duty() default {};
}
```

### lib_compiler中处理注解

创建`StudentProcessor`类，继承`AbstractProcessor`，其上添加注解`@AutoService(Processor.class)`，代码如下：

``` java
package com.ttdevs.demo.lib.compiler;
...
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions({StudentProcessor.OPTIONS_PARAM_DEBUG})
public class StudentProcessor extends AbstractProcessor {
    protected static final String OPTIONS_PARAM_DEBUG = "debug";

    private Filer mFiler;
    private Elements mElements; // source file
    private Map<String, Element> mClassMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        LogUtils.init(processingEnv.getMessager());
        mFiler = processingEnv.getFiler();
        mElements = processingEnv.getElementUtils();

        LogUtils.d("Init debug: " + processingEnv.getOptions().get(OPTIONS_PARAM_DEBUG));
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(ClassUtils.CLASS_STUDENT.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        LogUtils.d(String.format("=========Process %s============", !roundEnv.processingOver() ? "start" : "  end"));

        for (Element item : roundEnv.getRootElements()) {
            LogUtils.d("Process Class: " + item.getSimpleName());
        }

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ClassUtils.CLASS_STUDENT);
        if (null != elements && !elements.isEmpty()) {
            for (Element element : elements) {
                if (element.getKind() == ElementKind.CLASS) {
                    Student student = element.getAnnotation(ClassUtils.CLASS_STUDENT);
                    mClassMap.put(student.name(), element);
                }
            }
            // Create StudentManager.java
            StudentManagerBuilder.create()
                    .filer(mFiler)
                    .build(mClassMap);
        }
        return true;
    }
}
```

不用太在意代码长度，只需关注关键点即可。下面对这个类做简要分析：

1. `@AutoService(Processor.class)`

    这个注解会在`META-INF/services/javax.annotation.processing.Processor`这个文件中添加一行，内容为当前类的完整路径。若你有多个注解处理器类，则会每个注解处理器都会在这个文件中占一行。别问为什么要这样做，问就是[javac](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/javac.html)规定的。
    ``` shell
    DemoAnnotation/lib_compiler
    └── build
        └── classes
            └── java
                └── main
                    └── META-INF
                        └── services
                            └── javax.annotation.processing.Processor
    
    ➜  DemoAnnotation git:(master) ✗ cat lib_compiler/build/classes/java/main/META-INF/services/javax.annotation.processing.Processor
    com.ttdevs.demo.lib.compiler.StudentProcessor
    ➜  DemoAnnotation git:(master) ✗
    ```

1. 重写几个重要方法
    
    - `init(ProcessingEnvironment processingEnv)`
        
        初始化的配置，一般包含`Messager`，`Elements`和`Filer`。`Messager`用于打印Log。注解处理器创建之后此方法只会被调用一次。

    - `getSupportedSourceVersion()`
        
        配置源码的版本，等同于`@SupportedSourceVersion(SourceVersion.RELEASE_8)`。只在创建之后调用一次。

    - `getSupportedAnnotationTypes()`
        
        处理的注解类型，这里只有一个`Student`注解。只在创建之后调用一次。
        
    - `process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)`
        
        具体的处理逻辑，包含注解的处理，最终管理类的生成。每一轮注解处理此方法都会被调用。所以此方法会被调用多次。
        
1. 编译项目后会自动生成下面这个类
    
    ``` shell
    DemoAnnotation/app/build/generated/ap_generated_sources
        └── debug
            └── out
                └── com
                    └── ttdevs
                        └── demo
                            └── annotation
                                └── StudentManager.java
    ```
    
    此文件可以在自己的代码中直接调用，最终会和其他源码共同参与编译。具体如何生成参见后续介绍。

### app中添加数据

创建几个学生数据：

``` shell
DemoAnnotation/app/src/main/java
    └── com
        └── ttdevs
            └── demo
                └── annotation
                    ├── MainActivity.java
                    └── model
                        ├── BaseStudent.java
                        ├── David.java
                        ├── Harry.java
                        ├── Jason.java
                        └── Norris.java
```

重新编译项目，下面看一下具体生产的StudentManager代码：

``` java
package com.ttdevs.demo.annotation;
...
public class StudentManager {
    private static final Map<String, BaseStudent> MAP_STUDENT_NAME = new HashMap<>();
    private static final Map<String, BaseStudent> MAP_STUDENT_DUTY = new HashMap<>();

    public static final StudentManager INSTANCE = new StudentManager();

    private StudentManager() {
        BaseStudent tempNorris = new BaseStudent();
        tempNorris.name = "Norris";
        tempNorris.age = 28;
        MAP_STUDENT_NAME.put("Norris", tempNorris);
        BaseStudent tempHarry = new BaseStudent();
        tempHarry.name = "Harry";
        tempHarry.age = 30;
        tempHarry.duty = new java.lang.String[]{"Math"};
        MAP_STUDENT_NAME.put("Harry", tempHarry);
        BaseStudent tempDavid = new BaseStudent();
        tempDavid.name = "David";
        tempDavid.age = 50;
        tempDavid.duty = new java.lang.String[]{"ClassMonitor"};
        MAP_STUDENT_NAME.put("David", tempDavid);
        BaseStudent tempJason = new BaseStudent();
        tempJason.name = "Jason";
        tempJason.age = 20;
        tempJason.duty = new java.lang.String[]{"Chinese", "Sport"};
        MAP_STUDENT_NAME.put("Jason", tempJason);
        ;
        MAP_STUDENT_DUTY.put("Math", MAP_STUDENT_NAME.get("Harry"));
        MAP_STUDENT_DUTY.put("ClassMonitor", MAP_STUDENT_NAME.get("David"));
        MAP_STUDENT_DUTY.put("Chinese", MAP_STUDENT_NAME.get("Jason"));
        MAP_STUDENT_DUTY.put("Sport", MAP_STUDENT_NAME.get("Jason"));
    }

    /**
     * Get student by duty
     *
     * @param duty
     * @return
     */
    public BaseStudent getStudent(String duty) {
        return MAP_STUDENT_DUTY.get(duty);
    }

    public int exam() {
        int result = 0;
        for (String key : MAP_STUDENT_NAME.keySet()) {
            BaseStudent student = MAP_STUDENT_NAME.get(key);
            result += student.exam();
        }
        return result / MAP_STUDENT_NAME.size();
    }

    public void study() {
        for (String key : MAP_STUDENT_NAME.keySet()) {
            BaseStudent student = MAP_STUDENT_NAME.get(key);
            student.study();
        }
    }

    public void work() {
        for (String key : MAP_STUDENT_NAME.keySet()) {
            BaseStudent student = MAP_STUDENT_NAME.get(key);
            student.work();
        }
    }
}
```

构造方法中，我们创建了一个以学生姓名为Key的Map，一个学生职责为Key的Map。我们可以通过职责或者姓名查找到对应的学生，然后执行他的方法。也可以对全班同学进行操作，如考试等。

完整的代码参考[这里](https://github.com/ttdevs/android/DemoAnnotation)，根据这个例子，班级中若有学生加入或者离开，我们只需删除或者添加对应的学生类重新编译即可。

## 0x02 annotationProcessor

APT是什么？javac、apt、android-apt和annotationProcessor这几个又是什么关系？

### APT和javac

- APT：Annotation Processing Tool
    
    APT是Sun（没错，不是Oracle）在JDK1.5版本提供的处理源码级别注解的工具（注解也是在JDK1.5版本引入的）。作用是根据源码中的注解生成新的文件，这里主要还是java文件。不过在JDK1.6就无情的被javac取代了。

- annotationProcessor和android-apt 
    
    二者是相同的东西，android-apt为个人开发者开发的，gradle2.2之前的版本被广泛使用。gradle2.2版本，google官方出了annotationProcessor，android-apt也随之退出历史舞台。我的理解：annotationProcessor是一个将我们的写的注解相关代码（注解，注解处理器等）打包传给javac处理的工具，最终注解的处理还是由javac来完成。

- javac 
    
    javac不仅负责java的编译工作，同时还负责处理java源码中的编译期注解。引用一段关于javac的说明：

> [The javac command provides direct support for annotation processing, superseding the need for the separate annotation processing command, apt.](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/javac.html) 
> 简单翻译：javac提供了对注解处理的直接支持，从而取代了对单独处理注解命令apt的需求。

### javac对注解的处理流程

- 首先javac扫描所有源文件，确定有哪些类中包含注解；
- 然后javac查询注解处理器确定他们处理的注解，查找路径为`META-INF/services/javax.annotation.processing.Processor`，此文件记录了用户的所有注解处理器，每行一个（用户在自己的注解处理器中可声明所处理的具体注解，若你不声明则不会调用这个注解处理器的process方法）；
- 根据注解处理器声明的所处理的注解，将相应的注解分配给对应的注解处理类处理；
- 若注解处理类产生了新的源文件，则重复上述动作，直到产生的新文件无注解为止；
- 至此，注解处理流程结束，javac转去处理其他工作。

> 更详细准确的介绍参见这里：https://docs.oracle.com/javase/8/docs/api/javax/annotation/processing/Processor.html

### annotationProcessor

第一部分的例子已经详细介绍如何使用annotationProcessor，下面介绍一下原理。

#### com.google.auto.service:auto-service-annotations:1.0

这个Library仅仅包含了AutoService这个注解，源码如下：

``` java
package com.google.auto.service;
@Documented
@Retention(CLASS)
@Target(TYPE)
public @interface AutoService {
  /** Returns the interfaces implemented by this service provider. */
  Class<?>[] value();
}
```

通过注释，我们可以得知，使用时必须注意下面几点：

- 必须用在非内部，非匿名，具体的类上
- 这个类必须包含一个public无参的构造函数
- 实现values()返回的接口类型

#### com.google.auto.service:auto-service:1.0

``` shell
➜  auto-service-1.0-sources tree
.
├── META-INF
│   ├── MANIFEST.MF
│   ├── gradle
│   │   └── incremental.annotation.processors
│   └── services
│       └── javax.annotation.processing.Processor
└── com
    └── google
        └── auto
            └── service
                └── processor
                    ├── AutoServiceProcessor.java
                    ├── ServicesFiles.java
                    └── package-info.java
```

jar包中主要包含两部分

- META-INF/services/javax.annotation.processing.Processor

    其内容仅有一行，如下：
    ``` java
    com.google.auto.service.processor.AutoServiceProcessor
    ```
    
- AutoServiceProcessor

    ``` java
    public class AutoServiceProcessor extends AbstractProcessor {
      ...
      @Override
      public ImmutableSet<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(AutoService.class.getName());
      }
      ...
      @Override
      public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {...}
    }
    ```
    
以上信息可以得知，AutoServiceProcessor是处理AutoService的注解处理器。通过源码可以得知其主要功能是帮我们在`META-INF/services/javax.annotation.processing.Processor`中配置自定义的注解处理器。

另外，`AutoService.java`在`com.google.auto.service:auto-service:1.0`这个库中定义。其内容仅仅包含`AutoService.java`这个注解的定义。大家思考一下为什么就这一个类不和`com.google.auto.service:auto-service:1.0`定义在一起？请自寻答案。

#### 工作流程

用户创建自定义注解，同时创建处理这个注解的注解处理器，在注解处理器中使用`@AutoService`注解，javac检测到这个注解丢给AutoServiceProcessor处理，AutoServiceProcessor自动帮我们把自定义的注解处理器配置到`META-INF/services/javax.annotation.processing.Processor`（当然你也可以不用`@AutoService`注解自己手动配置）。

以上可知，`annotationProcessor`仅仅告诉javac这个java library内有注解需要处理。

## 0x03 JavaPoet

生成Java文件，待续。

## 0x04 Debug

### Log

`StudentProcessor`的`init(ProcessingEnvironment processingEnv)`被调用的时候，我们可以获取一个`Messager`对象，通过这个对象我们可以向编译控制台输出我们的调试信息，如下：

``` shell
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class StudentProcessor extends AbstractProcessor {
    private Messager messager;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format("=========Process %s============",
                !roundEnv.processingOver() ? "start" : "  end"));

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ClassUtils.CLASS_STUDENT);
        if (null == elements || elements.isEmpty()) {
            return true;
        }
        for (Element element : elements) {
            Student student = element.getAnnotation(ClassUtils.CLASS_STUDENT);
            messager.printMessage(Diagnostic.Kind.ERROR, student.name()));
        }
        return true;
    }
}
```

实际使用我们可以把`Messager`封装到一个工具类，具体可参见`DemoAndroid`。

### Debug

除了打Log，我们也可以对相关代码进行远程调试，操作如下：

1. `Run/Debug Configuration` > `Edit Configurations...` 
    
   ![Debug_01_Configuration.png](https://upload-images.jianshu.io/upload_images/1801981-d318e4a88e70e3af.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
    
2. `+` > `Remote`:
    - Input Configuration Name: `Your Config Name`
    - Copy `Command line arguments for remote JVM`: `-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005`
    - Click `OK`

  ![Debug_02_Add_Configuration.png](https://upload-images.jianshu.io/upload_images/1801981-b84351f67aba059b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  

3. Open`gradle.properties`, add line in the end:
    `org.gradle.jvmargs=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005`

  ![Debug_03_Edit_Gradle_Properties.png](https://upload-images.jianshu.io/upload_images/1801981-4e19fd08e3b1cbd0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
    
4. Add breakpoints in your Processor files and click `Debug` button

  ![Debug_04_Add_Breakpoints_Debug.png](https://upload-images.jianshu.io/upload_images/1801981-fa5f755ac0af203f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

5. Rebuild your project, start debug

Congratulations!

## 0xFF Reference

1. [Interface: javax.annotation.processing.Processor](https://docs.oracle.com/javase/8/docs/api/javax/annotation/processing/Processor.html)
2. [javac: Annotation Processing](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/javac.html)
3. [Annotation Processing Tool (apt)](https://docs.oracle.com/javase/7/docs/technotes/guides/apt/index.html)
4. [Annotation Processing 101](https://hannesdorfmann.com/annotation-processing/annotationprocessing101/)
5. [Annotation-Processing-Tool详解](https://www.open-open.com/lib/view/open1470735314518.html)
6. [how do you debug java annotation processors using intellij?](https://stackoverflow.com/questions/8587096/how-do-you-debug-java-annotation-processors-using-intellij)
7. [auto-service-1.0](https://github.com/google/auto/releases/tag/auto-service-1.0)

