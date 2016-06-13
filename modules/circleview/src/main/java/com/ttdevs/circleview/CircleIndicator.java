/*
 * Created by ttdevs at 16-6-13 上午10:44.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CircleIndicator extends View {
    public class Item {
        public float start;
        public float end;
        public String value;
        public int color;
    }

    private static final int CIRCLE_START_ANGLE = 135; // 起始角度
    private static final int CIRCLE_SWEEP_ANGLE = 270; // 展示角度
    private static final int CIRCLE_STROKE_WIDTH = 16; // 指示圆环的宽度
    private static final float CIRCLE_GAP = 2.6f;

    private int mOutIndicatorTextColor = Color.parseColor("#FFDFDFDF");
    private int mTitleColor, mAlertColor, mContentColor, mUnitColor;
    private int mCircleBackground, mCircleGray, mCircleGreen, mCircleRed, mCircleYellow, mCircleWhite;
    private int mIndicatorCenter, mIndicatorGray, mIndicatorLight;

    private Paint mNormalTextPaint = new Paint();
    private int mTextHeight;

    private int mWidth = 0;
    private int mHeight = 0;

    public CircleIndicator(Context context) {
        this(context, null);
    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mTitleColor = getResources().getColor(R.color.cp_circle_title);
        mAlertColor = getResources().getColor(R.color.cp_circle_alert);
        mContentColor = getResources().getColor(R.color.cp_circle_content);
        mUnitColor = getResources().getColor(R.color.cp_circle_unit);
        mCircleBackground = getResources().getColor(R.color.cp_circle_background);
        mCircleGray = getResources().getColor(R.color.cp_circle_gray);
        mCircleGreen = getResources().getColor(R.color.cp_circle_green);
        mCircleRed = getResources().getColor(R.color.cp_circle_red);
        mCircleYellow = getResources().getColor(R.color.cp_circle_yellow);
        mCircleWhite = getResources().getColor(R.color.cp_circle_white);
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

        List<Item> dividerIndicator = new ArrayList<>();
        Item item1 = new Item();
        item1.start = 5;
        item1.end = 13;
        item1.value = "过低";
        item1.color = mCircleYellow;
        dividerIndicator.add(item1);

        Item item2 = new Item();
        item2.start = 13;
        item2.end = 20;
        item2.value = "正常";
        item2.color = mCircleGreen;
        dividerIndicator.add(item2);

        Item item3 = new Item();
        item3.start = 20;
        item3.end = 60;
        item3.value = "过高";
        item3.color = mCircleRed;
        dividerIndicator.add(item3);

        setIndicatorValue(dividerIndicator, 13);
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

        drawOther(canvas);
    }

    private void drawOther(Canvas canvas) {
        canvas.drawLine(0, 0, mWidth, mHeight, mNormalTextPaint);
        canvas.drawLine(mWidth, 0, 0, mHeight, mNormalTextPaint);
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

        // 画其他的指示数字，取Item.end字段
        float perAngle = CIRCLE_SWEEP_ANGLE / (mEndIndicator - mStartIndicator);
        for (Item item : mDividerIndicator) {
            canvas.drawTextOnPath(String.valueOf(item.end), path,
                    Utils.getCirclePathLength(radius,
                            (CIRCLE_START_ANGLE + perAngle * (item.end - mStartIndicator))),
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
     * 画中间大的圆环，先画背景，后画绿色进度
     * 半径：view_radius － CIRCLE_GAP ＊ mTextHeight
     *
     * @param canvas 画布
     */
    private void drawCircle(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(mCircleWhite);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getResources().getDimension(R.dimen.cp_out_indicator_text));
        textPaint.setStyle(Paint.Style.STROKE);

        int radius = (int) (getViewRadius() - CIRCLE_GAP * mTextHeight);
        float textHeight = Utils.getTextHeight(textPaint);

        RectF oval = new RectF(
                getCenterX() - radius,
                getCenterY() - radius,
                getCenterX() + radius,
                getCenterY() + radius);

        canvas.drawOval(oval, mNormalTextPaint);  // TODO: 画一个圆 16/6/13

        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(textHeight + CIRCLE_STROKE_WIDTH);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeCap(Paint.Cap.BUTT);

        float textRadius = radius - textHeight / 2;

        Path path = new Path();
        path.addCircle(getCenterX(), getCenterY(), textRadius, Path.Direction.CW);//顺时针绘制（or CCW）

        canvas.drawPath(path, mNormalTextPaint); // TODO: 画文字所在圆 16/6/12

        float perAngle = CIRCLE_SWEEP_ANGLE / (mEndIndicator - mStartIndicator);

        for (Item item : mDividerIndicator) {
            circlePaint.setColor(item.color);

            float startAngle = CIRCLE_START_ANGLE;
            startAngle += perAngle * (item.start - mStartIndicator);
            float endAngle = perAngle * (item.end - item.start);
            canvas.drawArc(oval, startAngle, endAngle, false, circlePaint);

            float circlePathLength = Utils.getCirclePathLength(textRadius,
                    (startAngle + endAngle / 2));
            circlePathLength -= Utils.getTextWidth(textPaint, item.value) / 2;
            canvas.drawTextOnPath(String.valueOf(item.value),
                    path, circlePathLength, 0, textPaint);
        }
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
        titlePaint.setColor(mTitleColor);
        titlePaint.setStyle(Paint.Style.STROKE);

        int titleX = getCenterX() - Utils.getTextWidth(titlePaint, mTitle) / 2;
        int titleY = getCenterY() - radius * 3 / 5;
        canvas.drawText(mTitle, titleX, titleY, titlePaint);

        int lineY = 0, startX = 0, startY = 0, stopX = 0, stopY = 0;
        startX = getCenterX() - (int) (radius * Math.sin(Math.PI / 4)) + mTextHeight;
        stopX = getCenterX() + (int) (radius * Math.sin(Math.PI / 4)) - mTextHeight;
        lineY = getCenterY() + (int) (radius * Math.sin(Math.PI / 4));
        lineY += mTextHeight / 2; // 矫正，不矫正会偏下
        startY = lineY;
        stopY = lineY;
        canvas.drawLine(startX, startY, stopX, stopY, line);

        Paint alertPaint = new Paint();
        alertPaint.setTextSize(getResources().getDimension(R.dimen.cp_alert_indicator_text));
        alertPaint.setColor(mAlertColor);
        alertPaint.setStyle(Paint.Style.STROKE);

        int lineMargin = mTextHeight * 3 / 2; // 距离分割横线的距离

        int alertX = getCenterX() - Utils.getTextWidth(alertPaint, mAlert) / 2;
        int alertY = lineY + lineMargin;
        canvas.drawText(mAlert, alertX, alertY, alertPaint);

        Paint contentPaint = new Paint();
        contentPaint.setTextSize(getResources().getDimension(R.dimen.cp_content_indicator_text));
        contentPaint.setColor(mContentColor);
        contentPaint.setStyle(Paint.Style.STROKE);

        Paint unitPaint = new Paint();
        unitPaint.setTextSize(getResources().getDimension(R.dimen.cp_unit_indicator_text));
        unitPaint.setColor(mUnitColor);
        unitPaint.setStyle(Paint.Style.STROKE);

        int contentWidth = Utils.getTextWidth(contentPaint, mContent);
        if (!TextUtils.isEmpty(mUnit)) {
            contentWidth += Utils.getTextWidth(unitPaint, mUnit);
        }

        lineMargin = mTextHeight / 2; // 距离分割横线的距离
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
        int out = 86, middle = 48, inside = 24; // 半径

        Paint centerPaint = new Paint();
        centerPaint.setColor(mIndicatorCenter);
        canvas.drawCircle(getCenterX(), getCenterY(), out, centerPaint);// 最大圆环

        RectF oval = new RectF(
                getCenterX() - middle,
                getCenterY() - middle,
                getCenterX() + middle,
                getCenterY() + middle);

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
                getCenterX() - inside,
                getCenterY() - inside,
                getCenterX() + inside,
                getCenterY() + inside);
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
        int middleRadius = (int) (getViewRadius() - CIRCLE_GAP * mTextHeight);
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

    private float mStartIndicator = 0, mEndIndicator = 0, mIndicator = 0;
    private List<Item> mDividerIndicator = new ArrayList<>();

    public void setIndicatorValue(List<Item> data, float indicator) {
        if (null == data || data.size() == 0) {
            return;
        }

        Collections.sort(data, new Comparator<Item>() {

            @Override
            public int compare(Item lhs, Item rhs) {
                return lhs.start - rhs.start > 0 ? 1 : -1; // TODO: 16/6/13
            }
        });

        mStartIndicator = data.get(0).start;
        mEndIndicator = data.get(data.size() - 1).end;

        if (indicator < mStartIndicator || indicator > mEndIndicator) {
            mIndicator = mStartIndicator;
        } else {
            mIndicator = indicator;
        }

        mDividerIndicator.clear();
        mDividerIndicator.addAll(data);

        postInvalidate();
    }

    private String mTitle = "体脂率", mContent = "23", mUnit = " ％", mAlert = "体脂过高";

    public void setContent(String title, String content, String unit, String alert) {
        mTitle = title;
        mContent = content;
        mUnit = unit;
        mAlert = alert;

        postInvalidate();
    }


//    public void setProgress(float indicator) {
//        Log.d("indicator", String.valueOf(indicator));
//        if (indicator < mStartIndicator) {
//            return;
//        }
//        mIndicator = indicator > mEndIndicator ? mEndIndicator : indicator;
//        postInvalidate();
//    }
//
//    public void animateProgress(float progress) {
//        animateProgress(progress, new AccelerateDecelerateInterpolator());
//    }
//
//    public void animateProgress(float progress, Interpolator interpolator) {
//        ObjectAnimator animation = ObjectAnimator.ofFloat(this, "progress", progress);
//        animation.setDuration(2000);
//        animation.setInterpolator(interpolator);
//        animation.start();
//    }
}
