package com.dindong.dingdong.network.bean.store;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.network.bean.entity.GlobalImage;

/**
 * Created by wcong on 2018/4/14.
 * <p>
 * 门店商品 </>
 */

public class ShopGood implements Serializable {
  private String id;// 商品id
  private String name;// 商品名称
  private Shop store;// 所属门店
  private List<GlobalImage> images = new ArrayList<>();// 商品图片

  private BigDecimal amount = BigDecimal.ZERO;// 商品价格，现价
  private BigDecimal originalAmount = BigDecimal.ZERO;// 商品价格，原价
  private BigDecimal qty = BigDecimal.ZERO;// 数量
  private String unit;// 价格单位
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

  public Shop getStore() {
    return store;
  }

  public void setStore(Shop store) {
    this.store = store;
  }

  public List<GlobalImage> getImages() {
    return images;
  }

  public void setImages(List<GlobalImage> images) {
    this.images = images;
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

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
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
}
