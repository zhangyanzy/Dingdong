package com.dindong.dingdong.network.api.activity.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.activity.ActivityServiceApi;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import rx.Observable;

/**
 * Created by wcong on 2018/5/24.
 * <p>
 * </>
 */

public class ListActivityCase extends BaseUseCase<ActivityServiceApi> {
  private QueryParam queryParam;

  public ListActivityCase(QueryParam queryParam) {
    this.queryParam = queryParam;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().listActivity(queryParam);
  }
}
