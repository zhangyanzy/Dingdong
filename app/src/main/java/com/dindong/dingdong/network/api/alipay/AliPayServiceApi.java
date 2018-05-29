package com.dindong.dingdong.network.api.alipay;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.wxpay.WxUnifiedOrderResult;

import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by wcong on 2018/5/29.
 * <p>
 * </>
 */

public interface AliPayServiceApi {

  /**
   * 发起支付宝下单支付
   * 
   * @param orderId
   * @return
   */
  @POST("alipay/traceapppay/{id}")
  Observable<Response<String>> traceapPay(@Path("id") String orderId);
}
