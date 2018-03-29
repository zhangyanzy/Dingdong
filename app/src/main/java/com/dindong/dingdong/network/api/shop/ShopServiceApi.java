package com.dindong.dingdong.network.api.shop;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.network.bean.store.Teacher;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wcong on 2018/3/13. 门店API
 */

public interface ShopServiceApi {

  /**
   * 查询门店列表，支持分页查询。
   * 
   * @param param
   *          keyword，关键字类似于，支持门店名称<br/>
   *          longitude，经度<br/>
   *          latitude，纬度<br/>
   *          cityCode，城市编号<br/>
   *          districtCode，地区编号<br/>
   *          range，距离<br/>
   * @return
   */
  @POST("app/shop/list")
  Observable<Response<List<Shop>>> listShop(@Body QueryParam param);

  /**
   * 获取门店详情
   * 
   * @param shopId
   * @return
   */
  @GET("app/shop/get")
  Observable<Response<Shop>> get(@Query("shopId") String shopId);

  /**
   * 查询门店老师列表，支持分页查询。
   * 
   * @param id
   * @param param
   * @return
   */
  @POST("app/{shop}/shop/teacher/list")
  Observable<Response<List<Teacher>>> listTeacher(@Path("shop") String id, @Body QueryParam param);
}
