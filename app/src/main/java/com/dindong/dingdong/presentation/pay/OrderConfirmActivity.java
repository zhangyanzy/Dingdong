package com.dindong.dingdong.presentation.pay;

import java.math.BigDecimal;
import java.util.UUID;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityOrderConfirmBinding;
import com.dindong.dingdong.manager.pay.PayCallback;
import com.dindong.dingdong.manager.pay.PayManager;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.pay.usecase.GroupBuyCase;
import com.dindong.dingdong.network.api.pay.usecase.PreSubmitOrderCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.pay.Order;
import com.dindong.dingdong.network.bean.pay.OrderType;
import com.dindong.dingdong.network.bean.pay.PayMode;
import com.dindong.dingdong.presentation.main.MainActivity;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.CountView;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.sweetAlert.SweetAlertDialog;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

/**
 * 确认订单
 */
public class OrderConfirmActivity extends BaseActivity {

  ActivityOrderConfirmBinding binding;

  private View lastTab;
  private int tabIndex = 0;// 0-微信，1-支付宝

  private Order order;

  private SweetAlertDialog dialog;

  private boolean isGroup;// 是否为团购，仅支持课程

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_order_confirm);

    dialog = DialogUtil.getProgressDialog(this);
    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    binding.cv.setMaxValue(BigDecimal.valueOf(99));
    lastTab = binding.tabWex;
    tabSwitch(lastTab, tabIndex);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    order = (Order) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
    binding.txtShopName.setText(getIntent().getStringExtra("shop"));
    isGroup = getIntent().getBooleanExtra("isGroup", false);
    binding.setUnit(getIntent().getStringExtra("unit"));
    binding.setIsGroup(isGroup);
    binding.setOrder(order);

    initLabel(order.getItemType());
  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
    binding.nb
        .setNavigationTopBarClickListener(new NavigationTopBar.NavigationTopBarClickListener() {
          @Override
          public void leftImageClick() {
            finish();
          }
        });
    binding.cv.setOnCountChangeListener(CountView.BUY_ALL, new CountView.OnCountChangeListener() {
      @Override
      public void onCountChange(BigDecimal count) {
        // 数量变化时，更新总金额
        BigDecimal totalAmount = count.multiply(order.getDisPrice());
        binding.txtTotalAmount.setText(StringUtil.amount(totalAmount));
        binding.txtPayAmount
            .setText(StringUtil.format(getString(R.string.global_amount_format), totalAmount));
        if (binding.cv.getIvAdd() != null)
          binding.cv.getIvAdd().setImageResource(
              count.compareTo(BigDecimal.valueOf(99)) >= 0 ? R.mipmap.btn_add_disable
                  : R.mipmap.btn_add);
      }
    });

  }

  /**
   * 根据订单不同类型，加载左侧不同label
   */
  private void initLabel(String itemType) {
    if (itemType.equals(OrderType.goods.toString())) {
      binding.txtLabelName.setText("商品");
      binding.txtLabelAmount.setText("商品价格");
    } else if (itemType.equals(OrderType.activity.toString())) {
      binding.txtLabelName.setText("活动");
      binding.txtLabelAmount.setText("活动价格");
    }
  }

  private void tabSwitch(View currentView, int tabIndex) {
    this.tabIndex = tabIndex;
    lastTab.setSelected(false);
    lastTab = currentView;
    lastTab.setSelected(true);
  }

  /**
   * 预下单
   */
  private void preSubmit() {
    order.setQty(binding.cv.getCount());
    order.setRealAmount(order.getDisPrice().multiply(binding.cv.getCount()));
    if (IsEmpty.string(order.getId()) || order.getQty().compareTo(binding.cv.getCount()) != 0) {
      // 防止由于网络错误，创建新订单
      order.setId(UUID.randomUUID().toString());
      if (dialog != null)
        dialog.show();
      if (isGroup) {
        new GroupBuyCase(order).execute(orderHttpSubscriber);
      } else
        // 正常商品则下单购买
        new PreSubmitOrderCase(order).execute(orderHttpSubscriber);
    } else {
      // 如果已经创建订单，则直接去支付
      pay(order);
    }

  }

  /**
   * 统一下单回调
   */
  private HttpSubscriber<Order> orderHttpSubscriber = new HttpSubscriber<Order>() {
    @Override
    public void onFailure(String errorMsg, Response<Order> response) {
      if (dialog != null)
        dialog.dismiss();
      DialogUtil.getErrorDialog(OrderConfirmActivity.this, errorMsg).show();
      // 请求失败，则清空订单，重新创建新订单
      order = null;
    }

    @Override
    public void onSuccess(Response<Order> response) {
      if (dialog != null)
        dialog.dismiss();
      order = response.getData();
      ToastUtil.toastSuccess(OrderConfirmActivity.this, "下单成功");
      pay(order);// 下单成功后，去支付
    }
  };

  private void pay(Order order) {
    new PayManager(OrderConfirmActivity.this, tabIndex == 0 ? PayMode.weiXin : PayMode.aliPay)
        .setOrder(order).setCallback(new PayCallback.Callback() {
          @Override
          public void onPaySuccess() {
            ToastUtil.toastSuccess(OrderConfirmActivity.this, "支付成功");

            // 支付成功，跳转到订单列表
            Intent intent = new Intent(OrderConfirmActivity.this, MainActivity.class);
            intent.putExtra("position", MainActivity.TAB_POSITION_MINE);
            startActivity(intent);

            Intent intent2 = new Intent(OrderConfirmActivity.this, OrderListActivity.class);
            intent2.putExtra(AppConfig.IntentKey.DATA, OrderListActivity.TYPE_USE);
            startActivity(intent2);
          }

          @Override
          public void onPayFailure() {
            ToastUtil.toastFailure(OrderConfirmActivity.this, "支付失败");
          }
        }).pay();

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  public class Presenter {

    /**
     * 切换支付方式
     * 
     * @param view
     * @param index
     */
    public void onPaymentSelect(View view, int index) {
      tabSwitch(view, index);
    }

    public void onPay(View view) {
      preSubmit();
    }
  }
}
