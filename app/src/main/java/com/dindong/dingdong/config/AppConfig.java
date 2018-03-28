package com.dindong.dingdong.config;

/**
 * Created by wcong on 2018/3/10.
 */

public class AppConfig {

  public static class Http {
    public static final String BASE_URL = "http://console.dingdongbanxue.com/";
  }

  public static class IntentKey {
    public static final String DATA = "data";
  }

  // 第三方申请key
  public static class PartyKey {
    public static final String GD_MAP = "c6b87d66ff3941cb8f1d63d69807b5e4";// 高德地图
  }

  public static class Wrist {
    public static final String BASE_RULE = "http://www.dingdongbanxue.com/lsh/";// 蓝手环二维码前缀
  }
}
