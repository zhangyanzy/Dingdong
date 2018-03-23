package com.dindong.dingdong.network.api.auth.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.auth.AuthServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/3/24.
 * <p>
 * </>
 */

public class ResetMobileCase extends BaseUseCase<AuthServiceApi> {
  private String oldMobile;
  private String newMobile;
  private String password;
  private String authCode;

  public ResetMobileCase(String oldMobile, String newMobile, String password, String authCode) {
    this.oldMobile = oldMobile;
    this.newMobile = newMobile;
    this.password = password;
    this.authCode = authCode;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().resetMobile(oldMobile, newMobile, password, authCode);
  }
}
