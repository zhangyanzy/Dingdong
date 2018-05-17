package com.dindong.dingdong.network.bean.store;

/**
 * Created by wcong on 2018/3/13.
 */

public enum SubjectType {
  NORMAL("普通"), GROUP("团购"), AUDITION("试听");

  private String name;

  SubjectType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
