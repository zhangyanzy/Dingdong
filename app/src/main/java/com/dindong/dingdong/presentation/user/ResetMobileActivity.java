package com.dindong.dingdong.presentation.user;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityResetMobileBinding;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.manager.ShortcutMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.auth.usecase.ResetMobileCase;
import com.dindong.dingdong.network.api.auth.usecase.SendSmsCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.auth.User;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.wcong.validator.ValidateResultCall;
import com.wcong.validator.Validator;
import com.wcong.validator.rules.MinLengthRule;
import com.wcong.validator.rules.RequiredRule;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResetMobileActivity extends BaseActivity {

  ActivityResetMobileBinding binding;

  private Validator validator;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_mobile);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    registerValidator();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    binding.txtOldMobile.setText(SessionMgr.getUser().getMobile());
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
   * 发送短信
   * 
   * @param mobile
   */
  private void sendSms(String mobile) {
    new SendSmsCase(mobile,"4").execute(new HttpSubscriber<User>() {
      @Override
      public void onFailure(String errorMsg, Response<User> response) {
        DialogUtil.getErrorDialog(ResetMobileActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<User> response) {
        binding.ct.startCount();
      }
    });
  }

  private void resetMobile() {
    new ResetMobileCase(SessionMgr.getUser().getMobile(),
        binding.edtNewMobile.getText().toString().trim(),
        binding.edtPassword.getText().toString().trim(),
        binding.edtCode.getText().toString().trim()).execute(new HttpSubscriber(this) {
          @Override
          public void onNext(Object o) {

          }

          @Override
          public void onFailure(String errorMsg, Response response) {
            DialogUtil.getErrorDialog(ResetMobileActivity.this, errorMsg).show();
          }

          @Override
          public void onSuccess(Response response) {
            ToastUtil.toastSuccess(ResetMobileActivity.this,
                getString(R.string.reset_mobile_success));
            ShortcutMgr.logout();
          }
        });
  }

  private void registerValidator() {
    validator = new Validator();
    validator.register(binding.edtNewMobile,
        new RequiredRule(getString(R.string.reset_mobile_tip_mobile)),
        new MinLengthRule(11, getString(R.string.reset_mobile_tip_mobile_len)));
    validator.register(binding.edtCode,
        new RequiredRule(getString(R.string.reset_mobile_tip_code)));
    validator.register(binding.edtPassword,
        new RequiredRule(getString(R.string.reset_mobile_tip_password)));
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
          resetMobile();
        }

        @Override
        public void onFailure(TextView view, String message) {
          ToastUtil.toastHint(ResetMobileActivity.this, message);
        }
      });
    }
  }
}
