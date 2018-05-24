package com.dindong.dingdong.presentation.user.cooperation;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityApplyMainBinding;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

public class ApplyMainActivity extends BaseActivity {
  ActivityApplyMainBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_apply_main);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
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
     * 平台老师认证
     */
    public void onApplyTeacher() {
      startActivity(new Intent(ApplyMainActivity.this, ApplyTeacherActivity.class));
    }

    /**
     * 机构入驻
     */
    public void onApplyInstitution() {
      startActivity(new Intent(ApplyMainActivity.this, ApplyInstitutionActivity.class));
    }

    /**
     * 申请代理
     */
    public void onApplyProxy() {
      startActivity(new Intent(ApplyMainActivity.this, ApplyProxyActivity.class));
    }

  }

}
