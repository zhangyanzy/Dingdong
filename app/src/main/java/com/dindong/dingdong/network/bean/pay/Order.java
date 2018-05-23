package com.dindong.dingdong.network.bean.pay;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.dindong.dingdong.network.bean.store.Shop;

/**
 * Created by wangcong on 2018/3/22.
 * <p>
 * 课程订单
 */

public class Order implements Serializable {
  private String id;// UUID
  private Date date;// 订单创建时间
  private String billNo;// 订单号

  private String state;// 订单状态
  private String stateName;// 订单状态名称
  private BigDecimal disPrice = BigDecimal.ZERO;// 商品现价
  private BigDecimal price = BigDecimal.ZERO;// 商品原价
  private BigDecimal qty = BigDecimal.ZERO;// 购买数量
  private BigDecimal realAmount = BigDecimal.ZERO;// 商品总额
  private String payMode;// 支付方式
  private String checkCode;// 销核码
  private Shop store;

  // 商品信息
  private String itemId;// 课程/商品/活动ID
  private String itemImageUrl;// 课程/商品/活动图片
  private String itemName;// 课程/商品/活动名称
  private String itemType;// 类型：课程/商品/活动
  private String itemTypeName;// 订单类型名称
  private boolean groupBuyOrder;// 是否团购订单

  private String userId;// 会员用户ID

  private String groupBuyId;// 参团ID

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getBillNo() {
    return billNo;
  }

  public void setBillNo(String billNo) {
    this.billNo = billNo;
  }

  public String getItemTypeName() {
    return itemTypeName;
  }

  public void setItemTypeName(String itemTypeName) {
    this.itemTypeName = itemTypeName;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public BigDecimal getDisPrice() {
    return disPrice;
  }

  public void setDisPrice(BigDecimal disPrice) {
    this.disPrice = disPrice;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public BigDecimal getRealAmount() {
    return realAmount;
  }

  public void setRealAmount(BigDecimal realAmount) {
    this.realAmount = realAmount;
  }

  public String getPayMode() {
    return payMode;
  }

  public void setPayMode(String payMode) {
    this.payMode = payMode;
  }

  public String getCheckCode() {
    return checkCode;
  }

  public void setCheckCode(String checkCode) {
    this.checkCode = checkCode;
  }

  public String getStateName() {
    return stateName;
  }

  public void setStateName(String stateName) {
    this.stateName = stateName;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public String getItemImageUrl() {
    return itemImageUrl;
  }

  public void setItemImageUrl(String itemImageUrl) {
    this.itemImageUrl = itemImageUrl;
  }

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public String getItemType() {
    return itemType;
  }

  public void setItemType(String itemType) {
    this.itemType = itemType;
  }

  public Shop getStore() {
    return store;
  }

  public void setStore(Shop store) {
    this.store = store;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getGroupBuyId() {
    return groupBuyId;
  }

  public void setGroupBuyId(String groupBuyId) {
    this.groupBuyId = groupBuyId;
  }

  public boolean isGroupBuyOrder() {
    return groupBuyOrder;
  }

  public void setGroupBuyOrder(boolean groupBuyOrder) {
    this.groupBuyOrder = groupBuyOrder;
  }
}
