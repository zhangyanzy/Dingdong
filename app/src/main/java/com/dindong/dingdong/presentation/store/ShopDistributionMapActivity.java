package com.dindong.dingdong.presentation.store;

import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityShopDistributionMapBinding;
import com.dindong.dingdong.manager.LocationMgr;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.shop.usecase.ListShopCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.FilterParam;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.ToastUtil;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

/**
 * 地图找门店
 */
public class ShopDistributionMapActivity extends BaseActivity {
  ActivityShopDistributionMapBinding binding;

  private AMap aMap;

  private int mapHeight = -1;

  private String cityCode = "330500";

  private ListShopCase listShopCase;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_distribution_map);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    cityCode = SessionMgr.getCurrentAdd().getCity().getId();
    final String latitude = SessionMgr.getCurrentAdd().getLatitude();
    final String longitude = SessionMgr.getCurrentAdd().getLongitude();
    binding.map.onCreate(savedInstanceState);// 此方法必须重写
    if (aMap == null) {
      aMap = binding.map.getMap();
      // aMap.getUiSettings().setZoomControlsEnabled(false);// 隐藏缩放按钮
      aMap.setOnCameraChangeListener(onCameraChangeListener);

      // 设置我的定位
      // aMap.setLocationSource(locationSource);// 设置定位监听
      aMap.getUiSettings().setMyLocationButtonEnabled(true); // 是否显示默认的定位按钮
      aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
    }

    LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));

    CameraUpdate cameraUpdate = CameraUpdateFactory
        .newCameraPosition(new CameraPosition(latLng, 19, 0, 30));
    aMap.moveCamera(cameraUpdate);

    binding.map.post(new Runnable() {
      @Override
      public void run() {
        mapHeight = binding.map.getMeasuredHeight();
        queryShop(latitude, longitude, String.valueOf(aMap.getScalePerPixel() * mapHeight));
      }
    });

    // 进入界面时，定位一次
    LocationMgr.startLocation(new LocationMgr.ILocationCallback() {
      @Override
      public void onSuccess(AMapLocation location) {

        LatLng latLng = new LatLng(Double.valueOf(location.getLatitude()),
            Double.valueOf(location.getLongitude()));

        CameraUpdate cameraUpdate = CameraUpdateFactory
            .newCameraPosition(new CameraPosition(latLng, 19, 0, 30));
        aMap.moveCamera(cameraUpdate);

        cityCode = location.getAdCode();
        queryShop(latitude, longitude, String.valueOf(aMap.getScalePerPixel() * mapHeight));
      }

      @Override
      public void onFailure(int errorCode, String msg) {
        ToastUtil.toastFailure(ShopDistributionMapActivity.this, "定位失败");
      }
    });

  }

  /**
   * 向地图中添加标记点
   *
   * @param latitude
   * @param longitude
   */
  private void addMark(String latitude, String longitude, String title) {
    LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
    aMap.addMarker(new MarkerOptions().position(latLng).anchor(1.0f, 1f).title(title));// 绘制点标记
  }

  /**
   * 监听地图滑动、缩放
   */
  private AMap.OnCameraChangeListener onCameraChangeListener = new AMap.OnCameraChangeListener() {
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
      if (mapHeight < 0)
        return;
      Log.e("比例", aMap.getScalePerPixel() + "");
      Log.e("range", aMap.getScalePerPixel() * mapHeight + "");
      Log.e("target", cameraPosition.target.latitude + "\b\b" + cameraPosition.target.longitude);
      queryShop(String.valueOf(cameraPosition.target.latitude),
          String.valueOf(cameraPosition.target.longitude),
          String.valueOf(aMap.getScalePerPixel() * mapHeight));
    }
  };

  /**
   * 查询附近门店，并在地图上标记
   *
   * @param latitude
   *          纬度
   * @param longitude
   *          经度
   * @param range
   *          查询范围
   */
  private void queryShop(String latitude, String longitude, String range) {
    if (IsEmpty.string(cityCode))
      return;
    QueryParam queryParam = new QueryParam();
    queryParam.setLimit(99);
    queryParam.getFilters()
        .add(new FilterParam("queryType:", ShopListActivity.ShopQueryType.all.toString()));
    queryParam.getFilters().add(new FilterParam("cityCode", cityCode));
    queryParam.getFilters().add(new FilterParam("longitude", longitude));
    queryParam.getFilters().add(new FilterParam("latitude", latitude));
    queryParam.getFilters().add(new FilterParam("range", range));

    if (listShopCase != null)
      listShopCase.unSubscribe();// 取消上次请求

    listShopCase = new ListShopCase(queryParam);
    listShopCase.execute(new HttpSubscriber<List<Shop>>() {
      @Override
      public void onFailure(String errorMsg, Response<List<Shop>> response) {
      }

      @Override
      public void onSuccess(Response<List<Shop>> response) {
        aMap.clear();
        for (Shop shop : response.getData()) {
          addMark(shop.getLatitude(), shop.getLongitude(), shop.getName());
        }
      }
    });
  }

  // private LocationSource locationSource = new LocationSource() {
  // /**
  // * 激活定位
  // *
  // * @param onLocationChangedListener
  // */
  // @Override
  // public void activate(OnLocationChangedListener onLocationChangedListener) {
  // mListener = listener;
  // if (mlocationClient == null) {
  // mlocationClient = new AMapLocationClient(this);
  // mLocationOption = new AMapLocationClientOption();
  // // 设置定位监听
  // mlocationClient.setLocationListener(this);
  // // 设置为高精度定位模式
  // mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
  // // 设置定位参数
  // mlocationClient.setLocationOption(mLocationOption);
  // // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
  // // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
  // // 在定位结束后，在合适的生命周期调用onDestroy()方法
  // // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
  // mlocationClient.startLocation();
  // }
  // }
  //
  // /**
  // * 停止定位
  // */
  // @Override
  // public void deactivate() {
  // mListener = null;
  // if (mlocationClient != null) {
  // mlocationClient.stopLocation();
  // mlocationClient.onDestroy();
  // }
  // mlocationClient = null;
  // }
  // };

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
