package com.dindong.dingdong.presentation.main;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityLoginBinding;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.manager.ShortcutMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.auth.usecase.LoginCase;
import com.dindong.dingdong.network.api.auth.usecase.SendSmsCase;
import com.dindong.dingdong.network.api.auth.usecase.ShortLoginCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.auth.User;
import com.dindong.dingdong.presentation.main.fragment.UserAgreementActivity;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.FocusValidator;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.CountTimeTextView;
import com.wcong.validator.ValidateResultCall;
import com.wcong.validator.Validator;
import com.wcong.validator.rules.MinLengthRule;
import com.wcong.validator.rules.RequiredRule;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends BaseActivity {

  ActivityLoginBinding binding;

  private int loginType = 1;// 0-快捷登录，1-帐号密码登录

  private Validator shortValidator;// 快捷登录校验器
  private Validator normalValidator;// 正常登录校验器

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

    binding.ct.setViewType(CountTimeTextView.LayoutType.red);
    binding.cbAgreement.setChecked(true);
    switchLoginType(loginType);
    registerEditValidator();
    registerFocusValidator();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {

    if (SessionMgr.getUser() != null) {
      binding.edtShortMobile.setText(SessionMgr.getUser().getMobile());
      binding.edtMobile.setText(SessionMgr.getUser().getMobile());
    }

  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
  }

  /**
   * 注册表单校验器
   */
  private void registerEditValidator() {
    shortValidator = new Validator();
    shortValidator.register(binding.edtShortMobile,
        new RequiredRule(getString(R.string.login_empty_msg_mobile)),
        new MinLengthRule(11, getString(R.string.reset_mobile_tip_mobile_len)));
    shortValidator.register(binding.edtShortCode,
        new RequiredRule(getString(R.string.login_empty_msg_code)));

    normalValidator = new Validator();
    normalValidator.register(binding.edtMobile,
        new RequiredRule(getString(R.string.login_empty_msg_mobile)),
        new MinLengthRule(11, getString(R.string.reset_mobile_tip_mobile_len)));
    normalValidator.register(binding.edtPassword,
        new RequiredRule(getString(R.string.login_empty_msg_password)));
  }

  /**
   * 注册焦点校验
   */
  private void registerFocusValidator() {
    FocusValidator focusValidator = new FocusValidator();
    focusValidator.verify(binding.edtMobile, binding.imgMobile, binding.lineNormalMobile);// 正常登录手机号
    focusValidator.verify(binding.edtPassword, binding.imgPassword, binding.linePassword);// 正常登录密码
    focusValidator.verify(binding.edtShortMobile, binding.imgShortMobile, binding.lineShortMobile);// 快捷登录手机号
    focusValidator.verify(binding.edtShortCode, binding.imgShortCode, binding.lineCode);// 快捷登录验证码
  }

  /**
   * 切换登录方式
   * 
   * @param loginType
   */
  private void switchLoginType(int loginType) {
    this.loginType = loginType;
    boolean isShortLogin = loginType == 0;
    binding.txtShortLogin.getPaint().setFakeBoldText(isShortLogin);
    binding.txtShortLogin.postDelayed(new Runnable() {
      @Override
      public void run() {
        binding.txtShortLogin.invalidate();
      }
    }, 0);
    binding.txtNormalLogin.getPaint().setFakeBoldText(!isShortLogin);
    binding.txtNormalLogin.postDelayed(new Runnable() {
      @Override
      public void run() {
        binding.txtNormalLogin.invalidate();
      }
    }, 0);

    binding.indicatorShortLogin
        .setBackgroundColor(isShortLogin ? getResources().getColor(R.color.white) : 0);
    binding.layoutShortLogin.setVisibility(isShortLogin ? View.VISIBLE : View.GONE);
    binding.indicatorNormalLogin
        .setBackgroundColor(!isShortLogin ? getResources().getColor(R.color.white) : 0);
    binding.layoutNormalLogin.setVisibility(!isShortLogin ? View.VISIBLE : View.GONE);
  }

  /**
   * 帐号密码登录
   * 
   * @param mobile
   * @param password
   */
  private void login(String mobile, String password) {
    new LoginCase(mobile, password).execute(new HttpSubscriber<User>(LoginActivity.this) {
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
    new ShortLoginCase(mobile, authCode).execute(new HttpSubscriber<User>(LoginActivity.this) {
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
    new SendSmsCase(mobile, "3").execute(new HttpSubscriber<User>(LoginActivity.this) {
      @Override
      public void onFailure(String errorMsg, Response<User> response) {
        DialogUtil.getErrorDialog(LoginActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<User> response) {
        ToastUtil.toastSuccess(LoginActivity.this, getString(R.string.login_send_sms_success));
        binding.ct.startCount();
      }
    });
  }

  public class Presenter {

    /**
     * 登录方式切换
     * 
     * @param index
     */
    public void onLoginTypeSelect(int index) {
      switchLoginType(index);
    }

    public void onLogin(View view) {
      switch (loginType) {
      case 0:
        // 快捷登录
        shortValidator.validateAll(new ValidateResultCall() {
          @Override
          public void onSuccess() {
            if (!binding.cbAgreement.isChecked()) {
              ToastUtil.toastHint(LoginActivity.this, getString(R.string.login_tip_agreement));
              return;
            }
            shortLogin(binding.edtShortMobile.getText().toString().trim(),
                binding.edtShortCode.getText().toString().trim());
          }

          @Override
          public void onFailure(TextView view, String message) {
            ToastUtil.toastSuccess(LoginActivity.this, message);
          }
        });

        break;
      case 1:
        // 正常登录
        normalValidator.validateAll(new ValidateResultCall() {
          @Override
          public void onSuccess() {
            login(binding.edtMobile.getText().toString().trim(),
                binding.edtPassword.getText().toString().trim());
          }

          @Override
          public void onFailure(TextView view, String message) {
            ToastUtil.toastSuccess(LoginActivity.this, message);
          }
        });
        break;
      }
    }

    /**
     * 明密文切换
     */
    public void onVisible(View view) {
      view.setSelected(!view.isSelected());
      binding.edtPassword
          .setInputType(view.isSelected() ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
              : (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
      binding.edtPassword.setSelection(binding.edtPassword.getText().length());
    }

    public void onSendSms(View view) {
      if (IsEmpty.string(binding.edtShortMobile.getText().toString().trim())) {
        ToastUtil.toastHint(LoginActivity.this, getString(R.string.login_empty_msg_mobile));
        return;
      } else if (binding.edtShortMobile.getText().toString().trim().length() < 11) {
        ToastUtil.toastHint(LoginActivity.this, getString(R.string.login_len_mobile));
        return;
      }
      sendSms(binding.edtShortMobile.getText().toString().trim());
    }

    /**
     * 新用户注册
     */
    public void onRegister() {
      startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    /**
     * 用户协议
     */
    public void onAgreement() {
      Intent intent = new Intent(LoginActivity.this, UserAgreementActivity.class);
      intent.putExtra(AppConfig.IntentKey.URL, AppConfig.Http.AGREEMENT_USER_URL);
      startActivity(intent);
    }

    /**
     * 重置密码
     */
    public void onResetPassword() {
      Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
      String mobile = binding.edtMobile.getText().toString().trim();
      if (!IsEmpty.string(mobile) && mobile.length() == 11)
        intent.putExtra(AppConfig.IntentKey.DATA, mobile);
      startActivity(intent);
    }
  }
}
