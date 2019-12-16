package com.evsoft.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 日期处理
 * 
 *
 *
 * @date 2016年12月21日 下午12:53:33
 */
public class DateUtils {
    private DateUtils(){}
	/** 时间格式(yyyy-MM-dd) */
	public static final  String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public static final  String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD = "yyyyMMdd";

    public static final String YYYY_MM = "yyyyMM";

	
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    public static String formatDateTime(Date date) {
        return format(date, DATE_TIME_PATTERN);
    }

    public static LocalDate objectToLocalDate(Object strdate) throws ParseException {
        if(strdate!=null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
            Date date = dateFormat.parse(strdate.toString());
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            return instant.atZone(zoneId).toLocalDate();
        }
        return null;
    }

    public static LocalDateTime objectToLocalDateTime(Object strdate) throws ParseException {
        if(strdate!=null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
            Date date = dateFormat.parse(strdate.toString());
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            return instant.atZone(zoneId).toLocalDateTime();
        }
        return null;
    }

    public static String secToTime(Integer time) {
	    if(time==null) return "0";
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

}
