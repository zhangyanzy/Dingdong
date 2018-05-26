package com.dindong.dingdong.network.bean.auth;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dindong.dingdong.base.ShallowCopy;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.util.IsEmpty;

/**
 * Created by wcong on 2018/3/9.
 *
 * 用户基本信息
 */

public class User extends ShallowCopy<User> implements Serializable, Cloneable {
  private String id;// UUID
  private List<String> identities=new ArrayList<>();// 用户身份
  private String identitie;// 用户身份
  private String name;// 昵称
  private String mobile;// 手机号
  private String sex;// 性别
  private Date birthday;// 生日 格式:YYYY-MM-DD
  private String remark;// 个人说明
  private String dingdongId;// 叮咚号
  private BigDecimal favShop = BigDecimal.ZERO;// 关注门店数
  private BigDecimal favSubject = BigDecimal.ZERO;// 关注课程数
  private BigDecimal favUser = BigDecimal.ZERO;// 关注人数
  private int fans = 0;// 粉丝
  private GlobalImage userImage;// 用户头像
  private boolean favorite;// 是否已关注
  private List<String> tags = new ArrayList<>();// 个人标签

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<String> getIdentities() {
    return identities;
  }

  public void setIdentities(List<String> identities) {
    this.identities = identities;
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
    return sex == null || IsEmpty.string(sex) ? "" : ((Integer.valueOf(sex) == 0) ? "男" : "女");
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

  public String getDingdongId() {
    return dingdongId;
  }

  public void setDingdongId(String dingdongId) {
    this.dingdongId = dingdongId;
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

  public int getFans() {
    return fans;
  }

  public void setFans(int fans) {
    this.fans = fans;
  }

  public GlobalImage getUserImage() {
    return userImage;
  }

  public void setUserImage(GlobalImage userImage) {
    this.userImage = userImage;
  }

  public String getIdentitie() {
    return identitie;
  }

  public void setIdentitie(String identitie) {
    this.identitie = identitie;
  }

  public boolean isFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }
}
