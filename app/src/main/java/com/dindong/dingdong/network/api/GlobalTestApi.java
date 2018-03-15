package com.dindong.dingdong.network.api;

import com.dindong.dingdong.network.bean.Response;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wcong on 2018/3/10. 测试接口，待删
 */

public interface GlobalTestApi {

  @POST("app/v2/auth/login")
  Observable<Response> test(@Query("mobile") String mobile, @Query("password") String password);
}
