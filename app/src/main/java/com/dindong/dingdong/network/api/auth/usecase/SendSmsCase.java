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
  private String operator;

  /**
   * @param mobile
   * @param operator
   *          操作。0-注册，1-忘记密码，2-修改密码，3-快捷登录，4-更换手机
   */
  public SendSmsCase(String mobile, String operator) {
    this.mobile = mobile;
    this.operator = operator;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().sendSmsCode(mobile, operator);
  }
}
