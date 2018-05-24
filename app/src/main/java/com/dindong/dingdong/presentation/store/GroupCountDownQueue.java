package com.dindong.dingdong.presentation.store;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dindong.dingdong.util.DateUtil;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by wangcong on 2018/5/24.
 * <p>
 */

public class GroupCountDownQueue {
  private static GroupCountDownQueue instance;

  private CountDownTimer timer;

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
      Log.e(this.getClass().getSimpleName(), textView.toString() + "活动已结束");
      return;
    }

    // 创建一个定时器，每100ms更新一次时间
    CountDownTimer countDownTimer = new CountDownTimer(endDate.getTime() - new Date().getTime(),
        100) {
      @Override
      public void onTick(long millisUntilFinished) {
        textView.setText(
            DateUtil.format(new Date(millisUntilFinished), DateUtil.DEFAULT_DATE_FORMAT_10));
      }

      @Override
      public void onFinish() {
        Log.e(this.getClass().getSimpleName(), textView.toString() + "活动已结束");
      }
    };
    timerQueue.add(countDownTimer);
    countDownTimer.start();
  }

}
