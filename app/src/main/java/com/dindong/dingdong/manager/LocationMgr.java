package com.dindong.dingdong.manager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import android.content.Context;

/**
 * Created by wangcong on 2018/3/22.
 * <p>
 */

public class LocationMgr {

  private static Context context;

  // 声明AMapLocationClient类对象
  private static AMapLocationClient mLocationClient = null;
  // 声明AMapLocationClientOption对象
  public static AMapLocationClientOption mLocationOption = null;

  private static ILocationCallback iLocationCallback;

  public static void init(Context context) {
    LocationMgr.context = context;
    newInstance(context);
  }

  private static void newInstance(Context context) {
    mLocationClient = new AMapLocationClient(context);
    mLocationClient.setLocationListener(locationListener);
    initDefaultOption();
  }

  /**
   * 设置定位参数
   */
  private static void initDefaultOption() {
    mLocationOption = new AMapLocationClientOption();
    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);// 可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
    mLocationOption.setGpsFirst(false);// 可选，设置是否gps优先，只在高精度模式下有效。默认关闭
    mLocationOption.setHttpTimeOut(30000);// 可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
    mLocationOption.setInterval(5000);// 可选，设置定位间隔。默认为2秒
    mLocationOption.setNeedAddress(true);// 可选，设置是否返回逆地理地址信息。默认是true
    mLocationOption.setOnceLocation(true);// 可选，设置是否单次定位。默认是false
    mLocationOption.setOnceLocationLatest(false);// 可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
    AMapLocationClientOption
        .setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);// 可选，
    // 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
    mLocationOption.setSensorEnable(false);// 可选，设置是否使用传感器。默认是false
    mLocationOption.setWifiScan(true); // 可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
    mLocationOption.setLocationCacheEnable(true); // 可选，设置是否使用缓存定位，默认为true
  }

  public static void startLocation() {
    startLocation(null);
  }

  public static void startLocation(ILocationCallback callback) {
    LocationMgr.iLocationCallback = callback;
    // 启动定位
    if (mLocationClient != null) {
      // 设置定位参数
      mLocationClient.setLocationOption(mLocationOption);
      mLocationClient.startLocation();
    }else {
      newInstance(context);
      mLocationClient.setLocationOption(mLocationOption);
      mLocationClient.startLocation();
    }
  }

  public static void stopLocation() {
    LocationMgr.iLocationCallback = null;
    // 停止定位
    if (mLocationClient != null)
      mLocationClient.stopLocation();
  }

  /**
   * 销毁定位
   */
  public static void destroyLocation() {
    if (null != mLocationClient) {
      // 如果AMapLocationClient是在当前Activity实例化的，
      // 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
      mLocationClient.onDestroy();
      mLocationClient = null;
      if (mLocationOption != null)
        mLocationOption = null;
    }
  }

  private static AMapLocationListener locationListener = new AMapLocationListener() {
    @Override
    public void onLocationChanged(AMapLocation location) {

      if (location != null) {

        if (location.getErrorCode() == 0) {
          if (iLocationCallback != null) {
            iLocationCallback.onSuccess(location);
          }
        } else {
          int code = location.getErrorCode();
          String msg = "定位失败";
          if (code == 7) {
            msg = "KEY鉴权失败";
          } else if (code == 4) {
            msg = "定位超时";
          } else if (code == 12) {
            msg = "缺少定位权限,请检查定位权限是否开启";
          }
          if (iLocationCallback != null) {
            iLocationCallback.onFailure(location.getErrorCode(), msg);
          }
        }
      } else {
        if (iLocationCallback != null) {
          iLocationCallback.onFailure(-1, "定位失败");
        }
      }

    }
  };

  public interface ILocationCallback {
    void onSuccess(AMapLocation location);

    void onFailure(int errorCode, String msg);
  }

}
