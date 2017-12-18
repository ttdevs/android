package com.ttdevs.android.transformers;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.ttdevs.android.utils.LogUtils;


public class CustomViewPager extends ViewPager {

    public CustomViewPager(Context context) {
        this(context, null);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        setClipToPadding(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int n) {
        int result = n;

        // 最后一个
        if (n == childCount - 1) {
            result = getCurrentItem();
        } else if(n >= getCurrentItem()) {
            result = n + 1;
        }

        LogUtils.debug(String.format("ChildCount:%d n:%d result:%d getCurrentItem:%d", childCount, n, result, getCurrentItem()));

        return result;
    }
}