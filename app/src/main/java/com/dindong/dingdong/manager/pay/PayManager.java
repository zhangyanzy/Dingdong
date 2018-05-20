package com.dindong.dingdong.manager.pay;

import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.wxpay.usecase.WxUnifiedOrderCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.pay.Order;
import com.dindong.dingdong.network.bean.pay.PayMode;
import com.dindong.dingdong.network.bean.wxpay.WxUnifiedOrderResult;
import com.dindong.dingdong.util.DialogUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.content.Context;

/**
 * Created by wcong on 2018/5/19.
 * <p>
 * </>
 */

public class PayManager {
  private Context context;
  private PayMode payMode;// 支付方式
  private Order order;
  private PayCallback.Callback callback;// 支付回调

  public PayManager(Context context, PayMode payMode) {
    this.context = context;
    this.payMode = payMode;
  }

  public void pay() {
    if (payMode.equals(PayMode.weiXin))
      wexPay();
  }

  public PayManager setOrder(Order order) {
    this.order = order;
    return this;
  }

  public PayManager setCallback(PayCallback.Callback callback) {
    this.callback = callback;
    return this;
  }

  /**
   * 微信支付
   */
  private void wexPay() {
    if (order == null || context == null)
      return;
    new WxUnifiedOrderCase(order.getId())
        .execute(new HttpSubscriber<WxUnifiedOrderResult>(context) {
          @Override
          public void onFailure(String errorMsg, Response<WxUnifiedOrderResult> response) {
            DialogUtil.getErrorDialog(context, errorMsg).show();
          }

          @Override
          public void onSuccess(Response<WxUnifiedOrderResult> response) {
            PayCallback.register(callback);

            IWXAPI api = WXAPIFactory.createWXAPI(context, AppConfig.PartyKey.WEX_PAY);
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
}
