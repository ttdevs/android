/*
 * Created by ttdevs at 16-6-16 下午4:42.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.indicator;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LineIndicator extends View {
    private static final int MARGIN = 24, GAP = 6;
    private static final int STROKE_WIDTH = 28;
    private int mAlertColor;
    private int mIndicatorBgColor, mIndicatorProgressColor, mIndicatorTextColor;
    private int mAlertSize, mIndicatorSize;

    private Paint mTextPaint;

    private int mWidth = 0, mHeight = 0;

    public LineIndicator(Context context) {
        this(context, null);
    }

    public LineIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mAlertSize = (int) getResources().getDimension(R.dimen.line_alert_size);
        mIndicatorSize = (int) getResources().getDimension(R.dimen.line_indicator_size);

        mAlertColor = getResources().getColor(R.color.line_alert);
        mIndicatorBgColor = getResources().getColor(R.color.line_indicator_bg);
        ;
        mIndicatorProgressColor = getResources().getColor(R.color.line_indicator_progress);
        mIndicatorTextColor = getResources().getColor(R.color.line_indicator_text);

        mTextPaint = getAlertPaint();
    }

    private Paint getAlertPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mAlertColor);
        paint.setTextSize(mAlertSize);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }

    private Paint getProgressPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mAlertColor);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;

        int textHeight = ViewUtils.getTextHeight(mTextPaint);
        height += textHeight;
        height += MARGIN;
        height += STROKE_WIDTH * 2;
        height += MARGIN;
        height += textHeight;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画左侧文字
        canvas.drawText(mLeftAlert, GAP, ViewUtils.getTextHeight(mTextPaint), mTextPaint);
        canvas.drawText(mLeftContent, GAP, mHeight - GAP, mTextPaint);

        // 画右侧文字
        int rightAlertX = mWidth - ViewUtils.getTextWidth(mTextPaint, mRightAlert) - GAP;
        int rightContentX = mWidth - ViewUtils.getTextWidth(mTextPaint, mRightContent) - GAP;
        canvas.drawText(mRightAlert, rightAlertX, ViewUtils.getTextHeight(mTextPaint), mTextPaint);
        canvas.drawText(mRightContent, rightContentX, mHeight - GAP, mTextPaint);

        drawProgress(canvas);
    }

    /**
     * 画绿色进度和指示信息
     *
     * @param canvas
     */
    private void drawProgress(Canvas canvas) {
        // 画灰色进度
        Paint bgPaint = getProgressPaint();
        bgPaint.setColor(mIndicatorBgColor);
        canvas.drawLine(STROKE_WIDTH / 2, mHeight / 2,
                mWidth - STROKE_WIDTH / 2, mHeight / 2,
                bgPaint);

        if (mProgress == 0) {
            return;
        }
        // 画绿色进度
        Paint progressPaint = getProgressPaint();
        progressPaint.setColor(mIndicatorProgressColor);
        int stopX = (int) ((mWidth - STROKE_WIDTH) * mProgress);
        canvas.drawLine(STROKE_WIDTH / 2, mHeight / 2, stopX, mHeight / 2, progressPaint);

        Paint textPain = getAlertPaint();
        textPain.setTextSize(mIndicatorSize);
        textPain.setColor(mIndicatorTextColor);
        int textWidth = ViewUtils.getTextWidth(textPain, mAlert);

        // 画指示背景
        Paint alertPaint = getProgressPaint();
        alertPaint.setColor(mIndicatorProgressColor);
        alertPaint.setStrokeWidth(STROKE_WIDTH * 2.8f);
        canvas.drawLine(stopX - textWidth / 2, mHeight / 2, stopX + textWidth / 2, mHeight / 2, alertPaint);

        // 画指示文字
        Paint.FontMetricsInt fmi = textPain.getFontMetricsInt();
        float alertTextY = mHeight / 2 + Math.abs(fmi.bottom + fmi.top) / 2;
        canvas.drawText(mAlert, stopX - textWidth / 2, alertTextY, textPain);
    }


    private float mProgress = 0.0f;
    private String mLeftAlert = " ", mLeftContent = " ", mRightAlert = " ", mRightContent = " ";
    private String mAlert;

    public void setContent(String leftAlert, String leftContent,
                           String rightAlert, String rightContent) {
        mLeftAlert = leftAlert;
        mLeftContent = leftContent;
        mRightAlert = rightAlert;
        mRightContent = rightContent;

        postInvalidate();
    }

    /**
     * 设置当前进度，如下数值，结果为50%
     *
     * @param start    开始数值，如：60
     * @param end      结束数值，如：50
     * @param progress 当前数值，如：55
     */
    public void setIndicator(float start, float end, float progress) {
        mProgress = 0;
        setIndicator(start, end, progress, "");
    }

    /**
     * 设置当前进度，如下数值，结果为50%，但是显示alert的内容
     *
     * @param start    开始数值，如：60
     * @param end      结束数值，如：50
     * @param progress 当前数值，如：55
     * @param alert    提示文字
     */
    public void setIndicator(float start, float end, float progress, String alert) {
        float value = Math.abs((progress - start) / (end - start));

        if (TextUtils.isEmpty(alert)) {
            mAlert = NumberFormat.getPercentInstance().format(value);
        } else {
            mAlert = alert;
        }
        animateIndicator(value);
    }

    /**
     * 设置进度
     *
     * @param progress 进度值
     */
    public static final float MIN = 0.065f, MAX = 0.942f;

    public void setProgress(float progress) {
        if (progress > MAX) {
            mProgress = MAX;
        } else if (progress < MIN) {
            mProgress = MIN;
        } else {
            mProgress = progress;
        }

        postInvalidate();
    }

    /**
     * 获取进度
     *
     * @return 当前进度值
     */
    public float getProgress() {
        return mProgress;
    }

    public void animateIndicator(float progress) {
        Interpolator interpolator = new AnticipateOvershootInterpolator(1.8f);
        ObjectAnimator animation = ObjectAnimator.ofFloat(this, "progress", progress);
        animation.setDuration(3000);
        animation.setInterpolator(interpolator);
        animation.start();
    }
}
