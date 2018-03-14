package com.dindong.dingdong.network.api.region;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by wcong on 2018/3/14.
 */

public interface RegionServiceApi {

  /**
   * 获取平台热门城市
   * 
   * @param id
   * @return
   */
  @GET("app/region/list")
  Observable<Response<List<String>>> listRegion(@Path("user") String id);
}
