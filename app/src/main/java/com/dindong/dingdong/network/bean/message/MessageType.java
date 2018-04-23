package com.dindong.dingdong.network.bean.message;

/**
 * Created by wcong on 2018/4/22.
 * <p>
 * </>
 */

public enum MessageType {
  system("系统消息"), notice("公告");
  private String name;

  MessageType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
