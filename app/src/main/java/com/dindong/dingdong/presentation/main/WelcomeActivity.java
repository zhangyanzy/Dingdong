package com.dindong.dingdong.presentation.main;

import com.dindong.dingdong.R;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.manager.ShortcutMgr;
import com.dindong.dingdong.manager.TokenMgr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    ShortcutMgr.logout();
    // token过期或未登录去登录，否则直接进入主界面
//    if (TokenMgr.isExpired() || SessionMgr.getUser() == null) {
//      ShortcutMgr.logout();
//    } else {
//      ShortcutMgr.login(SessionMgr.getUser());
//    }
  }
}
