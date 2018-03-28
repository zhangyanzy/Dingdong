package com.dindong.dingdong.widget.date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.util.DateUtil;
import com.dindong.dingdong.widget.loopview.LoopView;
import com.dindong.dingdong.widget.loopview.OnItemSelectedListener;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by wcong on 2018/3/28.
 * <p>
 * 日期选择</>
 */

public class DatePicker extends Dialog {

  private static int MIN_YEAR = 2013;
  private static int MAX_YEAR = 2017;// MAX_YEAR
  private Params params;

  public DatePicker(Context context, int themeResId) {
    super(context, themeResId);
  }

  private void setParams(DatePicker.Params params) {
    this.params = params;
  }

  public interface OnDateSelectedListener {
    void onDateSelected(Date date);// int[] dates

    void onCancel();
  }

  private static final class Params {
    private boolean shadow = true;
    private boolean canCancel = true;
    private LoopView loopYear, loopMonth, loopDay;
    private OnDateSelectedListener callback;
  }

  public static class Builder {
    private final Context context;
    private final DatePicker.Params params;
    private String title;
    private int minYear = 2013;
    private int maxYear = -1;
    private int selectYear = -1;
    private int minMonth = 1;
    private int maxMonth = -1;
    private int selectMonth = -1;
    private int minDay = 1;
    private int maxDay = -1;
    private int selectDay = -1;
    private int type = 0;

    public Builder(Context context) {
      this.context = context;
      params = new DatePicker.Params();
    }

    public Builder setTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder setMaxDate(Date maxDate) {
      String maxDateStr = DateUtil.format(maxDate, DateUtil.DEFAULT_DATE_FORMAT_3);
      String[] maxDates = maxDateStr.split("-");

      maxYear = Integer.parseInt(maxDates[0]);
      maxMonth = Integer.parseInt(maxDates[1]);
      maxDay = Integer.parseInt(maxDates[2]);
      DatePiackerUtil.e("Date", "--" + maxYear + "--" + maxMonth + "--" + maxDay);
      return this;
    }

    public Builder setMinDate(Date minDate) {
      String maxDateStr = DateUtil.format(minDate, DateUtil.DEFAULT_DATE_FORMAT_3);
      String[] minDates = maxDateStr.split("-");

      minYear = Integer.parseInt(minDates[0]);
      MIN_YEAR = minYear;
      minMonth = Integer.parseInt(minDates[1]);
      minDay = Integer.parseInt(minDates[2]);
      DatePiackerUtil.e("Date", "--" + minYear + "--" + minMonth + "--" + minDay);
      return this;
    }

    public Builder setSelectDate(Date date) {
      String dateStr = DateUtil.format(date, DateUtil.DEFAULT_DATE_FORMAT_3);
      String[] dates = dateStr.split("-");
      selectYear = Integer.parseInt(dates[0]);// - 1
      selectMonth = Integer.parseInt(dates[1]);// - 1
      selectDay = Integer.parseInt(dates[2]);// - 1

      DatePiackerUtil.e("Date", "--" + selectYear + "--" + selectMonth + "--" + selectDay);
      return this;
    }

    public Builder setType(int type) {
      this.type = type;
      return this;
    }

    /**
     * 获取当前选择的日期
     *
     * @return int[]数组形式返回。例[1990,6,15]
     */
    private final Date getCurrDateValues() {
      int currYear = Integer.parseInt(params.loopYear.getCurrentItemValue());
      int currMonth = Integer.parseInt(params.loopMonth.getCurrentItemValue());
      int currDay = Integer.parseInt(params.loopDay.getCurrentItemValue());
      c.set(currYear, currMonth - 1, currDay);
      return c.getTime();// new int[]{currYear, currMonth, currDay}
    }

    public Builder setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
      params.callback = onDateSelectedListener;
      return this;
    }

    private Calendar c;

    public DatePicker create() {
      final DatePicker dialog = new DatePicker(context,
          params.shadow ? R.style.FullScreenDialog : R.style.FullScreenDialog);
      View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_date, null);

      c = Calendar.getInstance();
      TextView tvSelectDate = (TextView) view.findViewById(R.id.tv_select_date);
      tvSelectDate.setText(title);

      // 初始显示年
      final LoopView loopYear = (LoopView) view.findViewById(R.id.loop_year);
      loopYear.setTextSize(18f);
      loopYear.setArrayList(numToList(MIN_YEAR, maxYear - MIN_YEAR + 1,
          context.getResources().getString(R.string.date_picker_year)));
      if (selectYear != -1) {
        loopYear.setCurrentItem(selectYear);
      } else {
        loopYear.setCurrentItem(maxYear);
      }
      loopYear.setNotLoop();

