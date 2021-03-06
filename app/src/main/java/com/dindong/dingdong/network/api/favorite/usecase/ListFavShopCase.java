package com.dindong.dingdong.network.api.favorite.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.favorite.FavoriteServiceApi;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import rx.Observable;

/**
 * Created by wcong on 2018/5/22.
 * <p>
 * </>
 */

public class ListFavShopCase extends BaseUseCase<FavoriteServiceApi> {
  private QueryParam queryParam;

  public ListFavShopCase(QueryParam queryParam) {
    this.queryParam = queryParam;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().listFavShop(queryParam);
  }
}
