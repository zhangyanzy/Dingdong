package com.dindong.dingdong.network.api.auth.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.auth.AuthServiceApi;
import com.dindong.dingdong.network.bean.auth.AuthLogin;

import rx.Observable;

/**
 * Created by wcong on 2018/3/14.
 */

public class LoginCase extends BaseUseCase<AuthServiceApi> {
  private String mobile;
  private String password;

  public LoginCase(String mobile, String password) {
    this.mobile = mobile;
    this.password = password;
  }

  @Override
  protected Observable buildCase() {
    return createConnection(true).login(mobile,password);
  }
}
