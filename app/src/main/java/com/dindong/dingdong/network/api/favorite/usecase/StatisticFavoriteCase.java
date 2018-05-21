package com.dindong.dingdong.network.api.favorite.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.favorite.FavoriteServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/22.
 * <p>
 * </>
 */

public class StatisticFavoriteCase extends BaseUseCase<FavoriteServiceApi> {
  @Override
  protected Observable buildCase() {
    return createConnection().statisticFavorite();
  }
}
