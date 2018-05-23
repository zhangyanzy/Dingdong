package com.dindong.dingdong.network.api.pay.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.pay.OrderServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/23.
 * <p>
 * </>
 */

public class StatisticOrderCase extends BaseUseCase<OrderServiceApi> {
  @Override
  protected Observable buildCase() {
    return createConnection().statisticOrder();
  }
}
