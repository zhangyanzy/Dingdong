package com.dindong.dingdong.network.api.region;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.Region;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wcong on 2018/3/14.
 */

public interface RegionServiceApi {

  /**
   * 获取平台热门城市
   * 
   * @return
   */
  @GET("region/serviceCity/list/")
  Observable<Response<List<Region>>> listRegion();

  /**
   * 获取地址
   *
   * @return
   */
  @GET("region/children")
  Observable<Response<List<Region>>> getRegion(@Query("parentCode") String parentCode);
}
