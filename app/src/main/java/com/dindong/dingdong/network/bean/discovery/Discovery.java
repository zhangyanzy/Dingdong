package com.dindong.dingdong.network.bean.discovery;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.network.bean.entity.Ucn;

/**
 * Created by wcong on 2018/4/14.
 * <p>
 * 发现实体</>
 */

public class Discovery implements Serializable {
  private String id;
  private Ucn ucn;// 发布用户信息
  private Date date;// 发布日期
  private String deviceInfo;// 发布设备
  private String message;// 发布内容
  private List<GlobalImage> images;// 添加图片

  private BigDecimal shareCount = BigDecimal.ZERO;// 分享次数
  private List<Comment> comments = new ArrayList<>();// 评论
  private BigDecimal praiseCount = BigDecimal.ZERO;// 点赞次数
  private boolean isPraise = false;// 是否被当前用户点赞

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Ucn getUcn() {
    return ucn;
  }

  public void setUcn(Ucn ucn) {
    this.ucn = ucn;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getDeviceInfo() {
    return deviceInfo;
  }

  public void setDeviceInfo(String deviceInfo) {
    this.deviceInfo = deviceInfo;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<GlobalImage> getImages() {
    return images;
  }

  public void setImages(List<GlobalImage> images) {
    this.images = images;
  }

  public BigDecimal getShareCount() {
    return shareCount;
  }

  public void setShareCount(BigDecimal shareCount) {
    this.shareCount = shareCount;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  public BigDecimal getPraiseCount() {
    return praiseCount;
  }

  public void setPraiseCount(BigDecimal praiseCount) {
    this.praiseCount = praiseCount;
  }

  public boolean isPraise() {
    return isPraise;
  }

  public void setPraise(boolean praise) {
    isPraise = praise;
  }
}
