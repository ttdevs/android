# AIDL（上）

Demo

## 0x01 Demo

创建一个Android项目，包含三个Module，app_client，app_server和ipc：

- app_client：`com.android.application`，Client
- app_server：`com.android.application`，Server
- ipc：`com.android.library`，放AIDL及model文件

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

### app_server中创建Service

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

### app_client中创建测试Activity

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
2020-09-11 14:46:29.029 5933-5933/com.ttdevs.ipc.client D/>>>>>Client: Connect success.
// 默认情况，Server收到Client数据，并向Client返回自己的数据
2020-09-11 14:46:32.354 6064-6102/com.ttdevs.ipc.server D/>>>>>Server: getStudent: Jim,18
2020-09-11 14:46:32.355 5933-5933/com.ttdevs.ipc.client D/>>>>>Client: getStudent: Tom,30
// oneway，Client调用之后立马返回，不会等Server执行完之后才返回
2020-09-11 14:46:37.396 5933-5933/com.ttdevs.ipc.client D/>>>>>Client: addStudentOneway: return
2020-09-11 14:46:37.396 6064-6102/com.ttdevs.ipc.server D/>>>>>Server: addStudentOneway: start Jim,18
2020-09-11 14:46:38.397 6064-6102/com.ttdevs.ipc.server D/>>>>>Server: addStudentOneway: end Jim,18
// in，Client传递数据到，Server的改动不会影响到Client
2020-09-11 14:46:40.606 6064-6102/com.ttdevs.ipc.server D/>>>>>Server: addStudentWithIn: Student{name='Jim', age=18}
2020-09-11 14:46:40.607 5933-5933/com.ttdevs.ipc.client D/>>>>>Client: Student{name='Jim', age=18}
// out，Client数据没有传递到Server，Server的数据传递到了Client
2020-09-11 14:46:43.689 5933-5933/com.ttdevs.ipc.client D/>>>>>Client: addStudentWithOut: Student{name='Jim', age=18}
2020-09-11 14:46:43.689 6064-6102/com.ttdevs.ipc.server D/>>>>>Server: addStudentWithOut: Student{name='null', age=0}
2020-09-11 14:46:43.690 5933-5933/com.ttdevs.ipc.client D/>>>>>Client: Student{name='Tom', age=30}
// inout,Client和Server的数据可以互相传递
2020-09-11 14:46:46.347 5933-5933/com.ttdevs.ipc.client D/>>>>>Client: addStudentWithInout: Student{name='Jim', age=18}
2020-09-11 14:46:46.347 6064-6102/com.ttdevs.ipc.server D/>>>>>Server: addStudentWithInout: Student{name='Jim', age=18}
2020-09-11 14:46:46.348 5933-5933/com.ttdevs.ipc.client D/>>>>>Client: Student{name='Tom', age=30}
// inout和return的功能都体现出来
2020-09-11 14:46:48.419 5933-5933/com.ttdevs.ipc.client D/>>>>>Client: addStudentWithInoutAndReturn: Student{name='Jim', age=18}
2020-09-11 14:46:48.420 6064-6102/com.ttdevs.ipc.server D/>>>>>Server: addStudentWithInoutAndReturn: Student{name='Jim', age=18}
2020-09-11 14:46:48.421 5933-5933/com.ttdevs.ipc.client D/>>>>>Client: addStudentWithInoutAndReturn: Student{name='Tom', age=30}
2020-09-11 14:46:48.421 5933-5933/com.ttdevs.ipc.client D/>>>>>Client: Student{name='Tom', age=30}
// Client注册到Server
2020-09-11 14:46:50.727 6064-6102/com.ttdevs.ipc.server D/>>>>>Server: Register listener
// Server向Client发送数据
2020-09-11 14:46:50.728 5933-5970/com.ttdevs.ipc.client D/>>>>>Client: From server: Student{name='Tom', age=31}
2020-09-11 14:46:51.890 6064-6102/com.ttdevs.ipc.server D/>>>>>Server: Unregister listener
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

### 更新app_server

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

### 更新app_client

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