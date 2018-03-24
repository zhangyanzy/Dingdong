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
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.RegionStorageUtil;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.ProvinceSelector;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

public class RegionSwitchActivity extends BaseActivity {

  ActivityRegionSwitchBinding binding;

  private String selectProvince;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_region_switch);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    selectProvince = SessionMgr.getCurrentProvince();
    binding.txtCurrentProvince.setText(StringUtil.formatProvince(selectProvince));
    listHistoryRegions();
    listHotRegions();
  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
    binding.psHistory.setProvinceSelectListener(new ProvinceSelector.ProvinceSelectListener() {
      @Override
      public void onSelect(String province) {
        selectProvince = province;
        RegionStorageUtil.add(province);
        onBackPressed();
      }
    });
    binding.psHot.setProvinceSelectListener(new ProvinceSelector.ProvinceSelectListener() {
      @Override
      public void onSelect(String province) {
        selectProvince = province;
        RegionStorageUtil.add(province);
        onBackPressed();
      }
    });
  }

  @Override
  public void onBackPressed() {
    // 界面销毁时，将选择城市返回给前一界面
    Intent intent = new Intent();
    intent.putExtra(AppConfig.IntentKey.DATA, selectProvince);
    setResult(RESULT_OK, intent);

    super.onBackPressed();
  }

  /**
   * 加载历史城市
   */
  private void listHistoryRegions() {
    binding.psHistory.init(selectProvince, RegionStorageUtil.getLocalRegion());
  }

  /**
   * 获取热门城市
   */
  private void listHotRegions() {
    new ListRegionCase().execute(new HttpSubscriber<List<String>>(this) {
      @Override
      public void onFailure(String errorMsg, Response<List<String>> response) {
        DialogUtil.getErrorDialog(RegionSwitchActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<List<String>> response) {
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
          selectProvince = location.getProvince();
          binding.txtCurrentProvince.setText(location.getProvince());
        }

        @Override
        public void onFailure(int errorCode, String msg) {
          ToastUtil.toastFailure(RegionSwitchActivity.this, getString(R.string.location_fail));
        }
      });

    }
  }
}
