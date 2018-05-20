package com.dindong.dingdong.network.api.account;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.account.Account;
import com.dindong.dingdong.network.bean.account.AccountLog;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wcong on 2018/5/21.
 * <p>
 * </>
 */

public interface AccountServiceApi {
  /**
   * 取得用户账户信息
   * 
   * @return
   */
  @GET("account")
  Observable<Response<Account>> getAccount();

  /**
   * 查询账户流水明细
   * 
   * @param param
   * @return
   */
  @POST("account")
  Observable<Response<List<AccountLog>>> listAccountLog(@Body QueryParam param);
}
