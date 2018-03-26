package com.dindong.dingdong.network.api.auth.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.auth.AuthServiceApi;
import com.dindong.dingdong.network.bean.auth.RegisterRequest;

import rx.Observable;

/**
 * Created by wcong on 2018/3/26.
 * <p>
 * 用户注册 </>
 */

public class RegisterCase extends BaseUseCase<AuthServiceApi> {

  private String authCode;
  private RegisterRequest request;

  public RegisterCase(String authCode, RegisterRequest request) {
    this.authCode = authCode;
    this.request = request;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().register(authCode, request);
  }
}
