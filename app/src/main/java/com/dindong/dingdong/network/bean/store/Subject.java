package com.dindong.dingdong.network.bean.store;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.network.bean.entity.Tag;

/**
 * Created by wcong on 2018/3/13. 门店课程
 */

public class Subject implements Serializable {
  private String id;// UUID
  private String name;// 课程名称
  private SubjectType subjectType;// 课程类型
  private Shop store;// 所属门店
  private GlobalImage image;// 课程主图
  private List<GlobalImage> images = new ArrayList<>();// 课程图片
  private List<Tag> tags = new ArrayList<>();// 课程分类

  private BigDecimal amount = BigDecimal.ZERO;// 课程价格，现价
  private BigDecimal originalAmount = BigDecimal.ZERO;// 课程价格，原价
  private BigDecimal qty = BigDecimal.ZERO;// 数量
  private String unit;// 价格单位即课时，如果10课节
  private int favCount = 0;// 收藏数
  private BigDecimal boughtCount = BigDecimal.ZERO;// 购买量
  private boolean prove = false;
  private boolean favorite = false;// 当前用户是否关注
  private int memberNumber;// 已购买数
  private String description;// 课程说明

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SubjectType getSubjectType() {
    return subjectType;
  }

  public void setSubjectType(SubjectType subjectType) {
    this.subjectType = subjectType;
  }

  public Shop getStore() {
    return store;
  }

  public void setStore(Shop store) {
    this.store = store;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public BigDecimal getOriginalAmount() {
    return originalAmount;
  }

  public void setOriginalAmount(BigDecimal originalAmount) {
    this.originalAmount = originalAmount;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public int getFavCount() {
    return favCount;
  }

  public void setFavCount(int favCount) {
    this.favCount = favCount;
  }

  public BigDecimal getBoughtCount() {
    return boughtCount;
  }

  public void setBoughtCount(BigDecimal boughtCount) {
    this.boughtCount = boughtCount;
  }

  public GlobalImage getImage() {
    return image;
  }

  public void setImage(GlobalImage image) {
    this.image = image;
  }

  public List<GlobalImage> getImages() {
    return images;
  }

  public void setImages(List<GlobalImage> images) {
    this.images = images;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public boolean isProve() {
    return prove;
  }

  public void setProve(boolean prove) {
    this.prove = prove;
  }

  public boolean isFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }

  public int getMemberNumber() {
    return memberNumber;
  }

  public void setMemberNumber(int memberNumber) {
    this.memberNumber = memberNumber;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
