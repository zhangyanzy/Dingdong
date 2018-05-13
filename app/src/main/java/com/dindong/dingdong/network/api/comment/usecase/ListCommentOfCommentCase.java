package com.dindong.dingdong.network.api.comment.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.comment.CommentServiceApi;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import rx.Observable;

/**
 * Created by wcong on 2018/5/13.
 * <p>
 * </>
 */

public class ListCommentOfCommentCase extends BaseUseCase<CommentServiceApi> {
  private QueryParam queryParam;

  public ListCommentOfCommentCase(QueryParam queryParam) {
    this.queryParam = queryParam;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().listCommentOfComment(queryParam);
  }
}
