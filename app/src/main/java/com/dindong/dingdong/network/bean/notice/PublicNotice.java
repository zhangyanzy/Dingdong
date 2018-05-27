package com.dindong.dingdong.network.bean.notice;

import java.io.Serializable;

/**
 * Created by wcong on 2018/5/28.
 * <p>
 * </>
 */

public class PublicNotice implements Serializable {
  private String content;// 内容
  private String profile;// 概要
  private String publishTime;// 发布时间
  private String publisher;// 发布人
  private String title;// 公告标题

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public String getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(String publishTime) {
    this.publishTime = publishTime;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
