package com.rocui.util.base;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Jdate {

    private Date date;
    private Calendar calendar;
    private final String patternday = "yyyy-MM-dd";
    private final String pattern = "yyyy-MM-dd HH:mm:ss";

    private Jdate(String pattern, String datestr) {
        this.date = this._get(pattern, datestr);
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(this.date);
    }

    private Jdate(String datestr) {
        this.date = this._get(pattern, datestr);
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(this.date);
    }

    private Jdate(Date date) {
        this.date = date;
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(this.date);
    }

    private Jdate(Calendar calendar) {
        this.calendar = calendar;
        this.date = calendar.getTime();
    }

    private Jdate() {
        this.date = new Date();
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(this.date);
    }

    public static Jdate with(String datestr, String pattern) {
        return new Jdate(pattern, datestr);
    }

    public static Jdate with(String datestr) {
        return new Jdate(datestr);
    }

    public static Jdate with(Date date) {
        return new Jdate(date);
    }

    public static Jdate with(Calendar calendar) {
        return new Jdate(calendar);
    }

    public static Jdate now() {
        return new Jdate();
    }

    public Date getDate() {
        return this.date;
    }

    public int year() {
        return this.calendar.get(Calendar.YEAR);
    }

    public int month() {
        return this.calendar.get(Calendar.MONTH) + 1;
    }

    public int day() {
        return this.calendar.get(Calendar.DATE);
    }

    public int hour() {
        return this.calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int minute() {
        return this.calendar.get(Calendar.MINUTE);
    }

    public int second() {
        return this.calendar.get(Calendar.SECOND);
    }

    public String date() {
        SimpleDateFormat df = new SimpleDateFormat(patternday);
        return df.format(this.date);
    }

    public String date(String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(this.date);
    }

    public String dateTime() {
        return this.toString();
    }

    public String dateTime(String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(this.date);
    }

    public long timeStamp() {
        return this.date.getTime();
    }

    public int toInt() {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return Integer.parseInt(sdf.format(this.date));
    }

    public Long toLong() {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return this.date.getTime();
    }

    public String chinaDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(this.date);
    }

    public String chinaDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return sdf.format(this.date);
    }

    public long differ(Jdate date) {
        if (date != null) {
            return Math.abs((this.date.getTime() - date.getDate().getTime()) / (24 * 60 * 60 * 1000));
        } else {
            return 0;
        }
    }

    public int between(Jdate date) {
        Calendar d1 = this.calendar;
        Calendar d2 = date.calendar;
        if (d1.after(d2)) {
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    public int differWokingDay(Jdate date) {
        Calendar d1 = this.calendar;
        Calendar d2 = date.calendar;
        int result = -1;
        if (d1.after(d2)) {
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int charge_start_date = 0;
        int charge_end_date = 0;
        int stmp;
        int etmp;
        stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
        etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);
        if (stmp != 0 && stmp != 6) {
            charge_start_date = stmp - 1;
        }
        if (etmp != 0 && etmp != 6) {
            charge_end_date = etmp - 1;
        }
        result = (int) (Jdate.with(getNextMonday(d1)).differ(Jdate.with(getNextMonday(d2))) / 7 * 5 + charge_start_date - charge_end_date);
        return result + 1;
    }

    private Calendar getNextMonday(Calendar date) {
        Calendar result = null;
        result = date;
        do {
            result = (Calendar) result.clone();
            result.add(Calendar.DATE, 1);
        } while (result.get(Calendar.DAY_OF_WEEK) != 2);
        return result;
    }

    public String firstDayOfMonth() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar gc = (GregorianCalendar) this.calendar;
        gc.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gc.getTime());
        return day_first;
    }

    public String endDayOfMonth() {
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, - 1);
        SimpleDateFormat simpleFormate = new SimpleDateFormat(" yyyy-MM-dd ");
        return simpleFormate.format(calendar.getTime());
    }

    public String recentlyTime(int day) {
        Calendar c = this.calendar;
        c.add(Calendar.DAY_OF_MONTH, day);
        Date d = c.getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(d);
    }

    public int differMonday() {
        Calendar cl = this.calendar;
        int dayOfWeek = cl.get(Calendar.DAY_OF_WEEK);
        if (cl.get(Calendar.DAY_OF_WEEK) == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    public int differSunday() {
        Calendar cl = this.calendar;
        int dayOfWeek = cl.get(Calendar.DAY_OF_WEEK);
        if (cl.get(Calendar.DAY_OF_WEEK) == 1) {
            return 0;
        } else {
            return 8 - dayOfWeek;
        }
    }

    public String currentMonday() {
        Calendar cl = this.calendar;
        int dayPlus = Jdate.now().differMonday();
        cl.add(Calendar.DATE, dayPlus);
        String moday = Jdate.with(cl).toString();
        return moday;
    }

    public String currentSunday() {
        Calendar cl = this.calendar;
        int dayPlus = Jdate.now().differSunday();
        cl.add(Calendar.DATE, dayPlus);
        String sunday = Jdate.with(cl).toString();
        return sunday;
    }

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(this.date);
    }

    private Date _get(String pattern, String datestr) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date datex;
        try {
            datex = df.parse(datestr);
        } catch (ParseException pe) {
            return null;
        }
        return datex;
    }
}
