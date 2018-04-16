package com.dindong.dingdong.network.bean.entity;

/**
 * Created by wcong on 2018/4/14.
 * <p>
 * 用户基本信息</>
 */

public class Ucn {
  private String userId;// 用户Id
  private String userName;// 用户名
  private GlobalImage image;// 用户头像

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public GlobalImage getImage() {
    return image;
  }

  public void setImage(GlobalImage image) {
    this.image = image;
  }
}
