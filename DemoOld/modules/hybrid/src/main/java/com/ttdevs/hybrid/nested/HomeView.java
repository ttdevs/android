/*
 * Created by ttdevs at 16-8-19 下午4:00.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.hybrid.nested;

import android.content.Context;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;

import android.util.AttributeSet;
import android.widget.LinearLayout;

public class HomeView extends LinearLayout implements NestedScrollingParent {

    private NestedScrollingParentHelper mHelper;

    public HomeView(Context context) {
        this(context, null);
    }

    public HomeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mHelper = new NestedScrollingParentHelper(this);
    }
}
