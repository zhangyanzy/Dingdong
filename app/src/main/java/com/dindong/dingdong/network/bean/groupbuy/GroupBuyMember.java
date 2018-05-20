package com.dindong.dingdong.network.bean.groupbuy;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * 团购成员 </>
 */

public class GroupBuyMember {
  private String avatarUrl;// 会员头像URL
  private String id;// 会员ID
  private String name;// 会员名称
  private boolean master;// 是否团长

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

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

  public boolean isMaster() {
    return master;
  }

  public void setMaster(boolean master) {
    this.master = master;
  }
}
