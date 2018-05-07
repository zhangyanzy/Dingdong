package com.dindong.dingdong.network.api.comment;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.network.bean.discovery.Discovery;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wcong on 2018/5/6.
 * <p>
 * </>
 */

public interface CommentServiceApi {
  /**
   * 发表评论
   * 
   * @param comment
   * @return
   */
  @POST("comments")
  Observable<Response<Comment>> comment(
      @Body Comment comment);
}
