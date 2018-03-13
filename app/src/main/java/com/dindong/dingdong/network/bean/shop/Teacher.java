package com.dindong.dingdong.network.bean.shop;

/**
 * Created by wcong on 2018/3/13.
 */

public class Teacher {
  private String id;// UUID
  private String name;// 教师名称
  private String remark;// 个人简介

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

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
