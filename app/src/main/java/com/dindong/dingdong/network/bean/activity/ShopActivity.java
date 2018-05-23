package com.dindong.dingdong.network.bean.activity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.network.bean.store.Shop;

/**
 * Created by wcong on 2018/5/24.
 * <p>
 * 门店活动 </>
 */

public class ShopActivity implements Serializable {
  private String id;// 活动ID
  private BigDecimal amount = BigDecimal.ZERO;// 活动现价/优惠价
  private BigDecimal originalAmount = BigDecimal.ZERO;// 活动原价
  private int boughtCount;// 活动购买量
  private String description;// 活动说明
  private boolean enable;// 活动状态
  private int favCount;// 活动收藏数
  private int qty;// 可购买数量
  private boolean favorite;// 当前用户是否关注
  private GlobalImage image;// 活动首图
  private List<GlobalImage> images;// 活动图片
  private String name;// 活动名称
  private Shop store;// 所属门店
  private String unit;// 单位
  private Date startTime;// 活动开始时间
  private Date endTime;// 活动结束时间
  private String memberNumber;// 已参与数

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

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public Shop getStore() {
    return store;
  }

  public void setStore(Shop store) {
    this.store = store;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public String getMemberNumber() {
    return memberNumber;
  }

  public void setMemberNumber(String memberNumber) {
    this.memberNumber = memberNumber;
  }
}
