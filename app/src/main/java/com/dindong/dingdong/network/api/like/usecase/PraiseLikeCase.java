package com.dindong.dingdong.network.api.like.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.like.LikeServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/6.
 * <p>
 * </>
 */

public class PraiseLikeCase extends BaseUseCase<LikeServiceApi> {

  private String entityId;

  public PraiseLikeCase(String entityId) {
    this.entityId = entityId;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().praiseLike(entityId);
  }
}