      // 初始显示月
      final LoopView loopMonth = (LoopView) view.findViewById(R.id.loop_month);
      loopMonth.setTextSize(16f);
      List<String> monthList;
      // 如果是一年之内的那么就显示几月到几月， 否则就显示12个月
      if (maxYear - MIN_YEAR <= 0) {
        monthList = numToList(minMonth, maxMonth - minMonth + 1,
            context.getResources().getString(R.string.date_picker_month));
      } else {
        // 这里有问题
        if (selectYear == minYear) {
          monthList = numToList(minMonth, 12 - minMonth + 1,
              context.getResources().getString(R.string.date_picker_month));
        } else if (selectYear == maxYear) {
          monthList = numToList(1, maxMonth,
              context.getResources().getString(R.string.date_picker_month));
        } else
          monthList = numToList(1, 12,
              context.getResources().getString(R.string.date_picker_month));
      }

      loopMonth.setNotLoop();// 这里是不设置循环
      loopMonth.setArrayList(monthList);// 当前月份

      if (selectMonth != -1) {
        loopMonth.setCurrentItem(selectMonth);
      } else {
        loopMonth.setCurrentItem(c.get(Calendar.MONTH));
      }

      final LoopView loopDay = (LoopView) view.findViewById(R.id.loop_day);
      loopDay.setTextSize(16f);
      // 这里需要根据设置的最大天数来显示日子数

      if (maxYear - MIN_YEAR <= 0) {
        if (selectMonth == minMonth) {
          // 最小的天数到当月的天数
          int maxDayOfMonth = DatePiackerUtil.getMonthLastDay(minYear, minMonth);
          loopDay.setArrayList(numToList(minDay, maxDayOfMonth - minDay + 1,
              context.getResources().getString(R.string.date_picker_day)));
        } else if (selectMonth == maxMonth) {
          loopDay.setArrayList(
              numToList(1, maxDay, context.getResources().getString(R.string.date_picker_day)));
        } else {
          int maxDayOfMonth = DatePiackerUtil.getMonthLastDay(selectYear, selectMonth);
          loopDay.setArrayList(numToList(1, maxDayOfMonth,
              context.getResources().getString(R.string.date_picker_day)));
        }
      } else {
        if (selectYear == minYear && selectMonth == minMonth) {
          int maxDayOfMonth = DatePiackerUtil.getMonthLastDay(minYear, minMonth);
          loopDay.setArrayList(numToList(minDay, maxDayOfMonth - minDay + 1,
              context.getResources().getString(R.string.date_picker_day)));
        } else if (selectYear == maxYear && selectMonth == maxMonth) {
          loopDay.setArrayList(
              numToList(1, maxDay, context.getResources().getString(R.string.date_picker_day)));
        } else {
          int maxDayOfMonth = DatePiackerUtil.getMonthLastDay(selectYear, selectMonth);
          loopDay.setArrayList(numToList(1, maxDayOfMonth,
              context.getResources().getString(R.string.date_picker_day)));
        }
      }
      if (selectDay != -1) {
        loopDay.setCurrentItem(selectDay);
      } else {
        loopDay.setCurrentItem(c.get(Calendar.DATE));
      }
      loopDay.setNotLoop();

      setLoopListener(loopDay, loopYear, loopMonth);

      setOnClickListener(dialog, view);
      setDialogToBottom(dialog);

      dialog.setContentView(view);
      dialog.setCanceledOnTouchOutside(params.canCancel);
      dialog.setCancelable(params.canCancel);

