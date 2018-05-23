package com.dindong.dingdong.network.api.good;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.good.ShopGood;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wcong on 2018/5/24.
 * <p>
 * </>
 */

public interface GoodServiceApi {
  /**
   * 获取商品列表
   *
   * @param queryParam
   *          shop，指定门店查询<br/>
   * @return
   */
  @POST("goods/list")
  Observable<Response<List<ShopGood>>> listGood(@Body QueryParam queryParam);

  /**
   * 根据ID获取商品详情
   *
   * @param id
   * @return
   */
  @GET("goods/get")
  Observable<Response<ShopGood>> getGood(@Query("id") String id);
}
