package com.dindong.dingdong.network.api.like.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.like.LikeServiceApi;
import com.dindong.dingdong.network.bean.like.LikeEntityType;

import rx.Observable;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * </>
 */

public class FollowLikeCase extends BaseUseCase<LikeServiceApi> {
  private LikeEntityType likeEntityType;
  private String entityId;

  public FollowLikeCase(LikeEntityType likeEntityType, String entityId) {
    this.likeEntityType = likeEntityType;
    this.entityId = entityId;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().followLike(likeEntityType, entityId);
  }
}
