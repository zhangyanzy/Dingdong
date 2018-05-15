package com.dindong.dingdong.network.api.moment;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wcong on 2018/5/5.
 * <p>
 * 动态相关api </>
 */

public interface MomentServiceApi {
  /**
   * 发表动态/评论
   * 
   * @param comment
   * 
   * @return
   */
  @POST("moments")
  Observable<Response<Comment>> moment(@Body Comment comment);

  /**
   * 获取动态评论列表
   * 
   * @param queryParam
   *          relationId，动态关联关系ID<br/>
   * @return
   */
  @POST("moments/comment/list")
  Observable<Response<List<Comment>>> listComment(@Body QueryParam queryParam);

  /**
   * 获取动态列表
   * 
   * @param queryParam
   * @return
   */
  @POST("moments/list")
  Observable<Response<List<Comment>>> listMoment(@Body QueryParam queryParam);

  /**
   * 删除动态
   * 
   * @param id
   * @return
   */
  @DELETE("/moments/{id}")
  Observable<Response<Comment>> delete(@Query("id") String id);

  /**
   * 获取指定动态
   * 
   * @param id
   * @return
   */
  @GET("moments/{id}")
  Observable<Response<Comment>> get(@Path("id") String id);

  /**
   * 统计指定动态的数据
   * 
   * @param id
   * @return
   */
  @GET("moments/statistics/{id}")
  Observable<Response<Comment>> statistics(@Path("id") String id);
}
