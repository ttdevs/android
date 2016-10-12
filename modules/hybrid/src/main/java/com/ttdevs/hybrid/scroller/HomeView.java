/*
 * Created by ttdevs at 16-8-18 下午1:56.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.hybrid.scroller;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class HomeView extends LinearLayout {
    private static final int DELAY = 300;

    private enum Status {
        one, two;
    }

    private Scroller mScroller;
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

        mScroller = new Scroller(context);

        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        mChildHeader = new ChildView(context);
        mWebView = new ChildWebView(context);

        addView(mChildHeader);
        addView(mWebView);

        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mWebView.setVerticalScrollBarEnabled(true);
//        mWebView.setHorizontalScrollBarEnabled(false);
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//            }
//        });
//        mWebView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
//            }
//        });
    }

    /**
     * 载入网页
     *
     * @param url
     */
    public void loadUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        }
    }

    /**
     * 添加header
     *
     * @param header
     */
    public void setHeaderView(View header) {
        if (null != header) {
            mChildHeader.removeAllViews();
            mChildHeader.addView(header);
        }
    }

    public WebView getWebView() {
        return mWebView;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWebView.getLayoutParams().height = h;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    private int mActivePointerId;
    private float mLastMotionY;
    private int mMove;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = event.getPointerId(0);
                mLastMotionY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int activePointerIndex = event.findPointerIndex(mActivePointerId);
                float y = event.getY(activePointerIndex);
                int move = (int) (y - mLastMotionY);    // 向上滑move<0

                switch (mStatus) {
                    case one:
                        if (mChildHeader.isScrollBottom() && move < 0) {
                            return true;
                        }
                        break;
                    case two:
                        if (mChildHeader.isScrollBottom() && mWebView.isScrollTop()) { //mWebView显示在最上
                            if (move > 0) {
                                return true;
                            }
                        }
                        break;

                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;

            default:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = event.getPointerId(0);
                mLastMotionY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int activePointerIndex = event.findPointerIndex(mActivePointerId);
                float y = event.getY(activePointerIndex);
                int move = (int) (y - mLastMotionY);    // 向上滑move<0

                if (getScrollY() - getHeight() > IScrollStatus.DISTANCE) {
                    return true; // 当webview的事件被拦截时，禁止上滑
                }

                mMove += move;
                mLastMotionY = y;
                scrollBy(0, -move);

                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (Math.abs(mMove) > IScrollStatus.DISTANCE) {
                    if (mMove > 0) { // 下滑
                        scrollToOne();
                    } else { // 上滑
                        scrollToTwo();
                    }
                }
                break;

            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    private void scrollToOne() {
        // scrollTo(0, 0);
        mStatus = Status.one;
        mMove = 0;

        mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), DELAY);
        invalidate();
    }

    private void scrollToTwo() {
        // scrollTo(0, mChildHeader.getMeasuredHeight());
        mStatus = Status.two;
        mMove = 0;

        mScroller.startScroll(0, getScrollY(), 0, mChildHeader.getMeasuredHeight() - getScrollY(), DELAY);
        invalidate();
    }
}
