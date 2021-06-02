package com.ttdevs.demo.annotation.model;

import com.ttdevs.demo.lib.annotation.Student;
import com.ttdevs.demo.lib.annotation.Teacher;

@Teacher(name = "Zhang")
@Student(name = "Norris", age = 28)
public class Norris extends BaseStudent {

}
