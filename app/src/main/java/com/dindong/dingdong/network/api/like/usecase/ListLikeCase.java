package com.dindong.dingdong.network.api.like.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.like.LikeServiceApi;
import com.dindong.dingdong.network.bean.like.LikeType;

import rx.Observable;

/**
 * Created by wcong on 2018/5/17.
 * <p>
 * </>
 */

public class ListLikeCase extends BaseUseCase<LikeServiceApi> {
  private LikeType likeType;
  private String id;

  public ListLikeCase(LikeType likeType, String id) {
    this.likeType = likeType;
    this.id = id;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().listLike(likeType, id);
  }
}
