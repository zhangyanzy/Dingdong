package com.dindong.dingdong.network.api.shop.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.shop.ShopServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * </>
 */

public class GetShopCase extends BaseUseCase<ShopServiceApi> {
  private String shopId;

  public GetShopCase(String shopId) {
    this.shopId = shopId;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().get(shopId);
  }
}
