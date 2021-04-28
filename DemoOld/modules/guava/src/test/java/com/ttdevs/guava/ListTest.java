/*
 * Created by ttdevs at 16-12-20 下午4:40.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.guava;

import com.google.common.base.Joiner;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ttdevs
 * 2016-12-20 (android)
 * https://github.com/ttdevs
 */
public class ListTest {

    @Test
    public void testListSeparate() {
        List<String> list = new ArrayList<>();
        list.add("java");
        list.add("oc");
        list.add("js");
        list.add(null);
        list.add("python");

        StringBuilder builder = new StringBuilder();
        for (String item : list) {
            if (item != null) {
                builder.append(item).append("|");
            }
        }
        builder.setLength(builder.length() - 1);
        System.out.println(builder.toString());

        String withoutNull = Joiner.on("|").skipNulls().join(list);
        System.out.println(withoutNull);

        String replaceNull = Joiner.on("|").useForNull("null value").join(list);
        System.out.println(replaceNull);
    }
}
