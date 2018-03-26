package com.dindong.dingdong.presentation.user.wrist;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityBlueWristAddBinding;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

public class BlueWristAddActivity extends BaseActivity {

  // http://www.dingdongbanxue.com/lsh/c1e384832e3492903febd058965df316
  ActivityBlueWristAddBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_blue_wrist_add);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {

  }
}
