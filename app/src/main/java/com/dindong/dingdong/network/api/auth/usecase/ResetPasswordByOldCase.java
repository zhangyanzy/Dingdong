package com.dindong.dingdong.network.api.auth.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.auth.AuthServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/3/24.
 * <p>
 * </>
 */

public class ResetPasswordByOldCase extends BaseUseCase<AuthServiceApi> {
  private String mobile;
  private String oldPassword;
  private String newPassword;
  private String authCode;

  public ResetPasswordByOldCase(String mobile, String oldPassword, String newPassword,
      String authCode) {
    this.mobile = mobile;
    this.oldPassword = oldPassword;
    this.newPassword = newPassword;
    this.authCode = authCode;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().resetPasswordByOld(mobile, oldPassword, newPassword, authCode);
  }
}
