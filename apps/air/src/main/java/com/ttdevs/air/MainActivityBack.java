//package com.ttdevs.air;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothClass;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothServerSocket;
//import android.bluetooth.BluetoothSocket;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Set;
//import java.util.UUID;
//
//public class MainActivityBack extends AppCompatActivity {
//
//    private static final int REQUEST_ENABLE_BT = 2333;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                findBluetooth();
//
//            }
//        });
//
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, filter);
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
//    private void findBluetooth() {
//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (null == mBluetoothAdapter) {
//            Toast.makeText(this, "mBluetoothAdapter is null", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!mBluetoothAdapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//            return;
//        }
//
////        {// 启用可发现
////            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
////            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
////            startActivity(discoverableIntent);
////        }
//
//        Toast.makeText(this, "BluetoothAdapter enable", Toast.LENGTH_SHORT).show();
//        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                String msg = device.getName() + "::" + device.getAddress();
//                System.err.println(">>>>> " + msg);
//            }
//        }
////        new AcceptThread().start();
//
////        {
////            if (mBluetoothAdapter.isDiscovering()) {
////                mBluetoothAdapter.cancelDiscovery();
////            }
////            mBluetoothAdapter.startDiscovery();
////        }
//
//        sendMessage(mBluetoothAdapter);
//    }
//
//    private void sendMessage(BluetoothAdapter mBluetoothAdapter) {
//        String PROTOCOL_SCHEME_RFCOMM = "btspp";
//        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
//        String address = "20:16:11:16:99:43";
//
//        try {
//            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
////            BluetoothServerSocket serverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM, uuid);
////            BluetoothSocket socket = serverSocket.accept();
//            socket.connect();
//            OutputStream out = socket.getOutputStream();
//            out.write("12345".getBytes());
//            out.flush();
//
//            InputStream in = socket.getInputStream();
//            byte[] buffer = new byte[1024];
//            int bytes;
//            while (true) {
//                try {
//                    // Read from the InputStream
//                    bytes = in.read(buffer);
//                    System.out.println("<><><><><>" + bytes);
//
//                    out.write("67890".getBytes());
//                    out.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
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
//                System.err.println(">>>>>found: " + msg);
//            }
//        }
//    };
//
//
//    class AcceptThread extends Thread {
//        private String NAME_SECURE = "BluetoothChatSecure";
//        private UUID MY_UUID_SECURE = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
//        private UUID HC_06 = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
//        private BluetoothServerSocket mmServerSocket;
//        private BluetoothAdapter mAdapter;
//        private BluetoothSocket socket = null;
//
//        public AcceptThread() {
//            mAdapter = BluetoothAdapter.getDefaultAdapter();
//        }
//
//        @Override
//        public void run() {
//            try {
//                mmServerSocket = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID_SECURE);
//                socket = mmServerSocket.accept();
//                System.err.println(">>>>> accept");
//                new ReceiveDataThread(socket).start();
//                mmServerSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//    private class ReceiveDataThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//
//        public ReceiveDataThread(BluetoothSocket socket) {
//            mmSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//        }
//
//        @Override
//        public void run() {
//            byte[] buffer = new byte[1024];
//            int bytes;
//            while (true) {
//                try {
//                    // Read from the InputStream
//                    bytes = mmInStream.read(buffer);
//                    System.out.println("<><><><><>" + bytes);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        public void write(byte[] buffer) {
//            try {
//                mmOutStream.write(buffer);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
