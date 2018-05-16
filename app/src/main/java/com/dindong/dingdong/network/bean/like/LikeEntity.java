package com.dindong.dingdong.network.bean.like;

import java.io.Serializable;

/**
 * Created by wcong on 2018/5/17.
 * <p>
 * </>
 */

public class LikeEntity implements Serializable {
  private String id;
  private String time;// 发生时间
  private String type;// 类型
  private String userId;// 用户ID
  private String userImageUrl;// 用户头像URL

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserImageUrl() {
    return userImageUrl;
  }

  public void setUserImageUrl(String userImageUrl) {
    this.userImageUrl = userImageUrl;
  }
}
