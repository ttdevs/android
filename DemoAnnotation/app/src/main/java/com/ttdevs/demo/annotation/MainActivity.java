package com.ttdevs.demo.annotation;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btExam:
//                int result = StudentManager.INSTANCE.exam();
//                Toast.makeText(this, "Exam: " + result, Toast.LENGTH_LONG).show();
//                break;
//            case R.id.btStudy:
//                StudentManager.INSTANCE.study();
//                break;
//            case R.id.btIntroduce:
//                StudentManager.INSTANCE.work();
//                break;
//            default:
//                break;
//        }
    }
}