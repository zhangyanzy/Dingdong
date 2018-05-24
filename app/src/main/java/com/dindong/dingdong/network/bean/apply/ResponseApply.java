package com.dindong.dingdong.network.bean.apply;

/**
 * Created by wcong on 2018/5/25.
 * <p>
 * 合作申请回复实体</>
 */

public class ResponseApply {
  private String id;// 申请ID
  private String billNo;// 申请编号

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getBillNo() {
    return billNo;
  }

  public void setBillNo(String billNo) {
    this.billNo = billNo;
  }
}
