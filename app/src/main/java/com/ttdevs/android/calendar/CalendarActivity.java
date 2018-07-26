package com.ttdevs.android.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.ttdevs.android.BaseActivity;
import com.ttdevs.android.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ttdevs
 */
public class CalendarActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvCalendar;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        tvCalendar = findViewById(R.id.tv_calendar);
        calendarView = findViewById(R.id.calendarView);

        init();
    }

    private void init() {
        int year = 2018, month = 7;
        Map<String, Calendar> map = new HashMap<>();
        for (int i = 1; i <= 30; i++) {
            Calendar calendar = getSchemeCalendar(year, month, i, "+1,22" + i);
            map.put(calendar.toString(), calendar);
        }
        calendarView.setSchemeDate(map);
        calendarView.setOnDateSelectedListener(new CalendarView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Calendar calendar, boolean isClick) {
                updateDate(calendar.getYear(), calendar.getMonth());
            }
        });
        calendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                updateDate(year, month);
            }
        });
    }

    private Calendar getSchemeCalendar(int year, int month, int day, String scheme) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(Color.GREEN);
        calendar.setScheme(scheme);
        return calendar;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_prev:
                calendarView.scrollToPre(true);
                break;
            case R.id.iv_next:
                calendarView.scrollToNext(true);
                break;
        }
    }

    private void updateDate(int year, int month) {
        tvCalendar.setText(String.format("%d年%02d月", year, month));
    }
}
