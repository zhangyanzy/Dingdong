package com.dindong.dingdong.network.api.auth;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.auth.RegisterRequest;
import com.dindong.dingdong.network.bean.auth.User;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wcong on 2018/3/12. 用户相关API
 */

public interface AuthServiceApi {
  /**
   * 登录，需帐号密码
   * 
   * @param username
   * @param password
   * @return
   */
  @POST("auth/login")
  Observable<Response<User>> login(@Query("username") String username,
      @Query("password") String password);

  /**
   * 快捷登录，需手机号和验证码
   * 
   * @param mobile
   * @param authCode
   * @return
   */
  @POST("auth/shortLogin")
  Observable<Response<User>> shortLogin(@Query("mobile") String mobile,
      @Query("authCode") String authCode);

  /**
   * 注册
   *
   * @param authCode
   *          验证码
   * @param req
   * @return
   */
  @POST("auth/register")
  Observable<Response<User>> register(@Query("authCode") String authCode,
      @Body RegisterRequest req);

  /**
   * 发送短信
   *
   * @param mobile
   *          手机号
   * @param operator
   *          操作。0-注册，1-忘记密码，2-修改密码，3-快捷登录，4-更换手机
   * @return
   */
  @GET("sms/send")
  Observable<Response> sendSmsCode(@Query("mobile") String mobile,
      @Query("operator") String operator);

  /**
   * 重置密码，用于忘记密码，无需旧密码
   * 
   * @param mobile
   * @param password
   *          新密码
   * @param authCode
   * @return
   */
  @PUT("auth/password/reset")
  Observable<Response> resetPassword(@Query(("mobile")) String mobile,
      @Query(("password")) String password, @Query(("authCode")) String authCode);

  /**
   * 重置密码，需旧密码
   * 
   * @param mobile
   * @param oldPassword
   * @param newPassword
   * @param authCode
   * @return
   */
  @PUT("auth/password/resetByOld")
  Observable<Response> resetPasswordByOld(@Query(("mobile")) String mobile,
      @Query(("oldPassword")) String oldPassword, @Query(("newPassword")) String newPassword,
      @Query(("authCode")) String authCode);

  /**
   * 更换手机号
   * 
   * @param oldMobile
   * @param newMobile
   * @param password
   * @param authCode
   * @return
   */
  @PUT("auth/mobile/reset")
  Observable<Response> resetMobile(@Query(("oldMobile")) String oldMobile,
      @Query(("newMobile")) String newMobile, @Query(("password")) String password,
      @Query(("authCode")) String authCode);
}
