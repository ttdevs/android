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

    private void bindService() {
        try {
            bindService(Constant.getServerIntent(), new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mStudentManager = IStudentManager.Stub.asInterface(service);

                    runOnUiThread(() -> btConnect.setEnabled(false));
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    runOnUiThread(() -> btConnect.setEnabled(true));

                }
            }, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    LogUtils.d(result);
                    return;
                case R.id.bt_set:
                    LogUtils.d("setStudent 1"); // 不加这行执行很慢？
                    mStudentManager.setStudent(student);
                    LogUtils.d("setStudent 2"); // 不加这行执行很慢？
                    mStudentManager.setStudentOneWay(student);
                    LogUtils.d("setStudent 3"); // 不加这行执行很慢？
                    return;

                default:
                    break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Student student = new Student();
        student.setName("Jim");
        student.setAge(18);
        try {
            switch (v.getId()) {
                case R.id.bt_in:
                    mStudentManager.addStudentWithIn(student);
                    break;
                case R.id.bt_out:
                    mStudentManager.addStudentWithOut(student);
                    break;
                case R.id.bt_inout:
                    mStudentManager.addStudentWithInout(student);
                    break;
                case R.id.bt_inout_return:
                    Student temp = mStudentManager.addStudentWithInoutAndReturn(student);
                    LogUtils.d("Return: " + temp.toString());
                    break;

                default:
                    break;
            }

            LogUtils.d(student.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}