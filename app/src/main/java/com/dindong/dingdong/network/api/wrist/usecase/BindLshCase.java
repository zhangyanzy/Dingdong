package com.dindong.dingdong.network.api.wrist.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.wrist.WristServiceApi;
import com.dindong.dingdong.network.bean.wrist.BlueWrist;

import rx.Observable;

/**
 * Created by wcong on 2018/3/28.
 * <p>
 * </>
 */

public class BindLshCase extends BaseUseCase<WristServiceApi> {
  private BlueWrist blueWrist;

  public BindLshCase(BlueWrist blueWrist) {
    this.blueWrist = blueWrist;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().bind(blueWrist);
  }
}
