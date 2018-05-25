package com.dindong.dingdong.network.api.favorite;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.auth.User;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.favorite.FavoriteStatic;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.network.bean.store.Subject;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wcong on 2018/5/22.
 * <p>
 * 关注/收藏管理 </>
 */

public interface FavoriteServiceApi {
  /**
   * 统计各关注的数量
   * 
   * @return
   */
  @GET("favorite/statistics")
  Observable<Response<FavoriteStatic>> statisticFavorite();

  /**
   * 获取用户关注课程列表
   * 
   * @param queryParam
   * @return
   */
  @POST("favorite/course/list")
  Observable<Response<List<Subject>>> listFavSubject(@Body QueryParam queryParam);

  /**
   * 获取用户关注门店列表
   * 
   * @param queryParam
   * @return
   */
  @POST("favorite/store/list")
  Observable<Response<List<Shop>>> listFavShop(@Body QueryParam queryParam);

  /**
   * 获取用户关注的人列表
   * 
   * @param queryParam
   * @return
   */
  @POST("favorite/member/list")
  Observable<Response<List<User>>> listMember(@Body QueryParam queryParam);

  /**
   * 获取用户粉丝列表
   * 
   * @param queryParam
   * @return
   */
  @POST("favorite/fans/list")
  Observable<Response<List<User>>> listFans(@Body QueryParam queryParam);

}
