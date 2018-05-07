package com.dindong.dingdong.network.api.moment.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.moment.MomentServiceApi;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import rx.Observable;

/**
 * Created by wcong on 2018/5/5.
 * <p>
 * </>
 */

public class ListMomentCase extends BaseUseCase<MomentServiceApi> {
  private QueryParam queryParam;

  public ListMomentCase(QueryParam queryParam) {
    this.queryParam = queryParam;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().listMoment(queryParam);
  }
}
