package com.dindong.dingdong.network.api.notice.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.notice.MessageServiceApi;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import rx.Observable;

/**
 * Created by wcong on 2018/4/22.
 * <p>
 * </>
 */

public class ListMsgCase extends BaseUseCase<MessageServiceApi> {
  private QueryParam queryParam;

  public ListMsgCase(QueryParam queryParam) {
    this.queryParam = queryParam;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().listMsg(queryParam);
  }
}
