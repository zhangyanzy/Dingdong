package com.dindong.dingdong.presentation.user;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivitySettingBinding;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.manager.ShortcutMgr;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

public class SettingActivity extends BaseActivity {

  ActivitySettingBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    binding.txtIdentity.setText(StringUtil.format(getString(R.string.setting_current_identity),
        SessionMgr.getUser().getIdentities().get(0).getName()));
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {

  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
    binding.nb
        .setNavigationTopBarClickListener(new NavigationTopBar.NavigationTopBarClickListener() {
          @Override
          public void leftImageClick() {
            finish();
          }
        });
  }

  public class Presenter {

    /**
     * 个人信息
     * 
     * @param view
     */
    public void onUserInfo(View view) {
      startActivity(new Intent(SettingActivity.this, UserInfoActivity.class));
    }

    /**
     * 更换手机号
     * 
     * @param view
     */
    public void onResetMobile(View view) {
      startActivity(new Intent(SettingActivity.this, ResetMobileActivity.class));
    }

    /**
     * 修改密码
     * 
     * @param view
     */
    public void onResetPassword(View view) {
      startActivity(new Intent(SettingActivity.this, ResetPasswordActivity.class));
    }

    /**
     * 退出登录
     * 
     * @param view
     */
    public void onLogout(View view) {
      ShortcutMgr.logout();
    }
  }
}
