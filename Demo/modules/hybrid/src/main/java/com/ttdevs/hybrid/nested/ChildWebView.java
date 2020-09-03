/*
 * Created by ttdevs at 16-8-19 下午4:00.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.hybrid.nested;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.ttdevs.hybrid.scroller.IScrollStatus;

public class ChildWebView extends WebView implements IScrollStatus {
    private boolean isScrollBottom;

    public ChildWebView(Context context) {
        this(context, null);
    }

    public ChildWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChildWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // from: food
        float contentHeight = getContentHeight() * getScale();
        float currentHeight = getHeight() + getScrollY();
        if ((contentHeight - currentHeight) <= DISTANCE) {
            isScrollBottom = true;
        } else {
            isScrollBottom = false;
        }
    }

    @Override
    public boolean isScrollTop() {
        return getScrollY() == 0;
    }

    @Override
    public boolean isScrollBottom() {
        return isScrollBottom;
    }
}
