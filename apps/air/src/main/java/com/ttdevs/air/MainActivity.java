package com.ttdevs.air;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
