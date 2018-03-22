package com.dindong.dingdong.network.bean.entity;

import java.io.Serializable;

/**
 * Created by wcong on 2018/3/22.
 * <p>
 * 分类标签实体 </>
 */

public class Tag implements Serializable {
  private String id;
  private String text;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
