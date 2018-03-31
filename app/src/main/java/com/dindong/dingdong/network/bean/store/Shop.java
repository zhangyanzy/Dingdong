package com.dindong.dingdong.network.bean.store;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.network.bean.entity.Address;
import com.dindong.dingdong.network.bean.entity.GlobalImage;

/**
 * Created by wcong on 2018/3/13. 门店实体
 */

public class Shop implements Serializable {
  private String id;// UUID
  private String name;// 门店名称
  private String phone;// 门店电话
  private boolean prove;// 是否被认证
  private List<String> tags = new ArrayList<>();// 门店标签可存门店授课类型

  // 地址相关
  private Address address;// 门店地址
  private String longitude;// 经度
  private String latitude;// 纬度
  private BigDecimal range = BigDecimal.ZERO;// 距离

  private List<GlobalImage> images = new ArrayList<>();// 门店展示图
  private GlobalImage logoImage = new GlobalImage();// 门店LOGO/首图

  private List<Subject> subjects = new ArrayList<>();// 课程列表，当为门店列表时最多有两个课程

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public boolean isProve() {
    return prove;
  }

  public void setProve(boolean prove) {
    this.prove = prove;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public BigDecimal getRange() {
    return range;
  }

  public void setRange(BigDecimal range) {
    this.range = range;
  }

  public List<GlobalImage> getImages() {
    return images;
  }

  public void setImages(List<GlobalImage> images) {
    this.images = images;
  }

  public List<Subject> getSubjects() {
    return subjects;
  }

  public void setSubjects(List<Subject> subjects) {
    this.subjects = subjects;
  }

  public GlobalImage getLogoImage() {
    return logoImage;
  }

  public void setLogoImage(GlobalImage logoImage) {
    this.logoImage = logoImage;
  }
}
