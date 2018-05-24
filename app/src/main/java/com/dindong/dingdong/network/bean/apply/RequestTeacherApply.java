package com.dindong.dingdong.network.bean.apply;

/**
 * Created by wcong on 2018/5/25.
 * <p>
 * 申请合作</>
 */

public class RequestTeacherApply extends RequestApply {
  private String profile;// 个人描述

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }
}
