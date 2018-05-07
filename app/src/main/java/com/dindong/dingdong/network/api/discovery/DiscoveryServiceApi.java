package com.dindong.dingdong.network.api.discovery;

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
 * Created by wcong on 2018/4/14.
 * <p>
 * 发现相关api </>
 */

public interface DiscoveryServiceApi {
  /**
   * 获取发现列表
   * 
   * @param userId
   * @param param
   * @return
   */
  @POST("app/discovery/listDiscovery")
  Observable<Response<List<Discovery>>> listDiscovery(@Query("userId") String userId,
      @Body QueryParam param);

  /**
   * 发布评论
   * 
   * @param discoveryId
   * @param comment
   * @return
   */
  @POST("app/discovery/comment")
  Observable<Response> comment(@Query("discoveryId") String discoveryId, @Body Comment comment);

  /**
   * 点赞
   * 
   * @param discoveryId
   * @param userId
   * @return
   */
  @POST("app/discovery/praise")
  Observable<Response> praise(@Query("discoveryId") String discoveryId,
      @Query("userId") String userId);

}
