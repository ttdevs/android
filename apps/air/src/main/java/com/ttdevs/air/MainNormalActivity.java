package com.ttdevs.air;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ttdevs.air.utils.ReceiveDataThread;

import java.util.Set;

public class MainNormalActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_ENABLE_BT = 2333;
    // private static final String DEFAULT_MAC = "20:16:11:16:99:43";
    private static final String DEFAULT_MAC = "82:EA:CA:00:00:01";

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mDevice;

    private ReceiveDataThread mWorker;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            tvContent.setText(String.valueOf(msg.obj));
        }
    };

    private RadioGroup viewPairedDevices;
    private Button btStart;
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_normal);

        initView();
        initBluetooth();
    }

    private void initView() {
        btStart = (Button) findViewById(R.id.btStart);
        tvContent = (TextView) findViewById(R.id.tvContent);

        viewPairedDevices = (RadioGroup) findViewById(R.id.viewPairedDevices);
        viewPairedDevices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checked = (RadioButton) group.findViewById(checkedId);
                mDevice = (BluetoothDevice) checked.getTag();
            }
        });
    }

    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (null == mBluetoothAdapter) {
            tvContent.setText("BluetoothAdapter is null");
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            tvContent.setText("BluetoothAdapter is disable, please open it");

            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        }

        tvContent.setText("Bluetooth init success");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            return true;
        } else if (id == R.id.action_scan) {
            ScanActivity.comeOnBaby(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // TODO: 2017/1/20
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mWorker) {
            mWorker.stopThread();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btInit:
                initBluetooth();
                break;
            case R.id.btPaired:
                pairedBluetooth();
                break;
            case R.id.btStart:
                startBluetooth();
                break;
            case R.id.btStop:
                if (null != mWorker) {
                    mWorker.stopThread();
                }
                break;

            default:
                break;
        }
    }

    private void pairedBluetooth() {
        viewPairedDevices.removeAllViews();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (null != pairedDevices && pairedDevices.size() > 0) {
            StringBuilder msgBuilder = new StringBuilder();
            int index = 0;
            for (BluetoothDevice device : pairedDevices) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setId(index++);
                radioButton.setText(String.format("%s\n%s", device.getName(), device.getAddress()));
                radioButton.setTag(device);
                viewPairedDevices.addView(radioButton);

                msgBuilder.append(String.format("%s %s\n", device.getName(), device.getAddress()));
            }
            tvContent.setText(msgBuilder.toString());
            viewPairedDevices.setVisibility(View.VISIBLE);
        } else {
            viewPairedDevices.setVisibility(View.GONE);
        }
    }

    private void startBluetooth() {
        try {
            btStart.setEnabled(false);

            if (null != mWorker && mWorker.isAlive()) {
                Toast.makeText(this, "Running", Toast.LENGTH_SHORT).show();
                return;
            }

            if (null == mDevice) {
                mDevice = mBluetoothAdapter.getRemoteDevice(DEFAULT_MAC);
            }

            showUUID();

            mWorker = new ReceiveDataThread(mDevice, mHandler);
            mWorker.startThread();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            btStart.setEnabled(true);
        }
    }

    private void showUUID() {
        StringBuilder builder = new StringBuilder();

        ParcelUuid[] uuids = mDevice.getUuids();
        if (null == uuids) {
            return;
        }
        for (int i = 0; i < uuids.length; i++) {
            ParcelUuid uuid = uuids[i];
            builder.append(uuid.getUuid().toString());
        }

        tvContent.setText(builder.toString());
    }
}
