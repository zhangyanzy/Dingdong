package com.dindong.dingdong.network.bean.auth;

/**
 * Created by wcong on 2018/3/9.
 */

public enum AuthIdentity {
  INSTITUTION("机构"), MEMBER("会员"), PTEACHER("平台老师"), ITEACHER("机构老师");
  private String name;

  AuthIdentity(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static AuthIdentity getAuthIdentity(String code) {
    for (AuthIdentity identity : AuthIdentity.values()) {
      if (code.equals(identity.toString()) || code.equals(identity.toString().toUpperCase())
          || code.equals(identity.toString().toLowerCase())) {
        return identity;
      }
    }
    return null;
  }
}
