// IStudentManager.aidl
package com.ttdevs.ipc;

import com.ttdevs.ipc.Student;
import com.ttdevs.ipc.IStudentListener;

/**
* Student Manager
*/
interface IStudentManager {
    // String getStudent(String student); // 不指定tag默认为in
    // void addStudentWithNull(Student student); // 自定义类型必须指定tag
    // void addStudentWithIn(String student); // 不能定义同名方法？
    // oneway void addStudentWithOut(out Student student); // oneway 不能合用 out
    // oneway void addStudentWithInout(inout Student student); // oneway 不能合用 inout

    String getStudent(String student);
    oneway void addStudentOneway(in String student);
    void addStudentWithIn(in Student student);
    void addStudentWithOut(out Student student);
    void addStudentWithInout(inout Student student);
    Student addStudentWithInoutAndReturn(inout Student student);

    oneway void register(IStudentListener listener);
    oneway void unregister(IStudentListener listener);
}
