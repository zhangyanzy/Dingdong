package com.dindong.dingdong.widget.date;

/**
 * Created by wcong on 2018/3/28.
 * <p>
 * </>
 */

public class YearMonthDay {
  private int year;
  private int month;
  private int week;
  private int day;
  private String monthDay;

  public String getMonthDay() {
    return monthDay;
  }

  public YearMonthDay setMonthDay(String monthDay) {
    this.monthDay = monthDay;
    return this;
  }

  public int getWeek() {
    return week;
  }

  public YearMonthDay setWeek(int week) {
    this.week = week;
    return this;
  }

  public YearMonthDay(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;
  }

  public int getYear() {
    return year;
  }

  public YearMonthDay setYear(int year) {
    this.year = year;
    return this;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getDay() {
    return day;
  }

  public void setDay(int day) {
    this.day = day;
  }

  @Override
  public String toString() {
    return "year=" + year + ", month=" + month;// + ", day=" + day
  }
}
