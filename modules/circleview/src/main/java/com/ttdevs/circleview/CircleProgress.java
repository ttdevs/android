/*
 * Created by ttdevs at 16-6-8 上午9:37.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.circleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.ttdevs.circleview.utils.Utils;

public class CircleProgress extends View {
    private static final int CIRCLE_START_ANGLE = 135; // 起始角度
    private static final int CIRCLE_SWEEP_ANGLE = 270; // 展示角度
    private static final int CIRCLE_STROKE_WIDTH = 56; // 指示圆环的宽度
    private static final float CIRCLE_GAP = 2.6f;

    private int mOutIndicatorTextColor = Color.parseColor("#FFDFDFDF");
    private int mCircleBackground;
    private int mCircleGray;
    private int mCircleGreen;
    private int mIndicatorCenter, mIndicatorGray, mIndicatorLight;

    private Paint mNormalTextPaint = new Paint();
    private int mTextHeight;

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

        initColor();
        initOther();
    }

    private void initColor() {
        mOutIndicatorTextColor = getResources().getColor(R.color.cp_normal_gray);
        mCircleBackground = getResources().getColor(R.color.cp_circle_background);
        mCircleGray = getResources().getColor(R.color.cp_circle_gray);
        mCircleGreen = getResources().getColor(R.color.cp_circle_green);
        mIndicatorCenter = getResources().getColor(R.color.cp_indicator_center);
        mIndicatorGray = getResources().getColor(R.color.cp_indicator_gray);
        mIndicatorLight = getResources().getColor(R.color.cp_indicator_light);
    }

    private void initOther() {
        mNormalTextPaint.setColor(mOutIndicatorTextColor);
        mNormalTextPaint.setAntiAlias(true);
        mNormalTextPaint.setTextSize(getResources().getDimension(R.dimen.cp_out_indicator_text));
        mNormalTextPaint.setStyle(Paint.Style.STROKE);

        mTextHeight = Utils.getTextHeight(mNormalTextPaint);
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
     * 半径：view_radius － mTextHeight
     *
     * @param canvas 画布
     */
    private void drawOutSideText(Canvas canvas) {
        int radius = getViewRadius() - mTextHeight; // 文字所在圆环半径

        Path path = new Path();
        path.addCircle(getCenterX(), getCenterY(), radius, Path.Direction.CW);//顺时针绘制（or CCW）

        // canvas.drawPath(path, mNormalTextPaint); // TODO: 16/6/12

        // 画第一个数字
        canvas.drawTextOnPath(String.valueOf(mStartIndicator),
                path, Utils.getCirclePathLength(radius, CIRCLE_START_ANGLE),
                0, mNormalTextPaint);
        // 画最后一个数字
        canvas.drawTextOnPath(String.valueOf(mEndIndicator),
                path, Utils.getCirclePathLength(radius, (CIRCLE_START_ANGLE + CIRCLE_SWEEP_ANGLE)),
                0, mNormalTextPaint);

        if (null == mDiviverIndicator || mDiviverIndicator.length == 0) {
            return;
        }

        // 画其他的指示数字
        int sizeOfDivider = mDiviverIndicator.length;
        int perAngle = CIRCLE_SWEEP_ANGLE / (sizeOfDivider + 1);
        for (int i = 1; i <= sizeOfDivider; i++) {
            canvas.drawTextOnPath(String.valueOf(mDiviverIndicator[i - 1]), path,
                    Utils.getCirclePathLength(radius, (CIRCLE_START_ANGLE + perAngle * i)),
                    0, mNormalTextPaint);
        }
    }

    /**
     * 画大的白色背景
     * 半径：view_radius － (3/2) * mTextHeight
     *
     * @param canvas 画布
     */
    private void drawBackground(Canvas canvas) {
        Paint bgPaint = new Paint();
        bgPaint.setColor(mCircleBackground);

        int radius = getViewRadius() - (mTextHeight * 3 / 2);

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
     * 画中间的圆环，先画背景，后画绿色进度
     * 半径：view_radius － CIRCLE_GAP ＊ mTextHeight
     *
     * @param canvas 画布
     */
    private void drawCircle(Canvas canvas) {
        int radius = (int) (getViewRadius() - CIRCLE_GAP * mTextHeight);

        RectF oval = new RectF(
                CIRCLE_GAP * mTextHeight,
                CIRCLE_GAP * mTextHeight,
                CIRCLE_GAP * mTextHeight + 2 * radius,
                CIRCLE_GAP * mTextHeight + 2 * radius);

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
        Paint line = new Paint();
        line.setStrokeWidth(2);
        line.setColor(mCircleGray);
        line.setStyle(Paint.Style.STROKE);

        int radius = (int) (getViewRadius() - CIRCLE_GAP * mTextHeight);

        Paint titlePaint = new Paint();
        titlePaint.setTextSize(getResources().getDimension(R.dimen.cp_title_indicator_text));
        titlePaint.setColor(mCircleGray);
        titlePaint.setStyle(Paint.Style.STROKE);

        int titleX = getViewRadius() - Utils.getTextWidth(titlePaint, mTitle) / 2;
        int titleY = radius * 2 / 3; // 2/3高度
        canvas.drawText(mTitle, titleX, titleY, titlePaint);

        int lineY = 0, startX = 0, startY = 0, stopX = 0, stopY = 0;
        startX = getViewRadius() - (int) (radius * Math.sin(Math.PI / 4)) + mTextHeight;
        stopX = getViewRadius() + (int) (radius * Math.sin(Math.PI / 4)) - mTextHeight;
        lineY = getViewRadius() + (int) (radius * Math.sin(Math.PI / 4));
        lineY += mTextHeight / 2; // 矫正，不矫正会偏下
        startY = lineY;
        stopY = lineY;
        canvas.drawLine(startX, startY, stopX, stopY, line);

        Paint alertPaint = new Paint();
        alertPaint.setTextSize(getResources().getDimension(R.dimen.cp_alert_indicator_text));
        alertPaint.setColor(mCircleGray);
        alertPaint.setStyle(Paint.Style.STROKE);

        int lineMargin = mTextHeight * 3 / 2; // 距离分割横线的距离

        int alertX = getViewRadius() - Utils.getTextWidth(titlePaint, mAlert) / 2;
        int alertY = lineY + lineMargin;
        canvas.drawText(mAlert, alertX, alertY, alertPaint);

        Paint contentPaint = new Paint();
        contentPaint.setTextSize(getResources().getDimension(R.dimen.cp_content_indicator_text));
        contentPaint.setColor(mCircleGreen);
        contentPaint.setStyle(Paint.Style.STROKE);

        Paint unitPaint = new Paint();
        unitPaint.setTextSize(getResources().getDimension(R.dimen.cp_unit_indicator_text));
        unitPaint.setColor(mCircleGreen);
        unitPaint.setStyle(Paint.Style.STROKE);

        int contentWidth = Utils.getTextWidth(contentPaint, mContent);
        if (!TextUtils.isEmpty(mUnit)) {
            contentWidth += Utils.getTextWidth(unitPaint, mUnit);
        }

        lineMargin = mTextHeight / 2; // 距离分割横线的距离
        int contentY = lineY - lineMargin;
        int contentX = getViewRadius() - contentWidth / 2;
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
        int out = 86, middle = 48, inside = 24; // 半径

        Paint centerPaint = new Paint();
        centerPaint.setColor(mIndicatorCenter);
        canvas.drawCircle(getCenterX(), getCenterY(), out, centerPaint);// 最大圆环

        RectF oval = new RectF(
                getViewRadius() - middle,
                getViewRadius() - middle,
                getViewRadius() + middle,
                getViewRadius() + middle);

        float angle = CIRCLE_SWEEP_ANGLE * (mIndicator - mStartIndicator) / (mEndIndicator - mStartIndicator);

        // 画两个1/4圆
        Paint lightPaint = new Paint();
        lightPaint.setColor(mIndicatorLight); // 颜色浅
        canvas.drawArc(oval, CIRCLE_START_ANGLE + angle + 90, 90, true, lightPaint);
        Paint grayPaint = new Paint();
        grayPaint.setColor(mIndicatorGray); // 颜色深
        canvas.drawArc(oval, CIRCLE_START_ANGLE + angle + 180, 90, true, grayPaint);

        // 画剪头
        drawArrow(canvas, angle, middle, grayPaint, false);
        drawArrow(canvas, angle, middle, lightPaint, true);

        // 话中间的小圆
        RectF center = new RectF(
                getViewRadius() - inside,
                getViewRadius() - inside,
                getViewRadius() + inside,
                getViewRadius() + inside);
        canvas.drawArc(center, 0, 360, true, centerPaint);
    }

    private void drawArrow(Canvas canvas, float angle, int radius, Paint paint, boolean isLight) {
        double oneAngle = (CIRCLE_START_ANGLE + angle - 90) * Math.PI / 180;
        if (isLight) {
            oneAngle = (CIRCLE_START_ANGLE + angle + 90) * Math.PI / 180;
        }
        int oneX = (int) (radius * Math.cos(oneAngle));
        oneX = getViewRadius() + oneX;
        int oneY = (int) (radius * Math.sin(oneAngle));
        oneY = getViewRadius() + oneY;

        double twoAngle = (CIRCLE_START_ANGLE + angle) * Math.PI / 180;
        int middleRadius = (int) (getViewRadius() - CIRCLE_GAP * mTextHeight);
        int twoX = (int) (middleRadius * Math.cos(twoAngle));
        twoX = getViewRadius() + twoX;
        int twoY = (int) (middleRadius * Math.sin(twoAngle));
        twoY = getViewRadius() + twoY;

        Path path = new Path();
        path.moveTo(getViewRadius(), getViewRadius());
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

    private int mStartIndicator = 10, mEndIndicator = 60, mProgress = 49, mIndicator = 25;
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

    private String mTitle = "BMI", mContent = "23", mUnit = "岁", mAlert = "显年轻4岁";

    public void setContent(String title, String content, String unit, String alert) {
        mTitle = title;
        mContent = content;
        mUnit = unit;
        mAlert = alert;

        // TODO: 16/6/8 refresh
    }
}
