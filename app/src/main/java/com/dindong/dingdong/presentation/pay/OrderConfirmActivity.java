package com.dindong.dingdong.presentation.pay;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityOrderConfirmBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.pay.usecase.PreSubmitOrderCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.pay.Order;
import com.dindong.dingdong.network.bean.pay.OrderType;
import com.dindong.dingdong.network.bean.pay.PayMode;
import com.dindong.dingdong.network.bean.shop.Subject;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.widget.CountView;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

/**
 * 确认订单
 */
public class OrderConfirmActivity extends BaseActivity {

  ActivityOrderConfirmBinding binding;

  private Subject subject;

  private View lastTab;
  private int tabIndex = 0;// 0-微信，1-支付宝

  private Order order;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_order_confirm);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    binding.cv.setMaxValue(BigDecimal.valueOf(99));
    lastTab = binding.tabWex;
    tabSwitch(lastTab, tabIndex);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    subject = (Subject) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
    binding.setSubject(subject);
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
        BigDecimal totalAmount = count.multiply(subject.getAmount());
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

  private void tabSwitch(View currentView, int tabIndex) {
    this.tabIndex = tabIndex;
    lastTab.setSelected(false);
    lastTab = currentView;
    lastTab.setSelected(true);
  }

  /**
   * 创建订单
   * 
   * @return
   */
  private Order createOrder() {
    Order order = new Order();
    order.setId(UUID.randomUUID().toString());
    order.setDate(new Date());
    order.setOrderType(OrderType.subject);
    order.setPrice(subject.getAmount());
    order.setOriginalPrice(subject.getOriginalAmount());
    order.setCount(binding.cv.getCount());
    order.setTotalAmount(subject.getAmount().multiply(binding.cv.getCount()));
    order.setFavourAmount(BigDecimal.ZERO);
    order.setPayMode(tabIndex == 0 ? PayMode.weiXin : PayMode.aliPay);
    order.setPayAmount(order.getTotalAmount().subtract(order.getFavourAmount()));
    order.setSubject(subject);
    return order;
  }

  /**
   * 预下单
   */
  private void preSubmit() {
    if (order == null)
      // 防止由于网络错误，创建新订单
      order = createOrder();
    new PreSubmitOrderCase(order).execute(new HttpSubscriber<Order>() {
      @Override
      public void onFailure(String errorMsg, Response<Order> response) {
        DialogUtil.getErrorDialog(OrderConfirmActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<Order> response) {
        order = response.getData();
      }
    });
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
