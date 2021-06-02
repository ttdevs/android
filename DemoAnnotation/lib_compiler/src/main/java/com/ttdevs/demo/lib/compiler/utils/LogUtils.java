package com.ttdevs.demo.lib.compiler.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class LogUtils {
    public static Messager mMessager;

    public static void init(Messager messager) {
        mMessager = messager;
    }

    public static void d(String msg) {
        if (null != mMessager) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
        }
    }

    public static void e(String msg) {
        if (null != mMessager) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, msg);
        }
    }
}
