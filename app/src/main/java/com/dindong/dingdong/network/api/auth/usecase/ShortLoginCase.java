package com.dindong.dingdong.network.api.auth.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.auth.AuthServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/3/23.
 * <p>
 * 快捷登录 </>
 */

public class ShortLoginCase extends BaseUseCase<AuthServiceApi> {
  private String mobile;
  private String authCode;

  public ShortLoginCase(String mobile, String authCode) {
    this.mobile = mobile;
    this.authCode = authCode;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().shortLogin(mobile, authCode);
  }
}
