package com.dindong.dingdong.network.api.shop.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.shop.ShopServiceApi;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import rx.Observable;

/**
 * Created by wcong on 2018/3/18.
 * <p>
 * </>
 */

public class ListShopCase extends BaseUseCase<ShopServiceApi> {
  private QueryParam queryParam;

  public ListShopCase(QueryParam queryParam) {
    this.queryParam = queryParam;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().listShop(queryParam);
  }
}
