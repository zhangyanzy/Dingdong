package com.dindong.dingdong.presentation.user.wrist;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityBlueWristDetailBinding;
import com.dindong.dingdong.network.bean.wrist.BlueWrist;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

public class BlueWristDetailActivity extends BaseActivity {

  ActivityBlueWristDetailBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_blue_wrist_detail);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    binding.setWrist(new BlueWrist());
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (getIntent().getSerializableExtra(AppConfig.IntentKey.DATA) != null)
      binding.setWrist((BlueWrist) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA));
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
}
