package com.dindong.dingdong.network.api.wrist.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.wrist.WristServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/3/27.
 * <p>
 * </>
 */

public class ListLshCase extends BaseUseCase<WristServiceApi> {
  @Override
  protected Observable buildCase() {
    return createConnection().list();
  }
}
