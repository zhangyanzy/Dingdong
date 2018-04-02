package com.dindong.dingdong.presentation.main;

import java.util.List;

import com.amap.api.location.AMapLocation;
import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityRegionSwitchBinding;
import com.dindong.dingdong.manager.LocationMgr;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.region.usecase.ListRegionCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.Region;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.RegionStorageUtil;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.CitySelector;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

public class RegionSwitchActivity extends BaseActivity {

  ActivityRegionSwitchBinding binding;

  private Region selectCity;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_region_switch);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    selectCity = SessionMgr.getCurrentAdd().getCity().clone();
    binding.txtCurrentProvince.setText(StringUtil.formatCity(selectCity.getText()));
    listHistoryRegions();
    listHotRegions();
  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
    binding.psHistory.setCitySelectListener(new CitySelector.CitySelectListener() {
      @Override
      public void onSelect(Region city) {
        selectCity = city;
        // 手动切换城市无
        SessionMgr.getCurrentAdd().setLatitude("");
        SessionMgr.getCurrentAdd().setLongitude("");
        RegionStorageUtil.add(city);
        onBackPressed();
      }

    });
    binding.psHot.setCitySelectListener(new CitySelector.CitySelectListener() {
      @Override
      public void onSelect(Region city) {
        selectCity = city;
        SessionMgr.getCurrentAdd().setLatitude("");
        SessionMgr.getCurrentAdd().setLongitude("");
        RegionStorageUtil.add(city);
        onBackPressed();
      }
    });
  }

  @Override
  public void onBackPressed() {
    // 界面销毁时，将选择城市返回给前一界面
    Intent intent = new Intent();
    intent.putExtra(AppConfig.IntentKey.DATA, selectCity);
    setResult(RESULT_OK, intent);

    super.onBackPressed();
  }

  /**
   * 加载历史城市
   */
  private void listHistoryRegions() {
    binding.psHistory.init(selectCity.getText(), RegionStorageUtil.getLocalRegion());
  }

  /**
   * 获取热门城市
   */
  private void listHotRegions() {
    new ListRegionCase().execute(new HttpSubscriber<List<Region>>(this) {
      @Override
      public void onFailure(String errorMsg, Response<List<Region>> response) {
        DialogUtil.getErrorDialog(RegionSwitchActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<List<Region>> response) {
        binding.psHot.init(response.getData());
      }
    });
  }

  public class Presenter {
    public void onBack() {
      onBackPressed();
    }

    /**
     * 定位
     */
    public void onLocation() {
      LocationMgr.startLocation(new LocationMgr.ILocationCallback() {
        @Override
        public void onSuccess(AMapLocation location) {
          selectCity.setId(location.getAdCode());
          selectCity.setCode(location.getAdCode());
          selectCity.setText(location.getCity());
          binding.txtCurrentProvince.setText(StringUtil.formatCity(location.getCity()));

          // 更新当前地理位置
          SessionMgr.SessionAddress add = new SessionMgr.SessionAddress();
          Region city = new Region();
          city.setId(location.getAdCode());
          city.setText(location.getCity());
          add.setCity(city);
          add.setLongitude(location.getLongitude() + "");
          add.setLatitude(location.getLatitude() + "");
          RegionStorageUtil.add(city);
        }

        @Override
        public void onFailure(int errorCode, String msg) {
          ToastUtil.toastFailure(RegionSwitchActivity.this, getString(R.string.location_fail));
        }
      });

    }
  }
}
