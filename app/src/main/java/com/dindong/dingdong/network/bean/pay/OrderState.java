package com.dindong.dingdong.network.bean.pay;

/**
 * Created by wangcong on 2018/3/22.
 * <p>
 * 订单状态
 */

public enum OrderState {
  waitPay("待付款"), grouping("拼团中"), confirmed("可使用"), finished("已完成"), canceled("支付失败");
  private String name;

  OrderState(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
