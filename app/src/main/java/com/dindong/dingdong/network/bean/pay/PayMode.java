package com.dindong.dingdong.network.bean.pay;

/**
 * Created by wangcong on 2018/3/22.
 * <p>
 * 支付方式
 */

public enum PayMode {
  other("其他"), aliPay("支付宝"), weiXin("微信");

  private String name;

  PayMode(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static String getName(String code) {
    for (PayMode payMode : PayMode.values()) {
      if (code.equals(payMode.toString())) {
        return payMode.getName();
      }
    }
    return "";
  }

}
