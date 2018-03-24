package com.dindong.dingdong.network.bean.pay;

/**
 * Created by wangcong on 2018/3/22.
 * <p>
 * 订单状态
 */

public enum OrderState {
  wait("待付款"), wait_group("拼团中"), can_use("可使用"), success("已完成"), fail("支付失败");
  private String name;

  OrderState(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
