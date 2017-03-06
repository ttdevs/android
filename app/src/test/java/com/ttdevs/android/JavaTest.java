package com.ttdevs.android;

import org.junit.Test;

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
}
