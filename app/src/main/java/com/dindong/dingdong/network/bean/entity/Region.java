package com.dindong.dingdong.network.bean.entity;

import com.dindong.dingdong.util.IsEmpty;

/**
 * Created by wcong on 2018/3/13.
 */

public class Region {
  private String id;
  private String text;

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

  public void setText(String text) {
    this.text = text;
  }
}
