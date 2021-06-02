package com.ttdevs.demo.lib.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Student {

    /**
     * Student name
     *
     * @return
     */
    String name();

    /**
     * Student age
     *
     * @return
     */
    int age() default 8;

    /**
     * Duty information, ClassMonitor, Chinese, Math, Sport, Art etc.
     *
     * @return
     */
    String[] duty() default {};
}
