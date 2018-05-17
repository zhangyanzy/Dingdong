package com.dindong.dingdong.network.api.pay.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.pay.OrderServiceApi;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import rx.Observable;

/**
 * Created by wcong on 2018/3/25.
 * <p>
 * </>
 */

public class ListOrderCase extends BaseUseCase<OrderServiceApi> {
  private QueryParam queryParam;

  public ListOrderCase(QueryParam queryParam) {
    this.queryParam = queryParam;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().listOrder(queryParam);
  }
}
