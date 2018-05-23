package com.dindong.dingdong.network.api.good.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.good.GoodServiceApi;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import rx.Observable;

/**
 * Created by wcong on 2018/5/24.
 * <p>
 * </>
 */

public class ListGoodsCase extends BaseUseCase<GoodServiceApi> {
  private QueryParam queryParam;

  public ListGoodsCase(QueryParam queryParam) {
    this.queryParam = queryParam;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().listGood(queryParam);
  }
}
