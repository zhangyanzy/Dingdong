package com.dindong.dingdong.network.api.moment.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.moment.MomentServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/15.
 * <p>
 * </>
 */

public class StatisticMomentCase extends BaseUseCase<MomentServiceApi> {
  private String id;

  public StatisticMomentCase(String id) {
    this.id = id;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().statistics(id);
  }
}
