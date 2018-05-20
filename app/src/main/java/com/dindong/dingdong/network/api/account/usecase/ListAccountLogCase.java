package com.dindong.dingdong.network.api.account.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.account.AccountServiceApi;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import rx.Observable;

/**
 * Created by wcong on 2018/5/21.
 * <p>
 * </>
 */

public class ListAccountLogCase extends BaseUseCase<AccountServiceApi> {
  private QueryParam queryParam;

  public ListAccountLogCase(QueryParam queryParam) {
    this.queryParam = queryParam;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().listAccountLog(queryParam);
  }
}
