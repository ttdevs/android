/*
 * Created by ttdevs at 16-8-8 上午10:25.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.serialize;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public String userName;
    public int age;
    public boolean login;
    public float score;

    protected User(Parcel in) {
        userName = in.readString();
        age = in.readInt();
        login = in.readByte() != 0;
        score = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeInt(age);
        dest.writeByte((byte) (login ? 1 : 0));
        dest.writeFloat(score);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
