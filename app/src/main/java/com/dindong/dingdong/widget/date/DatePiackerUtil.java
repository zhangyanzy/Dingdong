package com.dindong.dingdong.widget.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.dindong.dingdong.util.DateUtil;

import android.util.Log;

/**
 * Created by wcong on 2018/3/28.
 * <p>
 * </>
 */

public class DatePiackerUtil {
  public static String DEFAULT_DATE_FORMAT_1 = "yyyy-MM-dd HH:mm";
  public static String DEFAULT_DATE_FORMAT_2 = "yyyy/MM/dd HH:mm:ss";
  public static String DEFAULT_DATE_FORMAT_4 = "yyyy/MM/dd HH:mm";
  public static String DEFAULT_DATE_FORMAT_3 = "yyyy-MM-dd";
  public static String DEFAULT_DATE_FORMAT_5 = "M月d日";
  public static String DEFAULT_DATE_FORMAT_6 = "yyyy年M月d日";
  public static String DEFAULT_DATE_FORMAT_DAY = "yyyy/MM/dd";
  public static String DEFAULT_DATE_FORMAT_WEEK = "MM/dd";
  public static String DEFAULT_DATE_FORMAT_MONTH = "yyyy/MM";
  public static String DEFAULT_DATE_FORMAT_YEAR = "yyyy";
  private static boolean LOG = true;

  /**
   * 获取年月日集合
   */
  public static List<YearMonthDay> setYearMonthDay(int timeScope) {// int
    // startYear

    Calendar calendar = Calendar.getInstance();
    int mCurrYear = calendar.get(Calendar.YEAR);
    int mCurrMonth = calendar.get(Calendar.MONTH);// 2
    int mCurrDay = calendar.get(Calendar.DATE);
    List<YearMonthDay> list = new ArrayList<>();
    for (int year = mCurrYear - timeScope; year <= mCurrYear; year++) {// 2017
      for (int mon = 0; mon < 12; mon++) {
        if (year == mCurrYear && mon <= mCurrMonth + 1) {
          if (mon == mCurrMonth) {
            list.add(new YearMonthDay(year, mon, mCurrDay));
          } else
            list.add(new YearMonthDay(year, mon, 0));
        } else if (year < mCurrYear) {
          list.add(new YearMonthDay(year, mon, 0));
        }
      }
    }
    return list;
  }

  // 获取当前时间所在年的周数
  public static int getWeekOfYear(Date date) {
    Calendar c = Calendar.getInstance();
    c.setFirstDayOfWeek(Calendar.MONDAY);
    c.setMinimalDaysInFirstWeek(7);
    c.setTime(date);

    return c.get(Calendar.WEEK_OF_YEAR);
  }

  // 获取本周的开始时间
  public static Date getBeginDayOfWeek(Date date, Calendar mCalendar) {
    mCalendar.setTime(date);
    int dayofweek = mCalendar.get(Calendar.DAY_OF_WEEK);
    if (dayofweek == 1) {
      dayofweek += 7;
    }
    mCalendar.add(Calendar.DATE, 2 - dayofweek);
    return getDayStartTime(mCalendar.getTime(), mCalendar);
  }

  // 获取本周的结束时间
  public static Date getEndDayOfWeek(Date date, Calendar mCalendar) {
    mCalendar.setTime(getBeginDayOfWeek(date, mCalendar));
    mCalendar.add(Calendar.DAY_OF_WEEK, 6);
    Date weekEndSta = mCalendar.getTime();
    return getDayEndTime(weekEndSta, mCalendar);
  }

  // 获取某个日期的开始时间
  private static Date getDayStartTime(Date date, Calendar mCalendar) {
    if (null != date)
      mCalendar.setTime(date);
    mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
        mCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    return mCalendar.getTime();
  }

  // 获取某个日期的结束时间
  private static Date getDayEndTime(Date date, Calendar mCalendar) {
    if (null != date)
      mCalendar.setTime(date);
    mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
        mCalendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
    // mCalendar.set(Calendar.MILLISECOND);
    return mCalendar.getTime();// new Timestamp(mCalendar.getTimeInMillis());
  }

