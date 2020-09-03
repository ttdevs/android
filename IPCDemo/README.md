# IPCDemo

IPC & AIDL

## AIDL

### 基本类型
- java基本类型
- String & CharSequence
- List & Map

### Parcelable

### in out inout

### oneway

标识方法为异步方法。in可以带oneway，out和inout不能带oneway。源码中，oneway无reply.writeNoException()，所以无法用在out和inout上。

`IStudentManager > Stub(Server, onTransact(...)) > Proxy(Client)`

``` shell
public interface IStudentManager extends android.os.IInterface {
    /**
     * Default implementation for IStudentManager.
     */
    public static class Default implements com.ttdevs.ipc.IStudentManager {
        ...
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
            //    String getStudent(out String student);

            @Override
            public java.lang.String getStudent(java.lang.String student) throws android.os.RemoteException {
            }
        }

        
    //    String getStudent(out String student);
    public java.lang.String getStudent(java.lang.String student) throws android.os.RemoteException;
}

```

## Service

## 0xFF Reference

1. https://developer.android.com/guide/components/aidl
2. https://source.android.com/devices/architecture/aidl/overview









