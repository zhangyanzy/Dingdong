package com.dindong.dingdong.network.bean.groupbuy;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import com.dindong.dingdong.util.DateUtil;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * 团购实体 </>
 */

public class GroupBuy implements Serializable {
  private String billNo;// 团购单号
  private String endTime;// 结束时间
  private String id;// 团购ID
  private String itemId;// 商品ID
  private String itemName;// 商品名称
  private String itemType;// 商品类型 = ['course', 'goods', 'activity'],OrderType
  private String startTime;// 开团时间
  private String state;// 状态 = ['grouping', 'ready', 'cannceled']
  private String storeId;// 门店ID
  private String storeName;// 门店名称
  private String userAvatarUrl;// 团长头像URL
  private String userId;// 团长ID
  private String userName;// 团长名称
  private int groupOrderNumber;// 成团人数
  private int memberCount;// 已参团人数

  public String getBillNo() {
    return billNo;
  }

  public void setBillNo(String billNo) {
    this.billNo = billNo;
  }

  public Date getEndTime() {
    try {
      return DateUtil.parse(endTime, DateUtil.DEFAULT_DATE_FORMAT);
    } catch (ParseException e) {
      return new Date();
    }
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
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

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public String getStoreName() {
    return storeName;
  }

  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  public String getUserAvatarUrl() {
    return userAvatarUrl;
  }

  public void setUserAvatarUrl(String userAvatarUrl) {
    this.userAvatarUrl = userAvatarUrl;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public int getGroupOrderNumber() {
    return groupOrderNumber;
  }

  public void setGroupOrderNumber(int groupOrderNumber) {
    this.groupOrderNumber = groupOrderNumber;
  }

  public int getMemberCount() {
    return memberCount;
  }

  public void setMemberCount(int memberCount) {
    this.memberCount = memberCount;
  }
}
