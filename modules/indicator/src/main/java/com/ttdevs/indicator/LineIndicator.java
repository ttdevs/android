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
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.NumberFormat;


/**
 * Alert include: leftAlert, rightAlert, leftContent, rightContent<br/>
 * Indicator include: indicatorBg, indicatorProgress, indicatorText
 * <pre>
 *     ----------------------------------------
 *     |leftAlert                   rightAlert|
 *     |===============indicator==============|
 *     |leftContent               rightContent|
 *     ----------------------------------------
 * </pre>
 */
public class LineIndicator extends View {
    /**
     * 行间距，计算View高度时使用
     */
    private static final int LINE_MARGIN = 24;
    /**
     * Progress的高度
     */
    private static final float STROKE_WIDTH = 28;

    /**
     * Progress Indicator的高度
     */
    private static final float ALERT_STROKE_WIDTH = STROKE_WIDTH * 2.8f;

    /**
     * 最大最小进度
     */
    private float mMinProgress = 0f, mMaxProgress = 1f;

    /**
     * 四个提示文字大小
     */
    private float mAlertSize;
    /**
     * 四个提示文字颜色
     */
    private int mAlertColor;
    /**
     * Progress
     */
    private int mProgressBackground, mProgressColor;
    /**
     * Progress Indicator
     */
    private float mIndicatorTextSize;
    private int mIndicatorTextColor, mIndicatorBackground;

    private Paint mAlertTextPaint;

    /**
     * View的宽高
     */
    private int mWidth = 0, mHeight = 0;

    public LineIndicator(Context context) {
        this(context, null);
    }

    public LineIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mAlertSize = getResources().getDimension(R.dimen.line_alert_size);
        mAlertColor = getResources().getColor(R.color.line_alert_color);

        mProgressBackground = getResources().getColor(R.color.line_progress_background);
        mProgressColor = getResources().getColor(R.color.line_progress_color);

