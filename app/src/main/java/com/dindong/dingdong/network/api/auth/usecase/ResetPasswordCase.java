package com.dindong.dingdong.network.api.auth.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.auth.AuthServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/7.
 * <p>
 * </>
 */

public class ResetPasswordCase extends BaseUseCase<AuthServiceApi> {
  private String mobile;
  private String password;
  private String code;

  public ResetPasswordCase(String mobile, String password, String code) {
    this.mobile = mobile;
    this.password = password;
    this.code = code;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().resetPassword(mobile, password, code);
  }
}
