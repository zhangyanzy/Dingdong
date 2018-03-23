package com.dindong.dingdong.presentation.user;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityResetMobileBinding;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.auth.usecase.SendSmsCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.auth.User;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

public class ResetMobileActivity extends BaseActivity {

  ActivityResetMobileBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_mobile);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
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
        DialogUtil.getErrorDialog(ResetMobileActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<User> response) {
        binding.ct.startCount();
      }
    });
  }

  public class Presenter {

    public void onSendSms(View view) {
      sendSms(SessionMgr.getSession().getUser().getMobile());
    }

    public void onConfirm(View view) {

    }
  }
}
