package com.dindong.dingdong.network.api.activity.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.activity.ActivityServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/24.
 * <p>
 * </>
 */

public class GetShopActivityCase extends BaseUseCase<ActivityServiceApi> {
  private String id;

  public GetShopActivityCase(String id) {
    this.id = id;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().getActivity(id);
  }
}
