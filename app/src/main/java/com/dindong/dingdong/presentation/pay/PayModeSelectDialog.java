package com.dindong.dingdong.presentation.pay;

import com.dindong.dingdong.R;
import com.dindong.dingdong.databinding.LayoutPayModeSelectBinding;
import com.dindong.dingdong.manager.pay.PayCallback;
import com.dindong.dingdong.manager.pay.PayManager;
import com.dindong.dingdong.network.bean.pay.Order;
import com.dindong.dingdong.network.bean.pay.PayMode;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by wcong on 2018/5/19.
 * <p>
 * </>
 */

public class PayModeSelectDialog extends Dialog {

  LayoutPayModeSelectBinding binding;

  private Order order;

  private PayCallback.Callback payCallback;
  private Context context;

  public PayModeSelectDialog(Context context, Order order) {
    super(context, R.style.FullScreenDialog);
    this.order = order;
    this.context = context;
    setCanceledOnTouchOutside(true);
    binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
        R.layout.layout_pay_mode_select, null, false);
    binding.setPresenter(new Presenter());
    setContentView(binding.getRoot());
  }

  public PayModeSelectDialog setPayCallback(PayCallback.Callback payCallback) {
    this.payCallback = payCallback;
    return this;
  }

  @Override
  public void show() {
    super.show();
    Window win = getWindow();
    win.getDecorView().setPadding(0, 0, 0, 0);
    WindowManager.LayoutParams lp = win.getAttributes();
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    win.setAttributes(lp);
    win.setGravity(Gravity.BOTTOM);
    win.setWindowAnimations(R.style.theme_animation_bottom_rising);
  }

  public class Presenter {
    public void onCancel() {
      dismiss();
    }

    public void pay(PayMode payMode) {
      dismiss();
      new PayManager(context, payMode).setOrder(order).setCallback(payCallback).pay();
    }
  }
}
