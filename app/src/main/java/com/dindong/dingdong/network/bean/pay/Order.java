package com.dindong.dingdong.network.bean.pay;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.dindong.dingdong.network.bean.store.Subject;

/**
 * Created by wangcong on 2018/3/22.
 * <p>
 * 课程订单
 */

public class Order implements Serializable {
  private String id;// UUID
  private Date date;// 订单创建时间
  private String orderNum;// 订单号

  private OrderType orderType;// 订单类型 当前版本只支持门店课程
  private OrderState orderState;// 订单状态
  private BigDecimal price = BigDecimal.ZERO;// 商品现价
  private BigDecimal originalPrice = BigDecimal.ZERO;// 商品原价
  private BigDecimal count = BigDecimal.ZERO;// 购买数量
  private BigDecimal totalAmount = BigDecimal.ZERO;// 商品总额
  private BigDecimal payAmount = BigDecimal.ZERO;// 支付金额
  private BigDecimal favourAmount = BigDecimal.ZERO;// 优惠金额
  private PayMode payMode;// 支付方式
  private String code;// 销核码

  // 具体订单商品信息，现只支持课程购买，后续增加商品购买
  private Subject subject;

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

  public String getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(String orderNum) {
    this.orderNum = orderNum;
  }

  public OrderType getOrderType() {
    return orderType;
  }

  public void setOrderType(OrderType orderType) {
    this.orderType = orderType;
  }

  public OrderState getOrderState() {
    return orderState;
  }

  public void setOrderState(OrderState orderState) {
    this.orderState = orderState;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getOriginalPrice() {
    return originalPrice;
  }

  public void setOriginalPrice(BigDecimal originalPrice) {
    this.originalPrice = originalPrice;
  }

  public BigDecimal getCount() {
    return count;
  }

  public void setCount(BigDecimal count) {
    this.count = count;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public BigDecimal getPayAmount() {
    return payAmount;
  }

  public void setPayAmount(BigDecimal payAmount) {
    this.payAmount = payAmount;
  }

  public BigDecimal getFavourAmount() {
    return favourAmount;
  }

  public void setFavourAmount(BigDecimal favourAmount) {
    this.favourAmount = favourAmount;
  }

  public PayMode getPayMode() {
    return payMode;
  }

  public void setPayMode(PayMode payMode) {
    this.payMode = payMode;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }
}
