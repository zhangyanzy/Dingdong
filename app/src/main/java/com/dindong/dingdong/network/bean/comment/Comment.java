package com.dindong.dingdong.network.bean.comment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.network.bean.entity.Ucn;
import com.dindong.dingdong.network.bean.store.Shop;

/**
 * Created by wcong on 2018/4/14.
 * <p>
 * 评论/回复实体 </>
 */

public class Comment implements Serializable {
  private String id;
  private String relationId;// 关联关系ID
  private String storeId;// 所属门店Id
  private String orderId;// 所属订单Id
  private Ucn ucn;// 评论/回复人信息
  private String message;// 评论/回复内容
  private Date date;// 评论/回复时间 格式:YYYY-MM-DD HH:mm:ss
  private List<Comment> comments = new ArrayList<>();// 如果当前为评论实体，则可能包含回复
  private int commentCount = 0;// 评论数
  private List<GlobalImage> images;// 添加
  private boolean isPraise = false;// 是否被当前用户点赞
  private int praiseCount = 0;// 点赞次数
  private BigDecimal rating = BigDecimal.ZERO;// 评分
  private int shareCount = 0;// 分享次数
  private int viewCount = 0;// 浏览量
  private Shop store;

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

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  public String getRelationId() {
    return relationId;
  }

  public void setRelationId(String relationId) {
    this.relationId = relationId;
  }

  public int getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(int commentCount) {
    this.commentCount = commentCount;
  }

  public List<GlobalImage> getImages() {
    return images;
  }

  public void setImages(List<GlobalImage> images) {
    this.images = images;
  }

  public boolean isPraise() {
    return isPraise;
  }

  public void setPraise(boolean praise) {
    isPraise = praise;
  }

  public int getPraiseCount() {
    return praiseCount;
  }

  public void setPraiseCount(int praiseCount) {
    this.praiseCount = praiseCount;
  }

  public BigDecimal getRating() {
    return rating;
  }

  public void setRating(BigDecimal rating) {
    this.rating = rating;
  }

  public int getShareCount() {
    return shareCount;
  }

  public void setShareCount(int shareCount) {
    this.shareCount = shareCount;
  }

  public int getViewCount() {
    return viewCount;
  }

  public void setViewCount(int viewCount) {
    this.viewCount = viewCount;
  }

  public Shop getStore() {
    return store;
  }

  public void setStore(Shop store) {
    this.store = store;
  }

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
}
