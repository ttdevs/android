/*
 * Created by ttdevs at 16-6-16 下午4:42.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;

public class CircleProgress extends BaseCircle {
    private static final int CIRCLE_STROKE_WIDTH = 56; // 指示圆环的宽度

    private int mCircleWhite;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mCircleWhite = getResources().getColor(R.color.circle_white);
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
        textPaint.setTextSize(getResources().getDimension(R.dimen.out_indicator_size));
        textPaint.setStyle(Paint.Style.STROKE);

        int radius = getViewRadius() - mDividerWidth; // 文字所在圆环半径

        Path path = new Path();
        path.addCircle(getCenterX(), getCenterY(), radius, Path.Direction.CW);//顺时针绘制（or CCW）

        // 画第一个数字
        String content = formatNumber(mStartIndicator);
        float hOffset = ViewUtils.getCirclePathLength(radius, CIRCLE_START_ANGLE);
        hOffset -= ViewUtils.getTextWidth(textPaint, content) / 2;
        canvas.drawTextOnPath(content, path, hOffset, 0, textPaint);

        // 画最后一个数字
        content = formatNumber(mEndIndicator);
        hOffset = ViewUtils.getCirclePathLength(radius, (CIRCLE_START_ANGLE + CIRCLE_SWEEP_ANGLE));
        hOffset -= ViewUtils.getTextWidth(textPaint, content) / 2;
        canvas.drawTextOnPath(content, path, hOffset, 0, textPaint);

        if (null == mDividerIndicator || mDividerIndicator.length == 0) {
            return;
        }

        // 画其他的指示数字
        int sizeOfDivider = mDividerIndicator.length;
        int perAngle = CIRCLE_SWEEP_ANGLE / (sizeOfDivider + 1);
        for (int i = 1; i <= sizeOfDivider; i++) {
            content = formatNumber(mDividerIndicator[i - 1]);
            hOffset = ViewUtils.getCirclePathLength(radius, (CIRCLE_START_ANGLE + perAngle * i));
            hOffset -= ViewUtils.getTextWidth(textPaint, content) / 2;
            canvas.drawTextOnPath(content, path, hOffset, 0, textPaint);
        }
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

        if (mEndIndicator > mStartIndicator && mProgress >= mStartIndicator) {
            circlePaint.setColor(mCircleGreen);
            float angle = CIRCLE_SWEEP_ANGLE * (mProgress - mStartIndicator) / (mEndIndicator - mStartIndicator);
            canvas.drawArc(oval, CIRCLE_START_ANGLE, angle, false, circlePaint);

            if (!TextUtils.isEmpty(mProgressAlert)) {
                drawAlert(canvas, radius, angle);
            }
        }
    }

    private void drawAlert(Canvas canvas, int radius, float angle) {
        Paint textPaint = new Paint();
        textPaint.setColor(mCircleWhite);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getResources().getDimension(R.dimen.out_indicator_size));

        Paint.FontMetricsInt fmi = textPaint.getFontMetricsInt();
        float textRadius = radius - Math.abs(fmi.bottom + fmi.top) / 2;

        Path path = new Path();
        path.addCircle(getCenterX(), getCenterY(), textRadius, Path.Direction.CW);//顺时针绘制（or CCW）

        float circlePathLength = ViewUtils.getCirclePathLength(textRadius,
                (CIRCLE_START_ANGLE + angle / 2));
        circlePathLength -= ViewUtils.getTextWidth(textPaint, mProgressAlert) / 2;
        canvas.drawTextOnPath(String.valueOf(mProgressAlert), path, circlePathLength, 0, textPaint);
    }

    private float[] mDividerIndicator;
    private float mProgress;
    private String mProgressAlert;

    /**
     * 设置指示数字
     *
     * @param start     开始
     * @param end       结束
     * @param progress  进度
     * @param indicator 指示进度
     * @param divider   分割指示的数字
     */
    public void setIndicatorValue(float start, float end, float progress, float indicator, float... divider) {
        setIndicatorValue(start, end, progress, "", indicator, divider);
    }

    public void setIndicatorValue(float start, float end, float progress, String progressAlert, float indicator, float... divider) {
        if (start >= end) {
            return;
        }
        if (progress > end || progress < start) {
            return;
        }

        mStartIndicator = start;
        mEndIndicator = end;
        mProgress = progress;
        mProgressAlert = progressAlert;
        mDividerIndicator = divider;

        for (int i = 0; i < divider.length; i++) {
            if (divider[i] > end || divider[i] < start) {
                divider[i] = start;
            }
        }

        // mIndicator = indicator;
        animateIndicator(indicator);
    }
}
