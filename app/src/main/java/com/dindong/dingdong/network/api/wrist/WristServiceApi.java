package com.dindong.dingdong.network.api.wrist;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.wrist.BlueWrist;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wcong on 2018/3/27.
 * <p>
 * </>
 */

public interface WristServiceApi {

  /**
   * 获取指定ID的蓝手环信息
   * 
   * @param id
   * @return
   */
  @GET("/app/lsh/:id")
  Observable<Response<BlueWrist>> get(@Query("id") String id);

  /**
   * 更新
   * 
   * @param id
   * @param blueWrist
   * @return
   */
  @PUT("/app/lsh/:id")
  Observable<Response> update(@Query("id") String id, @Body BlueWrist blueWrist);

  /**
   * 绑定手环
   * 
   * @param blueWrist
   * @return
   */
  @PUT("/app/lsh/bind")
  Observable<Response> bind(@Body BlueWrist blueWrist);

  /**
   * 记录手环扫描记录
   * 
   * @param lshId
   * @param longitude
   *          经度
   * @param latitude
   *          纬度
   * @return
   */
  @POST("/app/lsh/scan")
  Observable<Response> scan(@Query("lshId") String lshId, @Query("longitude") String longitude,
      @Query("latitude") String latitude);

  /**
   * 获取当前会员蓝手环列表
   * 
   * @return
   */
  @POST("/app/lsh/user/list/")
  Observable<Response<List<BlueWrist>>> list();
}
