package com.dindong.dingdong.network.bean.auth;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.dindong.dingdong.base.ShallowCopy;
import com.dindong.dingdong.network.bean.entity.UserImage;

/**
 * Created by wcong on 2018/3/9.
 *
 * 用户基本信息
 */

public class User extends ShallowCopy<User> implements Serializable, Cloneable {
  private String id;// UUID
  private String userId;// 用户ID
  private AuthIdentity identity;// 用户身份
  private String name;// 昵称
  private String mobile;// 手机号
  private String sex;// 性别
  private Date birthday;// 生日 格式:YYYY-MM-DD
  private String remark;// 个人说明
  private String num;// 叮咚号
  private BigDecimal favShop = BigDecimal.ZERO;// 关注门店数
  private BigDecimal favSubject = BigDecimal.ZERO;// 关注课程数
  private BigDecimal favUser = BigDecimal.ZERO;// 关注人数
  private BigDecimal fans = BigDecimal.ZERO;// 粉丝
  private UserImage userImage;// 用户头像

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public AuthIdentity getIdentity() {
    return identity;
  }

  public void setIdentity(AuthIdentity identity) {
    this.identity = identity;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getNum() {
    return num;
  }

  public void setNum(String num) {
    this.num = num;
  }

  public BigDecimal getFavShop() {
    return favShop;
  }

  public void setFavShop(BigDecimal favShop) {
    this.favShop = favShop;
  }

  public BigDecimal getFavSubject() {
    return favSubject;
  }

  public void setFavSubject(BigDecimal favSubject) {
    this.favSubject = favSubject;
  }

  public BigDecimal getFavUser() {
    return favUser;
  }

  public void setFavUser(BigDecimal favUser) {
    this.favUser = favUser;
  }

  public BigDecimal getFans() {
    return fans;
  }

  public void setFans(BigDecimal fans) {
    this.fans = fans;
  }

  public UserImage getUserImage() {
    return userImage;
  }

  public void setUserImage(UserImage userImage) {
    this.userImage = userImage;
  }
}
