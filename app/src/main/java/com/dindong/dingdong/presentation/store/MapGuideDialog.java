package com.dindong.dingdong.presentation.store;

import java.net.URISyntaxException;

import com.dindong.dingdong.R;
import com.dindong.dingdong.databinding.LayoutMapGuideBinding;
import com.dindong.dingdong.util.PackageUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.tencent.mm.opensdk.utils.Log;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by wangcong on 2018/7/2.
 * <p>
 */

public class MapGuideDialog extends Dialog {

  LayoutMapGuideBinding binding;

  public static final int MAP_BAIDU = 0X12;// 百度地图
  public static final int MAP_GAODEI = 0X13;// 高德地图
  public static final int MAP_TX = 0X14;// 腾讯地图

  private String originLat;// 起点纬度
  private String originLon;// 起点经度
  private String originName;// 起点名称
  private String destinationLat;// 终点纬度
  private String destinationLon;// 终点经度
  private String destinationName;// 终点名称

  public MapGuideDialog(Context context) {
    super(context, R.style.FullScreenDialog);

    setCanceledOnTouchOutside(true);
    binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_map_guide,
        null, false);
    binding.setPresenter(new Presenter());
    setContentView(binding.getRoot());
  }

  @Override
  public void show() {
    super.show();
    Window win = getWindow();
    win.getDecorView().setPadding(0, 0, 0, 0);
    WindowManager.LayoutParams lp = win.getAttributes();
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    win.setAttributes(lp);
    win.setGravity(Gravity.BOTTOM);
    win.setWindowAnimations(R.style.theme_animation_bottom_rising);
  }

  /**
   * 设置起点信息
   * 
   * @param lat
   *          经度
   * @param lon
   *          纬度
   * @param name
   *          起点名称
   * @return
   */
  public MapGuideDialog setOrigin(String lat, String lon, String name) {
    this.originLat = lat;
    this.originLon = lon;
    this.originName = name;
    return this;
  }

  /**
   * 设置终点信息
   * 
   * @param lat
   *          经度
   * @param lon
   *          纬度
   * @param name
   *          终点名称
   */
  public MapGuideDialog setDestination(String lat, String lon, String name) {
    this.destinationLat = lat;
    this.destinationLon = lon;
    this.destinationName = name;
    return this;
  }

  /**
   * 百度导航
   */
  private void baiduMap() {
    if (PackageUtil.isAvilible(getContext(), "com.baidu.BaiduMap")) {// 传入指定应用包名

      try {
        Intent intent = Intent.getIntent("intent://map/direction?destination=latlng:"
            + destinationLat + "," + destinationLon + "|name:" + destinationName + // 终点
            "&mode=driving&" + // 导航路线方式
            "region=" + originName + //
            "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
        getContext().startActivity(intent); // 启动调用
      } catch (URISyntaxException e) {
        Log.e("intent", e.getMessage());
      }
    } else {
      ToastUtil.toastFailure(getContext(), "您尚未安装百度地图");
      Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
      Intent intent = new Intent(Intent.ACTION_VIEW, uri);
      getContext().startActivity(intent);
    }

  }

  /**
   * 高德地图
   */
  private void gaodeiMap() {
    if (PackageUtil.isAvilible(getContext(), "com.autonavi.minimap")) {
      try {
        Intent intent = Intent.getIntent("androidamap://navi?sourceApplication=ddbx&poiname="
            + destinationName + "&lat=" + destinationLat + "&lon=" + destinationLon + "&dev=0");
        getContext().startActivity(intent);
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    } else {
      ToastUtil.toastFailure(getContext(), "您尚未安装高德地图");
      Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
      Intent intent = new Intent(Intent.ACTION_VIEW, uri);
      getContext().startActivity(intent);
    }

  }

  /**
   * 腾讯地图
   */
  private void txMap() {
    if (PackageUtil.isAvilible(getContext(), "com.tencent.map")) {
      try {
        Intent intent = Intent.getIntent("http://apis.map.qq.com/uri/v1/marker?marker=coord:"
            + destinationLat + "," + destinationLon + ";" + "title:" + destinationName + ";addr:"
            + " " + "&referer=myapp");
        getContext().startActivity(intent);
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    } else {
      ToastUtil.toastFailure(getContext(), "您尚未安装腾讯地图");
      Uri uri = Uri.parse("market://details?id=com.tencent.map");
      Intent intent = new Intent(Intent.ACTION_VIEW, uri);
      getContext().startActivity(intent);
    }
  }

  public class Presenter {
    public void onCancel() {
      dismiss();
    }

    public void onGuide(int guideIndex) {
      dismiss();
      if (guideIndex == MAP_BAIDU) {
        baiduMap();
      } else if (guideIndex == MAP_GAODEI) {
        gaodeiMap();
      } else if (guideIndex == MAP_TX) {
        txMap();
      }
    }
  }

}
