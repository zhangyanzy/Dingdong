package com.dindong.dingdong.config;

/**
 * Created by wcong on 2018/3/10.
 */

public class AppConfig {

  public static class Http {
    public static final String BASE_URL = "http://console.dingdongbanxue.com/app/api/";

    public static final String AGREEMENT_USER_URL = "http://terms.dingdongbanxue.com/legal-agreement/terms/person.html";// 用户协议
    public static final String AGREEMENT_INSTITUTION_URL = "http://terms.dingdongbanxue.com/legal-agreement/terms/institution.html";// 机构入驻条款
  }

  public static class IntentKey {
    public static final String DATA = "data";
    public static final String SUMMARY = "summary";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ID = "id";
    public static final String URL = "url";
  }

  // 第三方申请key
  public static class PartyKey {
    public static final String GD_MAP = "c6b87d66ff3941cb8f1d63d69807b5e4";// 高德地图
    public static final String WEX_PAY = "wx5b0c56b85d8488f3";// 微信支付
  }

  public static class Wrist {
    public static final String BASE_RULE = "http://www.dingdongbanxue.com/lsh/";// 蓝手环二维码前缀
  }
}
