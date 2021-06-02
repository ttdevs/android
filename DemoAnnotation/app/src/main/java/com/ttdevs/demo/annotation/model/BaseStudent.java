package com.ttdevs.demo.annotation.model;

import java.util.Arrays;

public class BaseStudent {
    public static final String DUTY_MONITOR = "ClassMonitor";
    public static final String DUTY_CHINESE = "Chinese";
    public static final String DUTY_MATH = "Math";
    public static final String DUTY_ART = "Art";
    public static final String DUTY_SPORT = "Sport";

    public String name;
    public int age;
    public String[] duty;

    public BaseStudent() {

    }

    public int exam() {
        return (int) (Math.random() * 100);
    }

    public void study() {
        System.out.println(name + " is studying.");
    }

    public void work() {
        if (null == duty) {
            System.out.println(name + " duty is null");
        } else {
            System.out.println(name + " duty is: " + Arrays.asList(duty));
        }
    }
}
