/*
 * Created by ttdevs at 16-8-12 下午6:25.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.hybrid;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class ChildWebView extends WebView implements IScrollStatus {

    private boolean isScrollBottom;

    public ChildWebView(Context context) {
        super(context);
    }

    public ChildWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        // TODO: 16/8/16
        float webViewContentHeight = this.getContentHeight() * this.getScale();
        float webViewCurrentHeight = (this.getHeight() + this.getScrollY());
        if ((webViewContentHeight - webViewCurrentHeight) <= 2.0F) {
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
