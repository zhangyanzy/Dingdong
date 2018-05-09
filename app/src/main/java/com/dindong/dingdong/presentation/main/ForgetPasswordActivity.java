package com.dindong.dingdong.presentation.main;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityForgetPasswordBinding;
import com.dindong.dingdong.manager.ShortcutMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.auth.usecase.ResetPasswordCase;
import com.dindong.dingdong.network.api.auth.usecase.SendSmsCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.auth.User;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.FocusValidator;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.CountTimeTextView;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.wcong.validator.ValidateResultCall;
import com.wcong.validator.Validator;
import com.wcong.validator.rules.MinLengthRule;
import com.wcong.validator.rules.RequiredRule;
import com.wcong.validator.rules.UniformityRule;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

/**
 * 忘记密码
 */
public class ForgetPasswordActivity extends BaseActivity {
  ActivityForgetPasswordBinding binding;

  private Validator validator;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    binding.ct.setViewType(CountTimeTextView.LayoutType.red);
    registerEditValidator();
    registerFocusValidator();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (!IsEmpty.string(getIntent().getStringExtra(AppConfig.IntentKey.DATA))) {
      binding.edtMobile.setText(getIntent().getStringExtra(AppConfig.IntentKey.DATA));
    }
  }

  @Override
  protected void createEventHandlers() {
    binding.nb
        .setNavigationTopBarClickListener(new NavigationTopBar.NavigationTopBarClickListener() {
          @Override
          public void leftImageClick() {
            finish();
          }
        });
    binding.setPresenter(new Presenter());
  }

  /**
   * 注册表单校验器
   */
  private void registerEditValidator() {
    validator = new Validator();
    validator.register(binding.edtMobile,
        new RequiredRule(getString(R.string.login_empty_msg_mobile)),
        new MinLengthRule(11, getString(R.string.reset_mobile_tip_mobile_len)));
    validator.register(binding.edtCode, new RequiredRule(getString(R.string.login_empty_msg_code)));
    validator.register(binding.edtPassword,
        new RequiredRule(getString(R.string.login_empty_msg_password)),
        new MinLengthRule(6, getString(R.string.register_tip_password)));
    validator.register(binding.edtConfirmPassword,
        new RequiredRule(getString(R.string.reset_password_tip_confirm_password)),
        new MinLengthRule(6, getString(R.string.register_tip_password)),
        new UniformityRule(binding.edtPassword, getString(R.string.reset_password_tip_uniformity)));

  }

  /**
   * 注册焦点校验
   */
  private void registerFocusValidator() {
    FocusValidator focusValidator = new FocusValidator();
    focusValidator.verify(binding.edtMobile, binding.imgMobile, binding.lineMobile);
    focusValidator.verify(binding.edtCode, binding.imgCode, binding.lineCode);
    focusValidator.verify(binding.edtPassword, binding.imgPassword, binding.linePassword);
    focusValidator.verify(binding.edtConfirmPassword, binding.imgConfirmPassword,
        binding.lineConfirmPassword);
  }

  /**
   * 发送短信
   *
   * @param mobile
   */
  private void sendSms(String mobile) {
    new SendSmsCase(mobile, "2").execute(new HttpSubscriber<User>(ForgetPasswordActivity.this) {
      @Override
      public void onFailure(String errorMsg, Response<User> response) {
        DialogUtil.getErrorDialog(ForgetPasswordActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<User> response) {
        ToastUtil.toastSuccess(ForgetPasswordActivity.this,
            getString(R.string.login_send_sms_success));
        binding.ct.startCount();
      }
    });
  }

  /**
   * 重置密码
   */
  private void resetPassword() {
    new ResetPasswordCase(binding.edtMobile.getText().toString().trim(),
        binding.edtPassword.getText().toString().trim(),
        binding.edtCode.getText().toString().trim()).execute(new HttpSubscriber<Object>(this) {

          @Override
          public void onFailure(String errorMsg, Response<Object> response) {
            DialogUtil.getErrorDialog(ForgetPasswordActivity.this, errorMsg).show();
          }

          @Override
          public void onSuccess(Response<Object> response) {
            ToastUtil.toastSuccess(ForgetPasswordActivity.this,
                getString(R.string.reset_mobile_success));
            ShortcutMgr.logout();
          }
        });
  }

  public class Presenter {
    public void onConfirm(View view) {
      validator.validateAll(new ValidateResultCall() {
        @Override
        public void onSuccess() {
          resetPassword();
        }

        @Override
        public void onFailure(TextView view, String message) {
          ToastUtil.toastSuccess(ForgetPasswordActivity.this, message);
        }
      });

    }

    public void onSendSms(View view) {
      if (IsEmpty.string(binding.edtMobile.getText().toString().trim())) {
        ToastUtil.toastHint(ForgetPasswordActivity.this,
            getString(R.string.login_empty_msg_mobile));
        return;
      } else if (binding.edtMobile.getText().toString().trim().length() < 11) {
        ToastUtil.toastHint(ForgetPasswordActivity.this, getString(R.string.login_len_mobile));
        return;
      }
      sendSms(binding.edtMobile.getText().toString().trim());
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

    /**
     * 确认密码明密文切换
     */
    public void onConfirmVisible(View view) {
      view.setSelected(!view.isSelected());
      binding.edtConfirmPassword
          .setInputType(view.isSelected() ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
              : (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
      binding.edtConfirmPassword.setSelection(binding.edtPassword.getText().length());
    }

  }

}
