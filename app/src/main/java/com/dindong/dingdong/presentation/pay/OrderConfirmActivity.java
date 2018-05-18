package com.dindong.dingdong.presentation.pay;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityOrderConfirmBinding;
import com.dindong.dingdong.manager.PayCallback;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.pay.usecase.PreSubmitOrderCase;
import com.dindong.dingdong.network.api.wxpay.usecase.WxUnifiedOrderCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.pay.Order;
import com.dindong.dingdong.network.bean.pay.OrderType;
import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.network.bean.wxpay.WxUnifiedOrderResult;
import com.dindong.dingdong.presentation.main.MainActivity;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.CountView;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.content.Intent;
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
    order.setItemId(subject.getId());
    order.setItemImageUrl(subject.getImage().getUrl());
    order.setItemType(OrderType.course.toString());
    order.setDisPrice(subject.getAmount());
    order.setPrice(subject.getOriginalAmount());
    order.setQty(binding.cv.getCount());
    order.setRealAmount(subject.getAmount().multiply(binding.cv.getCount()));
    order.setUserId(SessionMgr.getUser().getId());
    // order.setPayMode(tabIndex == 0 ? PayMode.weiXin : PayMode.aliPay);
    return order;
  }

  /**
   * 预下单
   */
  private void preSubmit() {
    if (order == null)
      // 防止由于网络错误，创建新订单
      order = createOrder();
    new PreSubmitOrderCase(order).execute(new HttpSubscriber<Order>(this) {
      @Override
      public void onFailure(String errorMsg, Response<Order> response) {
        DialogUtil.getErrorDialog(OrderConfirmActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<Order> response) {
        order = response.getData();
        ToastUtil.toastSuccess(OrderConfirmActivity.this, "下单成功");
        new WxUnifiedOrderCase(order.getId())
            .execute(new HttpSubscriber<WxUnifiedOrderResult>(OrderConfirmActivity.this) {
              @Override
              public void onFailure(String errorMsg, Response<WxUnifiedOrderResult> response) {
                DialogUtil.getErrorDialog(OrderConfirmActivity.this, errorMsg).show();
              }

              @Override
              public void onSuccess(Response<WxUnifiedOrderResult> response) {
                PayCallback.register(new PayCallback.Callback() {
                  @Override
                  public void onPaySuccess() {
                    ToastUtil.toastSuccess(OrderConfirmActivity.this, "支付成功");
                    PayCallback.clean();

                    // 支付成功，跳转到订单列表
                    Intent intent = new Intent(OrderConfirmActivity.this, MainActivity.class);
                    intent
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(AppConfig.IntentKey.DATA,
                        SessionMgr.getUser().getIdentities().get(0));
                    intent.putExtra("position", MainActivity.TAB_POSITION_MINE);
                    startActivity(intent);

                    Intent intent2 = new Intent(OrderConfirmActivity.this, OrderListActivity.class);
                    intent2.putExtra(AppConfig.IntentKey.DATA, OrderListActivity.TYPE_FINISH);
                    startActivity(intent2);
                  }

                  @Override
                  public void onPayFailure() {
                    ToastUtil.toastFailure(OrderConfirmActivity.this, "支付失败");
                    PayCallback.clean();
                  }
                });

                IWXAPI api = WXAPIFactory.createWXAPI(OrderConfirmActivity.this,
                    AppConfig.PartyKey.WEX_PAY);
                PayReq request = new PayReq();
                request.appId = response.getData().getAppid();
                request.partnerId = response.getData().getPartnerid();
                request.prepayId = response.getData().getPrepayid();
                request.packageValue = response.getData().getPackage2();
                request.nonceStr = response.getData().getNoncestr();
                request.timeStamp = response.getData().getTimestamp();
                request.sign = response.getData().getSign();

                api.sendReq(request);
              }
            });
      }
    });
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
      if (tabIndex == 0)
        preSubmit();
      else if (tabIndex == 1)
        ToastUtil.toastHint(OrderConfirmActivity.this, "暂不支付微信支付");
    }
  }
}
