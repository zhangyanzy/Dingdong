package com.dindong.dingdong.network.bean.pay;

/**
 * Created by wcong on 2018/5/23.
 * <p>
 * 统计订单各状态订单数量</>
 */

public class OrderStatictis {
  private int waitPayCount;// 待付款订单数
  private int confirmedCount;// 可使用订单数
  private int groupingCount;// 拼团中订单数
  private int waitCommentCount;// 待评价订单数

  public int getWaitPayCount() {
    return waitPayCount;
  }

  public void setWaitPayCount(int waitPayCount) {
    this.waitPayCount = waitPayCount;
  }

  public int getConfirmedCount() {
    return confirmedCount;
  }

  public void setConfirmedCount(int confirmedCount) {
    this.confirmedCount = confirmedCount;
  }

  public int getGroupingCount() {
    return groupingCount;
  }

  public void setGroupingCount(int groupingCount) {
    this.groupingCount = groupingCount;
  }

  public int getWaitCommentCount() {
    return waitCommentCount;
  }

  public void setWaitCommentCount(int waitCommentCount) {
    this.waitCommentCount = waitCommentCount;
  }
}
