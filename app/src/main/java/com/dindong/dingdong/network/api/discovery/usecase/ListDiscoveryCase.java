package com.dindong.dingdong.network.api.discovery.usecase;

import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.discovery.DiscoveryServiceApi;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import rx.Observable;

/**
 * Created by wcong on 2018/4/25.
 * <p>
 * </>
 */

public class ListDiscoveryCase extends BaseUseCase<DiscoveryServiceApi> {
  private QueryParam queryParam;

  public ListDiscoveryCase(QueryParam queryParam) {
    this.queryParam = queryParam;
  }

  @Override
  protected Observable buildCase() {
    return createConnection(true).listDiscovery(SessionMgr.getUser().getId(), queryParam);
  }
}
