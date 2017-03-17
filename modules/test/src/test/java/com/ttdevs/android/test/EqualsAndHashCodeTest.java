package com.ttdevs.android.test;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * equals 和 hashCode
 * <p>
 * Created by ttdevs
 * 2017-03-03 (android)
 * https://github.com/ttdevs
 */
public class EqualsAndHashCodeTest {

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
            return this.id; // return super.hashCode();
        }
    }

    @Test
    public void equalsStudent() throws Exception {
        Student st1 = new Student(2333);
        Student st2 = new Student(2333);
        assertEquals(st1, st2);
    }

    @Test
    public void studentSet() throws Exception {
        Student st1 = new Student(2333);
        Student st2 = new Student(2333);

        Set<Student> students = new HashSet<>();
        students.add(st1);
        students.add(st2);
        assertEquals(1, students.size());
    }

    @Test
    public void equals() throws Exception {
        String s1 = "hello";
        String s2 = "hello";
        String s3 = new String("hello");
        assertEquals(s1, s2);
        assertNotEquals(s1, s3);
    }
}
