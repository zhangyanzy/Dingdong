package com.dindong.dingdong;

import android.support.multidex.MultiDexApplication;

import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.manager.StorageMgr;
import com.dindong.dingdong.network.ApiClient;

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

        // 初始化网络组件
        ApiClient.init(this, AppConfig.Http.BASE_URL);
        // 初始化缓存组件
        StorageMgr.init(this);
        // 初始化会话组件
        SessionMgr.init(this);
    }
}
