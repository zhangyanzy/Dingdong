package com.dindong.dingdong.presentation.user.wrist;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityBlueWristMainBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.wrist.usecase.ListLshCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.wrist.BlueWrist;
import com.dindong.dingdong.util.DensityUtil;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
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

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
  }

  @Override
  protected void onResume() {
    super.onResume();
    listWrist();
  }

  /**
   * 查询手环
   */
  private void listWrist() {
    new ListLshCase().execute(new HttpSubscriber<List<BlueWrist>>(this) {
      @Override
      public void onFailure(String errorMsg, Response<List<BlueWrist>> response) {
        DialogUtil.getErrorDialog(BlueWristMainActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<List<BlueWrist>> response) {
        if (IsEmpty.list(response.getData()))
          return;
      }
    });
  }

  public class Presenter {
    public void onScan(View view) {
      startActivity(new Intent(BlueWristMainActivity.this, BlueWristScanActivity.class));
    }
  }

}
