package com.dindong.dingdong.network.api.good.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.good.GoodServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/24.
 * <p>
 * </>
 */

public class GetGoodCase extends BaseUseCase<GoodServiceApi> {
  private String id;

  public GetGoodCase(String id) {
    this.id = id;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().getGood(id);
  }
}
