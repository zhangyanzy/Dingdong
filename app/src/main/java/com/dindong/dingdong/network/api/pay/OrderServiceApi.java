package com.dindong.dingdong.network.api.pay;

import java.util.List;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.pay.Order;
import com.dindong.dingdong.network.bean.pay.OrderStatictis;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wcong on 2018/3/22.
 * <p>
 * 支付API</>
 */

public interface OrderServiceApi {
  /**
   * 预下单
   * 
   * @param userId
   * @param order
   * @return
   */
  @POST("order")
  Observable<Response<Order>> preSubmit(@Query("userId") String userId, @Body Order order);

  /**
   * 新增团购订单
   *
   * @param order
   * @return
   */
  @POST("order/groupbuy")
  Observable<Response<Order>> groupBuy(@Body Order order);

  /**
   * 订单查询
   *
   * @param userId
   * @param orderId
   *          订单
   * @return
   */
  @GET("pay/get")
  Observable<Response<Order>> get(@Query("userId") String userId, @Query("orderId") String orderId);

  /**
   * 取消订单
   * 
   * @param orderId
   * @return
   */
  @PUT("order/cancel/{id}")
  Observable<Response<Void>> cancel(@Path("id") String orderId);

  /**
   * 商品销核
   * 
   * @param userId
   * @param code
   *          销核码
   * @return
   */
  @POST("pay/submitAndCancel")
  Observable<Response<Order>> submitAndCancel(@Query("userId") String userId,
      @Query("code") String code);

  /**
   * 查询所有订单,支持分页查询
   * 
   * @param param
   *          state，订单状态{@link com.dindong.dingdong.network.bean.pay.OrderState}
   *          为空则查询全部<br/>
   *          type:=，订单类型{@link com.dindong.dingdong.network.bean.pay.OrderType}
   *          为空则查询全部<br/>
   * @return
   */
  @POST("order/list")
  Observable<Response<List<Order>>> listOrder(@Body QueryParam param);

  /**
   * 统计订单各状态订单数量
   * 
   * @return
   */
  @GET("order/statistics")
  Observable<Response<OrderStatictis>> statisticOrder();

}