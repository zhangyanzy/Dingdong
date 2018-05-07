package com.dindong.dingdong.network.api.moment.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.moment.MomentServiceApi;
import com.dindong.dingdong.network.bean.comment.Comment;

import rx.Observable;

/**
 * Created by wcong on 2018/5/6.
 * <p>
 * </>
 */

public class CommentMomentCase extends BaseUseCase<MomentServiceApi> {
  private Comment comment;

  public CommentMomentCase(Comment comment) {
    this.comment = comment;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().commentMoment(comment);
  }
}
