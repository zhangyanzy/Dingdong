package com.dindong.dingdong.presentation.user.wrist;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityBlueWristMainBinding;
import com.dindong.dingdong.network.bean.wrist.BlueWrist;
import com.dindong.dingdong.util.DensityUtil;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BlueWristMainActivity extends BaseActivity {

  ActivityBlueWristMainBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_blue_wrist_main);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    ImageView imageView = new ImageView(this);
    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(DensityUtil.dip2px(this, 17),
        DensityUtil.dip2px(this, 17));
    imageView.setLayoutParams(layoutParams);
    imageView.setImageResource(R.mipmap.btn_add_disable);
    binding.nb.addRightView(imageView);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    binding.setWrist(new BlueWrist());
  }
}
