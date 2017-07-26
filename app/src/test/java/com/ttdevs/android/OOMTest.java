/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android;

import org.junit.Test;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;


public class OOMTest {

    @Test
    public void testStringLocale() throws Exception {
        Vector v = new Vector(10);
        for (int i = 1; i < 10000; i++) {
            Object o = new Object();
            v.add(o);
            o = null;
        }
    }
}