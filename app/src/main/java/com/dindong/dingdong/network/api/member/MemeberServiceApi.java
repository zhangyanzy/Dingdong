package com.dindong.dingdong.network.api.member;

import com.dindong.dingdong.network.bean.Response;

import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * 会员信息相关api</>
 */

public interface MemeberServiceApi {
  /**
   * 修改用户头像
   * 
   * @param avatarUrl
   *          用户头像
   * @return
   */
  @PUT("member/modify/avatar")
  Observable<Response<Void>> modifyAvatar(@Query("avatarUrl") String avatarUrl);

  /**
   * 修改用户生日
   * 
   * @param birthday
   *          用户生日
   * @return
   */
  @PUT("member/modify/birthday")
  Observable<Response<Void>> modifyBirthday(@Query("birthday") String birthday);

  /**
   * 修改用户个人简介
   * 
   * @param birthday
   *          个人简介
   * @return
   */
  @PUT("member/modify/description")
  Observable<Response<Void>> modifyDescription(@Query("description") String birthday);

  /**
   * 修改用户昵称
   * 
   * @param name
   *          昵称
   * @return
   */
  @PUT("member/modify/name")
  Observable<Response<Void>> modifyName(@Query("name") String name);

  /**
   * 修改用户性别
   * 
   * @param sex
   *          性别 0-男 1-女
   * @return
   */
  @PUT("member/modify/sex")
  Observable<Response<Void>> modifySex(@Query("sex") String sex);
}
