package com.dindong.dingdong.presentation.main;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityLoginBinding;
import com.dindong.dingdong.manager.ShortcutMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.auth.usecase.LoginCase;
import com.dindong.dingdong.network.api.auth.usecase.SendSmsCase;
import com.dindong.dingdong.network.api.auth.usecase.ShortLoginCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.auth.User;
import com.dindong.dingdong.util.DialogUtil;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

public class LoginActivity extends BaseActivity {

  ActivityLoginBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {

  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
  }

  /**
   * 帐号密码登录
   * 
   * @param mobile
   * @param password
   */
  private void login(String mobile, String password) {
    new LoginCase("13761140966", "admin").execute(new HttpSubscriber<User>(LoginActivity.this) {
      @Override
      public void onFailure(String errorMsg, Response<User> response) {
        DialogUtil.getErrorDialog(LoginActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<User> response) {
        ShortcutMgr.login(response.getData());
      }
    });
  }

  /**
   * 快捷登录
   * 
   * @param mobile
   * @param authCode
   */
  private void shortLogin(String mobile, String authCode) {
    new ShortLoginCase(mobile, authCode)
        .execute(new HttpSubscriber<User>(LoginActivity.this) {
          @Override
          public void onFailure(String errorMsg, Response<User> response) {
            DialogUtil.getErrorDialog(LoginActivity.this, errorMsg).show();
          }

          @Override
          public void onSuccess(Response<User> response) {
            ShortcutMgr.login(response.getData());
          }
        });
  }

  private void sendSms(String mobile) {
    new SendSmsCase(mobile).execute(new HttpSubscriber<User>(LoginActivity.this) {
      @Override
      public void onFailure(String errorMsg, Response<User> response) {
        DialogUtil.getErrorDialog(LoginActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<User> response) {

      }
    });
  }

  public class Presenter {

    public void onLogin() {
      login(binding.edtMobile.getText().toString().trim(),
          binding.edtPassword.getText().toString().trim());
    }

    public void onShortLogin() {
      shortLogin("15710129773",
              "888888");
    }

    public void onSendSms() {
      sendSms("15710129773");
    }
  }
}
