package com.dindong.dingdong.network.bean.entity;

import java.io.Serializable;

import com.dindong.dingdong.util.IsEmpty;

/**
 * Created by wcong on 2018/3/13. 地址实体
 */

public class Address implements Serializable,Cloneable {

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
    if (getProvince() == null || getCity() == null || getDistrict() == null)
      return "";

    return getProvince().getText()
        + (getProvince().getText().equals(getCity().getText()) ? "" : getCity().getText())
        + getDistrict().getText() + (IsEmpty.string(getStreet()) ? "" : getStreet());
  }

  public String toStringOfName() {
    if (getProvince().getName() == null || getCity().getName() == null
        || getDistrict().getName() == null)
      return "";
    return getProvince().getName()
        + (getProvince().getName().equals(getCity().getName()) ? "" : getCity().getName())
        + getDistrict().getName() + (IsEmpty.string(getStreet()) ? "" : getStreet());
  }
}
