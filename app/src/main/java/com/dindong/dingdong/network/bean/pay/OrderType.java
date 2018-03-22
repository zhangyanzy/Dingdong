package com.dindong.dingdong.network.bean.pay;

/**
 * Created by wcong on 2018/3/22.
 * <p>
 * 订单类型 </>
 */

public enum OrderType {
  subject("门店课程"), good("门店商品");
  private String name;

  OrderType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}