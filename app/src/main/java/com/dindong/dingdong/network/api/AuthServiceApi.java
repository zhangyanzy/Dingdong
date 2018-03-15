package com.dindong.dingdong.network.api;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.auth.RegisterRequest;
import com.dindong.dingdong.network.bean.auth.User;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wcong on 2018/3/12. 用户登录相关接口
 */

public interface AuthServiceApi {
  /**
   * 登录
   * 
   * @param mobile
   * @param password
   * @return
   */
  @POST("app/auth/login")
  Observable<Response<User>> login(@Query("mobile") String mobile,
      @Query("password") String password);

  /**
   * 注册
   *
   * @param authCode
   *          验证码
   * @param req
   * @return
   */
  @POST("app/auth/register")
  Observable<Response<User>> register(@Query("authCode") String authCode,
      @Body RegisterRequest req);

  /**
   * 发送短信
   *
   * @param mobile
   *          手机号
   * @return
   * @throws Exception
   */
  @POST("app/sms/send")
  Observable<Response> sendSmsCode(@Query("mobile") String mobile);

  /**
   * 重置密码
   * 
   * @param mobile
   * @param password
   *          新密码
   * @param authCode
   * @return
   */
  @POST("app/user/password/reset")
  Observable<Response> resetPassword(@Query(("mobile")) String mobile,
      @Query(("password")) String password, @Query(("authCode")) String authCode);
}
