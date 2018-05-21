package com.dindong.dingdong.network.api.like;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.like.LikeEntity;
import com.dindong.dingdong.network.bean.like.LikeEntityType;
import com.dindong.dingdong.network.bean.like.LikeType;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by wcong on 2018/5/6.
 * <p>
 * 点赞/收藏/关注 </>
 */

public interface LikeServiceApi {
  /**
   * 收藏
   * 
   * @param entityType
   *          收藏类型
   * @param entityId
   *          收藏的关联ID
   * @return
   */
  @POST("like/favorite/{entityType}/{entityId}")
  Observable<Response<Void>> favoriteLike(@Path("entityType") LikeEntityType entityType,
      @Path("entityId") String entityId);

  /**
   * 取消收藏
   * 
   * @param entityType
   * @param entityId
   * @return
   */
  @DELETE("like/favorite/{entityType}/{entityId}")
  Observable<Response<Void>> cancelFavoriteLike(@Path("entityType") LikeEntityType entityType,
      @Path("entityId") String entityId);

  /**
   * 关注
   * 
   * @param entityType
   * @param entityId
   * @return
   */
  @POST("like/follow/{entityType}/{entityId}")
  Observable<Response<Void>> followLike(@Path("entityType") LikeEntityType entityType,
      @Path("entityId") String entityId);

  /**
   * 取消关注
   * 
   * @param entityType
   * @param entityId
   * @return
   */
  @DELETE("like/follow/{entityType}/{entityId}")
  Observable<Response<Void>> cancelFollowLike(@Path("entityType") LikeEntityType entityType,
      @Path("entityId") String entityId);

  /**
   * 点赞
   * 
   * @param entityId
   * @return
   */
  @POST("like/praise/{entityId}")
  Observable<Response<Void>> praiseLike(@Path("entityId") String entityId);

  /**
   * 取消点赞
   * 
   * @param entityId
   * @return
   */
  @DELETE("like/praise/{entityId}")
  Observable<Response<Void>> cancelPraiseLike(@Path("entityId") String entityId);

  /**
   * 获取点赞/关注/收藏用户列表
   * 
   * @param likeType
   * @param entityId
   * @return
   */
  @GET("like/list/{likeType}/{entityId}")
  Observable<Response<List<LikeEntity>>> listLike(@Path("likeType") LikeType likeType,
      @Path("entityId") String entityId);
}
