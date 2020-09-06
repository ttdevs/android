package com.ttdevs.ipc.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;

import com.ttdevs.ipc.IStudentListener;
import com.ttdevs.ipc.IStudentManager;
import com.ttdevs.ipc.Student;
import com.ttdevs.ipc.utils.Constant;
import com.ttdevs.ipc.utils.LogUtils;

/**
 * This is MainActivity
 *
 * @author : ttdevs@gmail.com
 * @date : 2020/08/31
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private IStudentManager mStudentManager;
    private Button btConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogUtils.init(">>>>>Client ");

        btConnect = findViewById(R.id.bt_connect);
        bindService();
    }

    @Override
    public void onClick(View v) {
        try {
            String student = "Jim,18";
            switch (v.getId()) {
                case R.id.bt_connect:
                    bindService();
                    return;
                case R.id.bt_get:
                    String result = mStudentManager.getStudent(student);
                    LogUtils.d("getStudent: " + result);
                    return;
                case R.id.bt_oneway:
                    mStudentManager.addStudentOneway(student);
                    LogUtils.d("addStudentOneway: return");
                    return;
                case R.id.bt_register:
                    registerListener(true);
                    return;
                case R.id.bt_unregister:
                    registerListener(false);
                    return;

                default:
                    break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            Student student = new Student();
            student.setName("Jim");
            student.setAge(18);
            switch (v.getId()) {
                case R.id.bt_in:
                    mStudentManager.addStudentWithIn(student);
                    break;
                case R.id.bt_out:
                    LogUtils.d("addStudentWithOut: " + student.toString());
                    mStudentManager.addStudentWithOut(student);
                    break;
                case R.id.bt_inout:
                    LogUtils.d("addStudentWithInout: " + student.toString());
                    mStudentManager.addStudentWithInout(student);
                    break;
                case R.id.bt_inout_return:
                    LogUtils.d("addStudentWithInoutAndReturn: " + student.toString());
                    Student temp = mStudentManager.addStudentWithInoutAndReturn(student);
                    LogUtils.d("addStudentWithInoutAndReturn: " + temp.toString());
                    break;

                default:
                    break;
            }

            LogUtils.d(student.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static final IStudentListener mListener = new IStudentListener.Stub() {
        @Override
        public void updateStudent(Student student) throws RemoteException {
            LogUtils.d("From server: " + student.toString());
        }
    };

    /**
     * 注册/取消注册
     *
     * @param register
     */
    private void registerListener(boolean register) {
        try {
            if (register) {
                mStudentManager.register(mListener);
            } else {
                mStudentManager.unregister(mListener);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void bindService() {
        try {
            bindService(Constant.getServerIntent(), new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mStudentManager = IStudentManager.Stub.asInterface(service);

                    LogUtils.d("Connect success.");
                    runOnUiThread(() -> btConnect.setEnabled(false));
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    mStudentManager = null;

                    runOnUiThread(() -> btConnect.setEnabled(true));

                }
            }, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}