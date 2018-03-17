package com.dindong.dingdong.presentation.main;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityLoginBinding;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.auth.usecase.LoginCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.auth.User;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.ToastUtil;

import android.content.Intent;
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

  private void login(String mobile, String password) {
    new LoginCase(mobile, password).execute(new HttpSubscriber<User>(LoginActivity.this) {
      @Override
      public void onFailure(String errorMsg, Response<User> response) {
        DialogUtil.getErrorDialog(LoginActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<User> response) {
        SessionMgr.updateUser(response.getData());
        ToastUtil.toastHint(LoginActivity.this, "登录成功");
        startActivity(new Intent(LoginActivity.this, IdentitySwitchActivity.class));
        finish();
      }
    });
  }

  public class Presenter {

    public void onLogin() {
      login(binding.edtMobile.getText().toString().trim(),
          binding.edtPassword.getText().toString().trim());
    }
  }
}
