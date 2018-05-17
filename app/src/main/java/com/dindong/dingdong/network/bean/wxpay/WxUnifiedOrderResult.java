package com.dindong.dingdong.network.bean.wxpay;

/**
 * Created by wcong on 2018/5/18.
 * <p>
 * 微信下单实体</>
 */

public class WxUnifiedOrderResult {
  private String appid;
  private String noncestr;// 随机字符串
  private String package2;// 扩展字段
  private String partnerid;// 商户ID
  private String prepayid;// 预支付交易会话
  private String sign;// 签名
  private String timestamp;// 时间戳

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getNoncestr() {
    return noncestr;
  }

  public void setNoncestr(String noncestr) {
    this.noncestr = noncestr;
  }

  public String getPackage2() {
    return package2;
  }

  public void setPackage2(String package2) {
    this.package2 = package2;
  }

  public String getPartnerid() {
    return partnerid;
  }

  public void setPartnerid(String partnerid) {
    this.partnerid = partnerid;
  }

  public String getPrepayid() {
    return prepayid;
  }

  public void setPrepayid(String prepayid) {
    this.prepayid = prepayid;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
}
