# AIDL（中）

## RPC & IPC & AIDL

## AIDL支持的数据类型
- Java基本类型及对应的数组
- String & CharSequence
- List & Map
- Parceable
- AIDL

## Parcelable & Parcel

## Method: oneway

标识方法为异步方法，调用之后立马返回，不会等对端执行完毕，且(故)返回值类型只能为void。AIDL生成的对应Java文件中，mRemote.transact()方法最后一个参数为`android.os.IBinder.FLAG_ONEWAY`（其他情况此值为0）。

## Tag: in, out, inout

- in

Client传参给Server。
> 基本类型可省略

- out

Server传参给Client。如果你在调用处传了参数，实际上也是传不到Server的。
> 此处比较容易困惑

- inout

Server与Client互传参数。
> inout 性能

## Other

### Client异常结束

Client注册监听到Server，当我们结束Client时（`adb shell am force-stop com.ttdevs.ipc.client`），此Client注册的回调在Server中会自动被移除。

## Source code

AIDL编译之后自动生产对应的java文件，以上述IStudentManager.aidl为例，对应生成的java文件为IStudentManager.java。先来看下总体结构：

```java
public interface IStudentManager extends android.os.IInterface {
    public static class Default implements com.ttdevs.ipc.IStudentManager { }
    // Stub，Client中，服务绑定成功之后调用：IStudentManager.Stub.asInterface(service);
    public static abstract class Stub extends android.os.Binder implements com.ttdevs.ipc.IStudentManager {
        // Stub.Proxy，Client中调用的方法直接调到此处
        private static class Proxy implements com.ttdevs.ipc.IStudentManager { }
    }
}
```

第一次看这个文件，想到的第一个词就是：套娃。了解了真题结构，具体每一部分的含义，需要大家多看和理解。

