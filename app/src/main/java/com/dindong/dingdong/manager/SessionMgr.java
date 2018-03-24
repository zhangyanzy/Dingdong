package com.dindong.dingdong.manager;

import com.dindong.dingdong.network.bean.auth.User;
import com.dindong.dingdong.util.IsEmpty;

import android.content.Context;

/**
 * Created by wcong on 2018/3/9. 用户信息管理类
 */

public class SessionMgr {
  private static String KEY_SESSION = "session";
  private static AuthSession session;// 会话信息
  // private static Timer timer;
  private static Context mContext;

  private static String currentProvince = "湖州市";// 当前定位城市

  // 初始化
  public static void init(Context context) {
    mContext = context;
    // timer = new Timer(true);
    if (IsEmpty.object(session)) {
      session = new AuthSession();
    }
    session = StorageMgr.get(KEY_SESSION, AuthSession.class, StorageMgr.LEVEL_GLOBAL);
    if (IsEmpty.object(session)) {
      session = new AuthSession();
    }
    // timer.schedule(new Task(), 100, 1000 * 60 * 60);
  }

  // 更新 Session
  public static void updateSession(AuthSession session2) {
    session = session2;
    StorageMgr.set(KEY_SESSION, session, StorageMgr.LEVEL_GLOBAL);
  }

  // 更新 Session.user
  public static void updateUser(User user) {
    session.setUser(user);
    StorageMgr.set(KEY_SESSION, session, StorageMgr.LEVEL_GLOBAL);
  }

  // 获取 Session
  public static AuthSession getSession() {
    return session;
  }

  // 获取 Session.user
  public static User getUser() {
    return session.getUser();
  }

  public static String getCurrentProvince() {
    return currentProvince;
  }

  public static void setCurrentProvince(String currentProvince) {
    SessionMgr.currentProvince = currentProvince;
  }
}
