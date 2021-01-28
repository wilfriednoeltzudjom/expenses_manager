package com.thetechshrine.expensemanager.utils;

import android.content.Context;

import com.thetechshrine.expensemanager.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    private Context context;

    public DateUtils() {
    }

    public DateUtils(Context context) {
        this.context = context;
    }

    public static DateUtils from(Context context) {
        return new DateUtils(context);
    }

    public static boolean isSameDate(Date date, Date anotherDate) {
        return dateFormat.format(date).equals(dateFormat.format(anotherDate));
    }

    public static boolean isToday(Date date) {
        return dateFormat.format(date).equals(dateFormat.format(new Date()));
    }

    public static boolean isYesterday(Date date) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        Calendar target = Calendar.getInstance();
        target.setTime(date);

        return yesterday.get(Calendar.YEAR) == target.get(Calendar.YEAR) && yesterday.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR);
    }

    public static String formatDayMonth(Date date, boolean monthAsString) {
        String format = monthAsString ? "dd MMM" : "dd/MM";

        return new SimpleDateFormat(format, Locale.getDefault()).format(date);
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(date);
    }

    public static String formatMonthYear(Date date) {
        return new SimpleDateFormat("MMMM, yyyy", Locale.getDefault()).format(date);
    }

    public static String formatHourMinute(Date date) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
    }

    public static String formatDayMonthYear(Date date) {
        return new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(date);
    }

    public Date setTimeToStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public Date setTimeToEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTime();
    }

    public Date getDateWithoutTime(Date date) {
        return setTimeToStartOfDay(date);
    }

    public Date getFirstDateOfTheCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return setTimeToStartOfDay(calendar.getTime());
    }

    public Date getLastDateOfTheCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return setTimeToEndOfDay(calendar.getTime());
    }

    public Date getDate30DaysAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);

        return setTimeToStartOfDay(calendar.getTime());
    }

    public String getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                return context.getString(R.string.monday);
            case 2:
                return context.getString(R.string.tuesday);
            case 3:
                return context.getString(R.string.wednesday);
            case 4:
                return context.getString(R.string.thursday);
            case 5:
                return context.getString(R.string.friday);
            case 6:
                return context.getString(R.string.saturday);
            case 7:
                return context.getString(R.string.sunday);
            default:
                return "";

        }
    }
}
