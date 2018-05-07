package com.dindong.dingdong.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by wcong on 2018/5/6.
 * <p>
 * </>
 */

public class KeyboardUtil {

  /**
   * 主动显示/隐藏软键盘
   * 
   * @param view
   * @param isShow
   */
  public static void control(View view, boolean isShow) {
    InputMethodManager imm = (InputMethodManager) view.getContext()
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    if (isShow) {
      // 强制显示软键盘
      imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    } else {
      // 强制取消软键盘
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  /**
   * 监听软键盘显示/隐藏
   * 
   * @param view
   * @param activity
   * @param onKeyboardVisibleListener
   */
  public static void setKeyboardVisibleListener(View view, final Activity activity,
      final OnKeyboardVisibleListener onKeyboardVisibleListener) {
    view.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

          @Override
          public void onGlobalLayout() {
            final Rect rect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            final int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
            final int heightDifference = screenHeight - rect.bottom;
            if (onKeyboardVisibleListener != null)
              onKeyboardVisibleListener.onKeyboardVisible(heightDifference > screenHeight / 3);
          }
        });

  }

  public interface OnKeyboardVisibleListener {
    void onKeyboardVisible(boolean visible);
  }
}
