/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ttdevs.android.conceal.ConcealDBHelper;
import com.ttdevs.android.conceal.ConcealUtil;
import com.ttdevs.android.utils.FileUtils;
import com.ttdevs.android.utils.LogUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class ConcealActivity extends BaseActivity {

    private StringBuilder mBuilder = new StringBuilder();
    private ConcealDBHelper mHelper;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            printFileLog((String) msg.obj);
        }
    };

    @BindView(R.id.bt_encrypt_string)
    Button btEncryptString;
    @BindView(R.id.bt_database)
    Button btDatabase;
    @BindView(R.id.bt_encrypt_file)
    Button btEncryptFile;
    @BindView(R.id.bt_decrypt_file)
    Button btDecryptFile;
    @BindView(R.id.et_original_string)
    EditText etOriginalString;
    @BindView(R.id.tv_log_info)
    TextView tvLogInfo;

    @OnClick({R.id.bt_encrypt_string,
            R.id.bt_file_list,
            R.id.bt_encrypt_file,
            R.id.bt_decrypt_file,
            R.id.bt_database,
            R.id.tv_log})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_encrypt_string:
                encryptString();
                break;
            case R.id.bt_database:
                operateDataBase();
                break;
            case R.id.bt_file_list:
                getFileList();
                break;
            case R.id.bt_encrypt_file:
                encryptFile();
                break;
            case R.id.bt_decrypt_file:
                decryptFile();
                break;
            case R.id.tv_log:
                printConcealKey();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conceal);

        mHelper = new ConcealDBHelper(this);

        initViewLogic();
    }

    private void initViewLogic() {
        etOriginalString.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvLogInfo.setText("0");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                if (!value.isEmpty()) {
                    tvLogInfo.setText(String.valueOf(value.length()));
                }
            }
        });
    }

    /**
     * 对字符串加密:<br/>
     * 5000字符加密没问题,时间1ms～2ms<br/>
     * 20000的时候出现丢帧,时间在10ms+
     */
    private void encryptString() {
        String content = etOriginalString.getEditableText().toString();
        if (TextUtils.isEmpty(content)) {
            LogUtils.showSnack(etOriginalString, "Content is null!");
            return;
        }

        btEncryptString.setEnabled(false);

        mBuilder.delete(0, mBuilder.length());

        printStringEncrypt("Original string:");
        printStringEncrypt(content);
        printStringEncrypt("Ecrypt string:");
        byte[] encrypt = ConcealUtil.encryptString(content);
        printStringEncrypt(String.valueOf(encrypt)); // TODO
        printStringEncrypt("Dcrypt string:");
        String decrypt = ConcealUtil.decryptString(encrypt);
        printStringEncrypt(decrypt);
        printStringEncrypt("Done !");

        btEncryptString.setEnabled(true);
    }

    private void operateDataBase() {
        mBuilder.delete(0, mBuilder.length());

        String content = etOriginalString.getEditableText().toString();
        if (TextUtils.isEmpty(content)) {
            LogUtils.showSnack(etOriginalString, "Content is null!");
            return;
        }

        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ConcealDBHelper.NAME, content);
        values.put(ConcealDBHelper.TOKEN, ConcealUtil.encryptString(content));

        printStringEncrypt("Begin transaction");
        try {
            db.beginTransaction();
            db.insert(ConcealDBHelper.TABLE, null, values);
            db.setTransactionSuccessful();

            printStringEncrypt(String.format("Insert: %s", content));

            Cursor cursor = db.query(ConcealDBHelper.TABLE, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ConcealDBHelper.NAME));
                byte[] encryptToken = cursor.getBlob(cursor.getColumnIndex(ConcealDBHelper.TOKEN));
                String token = ConcealUtil.decryptString(encryptToken);
                printStringEncrypt(String.format("Read: name:%s token:%s", name, token));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        printStringEncrypt("End transaction");

        db.close();
    }

    private void getFileList() {
        mBuilder.delete(0, mBuilder.length());

        String dir = FileUtils.getBaseFileDir();
        File[] files = new File(dir).listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            mBuilder.append(String.format("%d: %s %dKB %dMB\n", i + 1, file.getName(), file.length() / 1024, file.length() / 1024 / 1024));
        }

        tvLogInfo.setText(mBuilder.toString());
    }

    private void encryptFile() {
        btEncryptFile.setEnabled(false);

        mBuilder.delete(0, mBuilder.length());

        new Thread(new Runnable() {
            @Override
            public void run() {

                String dir = FileUtils.getBaseFileDir();
                File[] files = new File(dir).listFiles();
                for (File file : files) {
                    String fileName = file.getName();
                    if (!fileName.startsWith(ConcealUtil.PREFIX_D) && !fileName.startsWith(ConcealUtil.PREFIX_E)) {
                        long start = System.currentTimeMillis();
                        File destFile = ConcealUtil.encryptFile(file);
                        long end = System.currentTimeMillis();

                        Message msg1 = new Message();
                        msg1.obj = String.format("Encrpyt %s %dKB %dms", fileName, file.length() / 1024, end - start);
                        mHandler.sendMessage(msg1);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btEncryptFile.setEnabled(true);
                    }
                });
            }
        }).start();
    }

    private void decryptFile() {
        btDecryptFile.setEnabled(false);

        mBuilder.delete(0, mBuilder.length());

        new Thread(new Runnable() {
            @Override
            public void run() {

                String dir = FileUtils.getBaseFileDir();
                File[] files = new File(dir).listFiles();
                for (File file : files) {
                    String fileName = file.getName();
                    if (fileName.startsWith(ConcealUtil.PREFIX_E)) {
                        long start = System.currentTimeMillis();
                        File destFile = ConcealUtil.decryptFile(file);
                        long end = System.currentTimeMillis();

                        Message msg1 = new Message();
                        msg1.obj = String.format("Decrpyt %s %dKB %dms", fileName, file.length() / 1024, end - start);
                        mHandler.sendMessage(msg1);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btDecryptFile.setEnabled(true);
                    }
                });
            }
        }).start();
    }

    private void printConcealKey() {
        SharedPreferences sharedPreferences = getSharedPreferences("crypto", 0);
        // TODO 获取用于加密的key
    }

    private void printFileLog(String msg) {
        mBuilder.append(msg + "\n");
        tvLogInfo.setText(mBuilder.toString());
    }

    private void printStringEncrypt(String msg) {
        String info = System.currentTimeMillis() + ": " + msg + " \n";
        mBuilder.append(info);
        tvLogInfo.setText(mBuilder.toString());
    }
}
