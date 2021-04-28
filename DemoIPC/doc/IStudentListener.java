/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.ttdevs.ipc;

public interface IStudentListener extends android.os.IInterface {
    /**
     * Default implementation for IStudentListener.
     */
    public static class Default implements com.ttdevs.ipc.IStudentListener {
        @Override
        public void updateStudent(com.ttdevs.ipc.Student student) throws android.os.RemoteException {
        }

        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements com.ttdevs.ipc.IStudentListener {
        private static final java.lang.String DESCRIPTOR = "com.ttdevs.ipc.IStudentListener";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an com.ttdevs.ipc.IStudentListener interface,
         * generating a proxy if needed.
         */
        public static com.ttdevs.ipc.IStudentListener asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof com.ttdevs.ipc.IStudentListener))) {
                return ((com.ttdevs.ipc.IStudentListener) iin);
            }
            return new com.ttdevs.ipc.IStudentListener.Stub.Proxy(obj);
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
                case TRANSACTION_updateStudent: {
                    data.enforceInterface(descriptor);
                    com.ttdevs.ipc.Student _arg0;
                    if ((0 != data.readInt())) {
                        _arg0 = com.ttdevs.ipc.Student.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    this.updateStudent(_arg0);
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }

        private static class Proxy implements com.ttdevs.ipc.IStudentListener {
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

            @Override
            public void updateStudent(com.ttdevs.ipc.Student student) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((student != null)) {
                        _data.writeInt(1);
                        student.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = mRemote.transact(Stub.TRANSACTION_updateStudent, _data, null, android.os.IBinder.FLAG_ONEWAY);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().updateStudent(student);
                        return;
                    }
                } finally {
                    _data.recycle();
                }
            }

            public static com.ttdevs.ipc.IStudentListener sDefaultImpl;
        }

        static final int TRANSACTION_updateStudent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);

        public static boolean setDefaultImpl(com.ttdevs.ipc.IStudentListener impl) {
            if (Stub.Proxy.sDefaultImpl == null && impl != null) {
                Stub.Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static com.ttdevs.ipc.IStudentListener getDefaultImpl() {
            return Stub.Proxy.sDefaultImpl;
        }
    }

    public void updateStudent(com.ttdevs.ipc.Student student) throws android.os.RemoteException;
}
