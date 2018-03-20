package com.dindong.dingdong.network.bean.shop;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.network.bean.entity.GlobalImage;

/**
 * Created by wcong on 2018/3/13. 门店课程
 */

public class Subject implements Serializable {
  private String id;// UUID
  private String name;// 课程名称
  private SubjectType subjectType;// 课程类型
  private Shop shop;// 所属门店
  private List<GlobalImage> images = new ArrayList<>();// 课程图片

  private BigDecimal amount = BigDecimal.ZERO;// 课程价格，现价
  private BigDecimal originalAmount = BigDecimal.ZERO;// 课程价格，原价
  private String unit;// 价格单位即课时，如果10课节
  private BigDecimal favCount = BigDecimal.ZERO;// 收藏数
  private BigDecimal boughtCount = BigDecimal.ZERO;// 购买量

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

  public Shop getShop() {
    return shop;
  }

  public void setShop(Shop shop) {
    this.shop = shop;
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

  public BigDecimal getFavCount() {
    return favCount;
  }

  public void setFavCount(BigDecimal favCount) {
    this.favCount = favCount;
  }

  public BigDecimal getBoughtCount() {
    return boughtCount;
  }

  public void setBoughtCount(BigDecimal boughtCount) {
    this.boughtCount = boughtCount;
  }

  public List<GlobalImage> getImages() {
    return images;
  }

  public void setImages(List<GlobalImage> images) {
    this.images = images;
  }
}
