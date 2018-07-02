package com.dindong.dingdong.presentation.store;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityShopMapBinding;
import com.dindong.dingdong.databinding.LayoutShopMapInfoWindowBinding;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.widget.NavigationTopBar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class ShopMapActivity extends BaseActivity {
  ActivityShopMapBinding binding;

  private AMap aMap;

  private String destinationLat;
  private String destinationLon;
  private String destinationName;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_map);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);

  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    final Shop shop = (Shop) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
    binding.setShop(shop);
    destinationLat = shop.getLatitude();
    destinationLon = shop.getLongitude();
    destinationName = shop.getName();

    binding.map.onCreate(savedInstanceState);// 此方法必须重写
    if (aMap == null) {
      aMap = binding.map.getMap();
      aMap.getUiSettings().setZoomControlsEnabled(false);// 隐藏缩放按钮
    }

    LatLng latLng = new LatLng(Double.valueOf(destinationLat), Double.valueOf(destinationLon));

    CameraUpdate cameraUpdate = CameraUpdateFactory
        .newCameraPosition(new CameraPosition(latLng, 19, 0, 30));
    aMap.moveCamera(cameraUpdate);

    final Marker marker = aMap.addMarker(new MarkerOptions()
        .position(latLng).title(shop.getName()).snippet(StringUtil
            .format(getString(R.string.global_range), StringUtil.formatRange(shop.getRange())))
        .anchor(1.0f, 1f));// 绘制点标记

    aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
      @Override
      public View getInfoWindow(Marker marker) {
        LayoutShopMapInfoWindowBinding binding = DataBindingUtil.inflate(
            LayoutInflater.from(ShopMapActivity.this), R.layout.layout_shop_map_info_window, null,
            false);
        binding.txtShopName.setText(shop.getName());
        binding.txtShopRange.setText(StringUtil.format(getString(R.string.global_range),
            StringUtil.formatRange(shop.getRange())));
        binding.txtGuide.setOnClickListener(onGuideListener);
        return binding.getRoot();
      }

      @Override
      public View getInfoContents(Marker marker) {
        return null;
      }
    });

    marker.showInfoWindow();
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

  private View.OnClickListener onGuideListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      // 显示导航选择框
      new MapGuideDialog(ShopMapActivity.this)
          .setDestination(destinationLat, destinationLon, destinationName).show();
    }
  };

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
