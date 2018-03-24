package com.dindong.dingdong.presentation.user;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityResetPasswordBinding;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.manager.ShortcutMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.auth.usecase.ResetPasswordByOldCase;
import com.dindong.dingdong.network.api.auth.usecase.SendSmsCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.auth.User;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.wcong.validator.ValidateResultCall;
import com.wcong.validator.Validator;
import com.wcong.validator.rules.RequiredRule;
import com.wcong.validator.rules.UniformityRule;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResetPasswordActivity extends BaseActivity {

  ActivityResetPasswordBinding binding;

  private Validator validator;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    registerValidator();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    binding.txtOldMobile.setText(SessionMgr.getUser().getMobile());
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

  private void registerValidator() {
    validator = new Validator();
    validator.register(binding.edtCode,
        new RequiredRule(getString(R.string.reset_mobile_tip_mobile)));
    validator.register(binding.edtOldPassword,
        new RequiredRule(getString(R.string.reset_password_tip_old_password)));
    validator.register(binding.edtNewPassword,
        new RequiredRule(getString(R.string.reset_password_tip_new_password)));
    validator.register(binding.edtConfirmPassword,
        new RequiredRule(getString(R.string.reset_password_tip_confirm_password)),
        new UniformityRule(binding.edtNewPassword,
            getString(R.string.reset_password_tip_uniformity)));
  }

  /**
   * 发送短信
   *
   * @param mobile
   */
  private void sendSms(String mobile) {
    new SendSmsCase(mobile).execute(new HttpSubscriber<User>() {
      @Override
      public void onFailure(String errorMsg, Response<User> response) {
        DialogUtil.getErrorDialog(ResetPasswordActivity.this, errorMsg).show();
        binding.ct.startCount();
      }

      @Override
      public void onSuccess(Response<User> response) {
        binding.ct.startCount();
      }
    });
  }

  /**
   * 修改密码
   */
  private void resetPasswordByOld() {
    new ResetPasswordByOldCase(SessionMgr.getUser().getMobile(),
        binding.edtOldPassword.getText().toString().trim(),
        binding.edtNewPassword.getText().toString().trim(),
        binding.edtCode.getText().toString().trim()).execute(new HttpSubscriber<Object>(this) {

          @Override
          public void onFailure(String errorMsg, Response<Object> response) {
            DialogUtil.getErrorDialog(ResetPasswordActivity.this, errorMsg).show();
          }

          @Override
          public void onSuccess(Response<Object> response) {
            ToastUtil.toastSuccess(ResetPasswordActivity.this,
                getString(R.string.reset_mobile_success));
            ShortcutMgr.logout();
          }
        });
  }

  public class Presenter {

    public void onSendSms(View view) {
      sendSms(SessionMgr.getSession().getUser().getMobile());
    }

    public void onConfirm(View view) {
      validator.validateAll(new ValidateResultCall() {
        @Override
        public void onSuccess() {
          // 校验成功后提交申请
          resetPasswordByOld();
        }

        @Override
        public void onFailure(TextView view, String message) {
          ToastUtil.toastHint(ResetPasswordActivity.this, message);
        }
      });
    }
  }

}
