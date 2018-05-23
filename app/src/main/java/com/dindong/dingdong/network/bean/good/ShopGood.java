package com.dindong.dingdong.network.bean.good;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.network.bean.store.Shop;

/**
 * Created by wcong on 2018/5/24.
 * <p>
 * 门店商品实体</>
 */

public class ShopGood implements Serializable {
  private String id;// ID
  private BigDecimal amount = BigDecimal.ZERO;// 商品现价/优惠价
  private BigDecimal originalAmount = BigDecimal.ZERO;// 活动原价
  private int boughtCount;// 商品购买量
  private String description;// 商品说明
  private boolean enable;// 商品状态
  private int favCount;// 收藏数
  private int qty;// 可购买数量
  private boolean favorite;// 当前用户是否关注
  private GlobalImage image;// 首图
  private List<GlobalImage> images;// 商品图片
  private String name;// 商品名称
  private Shop store;// 所属门店
  private String unit;// 单位
  private String memberNumber;// 已购买数

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public int getBoughtCount() {
    return boughtCount;
  }

  public void setBoughtCount(int boughtCount) {
    this.boughtCount = boughtCount;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public int getFavCount() {
    return favCount;
  }

  public void setFavCount(int favCount) {
    this.favCount = favCount;
  }

  public int getQty() {
    return qty;
  }

  public void setQty(int qty) {
    this.qty = qty;
  }

  public boolean isFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
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

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getMemberNumber() {
    return memberNumber;
  }

  public void setMemberNumber(String memberNumber) {
    this.memberNumber = memberNumber;
  }
}
