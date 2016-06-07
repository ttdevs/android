/*
 * Created by ttdevs at 16-5-6 下午4:23.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.deeplinking;

import android.app.Activity;
import android.os.Bundle;

/**
 * This Activity class defines the home screen for the recipe app.
 */
public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
