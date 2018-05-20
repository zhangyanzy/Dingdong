package com.dindong.dingdong.network.api.pay.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.pay.OrderServiceApi;
import com.dindong.dingdong.network.bean.pay.Order;

import rx.Observable;

/**
 * Created by wcong on 2018/5/19.
 * <p>
 * </>
 */

public class GroupBuyCase extends BaseUseCase<OrderServiceApi> {
  private Order order;

  public GroupBuyCase(Order order) {
    this.order = order;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().groupBuy(order);
  }
}
