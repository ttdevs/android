// IStudentManager.aidl
package com.ttdevs.ipc;

import com.ttdevs.ipc.Student;

/**
* 1. 不能定义同名方法？
* 2. 基本类型（包括String）可以不加in
* 3. 自定义对象类型必须指定tag？
* 4. in可以带oneway，out和inout不能带oneway
* 5. oneway无reply.writeNoException()，所以无法用在out和inout上
* 6. oneway异步，非oneway同步
*/
interface IStudentManager {
    String getStudent(String student);
    // synchronous method
    void setStudent(String student);
    // async method
    oneway void setStudentOneWay(String student);

    void addStudentWithIn(in Student student);
    void addStudentWithOut(out Student student);
    void addStudentWithInout(inout Student student);
    Student addStudentWithInoutAndReturn(inout Student student);
}
