package com.ttdevs.demo.annotation.model;

import com.ttdevs.demo.lib.annotation.Student;

@Student(name = "David", age = 50, duty = {BaseStudent.DUTY_MONITOR})
public class David extends BaseStudent {

}
