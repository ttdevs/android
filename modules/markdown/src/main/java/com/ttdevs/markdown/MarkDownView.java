/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.markdown;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * MarkDownView
 */
public class MarkDownView extends LinearLayout implements View.OnClickListener {
    private static final String DEFAULT_PATH = "file:///android_asset/markdown.html";
    private static final String JS_FORMAT_PATH = "javascript:parseMarkdown(\"%s\")";

    private WebView mWebView;
    private View viewBack;
    private ProgressBar viewProgress;

    private String mContent;
    private boolean isShowDefault;

    public MarkDownView(Context context) {
        this(context, null);
    }

    public MarkDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarkDownView);
        mContent = typedArray.getString(R.styleable.MarkDownView_mdv_content);
        isShowDefault = typedArray.getBoolean(R.styleable.MarkDownView_mdv_showDefault, true);
        typedArray.recycle();

        LayoutInflater.from(getContext()).inflate(R.layout.view_markdown, this);

        int height = context.getResources().getDisplayMetrics().heightPixels - getActionBarHeight(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
        // View view = LayoutInflater.from(getContext()).inflate(R.layout.view_markdown, null);
        // int height = context.getResources().getDisplayMetrics().heightPixels - getActionBarHeight(context);
        // addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, height));

        mWebView = (WebView) findViewById(R.id.wv_content);
        viewProgress = (ProgressBar) findViewById(R.id.view_progress);
        viewBack = findViewById(R.id.view_back);
        viewBack.setOnClickListener(this);

        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.loadUrl(DEFAULT_PATH);
    }

    public void loadMarkdown(String markdownStr) {
        if (TextUtils.isEmpty(markdownStr)) {
            return;
        }
        markdownStr = markdownStr.replace("\n", "\\n").replace("\"", "\\\"").replace("'", "\\'");
        mContent = markdownStr;

        mWebView.loadUrl(String.format(JS_FORMAT_PATH, markdownStr));
    }

    /**
     * 返回
     *
     * @return
     */
    public boolean goBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
        return mWebView.canGoBack();
    }

    @Override
    public void onClick(View v) {
        goBack();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (url.equals(DEFAULT_PATH)) {
//                loadMarkdown(mContent); // TODO
//                return true;
//            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            viewProgress.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            viewProgress.setVisibility(View.GONE);

            if (DEFAULT_PATH.equals(url)) {
                if (!isShowDefault) {
                    mContent = "";
                }
                loadMarkdown(mContent);
            }

            // viewBack.setVisibility(mWebView.canGoBack() ? View.VISIBLE : View.GONE);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            viewProgress.setProgress(newProgress);
        }
    }

    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        return context.getResources().getDimensionPixelSize(tv.resourceId);
    }
}
