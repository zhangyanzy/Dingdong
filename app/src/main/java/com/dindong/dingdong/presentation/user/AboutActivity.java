package com.dindong.dingdong.presentation.user;

import com.dindong.dingdong.BuildConfig;
import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityAboutBinding;
import com.dindong.dingdong.presentation.main.fragment.UserAgreementActivity;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

/**
 * 关于界面
 */
public class AboutActivity extends BaseActivity {
  ActivityAboutBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    binding.txtVersion
        .setText(StringUtil.format(getString(R.string.about_version), BuildConfig.VERSION_NAME));
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

  public class Presenter {
    /**
     * 用户协议
     */
    public void onAgreement() {
      startActivity(new Intent(AboutActivity.this, UserAgreementActivity.class));
    }
  }
}
