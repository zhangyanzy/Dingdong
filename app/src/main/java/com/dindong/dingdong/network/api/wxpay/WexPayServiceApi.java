package com.dindong.dingdong.network.api.wxpay;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.wxpay.WxUnifiedOrderResult;

import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by wcong on 2018/5/18.
 * <p>
 * </>
 */

public interface WexPayServiceApi {
  @POST("wxpay/unifiedOrder/{id}")
  Observable<Response<WxUnifiedOrderResult>> unifiedOrder(@Path("id") String orderId);
}
