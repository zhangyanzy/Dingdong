package com.dindong.dingdong.network.bean.wrist;

import java.util.Date;

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
  private String phone1;// 联系电话
  private String phone2;
  private String phone3;
  private Address address;// 家庭住址
  private String remark;// 简介
  private String status;// 手环状态：0-未绑定，1-已绑定

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

  public String getPhone1() {
    return phone1;
  }

  public void setPhone1(String phone1) {
    this.phone1 = phone1;
  }

  public String getPhone2() {
    return phone2;
  }

  public void setPhone2(String phone2) {
    this.phone2 = phone2;
  }

  public String getPhone3() {
    return phone3;
  }

  public void setPhone3(String phone3) {
    this.phone3 = phone3;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
