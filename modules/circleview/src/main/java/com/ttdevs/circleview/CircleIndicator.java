/*
 * Created by ttdevs at 16-6-13 下午4:44.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.circleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CircleIndicator extends BaseCircle {
    public class Item {
        public float start;
        public float end;
        public String value;
        public int color;
    }

    private static final int CIRCLE_STROKE_WIDTH = 16; // 指示圆环的宽度

    private int mCircleRed, mCircleYellow, mCircleWhite;

    public CircleIndicator(Context context) {
        this(context, null);
    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mCircleRed = getResources().getColor(R.color.circle_red);
        mCircleYellow = getResources().getColor(R.color.circle_yellow);
        mCircleWhite = getResources().getColor(R.color.circle_white);

        initOther();
    }

    private void initOther() {
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
        item3.end = 35;
        item3.value = "过高";
        item3.color = mNormalGray;
        dividerIndicator.add(item3);

        Item item4 = new Item();
        item4.start = 35;
        item4.end = 60;
        item4.value = "超级高";
        item4.color = mCircleRed;
        dividerIndicator.add(item4);

        setIndicatorValue(dividerIndicator, 19);
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

        // canvas.drawPath(path, mNormalTextPaint); // TODO: 16/6/12

        // 画第一个数字
        String content = formatNumber(mStartIndicator);
        float hOffset = ViewUtils.getCirclePathLength(radius, CIRCLE_START_ANGLE);
        hOffset -= ViewUtils.getTextWidth(textPaint, content) / 2;
        canvas.drawTextOnPath(content, path, hOffset, 0, textPaint);

        // 画其他的指示数字，取Item.end字段
        float perAngle = CIRCLE_SWEEP_ANGLE / (mEndIndicator - mStartIndicator);
        for (Item item : mDividerIndicator) {
            content = formatNumber(item.end);
            hOffset = ViewUtils.getCirclePathLength(radius, (CIRCLE_START_ANGLE + perAngle * (item.end - mStartIndicator)));
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
        Paint textPaint = new Paint();
        textPaint.setColor(mCircleWhite);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getResources().getDimension(R.dimen.out_indicator_size));
        textPaint.setStyle(Paint.Style.STROKE);

        float textHeight = ViewUtils.getTextHeight(textPaint);
        float perAngle = CIRCLE_SWEEP_ANGLE / (mEndIndicator - mStartIndicator);
        // 环的半径
        int radius = (int) (getViewRadius() - CIRCLE_GAP * mDividerWidth);
        // 文字的半径，参考：http://blog.csdn.net/aigestudio/article/details/41447349
        Paint.FontMetricsInt fmi = textPaint.getFontMetricsInt();
        float textRadius = radius - Math.abs(fmi.bottom + fmi.top) / 2;
        RectF oval = new RectF(
                getCenterX() - radius,
                getCenterY() - radius,
                getCenterX() + radius,
                getCenterY() + radius);

        Path path = new Path();
        path.addCircle(getCenterX(), getCenterY(), textRadius, Path.Direction.CW);//顺时针绘制（or CCW）

        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(textHeight + CIRCLE_STROKE_WIDTH);
        circlePaint.setStyle(Paint.Style.STROKE);

        circlePaint.setStrokeCap(Paint.Cap.ROUND);
        drawCircleContent(canvas, mDividerIndicator.get(0), oval, perAngle, textRadius, path, textPaint, circlePaint);
        int lastIndex = mDividerIndicator.size() - 1;
        drawCircleContent(canvas, mDividerIndicator.get(lastIndex), oval, perAngle, textRadius, path, textPaint, circlePaint);

        circlePaint.setStrokeCap(Paint.Cap.BUTT);
        for (int i = 1; i < mDividerIndicator.size() - 1; i++) {
            Item item = mDividerIndicator.get(i);
            drawCircleContent(canvas, item, oval, perAngle, textRadius, path, textPaint, circlePaint);
        }
    }

    private void drawCircleContent(Canvas canvas,
                                   Item item,
                                   RectF oval,
                                   float perAngle,
                                   float textRadius,
                                   Path path,
                                   Paint textPaint,
                                   Paint circlePaint) {

        circlePaint.setColor(item.color);

        float startAngle = CIRCLE_START_ANGLE;
        startAngle += perAngle * (item.start - mStartIndicator);
        float endAngle = perAngle * (item.end - item.start);
        canvas.drawArc(oval, startAngle, endAngle, false, circlePaint);
        float circlePathLength = ViewUtils.getCirclePathLength(textRadius,
                (startAngle + endAngle / 2));
        circlePathLength -= ViewUtils.getTextWidth(textPaint, item.value) / 2;
        canvas.drawTextOnPath(String.valueOf(item.value),
                path, circlePathLength, 0, textPaint);
    }

    private List<Item> mDividerIndicator = new ArrayList<>();

    /**
     * 设置内容
     *
     * @param data      当前要展示的数据
     * @param indicator 指针指示数字
     */
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

        mDividerIndicator.clear();
        mDividerIndicator.addAll(data);

        if (indicator < mStartIndicator || indicator > mEndIndicator) {
            mIndicator = mStartIndicator;
            postInvalidate();
        } else {
            animateIndicator(indicator);
        }
    }
}
