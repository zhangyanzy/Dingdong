package com.dindong.dingdong.network.bean.apply;

import java.io.Serializable;

/**
 * Created by wcong on 2018/5/25.
 * <p>
 * 申请合作</>
 */

public class RequestApply implements Serializable {

  private String idCardId1;// 身份证正面照ID
  private String idCardId2;// 身份证反面照ID
  private String idCardUrl1;// 身份证正面照URL
  private String idCardUrl2;// 身份证反面照URL

  public String getIdCardId1() {
    return idCardId1;
  }

  public void setIdCardId1(String idCardId1) {
    this.idCardId1 = idCardId1;
  }

  public String getIdCardId2() {
    return idCardId2;
  }

  public void setIdCardId2(String idCardId2) {
    this.idCardId2 = idCardId2;
  }

  public String getIdCardUrl1() {
    return idCardUrl1;
  }

  public void setIdCardUrl1(String idCardUrl1) {
    this.idCardUrl1 = idCardUrl1;
  }

  public String getIdCardUrl2() {
    return idCardUrl2;
  }

  public void setIdCardUrl2(String idCardUrl2) {
    this.idCardUrl2 = idCardUrl2;
  }
}
