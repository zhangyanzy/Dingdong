package com.dindong.dingdong.network.api.comment.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.comment.CommentServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/17.
 * <p>
 * </>
 */

public class GetCommentCase extends BaseUseCase<CommentServiceApi> {
  private String id;

  public GetCommentCase(String id) {
    this.id = id;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().get(id);
  }
}