      params.loopYear = loopYear;
      params.loopMonth = loopMonth;
      params.loopDay = loopDay;
      dialog.setParams(params);
      // type:0 表示天，1表示月， 2表示年
      if (type == 1) {
        loopDay.setVisibility(View.GONE);
      } else if (type == 2) {
        loopMonth.setVisibility(View.GONE);
        loopDay.setVisibility(View.GONE);
      }
      return dialog;
    }

    /**
     * 设置点击事件
     *
     * @param dialog
     * @param view
     */
    private void setOnClickListener(final DatePicker dialog, View view) {
      view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          dialog.dismiss();
          params.callback.onDateSelected(getCurrDateValues());
        }
      });
      view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          dialog.dismiss();
          params.callback.onCancel();
        }
      });
    }

    /**
     * 设置dialog在底部
     *
     * @param dialog
     */
    private void setDialogToBottom(DatePicker dialog) {
      Window win = dialog.getWindow();
      win.getDecorView().setPadding(0, 0, 0, 0);
      WindowManager.LayoutParams lp = win.getAttributes();
      lp.width = WindowManager.LayoutParams.MATCH_PARENT;
      lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
      win.setAttributes(lp);
      win.setGravity(Gravity.BOTTOM);
      win.setWindowAnimations(R.style.theme_animation_bottom_rising);
    }

    private OnItemSelectedListener monthLoopListener;
    private OnItemSelectedListener dayLoopListener;

    /**
     * 设置LoopListener的监听
     * 
     * @param loopDay
     * @param loopYear
     * @param loopMonth
     */
    private void setLoopListener(final LoopView loopDay, final LoopView loopYear,
        final LoopView loopMonth) {
      final OnItemSelectedListener yearSyncListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(int item) {
          if (maxYear - MIN_YEAR <= 0) {
            return;
          }
          if (minYear != -1 && maxYear != -1) {
            // 这里选择的年份等于最小的年份
            if (Integer.parseInt(loopYear.getCurrentItemValue()) < minYear) {

              loopYear.setCurrentItem(minYear);

            } else if (Integer.parseInt(loopYear.getCurrentItemValue()) == minYear) {

              List<String> monthList = numToList(minMonth, 12 - minMonth + 1,
                  context.getResources().getString(R.string.date_picker_month));
              loopMonth.setArrayList(monthList);

              // 如果选择的月份小于最小的月份
              if (minMonth != -1) {
                loopMonth.setCurrentPosition(0);
                loopMonth.postDelayed(new Runnable() {
                  @Override
                  public void run() {
                    monthLoopListener.onItemSelected(0);
                  }
                }, 0);

              }

            } else if (Integer.parseInt(loopYear.getCurrentItemValue()) == maxYear) {

              List<String> monthList = numToList(1, maxMonth,
                  context.getResources().getString(R.string.date_picker_month));
              loopMonth.setArrayList(monthList);

              if (maxMonth != -1) {
                loopMonth.setCurrentPosition(0);
                loopMonth.postDelayed(new Runnable() {
                  @Override
                  public void run() {
                    monthLoopListener.onItemSelected(0);
                  }
                }, 0);

              }

            } else if (Integer.parseInt(loopYear.getCurrentItemValue()) > maxYear) {

              loopYear.setCurrentItem(maxYear);

            } else {
              // 如果选择的月份在最大最小之间，就需要改变月份的时间
              List<String> monthList = numToList(1, 12,
                  context.getResources().getString(R.string.date_picker_month));
              loopMonth.setArrayList(monthList);
              loopMonth.setCurrentPosition(0);
              loopMonth.postDelayed(new Runnable() {
                @Override
                public void run() {
                  monthLoopListener.onItemSelected(0);
                }
              }, 0);
            }
          }
        }
      };

      monthLoopListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(int item) {

          // 如果等于最大的年份和月份
          if (Integer.parseInt(loopYear.getCurrentItemValue()) == maxYear
              && Integer.parseInt(loopMonth.getCurrentItemValue()) == maxMonth) {
            loopDay.setArrayList(
                numToList(1, maxDay, context.getResources().getString(R.string.date_picker_day)));
            loopDay.setCurrentPosition(0);

          } else if (Integer.parseInt(loopYear.getCurrentItemValue()) == minYear
              && Integer.parseInt(loopMonth.getCurrentItemValue()) == minMonth) {

            int maxDayOfMonth = DatePiackerUtil.getMonthLastDay(minYear, minMonth);// c.get(Calendar.DATE);
            loopDay.setArrayList(numToList(minDay, maxDayOfMonth - minDay + 1,
                context.getResources().getString(R.string.date_picker_day)));
            loopDay.setCurrentPosition(0);

          } else {
            int maxDayOfMonth = DatePiackerUtil.getMonthLastDay(
                Integer.parseInt(loopYear.getCurrentItemValue()),
                Integer.parseInt(loopMonth.getCurrentItemValue()));// c.get(Calendar.DATE);
            int fixedCurr = Integer.parseInt(loopDay.getCurrentItemValue());
            loopDay.setArrayList(numToList(1, maxDayOfMonth,
                context.getResources().getString(R.string.date_picker_day)));
            // 修正被选中的日期最大值
            if (fixedCurr > maxDayOfMonth)
              fixedCurr = maxDayOfMonth - 1;
            loopDay.setCurrentPosition(0);
          }
        }
      };
      dayLoopListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(int item) {
          // 如果比最小的日期还要小的话， 那么就等于最小的日期
          if (minYear != -1 && minMonth != -1 && minDay != -1
              && Integer.parseInt(loopYear.getCurrentItemValue()) == minYear
              && Integer.parseInt(loopMonth.getCurrentItemValue()) == minMonth
              && Integer.parseInt(loopDay.getCurrentItemValue()) < minDay) {
            loopDay.setCurrentItem(minDay);// -1
          }
          // 如果比最大的日期还要大的话， 那么就等于最大的日期
          if (maxYear != -1 && maxMonth != -1 && maxDay != -1
              && Integer.parseInt(loopYear.getCurrentItemValue()) == maxYear
              && Integer.parseInt(loopMonth.getCurrentItemValue()) == maxMonth
              && Integer.parseInt(loopDay.getCurrentItemValue()) > maxDay) {
            loopDay.setCurrentItem(maxDay);// -1
          }
        }
      };
      loopYear.setListener(yearSyncListener);
      loopMonth.setListener(monthLoopListener);
      // loopDay.setListener(dayLoopListener);
    }

    /**
     * 将数字传化为集合，并且补充0
     *
     * @param startNum
     *          数字起点 1 8
     * @param count
     *          数字个数 12 2
     * @param unit
     *          单位
     * @return
     */
    private static List<String> numToList(int startNum, int count, String unit) {
      List<String> list = new ArrayList<String>();
      for (int i = startNum; i < startNum + count; i++) {
        String tempValue = i + unit;
        list.add(tempValue);
      }
      return list;
    }

  }
}
