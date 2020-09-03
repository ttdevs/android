package com.ttdevs.android;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Created by ttdevs
 * 2017-03-03 (android)
 * https://github.com/ttdevs
 */
public class JavaTest {
    class Student {
        public int id;
        public String name;

        public Student(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (null == o) {
                return false;
            }

            if (o instanceof Student) {
                Student std = (Student) o;
                if (this.id == std.id) {
                    return true;
                }
            }

            return super.equals(o);
        }

        @Override
        public int hashCode() {
            return this.id;
            // return super.hashCode();
        }

        // ... getter and setter
    }

    class Jim extends Student {
        String school;

        public Jim(int id) {
            super(id);
        }
    }

    @Test
    public void getClazz() throws Exception {
        System.out.println(new Student(2).getClass());
        System.out.println(new Jim(1).getClass());
        System.out.println(new Jim(1).getClass().getSuperclass());
    }

    @Test
    public void testHeap() throws Exception {
        recursion();
    }

    int i = 0;

    private void recursion() {
        System.out.println("i:" + i++);
        recursion();
    }


    @Test
    public void testOOM() throws Exception {
        List<Object> dataList = new ArrayList<>();
        while (true) {
            dataList.add(String.valueOf(Math.random()).intern());

            System.out.println(dataList.size());
        }
    }

    @Test
    public void testString() throws Exception {
        String ss = "123456";
        System.out.println("ss = " + ss);
        String bb = ss.replace('1', '0');
        System.out.println("ss = " + ss);
        System.out.println("bb = " + bb);
        ss.intern();
        // ss.concat()
    }

    @Test
    public void testHashMap() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("first", "test");
        map.put(null, "one");
        map.put(null, "two");
        map.put(null, "three");
        map.put("name", "Whh");

        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> item = iterator.next();
            System.out.println(item.getKey() + "|" + item.getValue() + "|" + item.hashCode());
        }
    }

    class Person {
        public String name;
        public int age;

        public Person(String name) {
            this.name = name;
        }
    }

    @Test
    public void testSet() throws Exception {
        Person p1 = new Person("one");
        Person p2 = new Person("two");

        Set<Person> sets = new HashSet<>();
        sets.add(p1);
        sets.add(p2);
        System.out.println(sets.size());

        sets.add(p2);
        System.out.println(sets.size());

        System.out.println(p2.hashCode());
        p2.name = "two_new";
        System.out.println(p2.hashCode());

        sets.remove(p2);
        sets.add(p2);
        System.out.println(sets.size());

        for (Person person : sets) {
            System.out.println(person.name);
        }

        for (Iterator<Person> it = sets.iterator(); it.hasNext(); ) {
            System.out.println(it.next().name);
        }
    }

    @Test
    public void testOOM1() {
        Vector v = new Vector(10);
        for (int i = 1; i < 100; i++) {
            Object o = new Object();
            v.add(o);
            o = null;
        }

        // 监听器
        // 各种链接：数据库链接
        // 单例模式
    }

    @Test
    public void testCal() {
        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis(System.currentTimeMillis());
        int year = startCal.get(Calendar.YEAR);
        int month = startCal.get(Calendar.MONTH) + 1;
        int day = startCal.get(Calendar.DAY_OF_MONTH);
        String week = getWeek(startCal);
        int startHour = startCal.get(Calendar.HOUR_OF_DAY);
        int startMinute = startCal.get(Calendar.MINUTE);
        String format = "%d.%d.%d(%s) %d:%d ";
        String result = String.format(format,
                year, month, day,
                week,
                startHour, startMinute);
        System.out.println(result);
    }

    private String getWeek(Calendar calendar) {
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (week) {
            case 0:
                return "周日";
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";

            default:
                return "周日";
        }
    }
}
