package com.evsoft.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;

public class DateUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final String FormatDateTime         = "yyyy-MM-dd HH:mm:ss";
    public static final String FormatDate             = "yyyy-MM-dd";
    public static final String FormatDateTimeNOSecond = "yyyy-MM-dd HH:mm";
    public static final String FormatDateTimeMsec     = "yyyy-MM-dd HH:mm:ss.S";

    public static final String GeneralFormatDateTime  = "y-M-d H:m:s";
    public static final String GeneralFormatDate             = "y-M-d";
    public static final String GeneralSlashFormatDateTime  = "y/M/d H:m:s";
    public static final String GeneralSlashFormatDate  = "y/M/d";

    /**
     * 取当前时间，格式 2015-12-02 16:53:51.0452503
     * <p>
     * >>>>>>>>>>>>>>>>>>>精度有损失，待修改<<<<<<<<<<<<<<<<<<<<
     *
     * @return
     */
    public static String getNow() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSS");
        return sf.format(new Date());
    }

    public static Date toDate(String source) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            return sf.parse(source);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 字符串转日期
     *
     * @param source
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date stringToDateFormat(String source, String format){
        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            return sf.parse(source);
        } catch (ParseException e) {
            logger.error("格式化异常", e);
        }
        return null;
    }
    /**
     * 日期转字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToStringFormat(Date date, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }

    public static String dateToCommonString(Date date) {
        if(date==null)
            return "";
       try
       {
           SimpleDateFormat sf = new SimpleDateFormat(FormatDateTime);
           return sf.format(date);
       }
       catch (Exception e)
       {
           return "";
       }
    }

    public static String localDateToStringFormat(LocalDate localDate, String format) {
        if (localDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return formatter.format(localDate);
        }
        return "";
    }

    public static String localDateTimeToStringFormat(LocalDateTime localDateTime, String format) {
        if (localDateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return formatter.format(localDateTime);
        }
        return "";
    }

    public static LocalDate stringToLocalDateFormat(String dateStr, String format) {
        if (!StringUtils.isEmpty(dateStr) && CommonUtil.isDate(dateStr)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(dateStr, formatter);
        }
        return null;
    }

    /**
     * 转换日期格式(年月日)的字符串为日期格式，否则返回supplier值
     *
     * @param datestr           如果为空，则返回supplier提供的值
     * @param formatter
     * @param localDateSupplier 如果为空抛出空指针异常
     * @return
     */
    public static LocalDate safeConvertLocalDate(String datestr, DateTimeFormatter formatter, Supplier<LocalDate> localDateSupplier) {
        Objects.requireNonNull(localDateSupplier);
        if (StringUtils.isBlank(datestr)) {
            return localDateSupplier.get();
        }
        try {
            return LocalDate.parse(datestr, formatter);
        } catch (Exception e) {
            return localDateSupplier.get();
        }
    }

    /**
     * 对应数据库函数[f_GetRepaymentAdvance_Date]
     * 指定时间=date+months+1day
     *
     * @param date
     * @param months
     * @return
     */
    public static Date nextFewMonth(Date date, Integer months) {
        Objects.requireNonNull(date, "date is null");
        Objects.requireNonNull(months, "months is null");
        return 0 == months ? date : Date.from(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).plusMonths(1).plusDays(1).plusMonths((long)(months-1)).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 对应数据库函数[f_GetMaxRepayment_Date]
     * 指定时间=date+months+1day
     *
     * @param date
     * @param months
     * @param repaymentType
     * @return
     */
    public static Date maxRepaymentDate(Date date,Integer months,Integer repaymentType){
        if(repaymentType==1){
            Objects.requireNonNull(date, "date is null");
            Objects.requireNonNull(months, "months is null");
            return Date.from(LocalDateTime.ofInstant(date.toInstant(),ZoneId.systemDefault()).plusMonths(months).plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        }else{
            return nextFewMonth(date,months);
        }
    }

    public static String getCurrentTime(){
       return getCurrentTime("yyyy-MM-dd HH:mm:ss");
    }

    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat dt = new SimpleDateFormat(format);
        return dt.format(date);
    }

    public static String getCurrentDate() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(new Date());
    }

    public static String getCurrentMonthFirstDate() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-01");
        return sf.format(new Date());
    }

    public static String getCurrentTimeWithOnlyNumber() {
        return getCurrentTime("yyyyMMddHHmmss");
    }

    /**
     * 计算两个日期的时间间隔,返回xx天xx小时xx分钟xx秒
     * @param d1 第一个日期和时间
     * @param d2 第二个日期和时间
     * @return
     */
    public static String diff(LocalDateTime d1,LocalDateTime d2) {
        long begin= Timestamp.valueOf(d1).getTime(),end= Timestamp.valueOf(d2).getTime();
        long between=(end-begin)/1000;//除以1000是为了转换成秒
        long day1=between/(24*3600);
        long hour1=between%(24*3600)/3600;
        long minute1=between%3600/60;
        long second1=between%60;
        return day1+"天"+hour1+"小时"+minute1+"分"+second1+"秒";
    }

    public static Date getNextDay(Date date,int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);//+1今天的时间加一天
        date = calendar.getTime();
        return date;
    }

    public static String getNextDayString(Date date,int day) {
       return dateToCommonString(getNextDay(date,day));
    }

    /**
     * 判断日期是否为今天
     * @param localDateTime 要判断的日期
     * */
    public static boolean isToday(LocalDateTime localDateTime){
        LocalDate date = LocalDate.now();//今天日期
        String d1 = localDateTime.getYear()+""+localDateTime.getMonthValue()+""+localDateTime.getDayOfMonth();
        String d2 = date.getYear()+""+date.getMonthValue()+""+date.getDayOfMonth();
        return d1.equals(d2);
    }

    /**
     * 获取当前日期的前{month}月的年月
     * @param month
     * @return
     */
    public static Date getDateByMonth(Integer month){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -month);
        return c.getTime();
    }

    public static LocalDate parseLocalDate(String date) {
        return parseLocalDate(date, "yyyy-MM-dd");
    }

    public static LocalDateTime parse(String time, String... formats) {
        if(StringUtils.isNotBlank(time)){
            String timeStr = time.trim();
            for (String format : formats) {
                try {
                    return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(format));
                } catch (Exception e){
                    logger.warn("日期" + time + "默认格式" + format + "转换异常", e);
                }
            }
            try {
                if(timeStr.contains("-")){
                    if(timeStr.contains(" ")){
                        return LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern(GeneralFormatDateTime));
                    }else{
                        return LocalDate.parse(timeStr, DateTimeFormatter.ofPattern(GeneralFormatDate)).atStartOfDay();
                    }
                }else if(timeStr.contains("/")){
                    if(timeStr.contains(" ")){
                        return LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern(GeneralSlashFormatDateTime));
                    }else{
                        return LocalDate.parse(timeStr, DateTimeFormatter.ofPattern(GeneralSlashFormatDate)).atStartOfDay();
                    }
                }
            }catch (Exception e){
                logger.warn("日期" + time + "格式转换异常",e);
            }
        }
        return null;
    }

    public static LocalDate parseLocalDate(String date, String pattern) {
        if(StringUtils.isEmpty(pattern))
            pattern = "yyyy-MM-dd";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(date, formatter);
        } catch (Exception e) {
            logger.error("日期格式化异常", e);
            throw new RuntimeException("日期格式不正确");
        }
    }

    /**
     * 判断查询的日期所在的周一和周末
     * @param queryDate
     * @return
     */
    public static Map<String, LocalDate> currentWeekBeginAndEnd(String queryDate) {
//        Calendar cal = Calendar.getInstance();
//        Date time = null;
//        try {
//            if (queryDate == null || "".equals(queryDate.trim())) {//前台没有传日期过来
//                time = new Date();//设置当前日期
//            } else {
//                time = new SimpleDateFormat("yyyy-MM-dd").parse(queryDate);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        cal.setTime(time);
//        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
//        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
//        if (1 == dayWeek) {
//            cal.add(Calendar.DAY_OF_MONTH, -1);
//        }
//        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
//        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
//        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
//        Date startTime = cal.getTime();
//        cal.add(Calendar.DATE, 6);
//        Date endTime = cal.getTime();
//        Map<String, Date> map = new HashMap<String, Date>();
//        map.put("startTime", startTime);
//        map.put("endTime", endTime);

        LocalDate localDate = null;
        if (queryDate == null || "".equals(queryDate.trim())) {
            localDate = LocalDate.now();
        } else {
            localDate = parseLocalDate(queryDate);  //  默认格式："yyyy-MM-dd"
        }
        LocalDate startTime = localDate.with(DayOfWeek.MONDAY);
        LocalDate endTime = localDate.with(DayOfWeek.SUNDAY);

        Map<String, LocalDate> map = new HashMap<>();
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        return map;
    }
    public static String getEndDate(String dateStr) {
        if (!StringUtils.isEmpty(dateStr) && CommonUtil.isDate(dateStr)) {
            Date date =stringToDateFormat(dateStr,"yyyy-MM-dd");
            if(date!=null)
                return dateToStringFormat(date,"yyyy-MM-dd")+" 23:59:59";
        }
        return null;
    }

    /**
     *  获取前几个小时的时间
     */
    public static String getBeforeByHourTime(int ihour){
        String returnstr = "";
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - ihour);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        returnstr = df.format(calendar.getTime());
        return returnstr;
    }
}
