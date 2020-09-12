# AIDL（中）

## RPC & IPC & AIDL

## AIDL支持的数据类型

- Java基本类型及对应的数组
- String & CharSequence
- List & Map
- Parcelable
- AIDL

## Parcelable & Parcel

## Method: oneway

标识方法为异步方法，调用之后立马返回，不会等对端执行完毕，且(故)返回值类型只能为void。AIDL生成的对应Java文件中，mRemote.transact()方法最后一个参数为`android.os.IBinder.FLAG_ONEWAY`（其他情况此值为0：普通IPC调用）。

## Tag: in, out, inout

- in

Client传数据给Server。
> 基本类型可省略

- out

Server传数据给Client。如果你在调用处传了参数，Client不会发，Server不收。
> 此处比较容易困惑

- inout

Server与Client互传数据。
> inout性能？

## Other

### Client异常结束

Client注册监听到Server，当我们结束Client时（`adb shell am force-stop com.ttdevs.ipc.client`），此Client注册的回调在Server中会自动被移除。

## Source code

AIDL编译之后自动生产对应的java文件，以上述IStudentManager.aidl为例，对应生成的java文件为IStudentManager.java。先来看下总体结构：

```java
public interface IStudentManager extends android.os.IInterface {
    public static class Default implements com.ttdevs.ipc.IStudentManager { }
    // Stub，Server中实现
    public static abstract class Stub extends android.os.Binder implements com.ttdevs.ipc.IStudentManager {
        // Stub.Proxy，服务绑定成功之后调用：IStudentManager.Stub.asInterface(service); Client中调用的方法直接调到此处
        private static class Proxy implements com.ttdevs.ipc.IStudentManager { }
    }
}
```

第一次看这个文件，想到的第一个词就是：套娃。了解了整体结构，具体每一部分的含义，需要大家多看和理解。
