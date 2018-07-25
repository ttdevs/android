package com.ttdevs.android.calendar;

import android.graphics.Color;
import android.os.Bundle;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.ttdevs.android.BaseActivity;
import com.ttdevs.android.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ttdevs
 */
public class CalendarActivity extends BaseActivity {
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

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
}
