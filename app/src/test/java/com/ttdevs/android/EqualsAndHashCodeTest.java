package com.ttdevs.android;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by ttdevs
 * 2017-03-03 (android)
 * https://github.com/ttdevs
 */
public class EqualsAndHashCodeTest {
    @Test
    public void equals() throws Exception {
        String s1 = "nihao";
        String s2 = "nihao";
        String s3 = new String("nihao");
        assertEquals(s1, s2);
        assertNotEquals(s1, s3);

        System.out.println(s1 == s2);    //    true
        System.out.println(s1 == s3);    //    false
    }

    class Student {
        private int id;
        private String name;

        public Student(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (null == o) {
                return false;
            }

            // 有时候instanceof不合适的时候可以考虑用getClass()方法
            if (getClass() != o.getClass()) {
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

    @Test
    public void equalsStudent() throws Exception {
        Student st1 = new Student(2333);
        Student st2 = new Student(2333);
        assertEquals(st1, st2);
        // assertNotEquals(st1, st2);
    }

    @Test
    public void studentSet() throws Exception {
        Student st1 = new Student(2333);
        Student st2 = new Student(2333);

        Set<Student> stds = new HashSet<>();
        stds.add(st1);
        stds.add(st2);
        assertEquals(1, stds.size());
    }

    @Test
    public void stringTest() throws Exception {
        String str1 = "abc";
        String str2 = "abc";
        String str3 = new String("abc");

        System.out.println(str1 == str2);
        System.out.println(str1 == str3);

        assertEquals(str1, str2);
        assertEquals(str1, str3);
    }
}
