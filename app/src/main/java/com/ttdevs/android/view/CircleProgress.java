/*
 * Created by ttdevs at 16-9-20 下午5:34.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgress extends View {
    private static final int CIRCLE_START_ANGLE = -90; // 指示圆环开始角度
    private static final int CIRCLE_STROKE_WIDTH = 16; // 指示圆环的宽度
    private final int CIRCLE_STROKE_COLOR = Color.parseColor("#FFFFB182");
    private final int CIRCLE_STROKE_COLOR_END = Color.parseColor("#FFFF7C4D");

    private int mSize = 0;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(specSize, specSize); // TODO: 16/9/20 参考宽，处理成正方形
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mSize = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF oval = new RectF(
                CIRCLE_STROKE_WIDTH,
                CIRCLE_STROKE_WIDTH,
                mSize - CIRCLE_STROKE_WIDTH,
                mSize - CIRCLE_STROKE_WIDTH);

        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(CIRCLE_STROKE_WIDTH);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);
        Matrix matrix = new Matrix();
        matrix.preRotate(CIRCLE_START_ANGLE, mSize / 2, mSize / 2);
        SweepGradient gradient = new SweepGradient(mSize / 2, mSize / 2,
                CIRCLE_STROKE_COLOR, CIRCLE_STROKE_COLOR_END);
        gradient.setLocalMatrix(matrix);
        circlePaint.setShader(gradient);
        canvas.drawArc(oval, CIRCLE_START_ANGLE, mProgress, false, circlePaint);

        Paint fullPaint = new Paint();
        fullPaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        fullPaint.setStrokeCap(Paint.Cap.ROUND);
        fullPaint.setColor(Color.TRANSPARENT);

        if (mProgress == 360) {
            fullPaint.setColor(CIRCLE_STROKE_COLOR_END);
        } else if (mProgress > 1) {
            fullPaint.setColor(CIRCLE_STROKE_COLOR);
        }
        canvas.drawCircle(mSize / 2, CIRCLE_STROKE_WIDTH, CIRCLE_STROKE_WIDTH / 2, fullPaint);

    }

    private int mProgress = 0;

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(float progress) {
        if (progress < 0) {
            progress = 0f;
        }
        if (progress > 1) {
            progress = 1.0f;
        }
        mProgress = (int) (progress * 360);
        invalidate();
    }

    public int getProgress(){
        return mProgress;
    }
}