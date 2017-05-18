package com.ttdevs.air;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btNormal:
                startActivity(new Intent(this, MainNormalActivity.class));
                break;
            case R.id.btBLE:
                startActivity(new Intent(this, MainBLEActivity.class));
                break;
            case R.id.btBike:
                startActivity(new Intent(this, BikeActivity.class));
                break;

            default:
                break;
        }
    }
}
