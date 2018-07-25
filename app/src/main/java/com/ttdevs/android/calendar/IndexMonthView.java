package com.ttdevs.android.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

/**
 * Comma日历控件
 *
 * @author ttdevs
 * @date 2017/11/15
 */

public class IndexMonthView extends MonthView {
    private int mPadding, mRound, mRadius;

    public IndexMonthView(Context context) {
        super(context);

        mPadding = dipToPx(getContext(), 4);
        mRound = dipToPx(getContext(), 4);
        mRadius = dipToPx(getContext(), 4);
    }


    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        mSelectedPaint.setShadowLayer(16, 8, 8, 0xFF008DFF);

        RectF oval = new RectF(x + mPadding, y + mPadding, x + mItemWidth - mPadding, y + mItemHeight - mPadding);
        canvas.drawRoundRect(oval, mRound, mRound, mSelectedPaint);

        return true;
    }

    /**
     * onDrawSelected
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {

    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = y - mItemHeight / 6;

        // draw 当前日期的指示点，跟随data(lunar)的颜色
        if (calendar.isCurrentDay()) {
            canvas.drawCircle(x + mItemWidth - 3 * mRadius, y + 3 * mRadius, mRadius, isSelected ? mSelectedLunarTextPaint : mCurMonthLunarTextPaint);
        }

        // draw data,借用lunar
        canvas.drawText(calendar.getScheme(),
                cx,
                mTextBaseLine + y + mItemHeight / 10,
                calendar.isCurrentMonth() ? (isSelected ? mSelectedLunarTextPaint : mCurMonthLunarTextPaint) : mOtherMonthLunarTextPaint);

        // draw date
        canvas.drawText(String.format("%02d", calendar.getDay()),
                cx,
                mTextBaseLine + top,
                calendar.isCurrentMonth() ? (isSelected ? mCurDayTextPaint : mCurMonthTextPaint) : mOtherMonthTextPaint);
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