```java
/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.ttdevs.ipc;

/**
 * Student Manager
 */
public interface IStudentManager extends android.os.IInterface {
    /**
     * Default implementation for IStudentManager.
     */
    public static class Default implements com.ttdevs.ipc.IStudentManager {
        // String getStudent(String student); // 不指定tag默认为in
        // void addStudentWithNull(Student student); // 自定义类型必须指定tag
        // void addStudentWithIn(String student); // 不能定义同名方法？
        // oneway void addStudentWithOut(out Student student); // oneway 不能合用 out
        // oneway void addStudentWithInout(inout Student student); // oneway 不能合用 inout

        @Override
        public java.lang.String getStudent(java.lang.String student) throws android.os.RemoteException {
            return null;
        }

        @Override
        public void addStudentOneway(java.lang.String student) throws android.os.RemoteException {
        }

        @Override
        public void addStudentWithIn(com.ttdevs.ipc.Student student) throws android.os.RemoteException {
        }

        @Override
        public void addStudentWithOut(com.ttdevs.ipc.Student student) throws android.os.RemoteException {
        }

        @Override
        public void addStudentWithInout(com.ttdevs.ipc.Student student) throws android.os.RemoteException {
        }

        @Override
        public com.ttdevs.ipc.Student addStudentWithInoutAndReturn(com.ttdevs.ipc.Student student) throws android.os.RemoteException {
            return null;
        }

        @Override
        public void register(com.ttdevs.ipc.IStudentListener listener) throws android.os.RemoteException {
        }

        @Override
        public void unregister(com.ttdevs.ipc.IStudentListener listener) throws android.os.RemoteException {
        }

        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements com.ttdevs.ipc.IStudentManager {
        private static final java.lang.String DESCRIPTOR = "com.ttdevs.ipc.IStudentManager";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an com.ttdevs.ipc.IStudentManager interface,
         * generating a proxy if needed.
         */
        public static com.ttdevs.ipc.IStudentManager asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof com.ttdevs.ipc.IStudentManager))) {
                return ((com.ttdevs.ipc.IStudentManager) iin);
            }
            return new com.ttdevs.ipc.IStudentManager.Stub.Proxy(obj);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            java.lang.String descriptor = DESCRIPTOR;
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(descriptor);
                    return true;
                }
                case TRANSACTION_getStudent: {
                    data.enforceInterface(descriptor);
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    java.lang.String _result = this.getStudent(_arg0);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
                case TRANSACTION_addStudentOneway: {
                    data.enforceInterface(descriptor);
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    this.addStudentOneway(_arg0);
                    return true;
                }
                case TRANSACTION_addStudentWithIn: {
                    data.enforceInterface(descriptor);
                    com.ttdevs.ipc.Student _arg0;
                    if ((0 != data.readInt())) {
                        _arg0 = com.ttdevs.ipc.Student.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    this.addStudentWithIn(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_addStudentWithOut: {
                    data.enforceInterface(descriptor);
                    com.ttdevs.ipc.Student _arg0;
                    _arg0 = new com.ttdevs.ipc.Student();
                    this.addStudentWithOut(_arg0);
                    reply.writeNoException();
                    if ((_arg0 != null)) {
                        reply.writeInt(1);
                        _arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case TRANSACTION_addStudentWithInout: {
                    data.enforceInterface(descriptor);
                    com.ttdevs.ipc.Student _arg0;
                    if ((0 != data.readInt())) {
                        _arg0 = com.ttdevs.ipc.Student.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    this.addStudentWithInout(_arg0);
                    reply.writeNoException();
                    if ((_arg0 != null)) {
                        reply.writeInt(1);
                        _arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case TRANSACTION_addStudentWithInoutAndReturn: {
                    data.enforceInterface(descriptor);
                    com.ttdevs.ipc.Student _arg0;
                    if ((0 != data.readInt())) {
                        _arg0 = com.ttdevs.ipc.Student.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    com.ttdevs.ipc.Student _result = this.addStudentWithInoutAndReturn(_arg0);
                    reply.writeNoException();
                    if ((_result != null)) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    if ((_arg0 != null)) {
                        reply.writeInt(1);
                        _arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case TRANSACTION_register: {
                    data.enforceInterface(descriptor);
                    com.ttdevs.ipc.IStudentListener _arg0;
                    _arg0 = com.ttdevs.ipc.IStudentListener.Stub.asInterface(data.readStrongBinder());
                    this.register(_arg0);
                    return true;
                }
                case TRANSACTION_unregister: {
                    data.enforceInterface(descriptor);
                    com.ttdevs.ipc.IStudentListener _arg0;
                    _arg0 = com.ttdevs.ipc.IStudentListener.Stub.asInterface(data.readStrongBinder());
                    this.unregister(_arg0);
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }

        private static class Proxy implements com.ttdevs.ipc.IStudentManager {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }
            // String getStudent(String student); // 不指定tag默认为in
            // void addStudentWithNull(Student student); // 自定义类型必须指定tag
            // void addStudentWithIn(String student); // 不能定义同名方法？
            // oneway void addStudentWithOut(out Student student); // oneway 不能合用 out
            // oneway void addStudentWithInout(inout Student student); // oneway 不能合用 inout

            @Override
            public java.lang.String getStudent(java.lang.String student) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(student);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_getStudent, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        return getDefaultImpl().getStudent(student);
                    }
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public void addStudentOneway(java.lang.String student) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(student);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_addStudentOneway, _data, null, android.os.IBinder.FLAG_ONEWAY);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().addStudentOneway(student);
                        return;
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override
            public void addStudentWithIn(com.ttdevs.ipc.Student student) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((student != null)) {
                        _data.writeInt(1);
                        student.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = mRemote.transact(Stub.TRANSACTION_addStudentWithIn, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().addStudentWithIn(student);
                        return;
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void addStudentWithOut(com.ttdevs.ipc.Student student) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_addStudentWithOut, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().addStudentWithOut(student);
                        return;
                    }
                    _reply.readException();
                    if ((0 != _reply.readInt())) {
                        student.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void addStudentWithInout(com.ttdevs.ipc.Student student) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((student != null)) {
                        _data.writeInt(1);
                        student.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = mRemote.transact(Stub.TRANSACTION_addStudentWithInout, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().addStudentWithInout(student);
                        return;
                    }
                    _reply.readException();
                    if ((0 != _reply.readInt())) {
                        student.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public com.ttdevs.ipc.Student addStudentWithInoutAndReturn(com.ttdevs.ipc.Student student) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                com.ttdevs.ipc.Student _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((student != null)) {
                        _data.writeInt(1);
                        student.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = mRemote.transact(Stub.TRANSACTION_addStudentWithInoutAndReturn, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        return getDefaultImpl().addStudentWithInoutAndReturn(student);
                    }
                    _reply.readException();
                    if ((0 != _reply.readInt())) {
                        _result = com.ttdevs.ipc.Student.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    if ((0 != _reply.readInt())) {
                        student.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public void register(com.ttdevs.ipc.IStudentListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder((((listener != null)) ? (listener.asBinder()) : (null)));
                    boolean _status = mRemote.transact(Stub.TRANSACTION_register, _data, null, android.os.IBinder.FLAG_ONEWAY);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().register(listener);
                        return;
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override
            public void unregister(com.ttdevs.ipc.IStudentListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder((((listener != null)) ? (listener.asBinder()) : (null)));
                    boolean _status = mRemote.transact(Stub.TRANSACTION_unregister, _data, null, android.os.IBinder.FLAG_ONEWAY);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().unregister(listener);
                        return;
                    }
                } finally {
                    _data.recycle();
                }
            }

            public static com.ttdevs.ipc.IStudentManager sDefaultImpl;
        }

        static final int TRANSACTION_getStudent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_addStudentOneway = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_addStudentWithIn = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
        static final int TRANSACTION_addStudentWithOut = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
        static final int TRANSACTION_addStudentWithInout = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
        static final int TRANSACTION_addStudentWithInoutAndReturn = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
        static final int TRANSACTION_register = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
        static final int TRANSACTION_unregister = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);

        public static boolean setDefaultImpl(com.ttdevs.ipc.IStudentManager impl) {
            if (Stub.Proxy.sDefaultImpl == null && impl != null) {
                Stub.Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static com.ttdevs.ipc.IStudentManager getDefaultImpl() {
            return Stub.Proxy.sDefaultImpl;
        }
    }
    // String getStudent(String student); // 不指定tag默认为in
    // void addStudentWithNull(Student student); // 自定义类型必须指定tag
    // void addStudentWithIn(String student); // 不能定义同名方法？
    // oneway void addStudentWithOut(out Student student); // oneway 不能合用 out
    // oneway void addStudentWithInout(inout Student student); // oneway 不能合用 inout

    public java.lang.String getStudent(java.lang.String student) throws android.os.RemoteException;

    public void addStudentOneway(java.lang.String student) throws android.os.RemoteException;

    public void addStudentWithIn(com.ttdevs.ipc.Student student) throws android.os.RemoteException;

    public void addStudentWithOut(com.ttdevs.ipc.Student student) throws android.os.RemoteException;

    public void addStudentWithInout(com.ttdevs.ipc.Student student) throws android.os.RemoteException;

    public com.ttdevs.ipc.Student addStudentWithInoutAndReturn(com.ttdevs.ipc.Student student) throws android.os.RemoteException;

    public void register(com.ttdevs.ipc.IStudentListener listener) throws android.os.RemoteException;

    public void unregister(com.ttdevs.ipc.IStudentListener listener) throws android.os.RemoteException;
}
```