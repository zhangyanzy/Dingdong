package com.dindong.dingdong.network.bean.shop;

/**
 * Created by wcong on 2018/3/13.
 */

public enum SubjectType {
  NORMAL("普通"), GROUP("团购");

  private String name;

  SubjectType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
