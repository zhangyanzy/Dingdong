package com.dindong.dingdong.presentation.main.fragment;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.dindong.dingdong.R;
import com.dindong.dingdong.adapter.StoreAdapter;
import com.dindong.dingdong.adapter.StorePresenter;
import com.dindong.dingdong.adapter.SubjectPresenter;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.FragmentHomeBinding;
import com.dindong.dingdong.manager.LocationMgr;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.banner.usecase.ListBannerCase;
import com.dindong.dingdong.network.api.shop.usecase.ListShopCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.banner.Banner;
import com.dindong.dingdong.network.bean.entity.FilterParam;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.entity.Region;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.presentation.main.MainActivity;
import com.dindong.dingdong.presentation.main.RegionSwitchActivity;
import com.dindong.dingdong.presentation.store.ShopListActivity;
import com.dindong.dingdong.presentation.store.ShopMainActivity;
import com.dindong.dingdong.presentation.subject.SubjectDetailActivity;
import com.dindong.dingdong.presentation.subject.SubjectListActivity;
import com.dindong.dingdong.presentation.user.wrist.BlueWristMainActivity;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.PhotoUtil;
import com.dindong.dingdong.util.RegionStorageUtil;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by wcong on 2018/3/10. 首页
 */

public class HomeFragment extends BaseFragment {

  private FragmentHomeBinding binding;

  private List<String> mBannerList;

  @Override
  protected View initComponent(LayoutInflater inflater, ViewGroup container) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

