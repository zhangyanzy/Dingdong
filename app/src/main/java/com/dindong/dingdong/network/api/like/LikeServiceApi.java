package com.dindong.dingdong.network.api.like;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.like.LikeEntityType;

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
      String entityId);

  /**
   * 关注
   * 
   * @param entityType
   * @param entityId
   * @return
   */
  @POST("like/follow/{entityType}/{entityId}")
  Observable<Response<Void>> followLike(@Path("entityType") LikeEntityType entityType,
      String entityId);

  /**
   * 点赞
   * 
   * @param entityId
   * @return
   */
  @POST("like/praise/{entityId}")
  Observable<Response<Void>> praiseLike(@Path("entityId") String entityId);
}
