// IStudentListener.aidl
package com.ttdevs.ipc;

import com.ttdevs.ipc.Student;

interface IStudentListener {
    oneway void updateStudent(in Student student);
}
