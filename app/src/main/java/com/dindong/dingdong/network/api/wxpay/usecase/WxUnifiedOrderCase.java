package com.dindong.dingdong.network.api.wxpay.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.wxpay.WexPayServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/18.
 * <p></>
 */

public class WxUnifiedOrderCase extends BaseUseCase<WexPayServiceApi>{
    private String orderId;

    public WxUnifiedOrderCase(String orderId) {
        this.orderId = orderId;
    }

    @Override
    protected Observable buildCase() {
        return createConnection().unifiedOrder(orderId);
    }
}
