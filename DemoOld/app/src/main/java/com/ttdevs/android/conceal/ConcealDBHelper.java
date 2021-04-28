/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android.conceal;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import com.ttdevs.android.utils.LogUtils;

public class ConcealDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "conceal.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE = "conceal";
    public static final String NAME = "name";
    public static final String TOKEN = "token";

    public ConcealDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE +
                " (_id integer primary key autoincrement, " +
                NAME + " text, " +
                TOKEN + " blob not null);";
        LogUtils.debug(String.format("Create sql: %s", sql));
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
//            String sql = null;
//            if (oldVersion == 1) {
//                sql = "alter table events add note text;";
//            }
//
//            if (oldVersion == 2) {
//                sql = "";
//            }
//
//            Log.d("EventsData", "onUpgrade\t: " + sql);
//            if (sql != null) {
//                db.execSQL(sql);
//            }
        }
    }
}
