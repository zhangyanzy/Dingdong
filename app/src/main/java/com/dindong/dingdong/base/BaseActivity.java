package com.dindong.dingdong.base;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by wcong on 2018/3/9.
 */

public abstract class BaseActivity extends RxAppCompatActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    initComponent();
    loadData(savedInstanceState);
    createEventHandlers();
    // EventBus.getDefault().register(this);
  }

  /**
   * 初始化界面控件
   */
  protected abstract void initComponent();

  /**
   * 初次加载数据
   */
  protected abstract void loadData(Bundle savedInstanceState);

  /**
   * 判断界面表单数据填写是否有效
   */
  protected boolean isValid() {
    return true;
  }

  /**
   * 界面事件响应
   */
  protected void createEventHandlers() {
  }

  /**
   * 是否自动隐藏软键盘{@link this#dispatchTouchEvent(MotionEvent)}
   *
   * @return
   */
  protected boolean hideInputOut() {
    return true;
  }

//  @Subscribe(threadMode = ThreadMode.MAIN)
//  public void handleTokenExpired(AuthEvent event) {
//    // 如果删除成功，就将详情页设置为前一页
//    if (event.type == AuthEvent.TOKEN_EXPIRED) {
//      ToastUtil.toastHint(DposApp.getInstance(), ApiClient.TOKEN_EXPIRED);
//      ShortcutMgr.logout();
//    } else if (event.type == AuthEvent.SHOP_LOCKED) {
//      ShopUtil.showLocked(this);
//    } else if (event.type == AuthEvent.SHOP_SUSPEND) {
//      Intent intent = new Intent(this, ShopSwitchActivity.class);
//      intent.putExtra(ShopSwitchActivity.EXTRA_LOGOUT, true);
//      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//      DposApp.getInstance().startActivity(intent);
//    }
//  }

  /**
   * 点击空白处隐藏软键盘
   *
   * @param ev
   * @return
   */
  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    if (ev.getAction() == MotionEvent.ACTION_DOWN) {
      if (!hideInputOut())
        return super.dispatchTouchEvent(ev);
      View v = getCurrentFocus();
      if (isEdt(v, ev)) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(v.getWindowToken(), 0);
      }
      return super.dispatchTouchEvent(ev);
    }
    if (getWindow().superDispatchTouchEvent(ev)) {
      return true;
    }
    return onTouchEvent(ev);
  }

  /**
   * 判断当前焦点是否是输入框
   *
   * @param v
   * @param event
   * @return
   */
  public boolean isEdt(View v, MotionEvent event) {
    if (v != null && (v instanceof EditText)) {
      int[] leftTop = {
          0, 0 };
      v.getLocationInWindow(leftTop);
      int left = leftTop[0];
      int top = leftTop[1];
      int bottom = top + v.getHeight();
      int right = left + v.getWidth();
      if (event.getX() > left && event.getX() < right && event.getY() > top
          && event.getY() < bottom) {
        return false;
      } else {
        return true;
      }
    }
    return false;
  }

}
