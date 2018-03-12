package com.dindong.dingdong.manager;

/**
 * Created by wcong on 2018/3/12.
 */
public class TokenMgr {

  // key
  private static String KEY_TOKEN = "Token";
  private static String token;

  public static void init() {
    token = StorageMgr.get(KEY_TOKEN, StorageMgr.LEVEL_GLOBAL);
  }

  // 清除Token
  public static void clear() {
    token = null;
    StorageMgr.set(KEY_TOKEN, null, StorageMgr.LEVEL_GLOBAL);
  }

  // 更新Token
  public static void updateToken(String token) {
    TokenMgr.token = token;
    StorageMgr.set(KEY_TOKEN, token, StorageMgr.LEVEL_GLOBAL);
  }

  // 获取Token
  public static String getToken() {
    return token;
  }

  // 判断Token是否过期
  public static boolean isExpired() {
    return token == null;
  }
}
