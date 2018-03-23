package com.dindong.dingdong.network.api.auth.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.auth.AuthServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/3/23.
 * <p>
 * </>
 */

public class SendSmsCase extends BaseUseCase<AuthServiceApi> {
  private String mobile;

  public SendSmsCase(String mobile) {
    this.mobile = mobile;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().sendSmsCode(mobile);
  }
}
