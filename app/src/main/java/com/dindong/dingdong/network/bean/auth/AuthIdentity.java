package com.dindong.dingdong.network.bean.auth;

/**
 * Created by wcong on 2018/3/9.
 */

public enum AuthIdentity {
  INSTITUTION("机构"), MEMBER("会员/老师");
  private String name;

  AuthIdentity(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
