package com.dindong.dingdong.presentation.main;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityRegisterBinding;
import com.dindong.dingdong.manager.ShortcutMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.auth.usecase.RegisterCase;
import com.dindong.dingdong.network.api.auth.usecase.SendSmsCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.auth.RegisterRequest;
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

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

public class RegisterActivity extends BaseActivity {

  ActivityRegisterBinding binding;

  private Validator validator;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    binding.ct.setViewType(CountTimeTextView.LayoutType.red);
    binding.cbAgreement.setSelected(true);
    registerEditValidator();
    registerFocusValidator();
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

  }

  /**
   * 注册焦点校验
   */
  private void registerFocusValidator() {
    FocusValidator focusValidator = new FocusValidator();
    focusValidator.verify(binding.edtMobile, binding.imgMobile, binding.lineMobile);
    focusValidator.verify(binding.edtCode, binding.imgCode, binding.lineCode);
    focusValidator.verify(binding.edtPassword, binding.imgPassword, binding.linePassword);
  }

  /**
   * 注册
   */
  private void register() {
    RegisterRequest registerRequest = new RegisterRequest();
    registerRequest.setMobile(binding.edtMobile.getText().toString().trim());
    registerRequest.setPassword(binding.edtPassword.getText().toString().trim());
    new RegisterCase(binding.edtCode.getText().toString().trim(), registerRequest)
        .execute(new HttpSubscriber<User>(RegisterActivity.this) {
          @Override
          public void onFailure(String errorMsg, Response<User> response) {
            DialogUtil.getErrorDialog(RegisterActivity.this, errorMsg).show();
          }

          @Override
          public void onSuccess(Response<User> response) {
            ToastUtil.toastSuccess(RegisterActivity.this, getString(R.string.register_success));
            ShortcutMgr.login(response.getData());
          }
        });

  }

  /**
   * 发送短信
   * 
   * @param mobile
   */
  private void sendSms(String mobile) {
    new SendSmsCase(mobile).execute(new HttpSubscriber<User>(RegisterActivity.this) {
      @Override
      public void onFailure(String errorMsg, Response<User> response) {
        DialogUtil.getErrorDialog(RegisterActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<User> response) {
        ToastUtil.toastSuccess(RegisterActivity.this, getString(R.string.login_send_sms_success));
        binding.ct.startCount();
      }
    });
  }

  public class Presenter {
    public void onConfirm(View view) {
      validator.validateAll(new ValidateResultCall() {
        @Override
        public void onSuccess() {
          if (!binding.cbAgreement.isSelected()) {
            ToastUtil.toastHint(RegisterActivity.this, getString(R.string.login_tip_agreement));
            return;
          }
          register();
        }

        @Override
        public void onFailure(TextView view, String message) {
          ToastUtil.toastSuccess(RegisterActivity.this, message);
        }
      });

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
     * 授权协议
     *
     * @param view
     */
    public void onCheckAgreement(View view) {
      view.setSelected(!view.isSelected());
    }

    public void onSendSms(View view) {
      if (IsEmpty.string(binding.edtMobile.getText().toString().trim())) {
        ToastUtil.toastHint(RegisterActivity.this, getString(R.string.login_empty_msg_mobile));
        return;
      } else if (binding.edtMobile.getText().toString().trim().length() < 11) {
        ToastUtil.toastHint(RegisterActivity.this, getString(R.string.login_len_mobile));
        return;
      }
      sendSms(binding.edtMobile.toString().trim());
    }

  }
}
