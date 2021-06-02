package com.ttdevs.demo.lib.compiler.utils;

import com.squareup.javapoet.ClassName;
import com.ttdevs.demo.lib.annotation.Student;

public class ClassUtils {
    public static final String PKG = "com.ttdevs.demo.annotation";
    public static final String STUDENT_MANAGER_NAME = "StudentManager";
    public static final ClassName STUDENT_MANAGER = ClassName.get(PKG, STUDENT_MANAGER_NAME);
    public static final ClassName TEXT_UTILS = ClassName.get("android.text", "TextUtils");
    public static final ClassName BASE_STUDENT = ClassName.get("com.ttdevs.demo.annotation.model", "BaseStudent");

    public static final Class<Student> CLASS_STUDENT = Student.class;
}
