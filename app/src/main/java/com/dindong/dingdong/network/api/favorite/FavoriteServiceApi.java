package com.dindong.dingdong.network.api.favorite;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.favorite.FavoriteStatic;

import retrofit2.http.GET;
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
}
