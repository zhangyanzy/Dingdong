package com.dindong.dingdong.network.api.comment.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.comment.CommentServiceApi;
import com.dindong.dingdong.network.bean.comment.Comment;

import rx.Observable;

/**
 * Created by wcong on 2018/5/11.
 * <p>
 * </>
 */

public class CommentCase extends BaseUseCase<CommentServiceApi> {
  private Comment comment;

  public CommentCase(Comment comment) {
    this.comment = comment;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().comment(comment);
  }
}
