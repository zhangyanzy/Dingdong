package com.dindong.dingdong.network.api.region.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.region.RegionServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/3/24.
 * <p></>
 */

public class ListRegionCase extends BaseUseCase<RegionServiceApi>{
    @Override
    protected Observable buildCase() {
    return createConnection().listRegion();
    }
}
