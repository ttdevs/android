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
     * <p>
     * IStudentManager默认实现，暂未使用。
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
     * <p>
     * 1、看到Stub，多数情况是更IPC相关
     * 2、Server和Client都用到了这个类：
     * a）、Server中new IStudentManager.Stub()，实现我们在AIDL文件中定义的方法，处理Client发送的请求，此处处理了tag：in，out，inout；
     * b）、Client中IStudentManager.Stub.asInterface(service)，返回的是IStudentManager.Stub.Proxy()，处理如何将请求发送到Server，此处处理了oneway；
     * 3、此类完全可以自己实现
     */
    public static abstract class Stub extends android.os.Binder implements com.ttdevs.ipc.IStudentManager {
        // 此描述符用来标识这个AIDL
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
         * <p>
         * Client中通过此方法，创建一个Proxy对象。
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
                    // 接收Client传过来的数据，然后传给方法的实现者
                    java.lang.String _result = this.getStudent(_arg0);
                    // 与Client中_reply.readException();对应
                    reply.writeNoException();
                    // 将方法实现者返回的数据发给Client
                    reply.writeString(_result);
                    return true;
                }
                case TRANSACTION_addStudentOneway: {
                    data.enforceInterface(descriptor);
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    // oneway： 只处理Client发送过来的数据，数据单项流动
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
                    // Client端传递过来的数据
                    this.addStudentWithIn(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_addStudentWithOut: {
                    data.enforceInterface(descriptor);
                    com.ttdevs.ipc.Student _arg0;
                    _arg0 = new com.ttdevs.ipc.Student();
                    // Server端不处理Client传递过来的数据（其实，Client也没有传递数据过来）
                    this.addStudentWithOut(_arg0);
                    reply.writeNoException();
                    if ((_arg0 != null)) {
                        reply.writeInt(1);
                        // 将Server端改动的数据放入reply传给Client
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
                    // 处理Client传递过来的数据
                    this.addStudentWithInout(_arg0);
                    reply.writeNoException();
                    if ((_arg0 != null)) {
                        reply.writeInt(1);
                        // Server端处理之后的数据写会Client
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
                        // 先写return的数据
                        _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    if ((_arg0 != null)) {
                        reply.writeInt(1);
                        // 再写Server端改动的数据，与Prxoy中过程对应
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

        /**
         * getDefaultImpl()默认为null，不分析
         */
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
                // 对应Server端: case TRANSACTION_getStudent: {}
                // 获取两个Parcel对象，用于传递与Client与Server交互的数据
                // _data用于发送，_reply用于接收
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.lang.String _result;
                try {
                    // 先写出DESCRIPTOR：com.ttdevs.ipc.IStudentManager
                    _data.writeInterfaceToken(DESCRIPTOR);
                    // 写入要传递的数据
                    _data.writeString(student);
                    // 借助Binder将数据传到Server端
                    // Stub.TRANSACTION_getStudent：标识这个方法，值域FIRST_CALL_TRANSACTION到LAST_CALL_TRANSACTION
                    // _data：用于发送数据的Parcel
                    // _replay：用于接收Server传回数据的Parcel
                    // 0：标识请求类型，0为正常的IPC调用，FLAG_ONEWAY标识是oneway方式的IPC调用
                    boolean _status = mRemote.transact(Stub.TRANSACTION_getStudent, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        return getDefaultImpl().getStudent(student);
                    }
                    // 先读一次异常，对用Server端的reply.writeNoException();
                    _reply.readException();
                    // 读取Server返回的数据
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public void addStudentOneway(java.lang.String student) throws android.os.RemoteException {
                // 此方法标识为oneway，只获取用于发送数的Parcel，无处理返回的Parcel
                // 由此可见，oneway修饰的方法，只能发送数据，无法处理对端的返回
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
                // in修饰的参数
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((student != null)) {
                        // 有数据先写1
                        _data.writeInt(1);
                        // 将student数据写出发送数据的Parcel中
                        student.writeToParcel(_data, 0);
                    } else {
                        // 无数据直接写0
                        _data.writeInt(0);
                    }
                    // 上述逻辑整好和对端处理逻辑对应
                    // if ((0 != data.readInt())) { _arg0 = com.ttdevs.ipc.Student.CREATOR.createFromParcel(data); } else { _arg0 = null; }
                    // 调用Binder方法发送数据
                    boolean _status = mRemote.transact(Stub.TRANSACTION_addStudentWithIn, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().addStudentWithIn(student);
                        return;
                    }
                    // 读异常，若有异常会退出？
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void addStudentWithOut(com.ttdevs.ipc.Student student) throws android.os.RemoteException {
                // out修饰，结论：传入参数不会带到对端，仅用于接收对端返回的数据，
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    // 传入的参数student并为处理，所以对端不会收到传入的参数
                    boolean _status = mRemote.transact(Stub.TRANSACTION_addStudentWithOut, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().addStudentWithOut(student);
                        return;
                    }
                    _reply.readException();
                    // 接收对端返回的数据
                    if ((0 != _reply.readInt())) {
                        // 将对端返回的数据写入方法传入的参数
                        student.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void addStudentWithInout(com.ttdevs.ipc.Student student) throws android.os.RemoteException {
                // inout修饰，结论：参数传入数据会传递给对端，对端返回的数据会用来更新传入的参数
                // 与out区别是传入的参数会被带到对端
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    // 将传入的参数发给对端
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
                    // 处理对端返回的数据，更新到传入的参数中
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
                    // 处理传入参数
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
                    // 处理对端的返回，用于方法的返回
                    if ((0 != _reply.readInt())) {
                        _result = com.ttdevs.ipc.Student.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    // 处理对端的返回，此处inout这个标签的功能
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
                    // 与上述方法不同点传递的对象为binder
                    _data.writeStrongBinder((((listener != null)) ? (listener.asBinder()) : (null)));
                    // 默认的Flag是oneway
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
                    // 默认的Flag是oneway
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

        // IBinder.FIRST_CALL_TRANSACTION常量，值为0x00000001，还有一个IBinder.LAST_CALL_TRANSACTION
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
