package com.amily.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lizhuo
 * @since 2019/2/20 16:21
 */
public class DateUtilsPlus extends DateUtils {

    public static final String DATE_BASIC_STYLE = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_NORMAL_STYLE = "yyyy-MM-dd";
    private static final Pattern DATE_BASIC_PATTERN = Pattern.compile(RegexType.DATE_BASIC);
    private static final Pattern DATE_BASIC_FORMAT_PATTERN = Pattern.compile("(\\d{4})-(\\d+)-(\\d+).*");

    /**
     * 获取指定时间的年月日
     *
     * @param date 指定时间
     * @return 获取年份
     */
    public static int getYear(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate.getYear();
    }

    /**
     * 获取指定时间的月
     *
     * @param date 指定时间
     * @return 获取年份
     */
    public static int getMonth(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate.getMonth().getValue();
    }

    /**
     * 获取指定时间的天
     *
     * @param date 指定时间
     * @return 获取年份
     */
    public static int getDay(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate.getDayOfMonth();
    }


    /**
     * 得到当月最后一天的'时间'
     */
    public static Date nowMonthLast() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.DATE, now.getActualMaximum(Calendar.DAY_OF_MONTH));
        return now.getTime();
    }

    /**
     * 获取当前时间不含时分秒
     *
     * @return 当前时间，不含有时分秒
     */
    public static LocalDate nowLocalDate() {
        return LocalDate.now();
    }

    /**
     * 获取当前时间含有时分秒
     *
     * @return 当前时间，含有时分秒
     */
    public static LocalDateTime nowLocalDateTimie() {
        return LocalDateTime.now();
    }

    /**
     * 得到当月最后一天
     *
     * @return min:1 , max:31
     */
    public static int nowMonthLastDay() {
        return Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据时间(Date类型)得到指定格式的日期字符串
     */
    public static String formatDateByStyle(final Date date, final String dateStyle) {
        DateFormat format = new SimpleDateFormat(dateStyle);
        return format.format(date);
    }

    /**
     * {@link #formatDateByStyle(Date, String)} 默认格式格式 : {@link DateFormatStyle#CN_DATE_BASIC_STYLE}
     */
    public static String formatDateByStyle(final Date date) {
        return formatDateByStyle(date, DateFormatStyleEnum.CN_DATE_BASIC_STYLE.getDateStyle());
    }

    /**
     * 根据时间类型的字符串得到指定格式的日期 <br/>
     *
     * @param date      : 日期
     * @param dateStyle | yyyy : 年 | MM : 月 | dd : 日 | HH : 时 | mm : 分 | ss : 秒
     */
    public static Date formatStringByStyleToDate(final String date, final String dateStyle) {
        DateFormat format = new SimpleDateFormat(dateStyle);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * 得到两个日期之间的分钟差
     *
     * <pre>
     * DateUtil.getMinuteInterval(  2017-5-4 15:00:08 ,2017-5-4 15:51:08  ) = 51
     * DateUtil.getMinuteInterval(  2017-5-4 15:00:34 ,2017-5-4 15:51:34  ) = 51
     * DateUtil.getMinuteInterval(  2016-5-4 15:56:30 ,2017-5-4 15:56:30  ) = 525600
     * </pre>
     *
     * @param a : Date 类型,不分前后顺序
     * @param b : Date 类型,不分前后顺序
     * @return 日期之间的分钟间隔
     */
    public static long getMinuteInterval(Date a, Date b) {
        return Math.abs((a.getTime() - b.getTime()) / (1000 * 60));
    }

    /**
     * 得天数间隔时间
     *
     * <pre>
     * DateUtil.getDayInterval(  2016-5-4 15:56:30 ,2017-5-4 15:56:30  ) = 365
     * </pre>
     *
     * @param a : Date 类型,不分前后顺序
     * @param b : Date 类型,不分前后顺序
     * @return 日期之间的天数间隔
     */
    public static long getDayInterval(Date a, Date b) {
        return Math.abs((a.getTime() - b.getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔 通过计算两个日期相差的毫秒数来计算两个日期的天数差的。一样会有一个小问题，就是当他们相差是23个小时的时候，它就不算一天了
     *
     * @param date1 时间1
     * @param date2 事件2
     * @return 相差的天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
    }

    /**
     * 取出开始时间和结束时间之间的时间集合,也包括开始时间和结束时间
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 时间集合
     */
    public static List<Date> getDatesBetweenTwoDate(Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<>();
        dates.add(startDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        boolean bContinue = true;
        while (bContinue) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            if (endDate.after(cal.getTime())) {
                dates.add(cal.getTime());
            } else {
                break;
            }
        }
        dates.add(endDate);
        return dates;
    }

    /**
     * 判断时间是否在某个时间之前
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 判断结果
     */
    public static boolean before(Date date1, Date date2) {
        return date1.before(date2);
    }

    /**
     * 日期之间的小时数间隔
     *
     * @param a : Date 类型,不分前后顺序
     * @param b : Date 类型,不分前后顺序
     * @return 日期之间的小时数间隔
     */
    public static long getHourInterval(Date a, Date b) {
        return Math.abs((a.getTime() - b.getTime()) / (1000 * 60 * 60));
    }

    /**
     * 目前仅支持以下类型的判断
     * <p>
     * 2016-12-19 15:59:45 2016-12-19 2016/12/19 15:59:45 2016/12/19 20161219155945 20161219
     */
    public static boolean isDate(final String inputTime) {
        Matcher matcher = DATE_BASIC_PATTERN.matcher(inputTime);
        if (!matcher.matches()) {
            return false;
        }
        matcher = DATE_BASIC_FORMAT_PATTERN.matcher(inputTime);
        if (matcher.matches()) {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int date = Integer.parseInt(matcher.group(3));
            if (date > 28) {
                Calendar instance = Calendar.getInstance();
                instance.set(year, month - 1, 1);
                int lastDay = instance.getActualMaximum(Calendar.DAY_OF_MONTH);
                return (lastDay >= date);
            }
        }
        return true;
    }

    /**
     * 不是一个Date类型
     *
     * @see DateUtilsPlus#isDate(String)
     */
    public static boolean isNotDate(final String inputTime) {
        return !isDate(inputTime);
    }

    /**
     * 字符串转成Date类型
     * <p>
     * dateStyle:
     * <table border="1" summary="Pattern Tokens">
     * <tr>
     * <th>character</th>
     * <th>duration element</th>
     * </tr>
     * <tr>
     * <td>y</td>
     * <td>years</td>
     * </tr>
     * <tr>
     * <td>M</td>
     * <td>months</td>
     * </tr>
     * <tr>
     * <td>d</td>
     * <td>days</td>
     * </tr>
     * <tr>
     * <td>H</td>
     * <td>hours</td>
     * </tr>
     * <tr>
     * <td>m</td>
     * <td>minutes</td>
     * </tr>
     * <tr>
     * <td>s</td>
     * <td>seconds</td>
     * </tr>
     * <tr>
     * <td>S</td>
     * <td>milliseconds</td>
     * </tr>
     * </table>
     */
    public static Date formatStringByStyle(final String inputTime, final String dateStyle) {
        try {
            if (StringUtils.isBlank(inputTime)) {
                return null;
            }
            if (StringUtils.isBlank(dateStyle)) {
                return null;
            }
            DateFormat format = new SimpleDateFormat(dateStyle);
            return format.parse(inputTime);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * {@link #formatStringByStyle(String, String)}
     */
    public static LocalDateTime formatStringByStyleToLocalDateTime(final String inputTime, final String dateStyle) {
        if (StringUtils.isBlank(inputTime)) {
            return null;
        }
        if (StringUtils.isBlank(dateStyle)) {
            return null;
        }
        return LocalDateTime.parse(inputTime, DateTimeFormatter.ofPattern(dateStyle));
    }

    /**
     * 得到当前时间,字符串形式,默认格式:{@link DateFormatStyle#DATE_TIMESTAMP_STYLE}
     */
    public static String currentTimeString() {
        return currentTimeString(DateFormatStyleEnum.DATE_TIMESTAMP_STYLE.getDateStyle());
    }

    /**
     * 得到当前时间,字符串形式
     *
     * @param dateStyle {@link DateFormatStyle}
     */
    public static String currentTimeString(String dateStyle) {
        return formatDateByStyle(Calendar.getInstance().getTime(), dateStyle);
    }

    /**
     * 获取前 i 天日期
     */
    public static LocalDate getPreviousDay(int i) {
        LocalDate today = LocalDate.now();
        return today.minus(i, ChronoUnit.DAYS);
    }


    /**
     * 获取周第一天0点时间
     */
    public static Date getWeekFirstDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMinimum(Calendar.DAY_OF_WEEK));
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
        return cal.getTime();
    }

    /**
     * 获取本周一0点时间
     */
    public static Date getNowWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 解决周日会出现 并到下一周的情况
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        // 设置为0时0分0秒
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);

        return cal.getTime();
    }

    /**
     * 获取月的第一天0点时间
     */
    public static Date getMonthFirstDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
        return cal.getTime();
    }

    /**
     * 获取月的最后一天24:00:00点时间
     */
    public static Date getMonthLastDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 24, 0, 0);
        return cal.getTime();
    }

    /**
     * 当前时间是本月的第几天
     *
     * @param needDate :yyyy-MM-dd
     */
    public static int getDayOfMonth(String needDate) {
        Date date = null;
        // 得到日历的实例
        Calendar calendar = Calendar.getInstance();
        if (StringUtils.isBlank(needDate)) {
            date = new Date();
        } else {
            try {
                date = DateUtilsPlus.parseDate(needDate, DATE_NORMAL_STYLE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (date != null) {
            // 将现在的时间赋值给Calendar对象
            calendar.setTime(date);
        }

        // 取得日期
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前天数的第num天
     *
     * @param date :yyyy-MM-dd
     */
    public static String getBeforeDay(String date, int num) {
        Calendar calendar = Calendar.getInstance();
        if (StringUtils.isNotBlank(date)) {
            Date time = null;
            try {
                time = DateUtilsPlus.parseDate(date, DATE_NORMAL_STYLE);
                calendar.setTime(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        calendar.add(Calendar.DATE, -num);
        return DateFormatUtils.format(calendar, DATE_NORMAL_STYLE);
    }

    /**
     * 获取上个月的最后一天
     */
    public static String getLastDayOfBeforeMonth() {
        SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_NORMAL_STYLE);
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date strDateTo = calendar.getTime();
        return sdf1.format(strDateTo);
    }

    /**
     * 获得当前日的int值 如: 20180627
     *
     * @return 日int值
     */
    public static int getDayIntegerValue(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getYear() * 10000 + localDate.getMonthValue() * 100 + localDate.getDayOfMonth();
    }

    /**
     * 获得当前月的int值 如: 201806
     *
     * @return 月int值
     */
    public static int getMonthIntegerValue(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getYear() * 100 + localDate.getMonthValue();
    }


    /**
     * 时间累加方法
     *
     * @param dateTime 需要累计的时间
     * @param timeType 累计时间类型 Calendar.MINUTE(分钟) / Calendar.DAY_OF_MONTH(天数)
     * @param timeNo   增加分钟数|天数
     * @return String
     */
    public static Date dateAddTimes(Date dateTime, int timeType, int timeNo) {
        Calendar ca = Calendar.getInstance();
        try {
            ca.setTime(dateTime);
            if (timeType == Calendar.MINUTE) {
                ca.add(Calendar.MINUTE, timeNo);
            } else {
                ca.add(Calendar.DAY_OF_MONTH, timeNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ca.getTime();
    }

    /**
     * 获取零点
     *
     * @param 当前时间
     * @return date
     */
    public static Date getZeroOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}


