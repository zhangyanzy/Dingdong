package com.dindong.dingdong.manager;

import com.dindong.dingdong.network.bean.auth.User;
import com.dindong.dingdong.network.bean.entity.Address;
import com.dindong.dingdong.network.bean.entity.Region;
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

  private static SessionAddress currentAdd;// 当前定位位置

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

    initDefaultAddress();
    // timer.schedule(new Task(), 100, 1000 * 60 * 60);
  }

  /**
   * 初始化默认城市
   */
  private static void initDefaultAddress() {
    SessionAddress address = new SessionAddress();
    Region city = new Region();
    city.setId("330500");
    city.setText("湖州市");
    address.setCity(city);
    address.setDistrict(city);
    address.setLongitude("120.106040");
    address.setLatitude("30.869070");
    currentAdd = address;
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

  public static void updateUser() {
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

  public static SessionAddress getCurrentAdd() {
    return currentAdd;
  }

  public static void setCurrentAdd(SessionAddress currentAdd) {
    SessionMgr.currentAdd = currentAdd;
  }

  public static class SessionAddress extends Address {
    private String longitude;// 当前经度
    private String latitude;// 当前纬度

    public String getLongitude() {
      return longitude;
    }

    public void setLongitude(String longitude) {
      this.longitude = longitude;
    }

    public String getLatitude() {
      return latitude;
    }

    public void setLatitude(String latitude) {
      this.latitude = latitude;
    }
  }
}
