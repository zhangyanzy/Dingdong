package com.dindong.dingdong.presentation.pay;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityOrderDetailBinding;
import com.dindong.dingdong.manager.pay.PayCallback;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.pay.usecase.CancelOrderCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.pay.Order;
import com.dindong.dingdong.network.bean.pay.PayMode;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.presentation.store.ShopMapActivity;
import com.dindong.dingdong.util.CuteR;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.PhoneUtil;
import com.dindong.dingdong.util.PhotoUtil;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

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
    if (!IsEmpty.string(order.getCheckCode()))
      binding.imgCode.setImageBitmap(CuteR.ProductNormal(order.getCheckCode(), false, 0));
    binding.txtShopName.setText(order.getStore().getName());
    binding.txtShopAddress.setText(order.getStore().getAddress().toString());
    binding.txtRange.setText(StringUtil.format(getString(R.string.global_range),
        StringUtil.formatRange(order.getStore().getRange())));
    PhotoUtil.load(this, order.getItemImageUrl(), binding.img);
    binding.txtGoodName.setText(order.getItemName());
    binding.txtAmount.setText(StringUtil.amount(order.getDisPrice()));
    if (!IsEmpty.string(order.getPayMode())) {
      binding.layoutPayMode.setVisibility(View.VISIBLE);
      binding.txtPayMode.setText(PayMode.getName(order.getPayMode()));
    }
  }

  public class Presenter {

    /**
     * 地址详情
     *
     * @param shop
     */
    public void onAdd(Shop shop) {
      Intent intent = new Intent(OrderDetailActivity.this, ShopMapActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, shop);
      startActivity(intent);
    }

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
      new PayModeSelectDialog(OrderDetailActivity.this, order)
          .setPayCallback(new PayCallback.Callback() {
            @Override
            public void onPaySuccess() {
              ToastUtil.toastSuccess(OrderDetailActivity.this, "支付成功");
              Intent intent2 = new Intent(OrderDetailActivity.this, OrderListActivity.class);
              intent2.putExtra(AppConfig.IntentKey.DATA, OrderListActivity.TYPE_USE);
              startActivity(intent2);
              finish();
            }

            @Override
            public void onPayFailure() {
              ToastUtil.toastFailure(OrderDetailActivity.this, "支付失败");
            }
          }).show();

    }

    public void onMobile(final String mobile) {
      PhoneUtil.call(OrderDetailActivity.this, mobile);
    }
  }
}
