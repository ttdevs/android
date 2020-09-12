package com.ttdevs.ipc.server;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.ttdevs.ipc.IStudentListener;
import com.ttdevs.ipc.IStudentManager;
import com.ttdevs.ipc.Student;
import com.ttdevs.ipc.utils.LogUtils;

/**
 * This is StudentService
 *
 * @author : ttdevs@gmail.com
 * @date : 2020/08/31
 */
public class StudentService extends Service {

    public StudentService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sBinder;
    }

    private static IStudentManager.Stub sBinder = new IStudentManager.Stub() {
        @Override
        public String getStudent(String student) throws RemoteException {
            LogUtils.d("getStudent: " + student);
            return "Tom,30";
        }

        @Override
        public void addStudentOneway(String student) throws RemoteException {
            LogUtils.d("addStudentOneway: start " + student);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LogUtils.d("addStudentOneway: end " + student);
        }

        @Override
        public void addStudentWithIn(Student student) throws RemoteException {
            LogUtils.d("addStudentWithIn: " + student.toString());
            student.setName("Tom");
            student.setAge(30);
        }

        @Override
        public void addStudentWithOut(Student student) throws RemoteException {
            LogUtils.d("addStudentWithOut: " + student.toString());
            student.setName("Tom");
            student.setAge(30);
        }

        @Override
        public void addStudentWithInout(Student student) throws RemoteException {
            LogUtils.d("addStudentWithInout: " + student.toString());
            student.setName("Tom");
            student.setAge(30);
        }

        @Override
        public Student addStudentWithInoutAndReturn(Student student) throws RemoteException {
            LogUtils.d("addStudentWithInoutAndReturn: " + student.toString());
            student.setName("Tom");
            student.setAge(30);
            return student;
        }

        private final RemoteCallbackList<IStudentListener> mCallbackList = new RemoteCallbackList<>();

        @Override
        public void register(IStudentListener listener) throws RemoteException {
            if (null != listener) {
                mCallbackList.register(listener);

                LogUtils.d("Register listener");
            }
            sendCallbackMessage();
        }

        @Override
        public void unregister(IStudentListener listener) throws RemoteException {
            if (null != listener) {
                mCallbackList.unregister(listener);

                LogUtils.d("Unregister listener");
            }
            sendCallbackMessage();
        }

        private final Handler mHandler = new Handler(Looper.getMainLooper());

        private void sendCallbackMessage() {
            mHandler.removeCallbacksAndMessages(null);

            final Student student = new Student();
            student.setName("Tom");
            student.setAge(30);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mCallbackList.getRegisteredCallbackCount() <= 0) {
                            return;
                        }
                        student.setAge(student.getAge() + 1);
                        int num = mCallbackList.beginBroadcast();
                        for (int i = 0; i < num; i++) {
                            try {
                                mCallbackList.getBroadcastItem(i).updateStudent(student);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        mCallbackList.finishBroadcast();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.postDelayed(this, 8000);
                }
            });
        }
    };
}
