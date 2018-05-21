package com.dindong.dingdong;

import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.listener.ActivityLifecycleListener;
import com.dindong.dingdong.manager.LocationMgr;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.manager.StorageMgr;
import com.dindong.dingdong.manager.TokenMgr;
import com.dindong.dingdong.network.ApiClient;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

/**
 * Created by wcong on 2018/3/10.
 */

public class DDApp extends MultiDexApplication {

  private static DDApp instance;

  public static DDApp getInstance() {
    return instance;
  }

  @Override
  public void onCreate() {
    super.onCreate();

    instance = this;
    // 初始化网络组件
    ApiClient.init(this, AppConfig.Http.BASE_URL);
    // 初始化缓存组件
    StorageMgr.init(this);
    // 初始化会话组件
    SessionMgr.init(this);
    // 初始化定位服务
    LocationMgr.init(this);
    // 初始化cookie
    TokenMgr.init();

    listenForForeground();
  }

  @Override
  public void onTerminate() {
    super.onTerminate();
    LocationMgr.destroyLocation();
  }

  private void listenForForeground() {
    registerActivityLifecycleCallbacks(new ActivityLifecycleListener() {
      @Override
      public void onActivityDestroyed(Activity activity) {
        super.onActivityDestroyed(activity);
//        LocationMgr.stopLocation();
        Log.i("registerActivityLifecycleCallbacks","onActivityDestroyed");
      }
    });
  }

}
