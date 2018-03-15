package com.dindong.dingdong.network.bean.auth;

/**
 * Created by NeilSpears on 2016/11/19.
 *
 * 注册实体
 */

public class RegisterRequest {
  private String id;
  private String mobile;// 手机号
  private String name;// 昵称
  private String password;// 密码

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
