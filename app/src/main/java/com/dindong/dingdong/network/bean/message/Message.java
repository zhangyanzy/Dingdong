package com.dindong.dingdong.network.bean.message;

import java.io.Serializable;

/**
 * Created by wcong on 2018/4/14.
 * <p>
 * 消息实体</>
 */

public class Message implements Serializable {
  private String id;
  private String title;// 消息标题
  private String msg;// 消息内容

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
