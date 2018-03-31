package com.dindong.dingdong.network.api.wrist.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.wrist.WristServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/3/30.
 * <p>
 * </>
 */

public class ScanLshCase extends BaseUseCase<WristServiceApi> {
  private String lshId;
  private String longitude;
  private String latitude;

  public ScanLshCase(String lshId, String longitude, String latitude) {
    this.lshId = lshId;
    this.longitude = longitude;
    this.latitude = latitude;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().scan(lshId, longitude, latitude);
  }
}
