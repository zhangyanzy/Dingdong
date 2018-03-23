package com.dindong.dingdong.widget;

import com.dindong.dingdong.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by wcong on 2018/3/24.
 * <p>
 * 验证码倒计时组件</>
 */

@SuppressLint("AppCompatCustomView")
public class CountTimeTextView extends TextView {

  private long totalTime = 60000;
  private CountDownTimer timer;
  private boolean isCount;

  public CountTimeTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setGravity(Gravity.CENTER);

    initView();
  }

  private void initView() {
    setBackgroundResource(R.drawable.bg_count_timer);
    setTextColor(Color.parseColor("#FFFF9C09"));
    setText(getContext().getString(R.string.send_text));
    setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
  }

  public void startCount() {
    this.startCount(totalTime);
  }

  public void startCount(long totalTime) {
    this.totalTime = totalTime;
    long tickTime = 1000;
    setEnabled(false);
    isCount = true;
    timer = new CountDownTimer(totalTime, tickTime) {
      @Override
      public void onTick(long millisUntilFinished) {
        setText(getResources().getString(R.string.send_code_during) + "("
            + millisUntilFinished / 1000 + getResources().getString(R.string.send_code_time) + ")");
      }

      @Override
      public void onFinish() {
        isCount = false;
        setEnabled(true);
        setText(getResources().getString(R.string.send_code));
      }
    }.start();
  }

  public void finishCount() {
    if (timer != null) {
      timer.cancel();
      timer.onFinish();
    }
    // setEnabled(true);
  }

  public void setTotalTime(long totalTime) {
    this.totalTime = totalTime;
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    finishCount();
  }

  @Override
  public void setEnabled(boolean enabled) {
    if (!isCount) {
      super.setEnabled(enabled);
    }
  }
}