        mIndicatorTextSize = getResources().getDimension(R.dimen.line_indicator_text_size);
        mIndicatorTextColor = getResources().getColor(R.color.line_indicator_text_color);
        mIndicatorBackground = getResources().getColor(R.color.line_indicator_background);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineIndicator, defStyleAttr, 0);
        mAlertSize = typedArray.getDimension(R.styleable.LineIndicator_lineAlertSize, mAlertSize);
        mAlertColor = typedArray.getColor(R.styleable.LineIndicator_lineAlertColor, mAlertColor);

        mProgressBackground = typedArray.getColor(R.styleable.LineIndicator_lineProgressBackground, mProgressBackground);
        mProgressColor = typedArray.getColor(R.styleable.LineIndicator_lineProgressColor, mProgressColor);

        mIndicatorTextSize = typedArray.getDimension(R.styleable.LineIndicator_lineIndicatorTextSize, mIndicatorTextSize);
        mIndicatorTextColor = typedArray.getColor(R.styleable.LineIndicator_lineIndicatorTextColor, mIndicatorTextColor);
        mIndicatorBackground = typedArray.getColor(R.styleable.LineIndicator_lineIndicatorBackground, mIndicatorBackground);
        typedArray.recycle();

        mAlertTextPaint = getAlertPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int textHeight = ViewUtils.getTextHeight(mAlertTextPaint);

        int height = 0;
        if (!TextUtils.isEmpty(mLeftAlert) || !TextUtils.isEmpty(mRightAlert)) {
            height += textHeight;
        }
        height += LINE_MARGIN;
        height += STROKE_WIDTH * 2;
        height += LINE_MARGIN;
        if (!TextUtils.isEmpty(mLeftContent) || !TextUtils.isEmpty(mRightContent)) {
            height += textHeight;
        }

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
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

        drawAlertText(canvas);

        drawProgress(canvas);
    }

    private void drawAlertText(Canvas canvas) {
        // 画左侧文字
        int leftAlertY = ViewUtils.getTextHeight(mAlertTextPaint);
        int leftContentY = mHeight - ViewUtils.getTextBaseLine(mAlertTextPaint);
        canvas.drawText(mLeftAlert, 0, leftAlertY, mAlertTextPaint);
        canvas.drawText(mLeftContent, 0, leftContentY, mAlertTextPaint);

        // 画右侧文字
        int rightAlertX = mWidth - ViewUtils.getTextWidth(mAlertTextPaint, mRightAlert);
        int rightContentX = mWidth - ViewUtils.getTextWidth(mAlertTextPaint, mRightContent);
        int rightAlertY = leftAlertY;
        int rightContentY = leftContentY;
        canvas.drawText(mRightAlert, rightAlertX, rightAlertY, mAlertTextPaint);
        canvas.drawText(mRightContent, rightContentX, rightContentY, mAlertTextPaint);
    }


    /**
     * 画绿色进度和指示信息<br/>
     * 其实 线的总长度是(mWidth - STROKE_WIDTH)
     *
     * @param canvas
     */
    private void drawProgress(Canvas canvas) {
        // 画灰色进度
        Paint bgPaint = getProgressPaint();
        bgPaint.setColor(mProgressBackground);
        // 左右各扣除半个圆的距离
        canvas.drawLine(STROKE_WIDTH / 2,
                mHeight / 2,
                mWidth - STROKE_WIDTH / 2,
                mHeight / 2,
                bgPaint);

        if (mProgress == 0 || mProgress < mMinProgress) {
            return;
        }
        // 画绿色进度
        Paint progressPaint = getProgressPaint();
        progressPaint.setColor(mProgressColor);
        int stopX = (int) ((mWidth - STROKE_WIDTH) * mProgress);
        canvas.drawLine(STROKE_WIDTH / 2,
                mHeight / 2,
                stopX,
                mHeight / 2,
                progressPaint);

        // 文字Paint
        Paint textPain = getIndicatorTextPaint();
        int textWidth = ViewUtils.getTextWidth(textPain, mAlert);
        // 指示文字背景
        Paint indicatorPaint = getProgressPaint();
        indicatorPaint.setColor(mIndicatorBackground);
        indicatorPaint.setStrokeWidth(ALERT_STROKE_WIDTH);

        canvas.drawLine(stopX - textWidth / 2,
                mHeight / 2,
                stopX + textWidth / 2,
                mHeight / 2,
                indicatorPaint);

        // 画指示文字
        Paint.FontMetricsInt fmi = textPain.getFontMetricsInt();
        float alertTextY = mHeight / 2 + Math.abs(fmi.bottom + fmi.top) / 2;
        canvas.drawText(mAlert,
                stopX - textWidth / 2,
                alertTextY,
                textPain);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        // TODO: 16/6/17 屏幕旋转
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        // TODO: 16/6/17 屏幕旋转
    }

    private Paint getAlertPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mAlertColor);
        paint.setTextSize(mAlertSize);
        return paint;
    }

    private Paint getProgressPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }

    private Paint getIndicatorTextPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(mIndicatorTextSize);
        paint.setColor(mIndicatorTextColor);
        return paint;
    }

    private float mProgress = 0.0f;
    private String mLeftAlert = "", mLeftContent = "", mRightAlert = "", mRightContent = "";
    private String mAlert = "";

    public void setContent(String leftAlert, String leftContent,
                           String rightAlert, String rightContent) {
        mLeftAlert = leftAlert;
        mLeftContent = leftContent;
        mRightAlert = rightAlert;
        mRightContent = rightContent;

        requestLayout();
    }

    /**
     * 设置当前进度，如下数值，结果为50%
     *
     * @param start    开始数值，如：60
     * @param end      结束数值，如：50
     * @param progress 当前数值，如：55
     */
    public void setIndicator(float start, float end, float progress) {
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
        mProgress = 0;

        float value = Math.abs((progress - start) / (end - start));

        if (TextUtils.isEmpty(alert)) {
            mAlert = NumberFormat.getPercentInstance().format(value);
        } else {
            mAlert = alert;
        }

        float width = mWidth - STROKE_WIDTH;
        int textWidth = ViewUtils.getTextWidth(getIndicatorTextPaint(), mAlert);
        float offset = textWidth / 2 + ALERT_STROKE_WIDTH / 2;
        mMinProgress = offset / width;
        mMaxProgress = (mWidth - offset) / width;

        animateIndicator(value);
    }

    /**
     * 设置进度
     *
     * @param progress 进度值
     */
    public void setProgress(float progress) {
        if (progress > mMaxProgress) {
            mProgress = mMaxProgress;
        } else if (progress < mMinProgress) {
            mProgress = mMinProgress;
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
