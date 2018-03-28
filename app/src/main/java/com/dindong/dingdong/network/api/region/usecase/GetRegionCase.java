package com.dindong.dingdong.network.api.region.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.region.RegionServiceApi;

import rx.Observable;

/**
 * Created by wangcong on 2018/3/28.
 * <p>
 */

public class GetRegionCase extends BaseUseCase<RegionServiceApi> {
  private String parentCode;

  public GetRegionCase(String parentCode) {
    this.parentCode = parentCode;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().getRegion(parentCode);
  }
}
