package com.dindong.dingdong.network.bean.auth;

import com.dindong.dingdong.util.IsEmpty;

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

  public static String getName(String code) {
    return IsEmpty.string(code) ? null : getAuthIdentity(code).getName();
  }

  public static AuthIdentity getAuthIdentity(String code) {
    if (IsEmpty.string(code))
      return null;
    for (AuthIdentity identity : AuthIdentity.values()) {
      if (code.equals(identity.toString()) || code.equals(identity.toString().toUpperCase())
          || code.equals(identity.toString().toLowerCase())) {
        return identity;
      }
    }
    return null;
  }
}
