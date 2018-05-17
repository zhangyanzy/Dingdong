package com.dindong.dingdong.wxapi;

import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.manager.PayCallback;
import com.dindong.dingdong.util.IsEmpty;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

  private static final String TAG = "WXPayEntryActivity";
  private IWXAPI api;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // setContentView(R.layout.pay_results);
    api = WXAPIFactory.createWXAPI(this, AppConfig.PartyKey.WEX_PAY);
    api.handleIntent(getIntent(), this);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
  }

  @Override
  public void onReq(BaseReq req) {
  }

  @Override
  public void onResp(BaseResp resp) {
    if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
      // resp.errCode == -1
      // 原因：支付错误,可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
      // resp.errCode == -2 原因 用户取消,无需处理。发生场景：用户不支付了，点击取消，返回APP
      if (resp.errCode == 0) // 支付成功
      {
        PayCallback.requestSuccess();
        Log.i(TAG, "支付成功");
      } else {
        PayCallback.requestFailure();
        Log.e(TAG, "支付失败" + resp.errCode);
        if (!IsEmpty.string(resp.errStr))
          Log.e(TAG, resp.errStr);
        if (!IsEmpty.string(resp.transaction))
          Log.e(TAG, resp.transaction);
      }

      finish();
    }
  }
}