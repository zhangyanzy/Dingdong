package com.dindong.dingdong.network.api.comment.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.comment.CommentServiceApi;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import rx.Observable;

/**
 * Created by wcong on 2018/5/10.
 * <p>
 * </>
 */

public class ListCommentCase extends BaseUseCase<CommentServiceApi> {
  private QueryParam queryParam;

  public ListCommentCase(QueryParam queryParam) {
    this.queryParam = queryParam;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().listComment(queryParam);
  }
}
