package com.dindong.dingdong.network.api.apply.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.apply.ApplyServiceApi;
import com.dindong.dingdong.network.bean.apply.RequestApply;

import rx.Observable;

/**
 * Created by wcong on 2018/5/25.
 * <p>
 * </>
 */

public class ApplyProxyCase extends BaseUseCase<ApplyServiceApi> {
  private RequestApply requestApply;

  public ApplyProxyCase(RequestApply requestApply) {
    this.requestApply = requestApply;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().applyProxy(requestApply);
  }
}
