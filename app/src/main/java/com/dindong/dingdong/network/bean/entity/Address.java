package com.dindong.dingdong.network.bean.entity;

import java.io.Serializable;

/**
 * Created by wcong on 2018/3/13. 地址实体
 */

public class Address implements Serializable {

  private Region province;// 省
  private Region city;// 市
  private Region district;// 区
  private String street;// 街道即详细地址

  public Region getProvince() {
    return province;
  }

  public void setProvince(Region province) {
    this.province = province;
  }

  public Region getCity() {
    return city;
  }

  public void setCity(Region city) {
    this.city = city;
  }

  public Region getDistrict() {
    return district;
  }

  public void setDistrict(Region district) {
    this.district = district;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  @Override
  public String toString() {
    return getProvince().getText()
        + (getProvince().getText().equals(getCity().getText()) ? "" : getCity().getText())
        + getDistrict().getText() + getStreet();
  }

  public String toStringOfName() {
    return getProvince().getName()
        + (getProvince().getName().equals(getCity().getName()) ? "" : getCity().getName())
        + getDistrict().getName() + getStreet();
  }
}
