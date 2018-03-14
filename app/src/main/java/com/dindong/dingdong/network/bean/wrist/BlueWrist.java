package com.dindong.dingdong.network.bean.wrist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dindong.dingdong.network.bean.entity.Address;

/**
 * Created by wcong on 2018/3/14. 蓝手环信息
 */

public class BlueWrist {
  private String id;// UUID
  private String num;// 手环编号
  private String name;// 姓名
  private String sex;// 性别
  private Date birthday;// 出生年月 格式YYYY-MM-DD
  private List<String> phone = new ArrayList<>();// 联系电话
  private Address address;// 家庭住址
  private String remark;// 简介

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNum() {
    return num;
  }

  public void setNum(String num) {
    this.num = num;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public List<String> getPhone() {
    return phone;
  }

  public void setPhone(List<String> phone) {
    this.phone = phone;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
