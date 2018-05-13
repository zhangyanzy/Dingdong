package com.dindong.dingdong.network.api.comment;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wcong on 2018/5/6.
 * <p>
 * 门店及其他评论/动态api </>
 */

public interface CommentServiceApi {
  /**
   * 发表评论
   * 
   * @param comment
   * @return
   */
  @POST("comments")
  Observable<Response<Comment>> comment(@Body Comment comment);

  /**
   * 获取评论的回复列表
   * 
   * @param queryParam
   * @return
   */
  @POST("comments/comment/list")
  Observable<Response<List<Comment>>> listCommentOfComment(@Body QueryParam queryParam);

  /**
   * 获取评论列表
   * @param queryParam
   * @return
   */
  @POST("comments/list")
  Observable<Response<List<Comment>>> listComment(@Body QueryParam queryParam);
}
