package com.dindong.dingdong.network.api.banner;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.banner.Banner;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by wcong on 2018/3/29.
 * <p>
 * </>
 */

public interface BannerServiceApi {
  /**
   * 获取首页轮播图
   * 
   * @return
   */
  @GET("banner/list")
  Observable<Response<List<Banner>>> listBanner();
}
