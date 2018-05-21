package com.dindong.dingdong.network.bean.favorite;

/**
 * Created by wcong on 2018/5/22.
 * <p>
 * 用户关注相关</>
 */

public class FavoriteStatic {
  private int courseCount;// 关注课程数量
  private int fansCount;// 粉丝数量
  private int memberCount;// 关注会员数量
  private int storeCount;// 关注门店数量

  public int getCourseCount() {
    return courseCount;
  }

  public void setCourseCount(int courseCount) {
    this.courseCount = courseCount;
  }

  public int getFansCount() {
    return fansCount;
  }

  public void setFansCount(int fansCount) {
    this.fansCount = fansCount;
  }

  public int getMemberCount() {
    return memberCount;
  }

  public void setMemberCount(int memberCount) {
    this.memberCount = memberCount;
  }

  public int getStoreCount() {
    return storeCount;
  }

  public void setStoreCount(int storeCount) {
    this.storeCount = storeCount;
  }
}
