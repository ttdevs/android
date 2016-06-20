/*
 * Created by ttdevs at 16-6-16 下午4:42.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.indicator;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;

public class BaseCircle extends View {
    protected static final int CIRCLE_START_ANGLE = 135; // 起始角度
    protected static final int CIRCLE_SWEEP_ANGLE = 270; // 展示角度
    protected static final float CIRCLE_GAP = 2.6f;

    //为了可以改
    protected int mDividerWidth = 32;

    protected int mNormalGray = Color.parseColor("#FFDFDFDF");
    protected int mTitleColor, mAlertColor, mContentColor, mUnitColor;
    protected int mCircleBackground, mCircleGray, mCircleGreen;
    protected int mIndicatorCenter, mIndicatorGray, mIndicatorLight;
    protected int mCenterCircleOut, mCenterCircleMiddle, mCenterCircleInside;

    protected int mOutIndicatorSize, mTitleSize, mContentSize, mUnitSize, mAlertSize;

    private int mWidth = 0, mHeight = 0;

    public BaseCircle(Context context) {
        this(context, null);
    }

    public BaseCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mCircleBackground = getResources().getColor(R.color.circle_background);
        mCircleGray = getResources().getColor(R.color.circle_gray);
        mCircleGreen = getResources().getColor(R.color.circle_green);

        mDividerWidth = (int) getResources().getDimension(R.dimen.divider_width);
        mOutIndicatorSize = (int) getResources().getDimension(R.dimen.out_indicator_size);
        mTitleSize = (int) getResources().getDimension(R.dimen.title_indicator_size);
        mContentSize = (int) getResources().getDimension(R.dimen.content_indicator_size);
        mUnitSize = (int) getResources().getDimension(R.dimen.unit_indicator_size);
        mAlertSize = (int) getResources().getDimension(R.dimen.alert_indicator_size);

        mCenterCircleOut = (int) getResources().getDimension(R.dimen.center_circle_out);
        mCenterCircleMiddle = (int) getResources().getDimension(R.dimen.center_circle_middle);
        mCenterCircleInside = (int) getResources().getDimension(R.dimen.center_circle_inside);

        mNormalGray = getResources().getColor(R.color.normal_gray);

        mIndicatorCenter = getResources().getColor(R.color.indicator_center);
        mIndicatorGray = getResources().getColor(R.color.indicator_gray);
        mIndicatorLight = getResources().getColor(R.color.indicator_light);

        mTitleColor = getResources().getColor(R.color.circle_title);
        mAlertColor = getResources().getColor(R.color.circle_alert);
        mContentColor = getResources().getColor(R.color.circle_content);
        mUnitColor = getResources().getColor(R.color.circle_unit);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleView);
        mDividerWidth = (int) typedArray.getDimension(R.styleable.CircleView_circle_out_indicator_size, mDividerWidth);
        mNormalGray = typedArray.getColor(R.styleable.CircleView_circle_normal_gray, mNormalGray);

        mOutIndicatorSize = (int) typedArray.getDimension(R.styleable.CircleView_circle_out_indicator_size, mOutIndicatorSize);

        mTitleSize = (int) typedArray.getDimension(R.styleable.CircleView_circle_title_size, mTitleSize);
        mContentSize = (int) typedArray.getDimension(R.styleable.CircleView_circle_content_size, mContentSize);
        mUnitSize = (int) typedArray.getDimension(R.styleable.CircleView_circle_unit_size, mUnitSize);
        mAlertSize = (int) typedArray.getDimension(R.styleable.CircleView_circle_alert_size, mAlertSize);

        mCenterCircleOut = (int) typedArray.getDimension(R.styleable.CircleView_circle_center_out, mCenterCircleOut);
        mCenterCircleMiddle = (int) typedArray.getDimension(R.styleable.CircleView_circle_center_middle, mCenterCircleMiddle);
        mCenterCircleInside = (int) typedArray.getDimension(R.styleable.CircleView_circle_center_inside, mCenterCircleInside);

        mIndicatorCenter = typedArray.getColor(R.styleable.CircleView_circle_indicator_center, mIndicatorCenter);
        mIndicatorGray = typedArray.getColor(R.styleable.CircleView_circle_indicator_gray, mIndicatorGray);
        mIndicatorLight = typedArray.getColor(R.styleable.CircleView_circle_indicator_light, mIndicatorLight);

        mTitleColor = typedArray.getColor(R.styleable.CircleView_circle_title, mTitleColor);
        mAlertColor = typedArray.getColor(R.styleable.CircleView_circle_alert, mAlertColor);
        mContentColor = typedArray.getColor(R.styleable.CircleView_circle_content, mContentColor);
        mUnitColor = typedArray.getColor(R.styleable.CircleView_circle_unit, mUnitColor);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(specSize, specSize); // 参考宽，处理成正方形
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

//        drawOutSideText(canvas);
//
//        drawBackground(canvas);
//
//        drawCircle(canvas);
//
//        drawContent(canvas);
//
//        drawIndicator(canvas);
    }


    /**
     * 画大的白色背景
     * 半径：view_radius － (3/2) * mDividerWidth
     *
     * @param canvas 画布
     */
    protected void drawBackground(Canvas canvas) {
        Paint bgPaint = new Paint();
        bgPaint.setColor(mCircleBackground);

        int radius = getViewRadius() - (mDividerWidth * 3 / 2);

        canvas.drawCircle(getCenterX(), getCenterY(), radius, bgPaint);

        Path path = new Path();
        path.addCircle(getCenterX(), getCenterY(), radius, Path.Direction.CW);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mCircleGray);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawPath(path, paint);
    }

    /**
     * 画中间的内容，包括：内容，横线，指示
     *
     * @param canvas 画布
     */
    protected void drawContent(Canvas canvas) {
        int radius = (int) (getViewRadius() - CIRCLE_GAP * mDividerWidth);

        Paint titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(mTitleSize);
        titlePaint.setColor(mTitleColor);

        int titleX = getCenterX() - ViewUtils.getTextWidth(titlePaint, mTitle) / 2;
        int titleY = getCenterY() - radius * 3 / 5; // 上下3/5的比列
        canvas.drawText(mTitle, titleX, titleY, titlePaint);

        Paint line = new Paint();
        line.setStrokeWidth(3);
        line.setColor(mCircleGray);

        int startX = getCenterX() - (int) (radius * Math.sin(Math.PI / 4)) + mDividerWidth;
        int stopX = getCenterX() + (int) (radius * Math.sin(Math.PI / 4)) - mDividerWidth;
        int lineY = getCenterY() + (int) (radius * Math.sin(Math.PI / 4));
        lineY += mDividerWidth / 2; // 矫正，不矫正会偏下
        canvas.drawLine(startX, lineY, stopX, lineY, line);

        Paint alertPaint = new Paint();
        alertPaint.setAntiAlias(true);
        alertPaint.setTextSize(mAlertSize);
        alertPaint.setColor(mAlertColor);

        int lineMargin = mDividerWidth * 3 / 2; // 距离分割横线的距离

        int alertX = getCenterX() - ViewUtils.getTextWidth(alertPaint, mAlert) / 2;
        int alertY = lineY + lineMargin;
        canvas.drawText(mAlert, alertX, alertY, alertPaint);

        Paint contentPaint = new Paint();
        contentPaint.setAntiAlias(true);
        contentPaint.setTextSize(mContentSize);
        contentPaint.setColor(mContentColor);

        Paint unitPaint = new Paint();
        unitPaint.setAntiAlias(true);
        unitPaint.setTextSize(mUnitSize);
        unitPaint.setColor(mContentColor);

        int contentWidth = ViewUtils.getTextWidth(contentPaint, mContent);
        if (!TextUtils.isEmpty(mUnit)) {
            contentWidth += ViewUtils.getTextWidth(unitPaint, mUnit);
        }

        lineMargin = mDividerWidth / 2; // 距离分割横线的距离
        int contentY = lineY - lineMargin;
        int contentX = getCenterX() - contentWidth / 2;
        canvas.drawText(mContent, contentX, contentY, contentPaint);

        int unitY = lineY - lineMargin;
        int unitX = contentX + ViewUtils.getTextWidth(contentPaint, mContent);
        canvas.drawText(mUnit, unitX, unitY, unitPaint);
    }

    /**
     * 画中间的指针
     *
     * @param canvas 画布
     */
    protected void drawIndicator(Canvas canvas) {
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

    protected int getCenterX() {
        return mWidth / 2;
    }

    protected int getCenterY() {
        return mHeight / 2;
    }

    /**
     * 获取View的半径
     *
     * @return view的半径
     */
    protected int getViewRadius() {
        return getCenterY() > getCenterX() ? getCenterX() : getCenterY();
    }

    protected float mStartIndicator = 10f, mEndIndicator = 60f, mIndicator = 10f;

    protected String mTitle = " ", mContent = " ", mUnit = " ", mAlert = " ";

    /**
     * 设置显示的内容
     *
     * @param title   标题
     * @param content 内容
     * @param unit    单位
     * @param alert   提示文字
     */
    public void setContent(String title, String content, String unit, String alert) {
        mTitle = title;
        mContent = content;
        mUnit = unit;
        mAlert = alert;

        postInvalidate();
    }

    /**
     * 设置内容的颜色值（非resource的id）
     *
     * @param contentColor 内容的颜色值
     * @param unitColor    单位的颜色值
     */
    public void setContentColor(int contentColor, int unitColor) {
        mContentColor = contentColor;
        mUnitColor = unitColor;
    }

    /**
     * 设置进度
     *
     * @param indicator 进度值
     */
    public void setIndicator(float indicator) {
        if (indicator <= mStartIndicator) {
            mIndicator = mStartIndicator;
        } else if (indicator > mEndIndicator) {
            mIndicator = mEndIndicator;
        } else {
            mIndicator = indicator;
        }
        postInvalidate();
    }

    /**
     * 获取进度
     *
     * @return 当前进度值
     */
    public float getIndicator() {
        return mStartIndicator;
    }

    public void animateIndicator(float indicator) {
        Interpolator interpolator = new AnticipateOvershootInterpolator(1.8f);
        ObjectAnimator animation = ObjectAnimator.ofFloat(this, "indicator", indicator);
        animation.setDuration(2000);
        animation.setInterpolator(interpolator);
        animation.start();
    }

    /**
     * 格式化float不显示.0：1.2 >> 1.2, 1.0 >> 1
     *
     * @param value 要处理的数字
     * @return
     */
    public static String formatNumber(float value) {
        int temp = (int) value;
        if (value > temp) {
            return String.valueOf(value);
        }
        return String.valueOf(temp);
    }
}
