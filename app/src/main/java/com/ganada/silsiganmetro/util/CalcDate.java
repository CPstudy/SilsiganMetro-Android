package com.ganada.silsiganmetro.util;

import android.util.Log;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by user on 2017. 4. 8..
 */

public class CalcDate {

    Calendar ca = Calendar.getInstance();
    int hour = ca.get(Calendar.HOUR_OF_DAY);
    int min = ca.get(Calendar.MINUTE);
    int sec = ca.get(Calendar.SECOND);
    int ms = ca.get(Calendar.MILLISECOND);
    int year = ca.get(Calendar.YEAR);
    int month = ca.get(Calendar.MONTH) + 1;
    int date = ca.get(Calendar.DATE);
    int dayofweek = ca.get(Calendar.DAY_OF_WEEK);


    public String getDate() {
        String str;
        if(hour >= 0 && hour <= 3) {
            ca.add(Calendar.DATE, -1);
            year = ca.get(Calendar.YEAR);
            month = ca.get(Calendar.MONTH) + 1;
            date = ca.get(Calendar.DATE);
            dayofweek = ca.get(Calendar.DAY_OF_WEEK);
        }

        str = String.format(Locale.KOREA, "%d%02d%02d", year, month, date);
        return str;
    }

    public String getID() {
        return String.format(Locale.KOREA, "%4d%02d%02d%02d%02d%02d%04d", year, month, date, hour, min, sec, ms);
    }

    public int getDayWeek() {
        if(hour >= 0 && hour <= 4) {
            ca.add(Calendar.DATE, -1);
            year = ca.get(Calendar.YEAR);
            month = ca.get(Calendar.MONTH) + 1;
            date = ca.get(Calendar.DATE);
            dayofweek = ca.get(Calendar.DAY_OF_WEEK);
        }
        Log.e("date", "" + date);
        switch(dayofweek) {
            case 1: return 3;       // 일요일
            case 2:
            case 3:
            case 4:
            case 5:
            case 6: return 1;       // 평일
            case 7: return 2;       // 토요일
            default: return 1;
        }
    }

    public int getTimeList() {
        if(hour >= 5 && hour <= 24) {
            return hour - 5;
        } else {
            return 0;
        }
    }

}
