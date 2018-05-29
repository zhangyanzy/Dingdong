package com.dindong.dingdong.network.api.alipay.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.alipay.AliPayServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/29.
 * <p>
 * </>
 */

public class AliPayTraceapPayCase extends BaseUseCase<AliPayServiceApi> {
  private String orderId;

  public AliPayTraceapPayCase(String orderId) {
    this.orderId = orderId;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().traceapPay(orderId);
  }
}
