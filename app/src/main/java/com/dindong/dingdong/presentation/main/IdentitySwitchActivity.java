package com.dindong.dingdong.presentation.main;

import com.amap.api.location.AMapLocation;
import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityIdentitySwitchBinding;
import com.dindong.dingdong.manager.LocationMgr;
import com.dindong.dingdong.network.bean.auth.AuthIdentity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

public class IdentitySwitchActivity extends BaseActivity {

  private ActivityIdentitySwitchBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_identity_switch);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {

  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
  }

  public class Presenter {
    public void onIdentitySwitch(AuthIdentity identity) {
      // //同步登录信息
      // User user = SessionMgr.getUser();
      // user.setIdentity(identity);
      // SessionMgr.updateUser(user);
      //
      // Intent intent = new Intent(IdentitySwitchActivity.this,
      // MainActivity.class);
      // intent.putExtra(AppConfig.IntentKey.DATA, identity);
      // startActivity(intent);
      // finish();
      if (identity.equals(AuthIdentity.TEACHER)) {
        LocationMgr.startLocation(new LocationMgr.ILocationCallback() {
          @Override
          public void onSuccess(AMapLocation location) {
            Log.i("city", location.getCity());
            Log.i("Longitude", location.getLongitude() + "");
            Log.i("Latitude", location.getLatitude() + "");
          }

          @Override
          public void onFailure(int errorCode, String msg) {
            Log.i("err" + errorCode, msg);
          }
        });
      } else if (identity.equals(AuthIdentity.STUDENT)) {
        LocationMgr.stopLocation();
      }
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    LocationMgr.stopLocation();
  }
}
