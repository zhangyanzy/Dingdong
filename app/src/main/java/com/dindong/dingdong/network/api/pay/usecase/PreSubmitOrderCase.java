package com.dindong.dingdong.network.api.pay.usecase;

import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.pay.OrderServiceApi;
import com.dindong.dingdong.network.bean.pay.Order;

import rx.Observable;

/**
 * Created by wcong on 2018/3/24.
 * <p>
 * 订单预下单</>
 */

public class PreSubmitOrderCase extends BaseUseCase<OrderServiceApi> {
  private Order order;

  public PreSubmitOrderCase(Order order) {
    this.order = order;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().preSubmit(SessionMgr.getUser().getId(), order);
  }
}
