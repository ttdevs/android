package com.ttdevs.android.test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * java的深拷贝与浅拷贝
 * <p>
 * Created by ttdevs
 * 2017-03-14 (android)
 * https://github.com/ttdevs
 */

public class JavaObjectCopyTest {

    class Worker implements Cloneable {
        public int id;
        public Person person;

        public Worker(int id, Person person) {
            this.id = id;
            this.person = person;
        }

        @Override
        protected Worker clone() throws CloneNotSupportedException {
            Worker worker = (Worker) super.clone();
            worker.person = person.clone();
            return worker;
        }
}

    class Student implements Cloneable {
        public int id;
        public Person person;

        public Student(int id, Person person) {
            this.id = id;
            this.person = person;
        }

        @Override
        protected Student clone() throws CloneNotSupportedException {
            return (Student) super.clone();
        }
    }

    class Person implements Cloneable {
        public String name;
        public boolean isMan;

        public Person(String name, boolean isMan) {
            this.name = name;
            this.isMan = isMan;
        }

        @Override
        protected Person clone() throws CloneNotSupportedException {
            return (Person) super.clone();
        }
    }

    @Test
    public void addition_isCorrect() throws Exception {
        Person person = new Person("Jim", true);
        Student student = new Student(2017001, person);
        Worker worker = new Worker(90010001, person);

        Person person1 = person.clone();
        Student student1 = student.clone();
        Worker worker1 = worker.clone();

        assertNotEquals(student, student1);
        assertNotEquals(person, person1);
        assertEquals(student.person, student1.person);
        assertNotEquals(worker.person, worker1.person);
    }
}