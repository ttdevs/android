# AIDL（上）

Demo

## 0x01 Demo

创建一个Android项目，包含三个Module，app_client,app_server和ipc：

- app_client: `com.android.application`, Client
- app_server: `com.android.application`, Server
- ipc: `com.android.library`, 放AIDL及model文件

### ipc中创建AIDL文件

```aidl
// IStudentManager.aidl
package com.ttdevs.ipc;

import com.ttdevs.ipc.Student;

/**
* Student Manager
*/
interface IStudentManager {
    // String getStudent(String student); // 不指定tag默认为in
    // void addStudentWithNull(Student student); // 自定义类型必须指定tag
    // void addStudentWithIn(String student); // 不能定义同名方法？
    // oneway void addStudentWithOut(out Student student); // oneway 不能合用 out
    // oneway void addStudentWithInout(inout Student student); // oneway 不能合用 inout

    String getStudent(String student);
    oneway void addStudentOneway(in String student);
    void addStudentWithIn(in Student student);
    void addStudentWithOut(out Student student);
    void addStudentWithInout(inout Student student);
    Student addStudentWithInoutAndReturn(inout Student student);
}

// Student.aidl
package com.ttdevs.ipc;

// 不同包时需导入
// import com.ttdevs.ipc;

parcelable Student;
```

### Server中创建Service

```java
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
    };
}
```

### client中创建测试Activity

```java
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
```

分别运行Server和Client，点击Client上的Connect（默认会自动连接），即可在Client和Server两个进程之间传递数据。Client按钮全部点击一遍，Log如下：

```log
2020-09-04 16:40:01.802 21457-21457/com.ttdevs.ipc.client D/>>>>>Client: Connect success.
2020-09-04 16:40:06.074 19874-19891/com.ttdevs.ipc.server D/>>>>>Server: getStudent: Jim,18
2020-09-04 16:40:06.075 21457-21457/com.ttdevs.ipc.client D/>>>>>Client: getStudent: Tom,30
2020-09-04 16:40:07.200 21457-21457/com.ttdevs.ipc.client D/>>>>>Client: addStudentOneway: return
2020-09-04 16:40:07.200 19874-19891/com.ttdevs.ipc.server D/>>>>>Server: addStudentOneway: start Jim,18
2020-09-04 16:40:08.149 19874-19893/com.ttdevs.ipc.server D/>>>>>Server: addStudentWithIn: Student{name='Jim', age=18}
2020-09-04 16:40:08.151 21457-21457/com.ttdevs.ipc.client D/>>>>>Client: Student{name='Jim', age=18}
2020-09-04 16:40:08.201 19874-19891/com.ttdevs.ipc.server D/>>>>>Server: addStudentOneway: end Jim,18
2020-09-04 16:40:09.005 21457-21457/com.ttdevs.ipc.client D/>>>>>Client: addStudentWithOut: Student{name='Jim', age=18}
2020-09-04 16:40:09.005 19874-19891/com.ttdevs.ipc.server D/>>>>>Server: addStudentWithOut: Student{name='null', age=0}
2020-09-04 16:40:09.006 21457-21457/com.ttdevs.ipc.client D/>>>>>Client: Student{name='Tom', age=30}
2020-09-04 16:40:09.673 21457-21457/com.ttdevs.ipc.client D/>>>>>Client: addStudentWithInout: Student{name='Jim', age=18}
2020-09-04 16:40:09.674 19874-19891/com.ttdevs.ipc.server D/>>>>>Server: addStudentWithInout: Student{name='Jim', age=18}
2020-09-04 16:40:09.674 21457-21457/com.ttdevs.ipc.client D/>>>>>Client: Student{name='Tom', age=30}
2020-09-04 16:40:10.364 21457-21457/com.ttdevs.ipc.client D/>>>>>Client: addStudentWithInoutAndReturn: Student{name='Jim', age=18}
2020-09-04 16:40:10.365 19874-19891/com.ttdevs.ipc.server D/>>>>>Server: addStudentWithInoutAndReturn: Student{name='Jim', age=18}
2020-09-04 16:40:10.367 21457-21457/com.ttdevs.ipc.client D/>>>>>Client: addStudentWithInoutAndReturn: Student{name='Tom', age=30}
2020-09-04 16:40:10.367 21457-21457/com.ttdevs.ipc.client D/>>>>>Client: Student{name='Tom', age=30}
```

## Callback Demo

上述Demo主要演示Client主动向Server发起请求的情况，当Server需要主动向Client发送消息时如何处理？

### 更新ipc中AIDL文件

```aidl
// IStudentManager.aidl
package com.ttdevs.ipc;

import com.ttdevs.ipc.Student;
import com.ttdevs.ipc.IStudentListener;

/**
* Student Manager
*/
interface IStudentManager {
    // ...
    oneway void register(IStudentListener listener);
    oneway void unregister(IStudentListener listener);
}

// IStudentListener.aidl
package com.ttdevs.ipc;

import com.ttdevs.ipc.Student;

interface IStudentListener {
    oneway void updateStudent(in Student student);
}
```

### 更新Server

```java
public class StudentService extends Service {
    // ...
    private static IStudentManager.Stub sBinder = new IStudentManager.Stub() {
        // ...
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
```

### 更新Client

```java
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // ...
    @Override
    public void onClick(View v) {
        try {
            String student = "Jim,18";
            switch (v.getId()) {
                // ...
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

        // ...
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
    // ...
}
```

以上为常见的AIDL操作。

## 0xFF Reference

1. https://developer.android.com/guide/components/aidl
2. https://source.android.com/devices/architecture/aidl/overview