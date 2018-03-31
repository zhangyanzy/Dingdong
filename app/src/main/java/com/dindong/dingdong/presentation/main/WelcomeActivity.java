package com.dindong.dingdong.presentation.main;

import com.dindong.dingdong.R;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.manager.ShortcutMgr;
import com.dindong.dingdong.manager.TokenMgr;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class WelcomeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);// 取消标题栏
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
    setContentView(R.layout.activity_welcome);


    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {

        // token过期或未登录去登录，否则直接进入主界面
        if (TokenMgr.isExpired() || SessionMgr.getUser() == null) {
          ShortcutMgr.logout();
        } else {
          ShortcutMgr.login(SessionMgr.getUser());
        }
      }
    }, 1000);

  }
}
