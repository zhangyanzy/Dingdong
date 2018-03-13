package com.dindong.dingdong.network.api.shop;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.shop.Shop;
import com.dindong.dingdong.network.bean.shop.Subject;
import com.dindong.dingdong.network.bean.shop.Teacher;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by wcong on 2018/3/13. 门店及课程API
 */

public interface ShopServiceApi {

  /**
   * 查询门店列表，支持分页查询。
   * 
   * @param param
   *          grid:[.)，当前经纬度[longitude经,latitude纬]<br/>
   *          range:in，距离包含<br/>
   * @return
   */
  @GET("app/shop/list")
  Observable<Response<List<Shop>>> listShop(@Body QueryParam param);

  /**
   * 查询课程列表，支持分页查询。
   * 
   * @param id
   *          门店id
   * @param param
   *          SortParam#property=range,direction=asc，按距离查询<br/>
   *          SortParam#property=popular,direction=desc，按人气查询<br/>
   *          SortParam#property=evaluate,direction=desc，按评价查询<br/>
   * @return
   */
  @GET("app/{shop}/shop/subject/list")
  Observable<Response<List<Subject>>> listSubject(@Path("shop") String id, @Body QueryParam param);

  /**
   * 查询门店老师列表，支持分页查询。
   * 
   * @param id
   * @param param
   * @return
   */
  @GET("app/{shop}/shop/teacher/list")
  Observable<Response<List<Teacher>>> listTeacher(@Path("shop") String id, @Body QueryParam param);
}
