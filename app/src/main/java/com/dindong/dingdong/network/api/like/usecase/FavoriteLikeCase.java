package com.dindong.dingdong.network.api.like.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.like.LikeServiceApi;
import com.dindong.dingdong.network.bean.like.LikeEntityType;

import rx.Observable;

/**
 * Created by wcong on 2018/5/21.
 * <p>
 * </>
 */

public class FavoriteLikeCase extends BaseUseCase<LikeServiceApi> {
  private LikeEntityType likeEntityType;
  private String entityId;

  public FavoriteLikeCase(LikeEntityType likeEntityType, String entityId) {
    this.likeEntityType = likeEntityType;
    this.entityId = entityId;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().favoriteLike(likeEntityType, entityId);
  }
}
