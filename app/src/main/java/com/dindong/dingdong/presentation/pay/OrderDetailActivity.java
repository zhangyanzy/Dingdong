package com.dindong.dingdong.presentation.pay;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityOrderDetailBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.pay.usecase.CancelOrderCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.pay.Order;
import com.dindong.dingdong.network.bean.pay.OrderType;
import com.dindong.dingdong.util.CuteR;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.PhotoUtil;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

public class OrderDetailActivity extends BaseActivity {

  ActivityOrderDetailBinding binding;

  private Order order;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    order = (Order) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
    binding.setOrder(order);
    initOrderView(order);
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
  }

  /**
   * 加载订单商品数据
   * 
   * @param order
   */
  private void initOrderView(Order order) {
    binding.imgCode.setImageBitmap(CuteR.ProductNormal(order.getCode(), false, 0));
    if (order.getOrderType().equals(OrderType.subject)) {
      binding.txtShopName.setText(order.getSubject().getStore().getName());
      binding.txtShopAddress.setText(order.getSubject().getStore().getAddress().toString());
      binding.txtRange.setText(StringUtil.format(getString(R.string.global_range),
          StringUtil.formatRange(order.getSubject().getStore().getRange())));
      PhotoUtil.load(this, order.getSubject().getImages().get(0).getUrl(), binding.img);
      binding.txtGoodName.setText(order.getSubject().getName());
      binding.txtAmount.setText(StringUtil.amount(order.getSubject().getAmount()));
    } else if (order.getOrderType().equals(OrderType.good)) {
      binding.txtShopName.setText(order.getShopGood().getStore().getName());
      binding.txtShopAddress.setText(order.getShopGood().getStore().getAddress().toString());
      binding.txtRange.setText(StringUtil.format(getString(R.string.global_range),
          StringUtil.formatRange(order.getShopGood().getStore().getRange())));
      PhotoUtil.load(this, order.getShopGood().getImages().get(0).getUrl(), binding.img);
      binding.txtGoodName.setText(order.getShopGood().getName());
      binding.txtAmount.setText(StringUtil.amount(order.getShopGood().getAmount()));
    }
  }

  public class Presenter {
    /**
     * 取消订单
     * 
     * @param order
     */
    public void onCancel(Order order) {
      new CancelOrderCase(order.getId())
          .execute(new HttpSubscriber<Void>(OrderDetailActivity.this) {
            @Override
            public void onFailure(String errorMsg, Response<Void> response) {
              DialogUtil.getErrorDialog(OrderDetailActivity.this, errorMsg).show();
            }

            @Override
            public void onSuccess(Response<Void> response) {
              // 通知上一界面刷新数据
              setResult(RESULT_OK);
              finish();
            }
          });

    }

    /**
     * 去支付
     * 
     * @param order
     */
    public void onPay(Order order) {
      // 通知上一界面刷新数据
      setResult(RESULT_OK);
      finish();
    }
  }
}
