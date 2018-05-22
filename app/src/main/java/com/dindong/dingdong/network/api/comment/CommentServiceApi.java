package com.dindong.dingdong.network.api.comment;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
   * 
   * @param queryParam
   *          relationId，关联id，适用查询所有评论<br/>
   *          storeId，门店id，只适用查询门店评论<br/>
   *          orderId，订单id，只适用查询订单评论<br/>
   * @return
   */
  @POST("comments/list")
  Observable<Response<List<Comment>>> listComment(@Body QueryParam queryParam);

  /**
   * 统计指定动态的数据
   * 
   * @param id
   * @return
   */
  @GET("comments/statistics/{id}")
  Observable<Response<Comment>> statistic(@Path("id") String id);

  /**
   * 获取指定评论
   * 
   * @param id
   * @return
   */
  @GET("comments/{id}")
  Observable<Response<Comment>> get(@Path("id") String id);
}
