package com.dindong.dingdong.manager;

import com.dindong.dingdong.DDApp;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.network.bean.auth.User;
import com.dindong.dingdong.presentation.main.LoginActivity;
import com.dindong.dingdong.presentation.main.MainActivity;

import android.content.Intent;

/**
 * Created by wcong on 2018/3/23.
 * <p>
 * 用户快捷操作</>
 */

public class ShortcutMgr {

  /**
   * 登录成功调用，进入主界面
   */
  public static void login(User user) {
    SessionMgr.updateUser(user);
    Intent intent = new Intent(DDApp.getInstance(), MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra(AppConfig.IntentKey.DATA, user.getIdentities().get(0));
    DDApp.getInstance().startActivity(intent);
  }

  /**
   * 用户登出
   */
  public static void logout() {
    TokenMgr.clear();
    Intent intent = new Intent(DDApp.getInstance(), LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    DDApp.getInstance().startActivity(intent);
  }
}
