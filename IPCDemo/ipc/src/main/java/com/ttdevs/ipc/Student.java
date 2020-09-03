package com.ttdevs.ipc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is Student
 *
 * @author : ttdevs@gmail.com
 * @date : 2020/08/31
 */
public class Student implements Parcelable {
    private String name;
    private int age;

    public Student() {
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    protected Student(Parcel in) {
        readFromParcel(in);
    }

    /**
     * 需要手动编写。
     * 若无tag: out，则不用写此方，具体查看源码。
     *
     * @param in
     */
    public void readFromParcel(Parcel in) {
        name = in.readString();
        age = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

