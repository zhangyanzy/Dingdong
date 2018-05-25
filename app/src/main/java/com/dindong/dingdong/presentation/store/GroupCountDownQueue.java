package com.dindong.dingdong.presentation.store;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by wangcong on 2018/5/24.
 * <p>
 */

public class GroupCountDownQueue {
  private static GroupCountDownQueue instance;

  private List<CountDownTimer> timerQueue;

  public static GroupCountDownQueue getInstance() {
    if (instance == null)
      return instance = new GroupCountDownQueue();
    return instance;
  }

  public GroupCountDownQueue() {
    if (timerQueue == null)
      timerQueue = new ArrayList<>();
  }

  /**
   * 绑定textView，为其创建一个新线程去更新倒计时并放入队列里
   *
   * @param textView
   * @param endDate
   */
  public void attach(final TextView textView, Date endDate) {
    if (textView == null)
      return;
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(endDate);
    calendar.set(Calendar.MILLISECOND, 0);
    endDate = calendar.getTime();

    if (new Date().compareTo(endDate) >= 0) {
      // 当前时间大于等于结束时间，则为活动已结束
      Log.e(this.getClass().getSimpleName(), textView.toString() + "已结束");
      return;
    }

    // 创建一个定时器，每100ms更新一次时间
    CountDownTimer countDownTimer = new CountDownTimer(endDate.getTime() - new Date().getTime(),
        100) {
      @Override
      public void onTick(long millisUntilFinished) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.S");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(millisUntilFinished);
        if (hms.indexOf(".") > 0 && hms.length() - hms.indexOf(".") > 1) {
          hms = hms.substring(0, hms.length() - (hms.length() - hms.indexOf(".") - 2));
        }

        Log.e(this.getClass().getSimpleName(), textView.toString() + hms);
        textView.setText(hms);
      }

      @Override
      public void onFinish() {
        textView.setText("已结束");
        Log.e(this.getClass().getSimpleName(), textView.toString() + "已结束");
      }
    };
    timerQueue.add(countDownTimer);
    countDownTimer.start();
  }

  /**
   * 停止队列里所有线程，此方法必须在activity销毁时调用
   */
  public void cancel() {
    if (timerQueue == null)
      return;
    for (CountDownTimer timer : timerQueue)
      if (timer != null)
        timer.cancel();
    timerQueue = new ArrayList<>();
  }

}
