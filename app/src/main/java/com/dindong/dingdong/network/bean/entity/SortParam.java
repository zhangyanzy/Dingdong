package com.dindong.dingdong.network.bean.entity;

import java.io.Serializable;

/**
 * Created by wcong on 2018/3/13.
 */

public class SortParam implements Serializable{
  private String property;
  private String direction;

  public SortParam() {
  }

  public SortParam(String property, String direction) {
    this.property = property;
    this.direction = direction;
  }

  public String getProperty() {
    return this.property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public String getDirection() {
    return this.direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }
}
