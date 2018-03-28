package com.dindong.dingdong.network.api.banner.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.banner.BannerServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/3/29.
 * <p>
 * </>
 */

public class ListBannerCase extends BaseUseCase<BannerServiceApi> {
  @Override
  protected Observable buildCase() {
    return createConnection().listBanner();
  }
}
