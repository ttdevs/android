package com.ttdevs.air;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScanActivity extends AppCompatActivity {

    private static final List<BluetoothDevice> mDataList = new ArrayList<>();
    private RecyclerView rvContent;
    private DeviceAdapter mAdapter;

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        initBluetooth();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        registerReceiver(mReceiver, filter);
    }

    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            finish();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 2333);
        }
    }

    private void initView() {
        mAdapter = new DeviceAdapter(mDataList);
        rvContent = (RecyclerView) findViewById(R.id.rvContent);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setAdapter(mAdapter);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                } else {
                    mBluetoothAdapter.startDiscovery();
                    mDataList.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // go back
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mReceiver);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothClass clazz = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mDataList.add(device);
                mAdapter.notifyDataSetChanged();
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                System.out.println(">>>>>>change");
            } else if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)) {
                System.out.println(">>>>>>pairing");
            }
        }
    };

    class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {
        private List<BluetoothDevice> mDataList;

        public DeviceAdapter(List<BluetoothDevice> dataList) {
            mDataList = dataList;
        }

        @Override
        public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_scan_device, parent, false);
            DeviceHolder viewHolder = new DeviceHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(DeviceHolder holder, int position) {
            BluetoothDevice device = mDataList.get(position);
            holder.tvName.setText(device.getName());
            holder.tvMAC.setText(device.getAddress());
            holder.tvUUID.setText(showUUID(device));
            holder.viewRoot.setTag(device);
            holder.viewRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BluetoothDevice device = (BluetoothDevice) v.getTag();
                    showConnect(v.getContext(), device);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        private String showUUID(BluetoothDevice device) {
            StringBuilder builder = new StringBuilder();

            ParcelUuid[] uuids = device.getUuids();
            if (null == uuids) {
                return "";
            }
            for (int i = 0; i < uuids.length; i++) {
                ParcelUuid uuid = uuids[i];
                builder.append(uuid.getUuid().toString());
            }

            return builder.toString();
        }

        private void showConnect(Context context, final BluetoothDevice device) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(device.getName() + "  " + device.getAddress())
                    .setPositiveButton("连接", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

//                            try {
//                                device.createBond();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }

                            try {
                                //UUID uuid = UUID.fromString("00001000-0000-1000-8000-00805F9B34FB");
                                UUID uuid = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
                                ParcelUuid[] uuids = device.getUuids();
                                if (null != uuids && uuids.length > 0) {
                                    uuid = device.getUuids()[which].getUuid();
                                }
                                device.createRfcommSocketToServiceRecord(uuid).connect();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            ParcelUuid[] uuids = device.getUuids();
            if (null != uuids && uuids.length > 0) {
                int size = uuids.length;
                String[] uuidItems = new String[size];
                for (int i = 0; i < size; i++) {
                    ParcelUuid uuid = uuids[i];
                    uuidItems[i] = uuid.getUuid().toString();
                }
                builder.setSingleChoiceItems(uuidItems, 0, null);
            }

            builder.show();
        }

        class DeviceHolder extends RecyclerView.ViewHolder {
            public View viewRoot;
            public TextView tvName;
            public TextView tvMAC;
            public TextView tvUUID;
            public RadioButton rbPaired;

            public DeviceHolder(View itemView) {
                super(itemView);

                viewRoot = itemView.findViewById(R.id.viewRoot);
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                tvMAC = (TextView) itemView.findViewById(R.id.tvMAC);
                tvUUID = (TextView) itemView.findViewById(R.id.tvUUID);
                rbPaired = (RadioButton) itemView.findViewById(R.id.rbPaired);
            }
        }
    }

    public static void comeOnBaby(Context context) {
        if (null == context) {
            return;
        }
        context.startActivity(new Intent(context, ScanActivity.class));
    }
}
