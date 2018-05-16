package com.dindong.dingdong.network.api.shop;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.network.bean.store.ShopComment;
import com.dindong.dingdong.network.bean.store.ShopGood;
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
   *          queryType:
   *          audition-试听课，groupbuy-拼团上课，all-全部课程，recommend-品质优选，near-附近门店<br/>
   * @return
   */
  @POST("shop/list")
  Observable<Response<List<Shop>>> listShop(@Body QueryParam param);

  /**
   * 获取门店详情
   * 
   * @param shopId
   * @return
   */
  @GET("shop/get")
  Observable<Response<Shop>> get(@Query("shopId") String shopId);

  /**
   * 查询门店老师列表，支持分页查询。
   * 
   * @param shopId
   * @param param
   * @return
   */
  @POST("shop/{shopId}/teacher/list")
  Observable<Response<List<Teacher>>> listTeacher(@Path("shopId") String shopId,
      @Body QueryParam param);

  /**
   * 获取门店商品
   * 
   * @param shopId
   * @param param
   * @return
   */
  @POST("shop/{shopId}/good/list")
  Observable<Response<List<ShopGood>>> listGood(@Path("shopId") String shopId,
      @Body QueryParam param);

  /**
   * 获取门店评价
   * 
   * @param shopId
   * @param param
   * @return
   */
  @POST("shop/{shopId}/comment/listShopComment")
  Observable<Response<List<ShopComment>>> listShopComment(@Path("shopId") String shopId,
      @Body QueryParam param);

  /**
   * 提交门店评价
   * 
   * @param shopId
   * @param shopComment
   * @return
   */
  @POST("shop/{shopId}/comment/commit")
  Observable<Response> commitComment(@Path("shopId") String shopId, @Body ShopComment shopComment);
}
