package com.dindong.dingdong.network.bean.entity;

import java.io.Serializable;

/**
 * Created by wcong on 2018/3/13.
 */

public class FilterParam implements Serializable {
  private String property;
  private Object value;

  public FilterParam() {
  }

  public FilterParam(String property, Object value) {
    this.property = property;
    this.value = value;
  }

  public String getProperty() {
    return this.property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public Object getValue() {
    return this.value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String toString() {
    return this.property + this.value.toString();
  }
}
