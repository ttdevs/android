package com.ttdevs.ipc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

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
        return sStudentManger;
    }

    /**
     * asBinder();
     */
    private static final IStudentManager.Stub sStudentManger = new IStudentManager.Stub() {

        @Override
        public String getStudent(String student) throws RemoteException {
            LogUtils.d("getStudent: " + student);
            return "From Server: " + student;
        }

        @Override
        public void setStudent(String student) throws RemoteException {
            LogUtils.d("setStudent: start " + student);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LogUtils.d("setStudent: end " + student);
        }

        @Override
        public void setStudentOneWay(String student) throws RemoteException {
            LogUtils.d("setStudent: oneway start" + student);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LogUtils.d("setStudent: oneway end" + student);
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
    };
}
