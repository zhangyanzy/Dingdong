package com.dindong.dingdong.network.api.pay.usecase;

import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.pay.OrderServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/4/15.
 * <p>
 * 取消订单 </>
 */

public class CancelOrderCase extends BaseUseCase<OrderServiceApi> {
  private String orderId;

  public CancelOrderCase(String orderId) {
    this.orderId = orderId;
  }

  @Override
  protected Observable buildCase() {
    return createConnection(BaseUseCase.isMock).cancel(orderId);
  }
}
