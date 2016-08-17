/*
 * Created by ttdevs at 16-8-12 下午6:24.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.hybrid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class HomeView extends LinearLayout {
    public enum Status {
        one, two, three;
    }

    private Status mStatus = Status.one;

    private ChildView mChildHeader;
    private ChildWebView mWebView;

    public HomeView(Context context) {
        this(context, null);
    }

    public HomeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);

        mChildHeader = new ChildView(context);
        mChildHeader.setVerticalScrollBarEnabled(true);
        mWebView = new ChildWebView(context);

        addView(mChildHeader, new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        addView(mWebView, new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                Rect rect = new Rect();
                getDrawingRect(rect);
                int height = rect.bottom - rect.top;
                print("onPageFinished: " + height);
                mWebView.getLayoutParams().height = height;
                removeView(mWebView);
                addView(mWebView, 1, new LayoutParams(LayoutParams.MATCH_PARENT, height));
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    public void loadUrl(String url) {
        if (null != mWebView && !TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        }

        countViewSize(mWebView); // TODO: 16/8/16
    }

    public void setHeaderView(View header) {
        if (null != header) {
            mChildHeader.addView(
                    header,
                    new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT
                    )
            );
            header.invalidate();
        }
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);

        countViewSize(mChildHeader);
    }

    private int mActivePointerId;
    private float mLastMotionY;
    private int mMove;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        print("dispatchTouchEvent:" + event.getAction());

        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = event.getPointerId(0);
                mLastMotionY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int activePointerIndex = event.findPointerIndex(mActivePointerId);
                float y = event.getY(activePointerIndex);
                int move = (int) (y - mLastMotionY); //向上滑move<0,累积的mMove<0
                print("onInterceptTouchEvent move: " + move);
                mMove += move;

                switch (mStatus) {
                    case one:
                        if (mChildHeader.isScrollBottom() && move < -10) { // header滑到底部然后继续下滑
                            scrollTo(0, mChildHeader.getMeasuredHeight());
                            mStatus = Status.two;
                            return true;
                        }
                        break;
                    case two:
                        if (mWebView.isScrollTop() && move > -10) {
                            scrollTo(0, 0);
                            mStatus = Status.one;
                            return true;
                        }
                        break;
                    case three:

                        break;

                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;

            default:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

//    private void scrollToPos(int x, int y) {
//        int scrollY = getScrollY();
//        float des = Math.abs(y - scrollY) / 10;
//        for (int i = 0; i < 10; i++) {
//            if (y > scrollY) {
//                scrollTo(0, (int) (scrollY + des * i));
//            } else {
//                scrollTo(0, (int) (scrollY - des * i));
//            }
//
//        }
//    }

    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    private void printViewHeight() {
        String result = "Home:" + getHeight();
        result += " measure:" + getMeasuredHeight();
        result += " Header:" + mChildHeader.getHeight();
        result += " webview:" + mWebView.getHeight();
        print(result);
    }

    private void print(String msg) {
        String result = String.format(">>>>>%s", msg);
        System.err.println(result);
    }

    private void countViewSize(final View view) {
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        print(String.format("left:%d, top:%d, bottom:%d, right:%d", rect.left, rect.top, rect.bottom, rect.right));

        postDelayed(new Runnable() {
            @Override
            public void run() {
                Rect rect = new Rect();
                view.getWindowVisibleDisplayFrame(rect);
                print(String.format("getWindowVisibleDisplayFrame left:%d, top:%d, bottom:%d, right:%d", rect.left, rect.top, rect.bottom, rect.right));

                view.getDrawingRect(rect);
                print(String.format("getDrawingRect left:%d, top:%d, bottom:%d, right:%d", rect.left, rect.top, rect.bottom, rect.right));
            }
        }, 2000);
    }
}
