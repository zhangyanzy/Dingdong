package com.dindong.dingdong.network.api.wrist.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.wrist.WristServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/3/28.
 * <p>
 * </>
 */

public class GetWristCase extends BaseUseCase<WristServiceApi> {
  private String num;

  public GetWristCase(String num) {
    this.num = num;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().get(num);
  }
}
