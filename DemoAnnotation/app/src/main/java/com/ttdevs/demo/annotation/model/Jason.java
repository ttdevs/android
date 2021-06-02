package com.ttdevs.demo.annotation.model;

import com.ttdevs.demo.lib.annotation.Student;

@Student(name = "Jason", age = 20, duty = {BaseStudent.DUTY_CHINESE, BaseStudent.DUTY_SPORT})
public class Jason extends BaseStudent {

}