  // 获取某年的第几周的开始日期
  public static Date getFirstDayOfWeek(int year, int week) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, Calendar.JANUARY);
    c.set(Calendar.DATE, 1);

    Calendar cal = (GregorianCalendar) c.clone();
    cal.add(Calendar.DATE, week * 7);

    return getFirstDayOfWeek(cal.getTime());
  }

  // 获取某年的第几周的结束日期
  public static Date getLastDayOfWeek(int year, int week) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, Calendar.JANUARY);
    c.set(Calendar.DATE, 1);

    Calendar cal = (GregorianCalendar) c.clone();
    cal.add(Calendar.DATE, week * 7);

    return getLastDayOfWeek(cal.getTime());
  }

  // 获取当前时间所在周的开始日期
  public static Date getFirstDayOfWeek(Date date) {
    Calendar c = Calendar.getInstance();
    c.setFirstDayOfWeek(Calendar.MONDAY);
    c.setTime(date);
    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
    return c.getTime();
  }

  // 获取当前时间所在周的结束日期
  private static Date getLastDayOfWeek(Date date) {
    Calendar c = Calendar.getInstance();
    c.setFirstDayOfWeek(Calendar.MONDAY);
    c.setTime(date);
    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
    return c.getTime();
  }

  public static Long dateDiff(String startTime, String endTime, String format, String str) {
    // 按照传入的格式生成一个simpledateformate对象
    SimpleDateFormat sd = new SimpleDateFormat(format);
    long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
    long nh = 1000 * 60 * 60;// 一小时的毫秒数
    long nm = 1000 * 60;// 一分钟的毫秒数
    long ns = 1000;// 一秒钟的毫秒数
    long diff;
    long day = 0;
    long hour = 0;
    long min = 0;
    long sec = 0;
    // 获得两个时间的毫秒时间差异
    try {
      diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
      day = diff / nd;// 计算差多少天
      hour = diff % nd / nh + day * 24;// 计算差多少小时
      min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
      sec = diff % nd % nh % nm / ns;// 计算差多少秒
      // 输出结果
      System.out.println("时间相差：" + day + "天" + (hour - day * 24) + "小时" + (min - day * 24 * 60)
          + "分钟" + sec + "秒。");
      System.out.println("hour=" + hour + ",min=" + min);
      if (str.equalsIgnoreCase("h")) {
        return hour;
      } else {
        return min;
      }

    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (str.equalsIgnoreCase("h")) {
      return hour;
    } else {
      return min;
    }
  }

  public static void e(String tag, String text) {
    if (LOG) {
      Log.e(tag, text);
    }
  }

  /**
   * 通过集合获取角标
   *
   * @param list
   * @param num
   * @return
   */
  public static int getIndex(ArrayList<String> list, int num) {
    int index = 0;
    for (int i = 0; i < list.size(); i++) {
      String s = list.get(i);
      if (s.contains(num + "")) {
        index = i;
        break;
      }
    }
    return index;
  }

  public static int getResultByIndex(ArrayList<String> list, int index) {
    String s = list.get(index);
    return Integer.parseInt(s.substring(0, s.length() - 1));
  }

  /**
   * 获取年月
   */
  public static List<YearMonthDay> setYearMonth(int timeScope) {
    Calendar calendar = Calendar.getInstance();
    int mCurrYear = calendar.get(Calendar.YEAR);
    int mCurrMonth = calendar.get(Calendar.MONTH);// 2
    int mCurrDay = calendar.get(Calendar.DATE);
    List<YearMonthDay> list = new ArrayList<>();
    for (int year = mCurrYear - timeScope; year <= mCurrYear; year++) {// 2017
      for (int mon = 0; mon < 12; mon++) {
        if (year == mCurrYear && mon <= mCurrMonth) {
          list.add(new YearMonthDay(year, mon, 1));
        } else if (year < mCurrYear) {
          list.add(new YearMonthDay(year, mon, 1));
        }
      }
    }
    return list;
  }

  public static List<YearMonthDay> setYear(int timeScope) {
    Calendar calendar = Calendar.getInstance();
    int mCurrYear = calendar.get(Calendar.YEAR);
    int mCurrMonth = calendar.get(Calendar.MONTH);// 2
    int mCurrDay = calendar.get(Calendar.DATE);
    List<YearMonthDay> list = new ArrayList<>();
    for (int year = mCurrYear - timeScope; year <= mCurrYear; year++) {// 2017
      list.add(new YearMonthDay(year, 0, 0));
    }
    return list;
  }

  /**
   * 通过年份和月份 得到当月的日子
   *
   * @param year
   * @param month
   * @return
   */
  public static int getMonthDays(int year, int month) {
    month++;
    switch (month) {
    case 1:
    case 3:
    case 5:
    case 7:
    case 8:
    case 10:
    case 12:
      return 31;
    case 4:
    case 6:
    case 9:
    case 11:
      return 30;
    case 2:
      if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
        return 29;
      } else {
        return 28;
      }
    default:
      return -1;
    }
  }

  /**
   * 返回当前月份1号位于周几
   *
   * @param year
   *          年份
   * @param month
   *          月份，传入系统获取的，不需要正常的
   * @return 日：1 一：2 二：3 三：4 四：5 五：6 六：7
   */
  public static int getFirstDayWeek(int year, int month) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, 1);
    return calendar.get(Calendar.DAY_OF_WEEK);
  }

  /**
   * @param startNum
   *          数字起点 1
   * @return
   */
  public static List<YearMonthDay> weekToList(int startNum, int timeScope) {
    Calendar calendar = Calendar.getInstance();
    Date currentDate = calendar.getTime();
    int mCurrYear = calendar.get(Calendar.YEAR);
    int mCurrMonth = calendar.get(Calendar.MONTH);

    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
      int mCurrDay = calendar.get(Calendar.DATE) - 1;
      calendar.set(mCurrYear, mCurrMonth, mCurrDay);
    } else {
      int mCurrDay = calendar.get(Calendar.DATE);
      calendar.set(mCurrYear, mCurrMonth, mCurrDay);
    }

    int mCurrWeekNum = calendar.get(Calendar.WEEK_OF_YEAR);
    List<YearMonthDay> list = new ArrayList<>();

    for (int year = mCurrYear - timeScope; year <= mCurrYear; year++) {// 2017
      int countWeek = getMaxWeekNumOfYear(year);
      for (int weekNum = startNum; weekNum <= countWeek; weekNum++) {

        if (year == mCurrYear) {
          if (weekNum < mCurrWeekNum) {
            // calendar = Calendar.getInstance();
            Date date = getFirstDayOfWeek(year, weekNum);
            calendar.setTime(date);
            Date transDate = calendar.getTime();

            YearMonthDay yearMonthDay = new YearMonthDay(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            Date startDate = getFirstDayOfWeek(year, weekNum);
            Date endDate = getLastDayOfWeek(year, weekNum);
            yearMonthDay.setWeek(weekNum)
                .setMonthDay(DateUtil.format(startDate, DEFAULT_DATE_FORMAT_5) + "-"
                    + (DateUtil.format(endDate, DEFAULT_DATE_FORMAT_5)));
            list.add(yearMonthDay);
          } else if (weekNum == mCurrWeekNum) {
            YearMonthDay yearMonthDay = new YearMonthDay(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            Date startDate = getFirstDayOfWeek(currentDate);
            Date endDate = getLastDayOfWeek(currentDate);
            yearMonthDay.setWeek(weekNum)
                .setMonthDay(DateUtil.format(startDate, DEFAULT_DATE_FORMAT_5) + "-"
                    + (DateUtil.format(endDate, DEFAULT_DATE_FORMAT_5)));
            list.add(yearMonthDay);
          }

        } else if (year < mCurrYear) {
          calendar.setTime(getFirstDayOfWeek(year, weekNum));
          YearMonthDay yearMonthDay = new YearMonthDay(calendar.get(Calendar.YEAR),
              calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
          yearMonthDay.setWeek(weekNum).setMonthDay(
              DateUtil.format(getFirstDayOfWeek(year, weekNum), DEFAULT_DATE_FORMAT_5) + "-"
                  + (DateUtil.format(getLastDayOfWeek(year, weekNum), DEFAULT_DATE_FORMAT_5)));

          list.add(yearMonthDay);
        }
      }
    }
    return list;
  }

  // 获取当前时间所在年的最大周数
  public static int getMaxWeekNumOfYear(int year) {
    Calendar c = new GregorianCalendar();
    c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

    return getWeekOfYear(c.getTime());
  }

  /**
   * 获取前30天的日期
   *
   * @param date
   *          传入的日期
   * @return
   */
  public static Date getMonthAgo(Date date) {
    // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    // calendar.add(Calendar.MONTH, -1);
    calendar.add(Calendar.DATE, -29);
    Date monthAgo = calendar.getTime();
    // String monthAgo = simpleDateFormat.format(calendar.getTime());
    return calendar.getTime();
  }

  public static Date stringToDate(String str) {
    DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT_3);
    Date date = null;
    try {
      // Fri Feb 24 00:00:00 CST 2012
      date = format.parse(str);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    // 2012-02-24
    date = java.sql.Date.valueOf(str);

    return date;
  }

  /**
   * 得到指定月的天数
   */
  public static int getMonthLastDay(int year, int month) {
    Calendar a = Calendar.getInstance();
    a.set(Calendar.YEAR, year);
    a.set(Calendar.MONTH, month - 1);
    a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
    a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
    int maxDate = a.get(Calendar.DATE);
    return maxDate;
  }

  /**
   * 字符串转换成日期
   *
   * @param str
   * @return date
   */
  private static Date strToDate(String str) {

    SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT_3);
    Date date = null;
    try {
      date = format.parse(str);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }
}
