package com.dindong.dingdong.network.api.activity;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.activity.ShopActivity;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wcong on 2018/5/24.
 * <p>
 * 活动相关api </>
 */

public interface ActivityServiceApi {
  /**
   * 获取活动列表
   * 
   * @param queryParam
   *          shop，指定门店查询<br/>
   * @return
   */
  @POST("activity/list")
  Observable<Response<List<ShopActivity>>> listActivity(@Body QueryParam queryParam);

  /**
   * 根据ID获取活动详情
   * 
   * @param id
   * @return
   */
  @GET("activity/get")
  Observable<Response<ShopActivity>> getActivity(@Query("id") String id);
}
