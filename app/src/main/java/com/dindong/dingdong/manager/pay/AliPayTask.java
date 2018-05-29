package com.dindong.dingdong.manager.pay;

import java.util.Map;

import com.alipay.sdk.app.PayTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by wcong on 2018/5/29.
 * <p>
 * 支付宝支付 </>
 */

public class AliPayTask extends AsyncTask<Void, Void, Map<String, String>> {
  private Activity activity;

  private String orderInfo;

  private PayCallback.Callback callback;// 支付回调

  public AliPayTask(Activity activity, String orderInfo, PayCallback.Callback callback) {
    this.activity = activity;
    this.orderInfo = orderInfo;
    this.callback = callback;
  }

  @Override
  protected Map<String, String> doInBackground(Void... voids) {
    PayTask alipay = new PayTask(activity);
    Map<String, String> result = alipay.payV2(orderInfo, true);
    return result;
  }

  @Override
  protected void onPostExecute(Map<String, String> stringStringMap) {
    super.onPostExecute(stringStringMap);

    AuthResult payResult = new AuthResult(stringStringMap, true);
    String resultInfo = payResult.getResult();
    Log.i(this.getClass().getSimpleName(), "AliPay:" + resultInfo);
    String resultStatus = payResult.getResultStatus();
    // 判断resultStatus 为9000则代表支付成功
    if (TextUtils.equals(resultStatus, "9000")) {
      if (callback != null)
        callback.onPaySuccess();
    } else {
      if (callback != null)
        callback.onPayFailure();
    }
  }
}
