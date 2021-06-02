package com.ttdevs.demo.lib.compiler.utils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.ttdevs.demo.lib.annotation.Student;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class StudentManagerBuilder {
    private static final String KEY_MAP_STUDENT_NAME = "MAP_STUDENT_NAME";
    private static final String KEY_MAP_STUDENT_DUTY = "MAP_STUDENT_DUTY";
    private static final String KEY_INSTANCE = "INSTANCE";

    private Filer mFiler;

    private StudentManagerBuilder() {

    }

    public static StudentManagerBuilder create() {
        return new StudentManagerBuilder();
    }

    public StudentManagerBuilder filer(Filer filer) {
        mFiler = filer;
        return this;
    }

    /**
     * Create StudentManager.java
     *
     * @param values
     */
    public void build(Map<String, Element> values) {
        LogUtils.d("=========Create   file============");

        TypeSpec.Builder builder = TypeSpec.classBuilder(ClassUtils.STUDENT_MANAGER)
                .addModifiers(Modifier.PUBLIC);

        builder.addField(FieldSpec.builder(ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class), ClassUtils.BASE_STUDENT
        ), KEY_MAP_STUDENT_NAME)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T<>()", HashMap.class)
                .build());
        builder.addField(FieldSpec.builder(ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class), ClassUtils.BASE_STUDENT
        ), KEY_MAP_STUDENT_DUTY)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T<>()", HashMap.class)
                .build());

        builder.addField(FieldSpec.builder(ClassUtils.STUDENT_MANAGER, KEY_INSTANCE)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T()", ClassUtils.STUDENT_MANAGER)
                .build());

        builder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addCode(buildConstructor(values))
                .build());

        builder.addMethod(buildGetStudentMethod("duty"));
        builder.addMethod(buildExamMethod());
        builder.addMethod(buildStudyMethod());
        builder.addMethod(buildWorkMethod());

        try {
            JavaFile.builder(ClassUtils.PKG, builder.build())
                    .build()
                    .writeTo(mFiler);
        } catch (IOException e) {
            LogUtils.e(e.getMessage());
        }
    }


    /**
     * Build constructor
     *
     * @param values
     * @return
     */
    private CodeBlock buildConstructor(Map<String, Element> values) {
        CodeBlock.Builder builder = CodeBlock.builder();

        for (String key : values.keySet()) {
            Element value = values.get(key);
            Student student = value.getAnnotation(ClassUtils.CLASS_STUDENT);

            String keyName = "temp" + key;
            builder.addStatement("$T $N = new $T()", ClassUtils.BASE_STUDENT, keyName, ClassUtils.BASE_STUDENT);
            builder.addStatement("$N.name = $S", keyName, student.name());
            builder.addStatement("$N.age = $L", keyName, student.age());
            if (student.duty().length > 0) {
                StringBuilder classmate = new StringBuilder();
                classmate.append("new java.lang.String[]");
                classmate.append("{");
                classmate.append("\"" + student.duty()[0] + "\"");
                for (int i = 1; i < student.duty().length; i++) {
                    classmate.append(", \"" + student.duty()[i] + "\"");
                }
                classmate.append("}");
                LogUtils.d(classmate.toString());
                builder.addStatement("$N.duty = $N", keyName, classmate.toString());
            }
            builder.addStatement("$N.put($S,$N)", KEY_MAP_STUDENT_NAME, key, keyName);
        }
        builder.addStatement("");
        for (String key : values.keySet()) {
            Element value = values.get(key);
            Student student = value.getAnnotation(ClassUtils.CLASS_STUDENT);
            if (null != student.duty() && student.duty().length > 0) {
                for (String duty : student.duty()) {
                    builder.addStatement("$N.put($S,$N.get($S))", KEY_MAP_STUDENT_DUTY, duty, KEY_MAP_STUDENT_NAME, student.name());
                }
            }
        }
        return builder.build();
    }

    /**
     * Build getStudent method
     *
     * @return
     */
    private MethodSpec buildGetStudentMethod(String duty) {
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.addStatement("return $N.get($N)", KEY_MAP_STUDENT_DUTY, duty);
        return MethodSpec
                .methodBuilder("getStudent")
                .addJavadoc("Get student by duty \n\n@param $N \n@return", duty)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, duty)
                .returns(ClassUtils.BASE_STUDENT)
                .addCode(builder.build())
                .build();
    }

    /**
     * Build exam method
     *
     * @return
     */
    private MethodSpec buildExamMethod() {
        CodeBlock.Builder builder = CodeBlock.builder();

        builder.addStatement("int result = 0");
        builder.beginControlFlow("for ($T key : $N.keySet())", String.class, KEY_MAP_STUDENT_NAME)
                .addStatement("$T student = $N.get(key)", ClassUtils.BASE_STUDENT, KEY_MAP_STUDENT_NAME)
                .addStatement("result += student.exam()", ClassUtils.BASE_STUDENT, KEY_MAP_STUDENT_NAME)
                .endControlFlow();
        builder.addStatement("return result/$N.size()", KEY_MAP_STUDENT_NAME);
        return MethodSpec
                .methodBuilder("exam")
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addCode(builder.build())
                .build();
    }

    /**
     * Build study method
     *
     * @return
     */
    private MethodSpec buildStudyMethod() {
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.beginControlFlow("for ($T key : $N.keySet())", String.class, KEY_MAP_STUDENT_NAME)
                .addStatement("$T student = $N.get(key)", ClassUtils.BASE_STUDENT, KEY_MAP_STUDENT_NAME)
                .addStatement("student.study()")
                .endControlFlow();
        return MethodSpec
                .methodBuilder("study")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addCode(builder.build())
                .build();
    }

    /**
     * Build work method
     *
     * @return
     */
    private MethodSpec buildWorkMethod() {
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.beginControlFlow("for ($T key : $N.keySet())", String.class, KEY_MAP_STUDENT_NAME)
                .addStatement("$T student = $N.get(key)", ClassUtils.BASE_STUDENT, KEY_MAP_STUDENT_NAME)
                .addStatement("student.work()")
                .endControlFlow();
        return MethodSpec
                .methodBuilder("work")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addCode(builder.build())
                .build();
    }
}
