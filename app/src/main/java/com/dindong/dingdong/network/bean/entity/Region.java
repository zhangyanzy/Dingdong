package com.dindong.dingdong.network.bean.entity;

import java.io.Serializable;

import com.dindong.dingdong.util.IsEmpty;

/**
 * Created by wcong on 2018/3/13.
 */

public class Region implements Serializable {
  private String id;
  private String text;
  private String name;
  private String code;

  public Region() {
    super();
  }

  public Region(String id, String text) {
    super();
    this.id = id;
    this.text = text;
  }

  public String getId() {
    if (IsEmpty.string(id)) {
      return "";
    }
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getText() {
    if (IsEmpty.string(text)) {
      return "";
    }
    return text;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setText(String text) {
    this.text = text;
  }
}
