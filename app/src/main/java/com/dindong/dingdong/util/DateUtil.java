package com.dindong.dingdong.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wcong on 2018/3/24.
 * <p>
 * 日期格式化</>
 */

public class DateUtil {
  public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static String DEFAULT_DATE_FORMAT_1 = "yyyy-MM-dd HH:mm";
  public static String DEFAULT_DATE_FORMAT_2 = "yyyy/MM/dd";
  public static String DEFAULT_DATE_FORMAT_3 = "yyyy-MM-dd";
  public static String DEFAULT_DATE_FORMAT_4 = "yyyy/MM/dd HH:mm:ss";
  public static String DEFAULT_DATE_FORMAT_5 = "yyyy/MM/dd EE";
  public static String DEFAULT_DATE_FORMAT_6 = "yyyy年M月d日";
  public static String DEFAULT_DATE_FORMAT_7 = "HH:mm";
  public static String DEFAULT_DATE_FORMAT_8 = "yyyyMMdd HH:mm";
  public static String DEFAULT_DATE_FORMAT_9 = "M月dd日";
  public static String DEFAULT_DATE_FORMAT_10 = "HH:mm:ss";

  public static String format(Date date, String format) {
    if (IsEmpty.object(date))
      return null;
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
    return sdf.format(date);
  }

  public static String format(String date, String format) {
    if (IsEmpty.object(date))
      return null;
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
    try {
      return sdf.format(parse(date, DEFAULT_DATE_FORMAT));
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Date parse(String date, String format) throws ParseException {
    if (IsEmpty.object(date))
      return null;
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
    return sdf.parse(date);
  }
}