    return binding.getRoot();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
  }

  @Override
  protected void firstVisible() {
    refreshData();
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isVisibleToUser) {
      if (binding == null)
        return;
      // refreshData();
    }
  }

  /**
   * 更新数据
   */
  private void refreshData() {
    listBanner();
    // 获取当前地理位置
    LocationMgr.startLocation(new LocationMgr.ILocationCallback() {
      @Override
      public void onSuccess(AMapLocation location) {
        SessionMgr.SessionAddress add = new SessionMgr.SessionAddress();
        Region city = new Region();
        city.setId(location.getAdCode());
        city.setText(location.getCity());
        add.setCity(city);
        add.setLongitude(location.getLongitude() + "");
        add.setLatitude(location.getLatitude() + "");
        SessionMgr.setCurrentAdd(add);
        binding.txtProvince.setText(location.getProvince());
        RegionStorageUtil.add(city);
        loadListData();
      }

      @Override
      public void onFailure(int errorCode, String msg) {
        loadListData();
      }
    });

  }

  /**
   * 加载列表数据
   */
  private void loadListData() {
    listShop(0);
    listShop(1);
  }

  /**
   * 获取轮播图片
   */
  private void listBanner() {
    new ListBannerCase().execute(new HttpSubscriber<List<Banner>>() {
      @Override
      public void onFailure(String errorMsg, Response<List<Banner>> response) {

      }

      @Override
      public void onSuccess(Response<List<Banner>> response) {
        mBannerList = new ArrayList();
        for (Banner banner : response.getData()) {
          mBannerList.add(banner.getImageUrl());
        }
        binding.bannerHomeFragment.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
            .setIndicatorGravity(BannerConfig.CENTER).setDelayTime(2000)
            .setImageLoader(new GlideImageLoader()).setImages(mBannerList).start();
      }
    });
  }

  /**
   * 获取门店，最多6条
   * 
   * @param type
   *          0-品质门店，1-附近门第
   */
  private void listShop(final int type) {
    QueryParam param = new QueryParam();
    param.setLimit(6);
    param.getFilters().add(new FilterParam("queryType:", type == 0 ? "recommend" : "near"));
    param.getFilters()
        .add(new FilterParam("cityCode", SessionMgr.getCurrentAdd().getCity().getId()));
    if (!IsEmpty.string(SessionMgr.getCurrentAdd().getLongitude())) {
      param.getFilters()
          .add(new FilterParam("longitude", SessionMgr.getCurrentAdd().getLongitude()));
      param.getFilters().add(new FilterParam("latitude", SessionMgr.getCurrentAdd().getLatitude()));
    }

    new ListShopCase(param).execute(new HttpSubscriber<List<Shop>>() {
      @Override
      public void onFailure(String errorMsg, Response<List<Shop>> response) {
      }

      @Override
      public void onSuccess(Response<List<Shop>> response) {
        StoreAdapter storeAdapter = new StoreAdapter(getContext());
        storeAdapter.setPresenter(new Presenter());
        storeAdapter.set(response.getData());
        LinearLayoutManager manager = new LinearLayoutManager(getContext()) {
          @Override
          public boolean canScrollVertically() {
            return false;
          }
        };
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        if (type == 0) {
          binding.lstRecommendShop.setLayoutManager(manager);
          binding.lstRecommendShop.setAdapter(storeAdapter);
        } else if (type == 1) {
          binding.lstNearShop.setLayoutManager(manager);
          binding.lstNearShop.setAdapter(storeAdapter);
        }

      }
    });
  }

  /**
   * banner类图片加载
   */
  public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, final Object path, ImageView imageView) {
      int index = 0;
      for (int i = 0; i < mBannerList.size(); i++) {
        if (mBannerList.get(i).equals(path)) {
          index = i;
        }
      }
      PhotoUtil.load(getContext(), path, imageView);
      final int finalIndex = index;
      imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          switch (finalIndex) {
          case 0:
            Toast.makeText(getActivity(), (String) path, Toast.LENGTH_SHORT).show();
            break;
          case 1:
            Toast.makeText(getActivity(), (String) path, Toast.LENGTH_SHORT).show();
            break;
          case 2:
            Toast.makeText(getActivity(), (String) path, Toast.LENGTH_SHORT).show();
            break;
          default:
            break;
          }
        }
      });
    }
  }

  public void startImpressive() {
    ((MainActivity) getActivity()).showImpressive();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
      if (data.getSerializableExtra(AppConfig.IntentKey.DATA) == null
          || ((Region) data.getSerializableExtra(AppConfig.IntentKey.DATA)).getId()
              .equals(SessionMgr.getCurrentAdd().getCity().getId()))
        return;
      Region city = (Region) data.getSerializableExtra(AppConfig.IntentKey.DATA);
      // SessionMgr.SessionAddress address = new SessionMgr.SessionAddress();
      // address.setCity(city);
      // SessionMgr.setCurrentAdd(address);
      SessionMgr.getCurrentAdd().setCity(city);
      binding.txtProvince.setText(city.getText());
      loadListData();
    }
  }

  public class Presenter implements SubjectPresenter, StorePresenter {

    public void onTabClick(int index) {
      switch (index) {
      case 0:
        // 拼团上课
        Intent intent = new Intent(getContext(), ShopListActivity.class);
        intent.putExtra(AppConfig.IntentKey.DATA, ShopListActivity.ShopQueryType.groupbuy);
        startActivity(intent);
        break;
      case 1:
        // 试听课
        Intent intent2 = new Intent(getContext(), ShopListActivity.class);
        intent2.putExtra(AppConfig.IntentKey.DATA, ShopListActivity.ShopQueryType.audition);
        startActivity(intent2);
        break;
      case 2:
        // 所有课程
        startActivity(new Intent(getContext(), SubjectListActivity.class));
        break;
      case 3:
        // 有声有色
        startImpressive();
        break;
      case 4:
        // 叮咚公益
        startActivity(new Intent(getContext(), BlueWristMainActivity.class));
        break;
      }

    }

    /**
     * 城市切换
     */
    public void onProvinceSwitch() {
      startActivityForResult(new Intent(getContext(), RegionSwitchActivity.class), 0);
    }

    @Override
    public void onSubjectItemClick(Subject subject) {
      Intent intent = new Intent(getContext(), SubjectDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, subject);
      startActivity(intent);
    }

    /**
     * 更多品质门店
     */
    public void onMoreRecommendShop() {
      Intent intent = new Intent(getContext(), ShopListActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, ShopListActivity.ShopQueryType.recommend);
      startActivity(intent);
    }

    /**
     * 更多附近门店
     */
    public void onNearStore() {
      Intent intent = new Intent(getContext(), ShopListActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, ShopListActivity.ShopQueryType.near);
      startActivity(intent);
    }

    @Override
    public void onStoreItemClick(Shop shop) {
      Intent intent = new Intent(getContext(), ShopMainActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, shop);
      startActivity(intent);
    }
  }
}
