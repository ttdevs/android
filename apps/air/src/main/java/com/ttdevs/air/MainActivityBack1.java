//package com.ttdevs.air;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothClass;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Toast;
//
//import com.ttdevs.air.utils.BaseWorkerThread;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Set;
//import java.util.UUID;
//
//public class MainActivityBack1 extends AppCompatActivity implements View.OnClickListener {
//    private static final int REQUEST_ENABLE_BT = 2333;
//    public static final UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
//    public static final String DEFAULT_MAC = "20:16:11:16:99:43";
//    private RadioGroup viewPairedDevices;
//
//    private BluetoothDevice mDevice;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        initView();
//        initBluetooth();
//
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, filter);
//    }
//
//    private void initView() {
//        viewPairedDevices = (RadioGroup) findViewById(R.id.viewPairedDevices);
//        viewPairedDevices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton checked = (RadioButton) group.findViewById(checkedId);
//                mDevice = (BluetoothDevice) checked.getTag();
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        } else if (id == R.id.action_scan) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // TODO: 2017/1/20
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        unregisterReceiver(mReceiver);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btInit:
//                initBluetooth();
//                break;
//            case R.id.btPaired:
//                pairedBluetooth();
//                break;
//            case R.id.btActive:
//                activeBluetooth();
//                break;
//            case R.id.btStart:
//
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    private BluetoothAdapter mBluetoothAdapter;
//
//    private void initBluetooth() {
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (null == mBluetoothAdapter) {
//            Toast.makeText(this, "BluetoothAdapter is null", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!mBluetoothAdapter.isEnabled()) {
//            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(intent, REQUEST_ENABLE_BT);
//            return;
//        }
//    }
//
//    private void pairedBluetooth() {
//        viewPairedDevices.removeAllViews();
//
//        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//        if (null != pairedDevices && pairedDevices.size() > 0) {
//            int index = 0;
//            for (BluetoothDevice device : pairedDevices) {
//                String msg = String.format("%s\n%s", device.getName(), device.getAddress());
//                RadioButton radioButton = new RadioButton(this);
//                radioButton.setId(index++);
//                radioButton.setText(msg);
//                radioButton.setTag(device);
//                viewPairedDevices.addView(radioButton);
//            }
//            viewPairedDevices.setVisibility(View.VISIBLE);
//        } else {
//            viewPairedDevices.setVisibility(View.GONE);
//        }
//    }
//
//    private void activeBluetooth() {
//        try {
//            if(null == mDevice){
//                mDevice = mBluetoothAdapter.getRemoteDevice(DEFAULT_MAC);
//            }
//            BluetoothSocket socket = mDevice.createRfcommSocketToServiceRecord(DEFAULT_UUID);
//
//            socket.connect(); // 阻塞的
//
//            new ReceiveDataThread(socket).startThread();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                BluetoothClass clazz = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//
//                String msg = device.getName() + "::" + device.getAddress();
//                System.err.println(">>>>> found: " + msg);
//            }
//        }
//    };
//
//
//    private class ReceiveDataThread extends BaseWorkerThread {
//        private static final int SIZE = 32;
//
//        private BluetoothSocket mSocket;
//        private InputStream mIn;
//        private byte[] tempBuffer, readBuffer;
//        private int mBytes, mCount;
//
//        public ReceiveDataThread(BluetoothSocket socket) {
//            mSocket = socket;
//
//            try {
//                mIn = mSocket.getInputStream();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public boolean workerBefore() {
//            if (null == mIn) {
//                return false;
//            }
//            tempBuffer = new byte[SIZE];
//            readBuffer = new byte[SIZE];
//
//            return super.workerBefore();
//        }
//
//        @Override
//        public boolean workerCycle() {
//            try {
//                mBytes = mIn.read(readBuffer);
//                System.arraycopy(readBuffer, 0, tempBuffer, mCount, mBytes);
//                mCount += mBytes;
//                System.out.println(String.format("bytes:%d count:%d", mBytes, mCount));
//                if (mCount >= SIZE) {
//                    bytes2HexString(tempBuffer);
//                    mCount = 0;
//                    System.out.println("bytes:>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                return false;
//            }
//            return true;
//        }
//
//        private void bytes2HexString(byte[] b) {
//            String result = "";
//            for (int i = 0; i < b.length; i++) {
//                String hex = Integer.toHexString(b[i] & 0xFF);
//                result += hex;
//            }
//
//            System.err.println(">>>>>" + result.toUpperCase());
//            System.err.println("<<<<<" + result.toUpperCase());
//        }
//    }
//}
