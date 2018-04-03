package com.dindong.dingdong.presentation.store;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityShopMapBinding;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

public class ShopMapActivity extends BaseActivity {
  ActivityShopMapBinding binding;

  private AMap aMap;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_map);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);

  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    String latitude = getIntent().getStringExtra(AppConfig.IntentKey.LATITUDE);
    String longitude = getIntent().getStringExtra(AppConfig.IntentKey.LONGITUDE);
    binding.map.onCreate(savedInstanceState);// 此方法必须重写
    if (aMap == null) {
      aMap = binding.map.getMap();
      aMap.getUiSettings().setZoomControlsEnabled(false);// 隐藏缩放按钮
    }
    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(
        new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)), 15, 0, 30));
    aMap.moveCamera(cameraUpdate);
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
   * 方法必须重写
   */
  @Override
  protected void onResume() {
    super.onResume();
    binding.map.onResume();
  }

  /**
   * 方法必须重写
   */
  @Override
  protected void onPause() {
    super.onPause();
    binding.map.onPause();
  }

  /**
   * 方法必须重写
   */
  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding.map.onDestroy();
  }

}
