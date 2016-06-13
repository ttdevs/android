/*
 * Created by ttdevs at 16-6-8 上午9:37.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.circleview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.ttdevs.circleview.utils.Utils;

public class CircleProgress extends View {
    private static final int CIRCLE_START_ANGLE = 135; // 起始角度
    private static final int CIRCLE_SWEEP_ANGLE = 270; // 展示角度
    private static final int CIRCLE_STROKE_WIDTH = 56; // 指示圆环的宽度

    private static final float CIRCLE_GAP = 2.6f;

    //为了可以改
    private int mNormalGray = Color.parseColor("#FFDFDFDF");
    private int mTitleColor, mAlertColor, mContentColor;
    private int mCircleBackground, mCircleGray, mCircleGreen;
    private int mIndicatorCenter, mIndicatorGray, mIndicatorLight;
    private int mCenterCircleOut, mCenterCircleMiddle, mCenterCircleInside;
    private int mDividerWidth = 32;

    private int mWidth = 0;
    private int mHeight = 0;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircularProgressBar)
//        mBackgroundColor = a.getColor(R.styleable.CircularProgressBar_cpbProgressBackground, mBackgroundColor);
//        mProgressColor = a.getColor(R.styleable.CircularProgressBar_cpbProgressColor, mProgressColor);
//        mStrokeWidth = (int) a.getDimension(R.styleable.CircularProgressBar_cpbStrokeWidth, mStrokeWidth);
//        mStartAngle = a.getInt(R.styleable.CircularProgressBar_cpbStartAngle, mStartAngle);
//        mSweepAngle = a.getInt(R.styleable.CircularProgressBar_cpbSweepAngle, mSweepAngle);
//        mIsCapRound = a.getBoolean(R.styleable.CircularProgressBar_cpbIsCapRound, mIsCapRound);
//        a.recycle();

        initData();
    }

    private void initData() {
        mNormalGray = getResources().getColor(R.color.cp_normal_gray);

        mTitleColor = getResources().getColor(R.color.cp_circle_title);
        mAlertColor = getResources().getColor(R.color.cp_circle_alert);
        mContentColor = getResources().getColor(R.color.cp_circle_content);
        // mUnitColor = getResources().getColor(R.color.cp_circle_unit);

        mCircleBackground = getResources().getColor(R.color.cp_circle_background);
        mCircleGray = getResources().getColor(R.color.cp_circle_gray);
        mCircleGreen = getResources().getColor(R.color.cp_circle_green);

        mIndicatorCenter = getResources().getColor(R.color.cp_indicator_center);
        mIndicatorGray = getResources().getColor(R.color.cp_indicator_gray);
        mIndicatorLight = getResources().getColor(R.color.cp_indicator_light);

        mDividerWidth = (int) getResources().getDimension(R.dimen.cp_divider_width);

        mCenterCircleOut = (int) getResources().getDimension(R.dimen.center_circle_out);
        mCenterCircleMiddle = (int) getResources().getDimension(R.dimen.center_circle_middle);
        mCenterCircleInside = (int) getResources().getDimension(R.dimen.center_circle_inside);
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

        drawOutSideText(canvas);

        drawBackground(canvas);

        drawCircle(canvas);

        drawContent(canvas);

        drawIndicator(canvas);
    }

    /**
     * 画外侧的指示文字
     * 半径：view_radius － mDividerWidth
     *
     * @param canvas 画布
     */
    private void drawOutSideText(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(mNormalGray);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getResources().getDimension(R.dimen.cp_out_indicator_text));
        textPaint.setStyle(Paint.Style.STROKE);

        int radius = getViewRadius() - mDividerWidth; // 文字所在圆环半径

        Path path = new Path();
        path.addCircle(getCenterX(), getCenterY(), radius, Path.Direction.CW);//顺时针绘制（or CCW）

        // canvas.drawPath(path, textPaint); // TODO: 16/6/12

        // 画第一个数字
        String content = String.valueOf(mStartIndicator);
        float hOffset = Utils.getCirclePathLength(radius, CIRCLE_START_ANGLE);
        hOffset -= Utils.getTextWidth(textPaint, content) / 2;
        canvas.drawTextOnPath(content, path, hOffset, 0, textPaint);

        // 画最后一个数字
        content = String.valueOf(mEndIndicator);
        hOffset = Utils.getCirclePathLength(radius, (CIRCLE_START_ANGLE + CIRCLE_SWEEP_ANGLE));
        hOffset -= Utils.getTextWidth(textPaint, content) / 2;
        canvas.drawTextOnPath(content, path, hOffset, 0, textPaint);

        if (null == mDiviverIndicator || mDiviverIndicator.length == 0) {
            return;
        }

        // 画其他的指示数字
        int sizeOfDivider = mDiviverIndicator.length;
        int perAngle = CIRCLE_SWEEP_ANGLE / (sizeOfDivider + 1);
        for (int i = 1; i <= sizeOfDivider; i++) {
            content = String.valueOf(mDiviverIndicator[i - 1]);
            hOffset = Utils.getCirclePathLength(radius, (CIRCLE_START_ANGLE + perAngle * i));
            hOffset -= Utils.getTextWidth(textPaint, content) / 2;
            canvas.drawTextOnPath(content, path, hOffset, 0, textPaint);
        }
    }

    /**
     * 画大的白色背景
     * 半径：view_radius － (3/2) * mDividerWidth
     *
     * @param canvas 画布
     */
    private void drawBackground(Canvas canvas) {
        Paint bgPaint = new Paint();
        bgPaint.setColor(mCircleBackground);

        int radius = getViewRadius() - (mDividerWidth * 3 / 2);

        canvas.drawCircle(getCenterX(), getCenterY(), radius, bgPaint);

        Path path = new Path();
        path.addCircle(getCenterX(), getCenterY(), radius, Path.Direction.CW);

        Paint paint = new Paint();
        paint.setColor(mCircleGray);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawPath(path, paint);
    }

    /**
     * 画中间大的圆环，先画背景，后画绿色进度
     * 半径：view_radius － CIRCLE_GAP ＊ mDividerWidth
     *
     * @param canvas 画布
     */
    private void drawCircle(Canvas canvas) {
        int radius = (int) (getViewRadius() - CIRCLE_GAP * mDividerWidth);

        RectF oval = new RectF(
                getCenterX() - radius,
                getCenterY() - radius,
                getCenterX() + radius,
                getCenterY() + radius);

        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(CIRCLE_STROKE_WIDTH);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);

        circlePaint.setColor(mCircleGray);
        canvas.drawArc(oval, CIRCLE_START_ANGLE, CIRCLE_SWEEP_ANGLE, false, circlePaint);

        circlePaint.setColor(mCircleGreen);
        float angle = CIRCLE_SWEEP_ANGLE * (mProgress - mStartIndicator) / (mEndIndicator - mStartIndicator);
        canvas.drawArc(oval, CIRCLE_START_ANGLE, angle, false, circlePaint);
    }

    /**
     * 画中间的内容，包括：内容，横线，指示
     *
     * @param canvas 画布
     */
    private void drawContent(Canvas canvas) {
        int radius = (int) (getViewRadius() - CIRCLE_GAP * mDividerWidth);

        Paint titlePaint = new Paint();
        titlePaint.setTextSize(getResources().getDimension(R.dimen.cp_title_indicator_text));
        titlePaint.setColor(mTitleColor);
        titlePaint.setStyle(Paint.Style.STROKE);

        int titleX = getCenterX() - Utils.getTextWidth(titlePaint, mTitle) / 2;
        int titleY = getCenterY() - radius * 3 / 5; // 上下3/5的比列
        canvas.drawText(mTitle, titleX, titleY, titlePaint);

        Paint line = new Paint();
        line.setStrokeWidth(3);
        line.setColor(mCircleGray);
        line.setStyle(Paint.Style.STROKE);

        int lineY = 0, startX = 0, startY = 0, stopX = 0, stopY = 0;
        startX = getCenterX() - (int) (radius * Math.sin(Math.PI / 4)) + mDividerWidth;
        stopX = getCenterX() + (int) (radius * Math.sin(Math.PI / 4)) - mDividerWidth;
        lineY = getCenterY() + (int) (radius * Math.sin(Math.PI / 4));
        lineY += mDividerWidth / 2; // 矫正，不矫正会偏下
        startY = lineY;
        stopY = lineY;
        canvas.drawLine(startX, startY, stopX, stopY, line);

        Paint alertPaint = new Paint();
        alertPaint.setTextSize(getResources().getDimension(R.dimen.cp_alert_indicator_text));
        alertPaint.setColor(mAlertColor);
        alertPaint.setStyle(Paint.Style.STROKE);

        int lineMargin = mDividerWidth * 3 / 2; // 距离分割横线的距离

        int alertX = getCenterX() - Utils.getTextWidth(alertPaint, mAlert) / 2;
        int alertY = lineY + lineMargin;
        canvas.drawText(mAlert, alertX, alertY, alertPaint);

        Paint contentPaint = new Paint();
        contentPaint.setTextSize(getResources().getDimension(R.dimen.cp_content_indicator_text));
        contentPaint.setColor(mContentColor);
        contentPaint.setStyle(Paint.Style.STROKE);

        Paint unitPaint = new Paint();
        unitPaint.setTextSize(getResources().getDimension(R.dimen.cp_unit_indicator_text));
        unitPaint.setColor(mContentColor);
        unitPaint.setStyle(Paint.Style.STROKE);

        int contentWidth = Utils.getTextWidth(contentPaint, mContent);
        if (!TextUtils.isEmpty(mUnit)) {
            contentWidth += Utils.getTextWidth(unitPaint, mUnit);
        }

        lineMargin = mDividerWidth / 2; // 距离分割横线的距离
        int contentY = lineY - lineMargin;
        int contentX = getCenterX() - contentWidth / 2;
        canvas.drawText(mContent, contentX, contentY, contentPaint);

        int unitY = lineY - lineMargin;
        int unitX = contentX + Utils.getTextWidth(contentPaint, mContent);
        canvas.drawText(mUnit, unitX, unitY, unitPaint);
    }

    /**
     * 画中间的指针
     *
     * @param canvas 画布
     */
    private void drawIndicator(Canvas canvas) {
        Paint centerPaint = new Paint();
        centerPaint.setColor(mIndicatorCenter);
        canvas.drawCircle(getCenterX(), getCenterY(), mCenterCircleOut, centerPaint);// 最大圆环

        RectF oval = new RectF(
                getCenterX() - mCenterCircleMiddle,
                getCenterY() - mCenterCircleMiddle,
                getCenterX() + mCenterCircleMiddle,
                getCenterY() + mCenterCircleMiddle);

        float angle = CIRCLE_SWEEP_ANGLE * (mIndicator - mStartIndicator) / (mEndIndicator - mStartIndicator);

        // 画两个1/4圆
        Paint lightPaint = new Paint();
        lightPaint.setColor(mIndicatorLight); // 颜色浅
        canvas.drawArc(oval, CIRCLE_START_ANGLE + angle + 90, 90, true, lightPaint);
        Paint grayPaint = new Paint();
        grayPaint.setColor(mIndicatorGray); // 颜色深
        canvas.drawArc(oval, CIRCLE_START_ANGLE + angle + 180, 90, true, grayPaint);

        // 画剪头
        drawArrow(canvas, angle, mCenterCircleMiddle, grayPaint, false);
        drawArrow(canvas, angle, mCenterCircleMiddle, lightPaint, true);

        // 话中间的小圆
        RectF center = new RectF(
                getCenterX() - mCenterCircleInside,
                getCenterY() - mCenterCircleInside,
                getCenterX() + mCenterCircleInside,
                getCenterY() + mCenterCircleInside);
        canvas.drawArc(center, 0, 360, true, centerPaint);
    }

    private void drawArrow(Canvas canvas, float angle, int radius, Paint paint, boolean isLight) {
        double oneAngle = (CIRCLE_START_ANGLE + angle - 90) * Math.PI / 180;
        if (isLight) {
            oneAngle = (CIRCLE_START_ANGLE + angle + 90) * Math.PI / 180;
        }
        int oneX = (int) (radius * Math.cos(oneAngle));
        oneX = getCenterX() + oneX;
        int oneY = (int) (radius * Math.sin(oneAngle));
        oneY = getCenterY() + oneY;

        double twoAngle = (CIRCLE_START_ANGLE + angle) * Math.PI / 180;
        int middleRadius = (int) (getViewRadius() - CIRCLE_GAP * mDividerWidth);
        int twoX = (int) (middleRadius * Math.cos(twoAngle));
        twoX = getCenterX() + twoX;
        int twoY = (int) (middleRadius * Math.sin(twoAngle));
        twoY = getCenterY() + twoY;

        Path path = new Path();
        path.moveTo(getCenterX(), getCenterY());
        path.lineTo(oneX, oneY);
        path.lineTo(twoX, twoY);
        path.close();
        canvas.drawPath(path, paint);
    }

    private int getCenterX() {
        return mWidth / 2;
    }

    private int getCenterY() {
        return mHeight / 2;
    }

    /**
     * 获取View的半径
     *
     * @return view的半径
     */
    private int getViewRadius() {
        return getCenterY() > getCenterX() ? getCenterX() : getCenterY();
    }

    private int mStartIndicator = 10, mEndIndicator = 60, mProgress = 50;
    private float mIndicator = 20;
    private int[] mDiviverIndicator = new int[]{20, 30, 40, 50};

    public void setIndicatorValue(int start, int end, int progress, int indicator, int... divider) {
        if (start >= end) {
            return;
        }
        if (mProgress > end || mProgress < start) {
            return;
        }

        mStartIndicator = start;
        mEndIndicator = end;
        mIndicator = indicator;
        mDiviverIndicator = divider;

        for (int i = 0; i < divider.length; i++) {
            if (divider[i] > end || divider[i] < start) {
                divider[i] = start;
            }
        }
        // TODO: 16/6/8 refresh
    }

    private String mTitle = "身体年龄", mContent = "23", mUnit = "岁", mAlert = "显年轻 4 岁";

    public void setContent(String title, String content, String unit, String alert) {
        setContent(title, content, unit, alert, mContentColor);
    }

    public void setContent(String title, String content, String unit, String alert, int color) {
        mTitle = title;
        mContent = content;
        mUnit = unit;
        mAlert = alert;
        mContentColor = color;

        postInvalidate();
    }


    public void setProgress(float indicator) {
        Log.d("indicator", String.valueOf(indicator));
        if (indicator < mStartIndicator) {
            return;
        }
        mIndicator = indicator > mEndIndicator ? mEndIndicator : indicator;
        postInvalidate();
    }

    public void animateProgress(float progress) {
        Interpolator interpolator = new AccelerateDecelerateInterpolator();
        ObjectAnimator animation = ObjectAnimator.ofFloat(this, "progress", progress);
        animation.setDuration(2000);
        animation.setInterpolator(interpolator);
        animation.start();
    }
}
