package com.dindong.dingdong.presentation.main.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amap.api.location.AMapLocation;
import com.dindong.dingdong.R;
import com.dindong.dingdong.adapter.SubjectAdapter;
import com.dindong.dingdong.adapter.SubjectPresenter;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.FragmentHomeBinding;
import com.dindong.dingdong.manager.LocationMgr;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.subject.ListHotSubjectCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.FilterParam;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.entity.Region;
import com.dindong.dingdong.network.bean.shop.Subject;
import com.dindong.dingdong.presentation.main.RegionSwitchActivity;
import com.dindong.dingdong.presentation.shop.ShopListActivity;
import com.dindong.dingdong.presentation.subject.SubjectDetailActivity;
import com.dindong.dingdong.presentation.subject.SubjectListActivity;
import com.dindong.dingdong.util.GlideUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.RegionStorageUtil;
import com.dindong.dingdong.util.ToastUtil;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * Created by wcong on 2018/3/10. 首页
 */

public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener {

  private FragmentHomeBinding binding;
  // GridView数据放置在此List中
  private List<Map<String, Object>> dataList;
  // GridView图片
  private int[] icon = {
      R.mipmap.seat, R.mipmap.seat, R.mipmap.seat, R.mipmap.seat };
  // GridView适配器
  private SimpleAdapter mSimpleAdapter;
  // RecycleView所需List
  private List<String> mData;

  private List<String> mBannerList;

  @Override
  protected View initComponent(LayoutInflater inflater, ViewGroup container) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

    return binding.getRoot();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    /**
     * 初始化GridView
     */
    dataList = new ArrayList<Map<String, Object>>();
    mSimpleAdapter = new SimpleAdapter(getContext(), getGridViewData(), R.layout.item_grid_view,
        new String[] {
            "image", "text" },
        new int[] {
            R.id.item_image, R.id.item_textview });
    binding.gvChannel.setAdapter(mSimpleAdapter);
    binding.gvChannel.setOnItemClickListener(this);

    /**
     * 将获取到的网络图片放置在Banner中
     */
    mBannerList = new ArrayList();
    mBannerList.add(
        "http://d.hiphotos.baidu.com/image/pic/item/d833c895d143ad4b3ae286d88e025aafa50f06de.jpg");
    mBannerList.add(
        "http://f.hiphotos.baidu.com/image/pic/item/9f2f070828381f305c2ce1b9a5014c086e06f0ed.jpg");
    mBannerList.add(
        "http://b.hiphotos.baidu.com/image/h%3D300/sign=46a99bea711ed21b66c928e59d6eddae/b21bb051f8198618f747e59c46ed2e738bd4e6a2.jpg");
    binding.bannerHomeFragment.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        .setIndicatorGravity(BannerConfig.CENTER).setDelayTime(2000)
        .setImageLoader(new GlideImageLoader()).setImages(mBannerList).start();

    refreshData();
  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isVisibleToUser) {
      if (binding == null)
        return;
      refreshData();
    }
  }

  /**
   * 加载GridView的图片和文件名
   */
  private List<Map<String, Object>> getGridViewData() {
    String[] tab = getResources().getStringArray(R.array.main_home_tab);
    for (int i = 0; i < icon.length; i++) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("image", icon[i]);
      map.put("text", tab[i]);
      dataList.add(map);
    }
    return dataList;
  }

  /**
   * GridView点击事件
   */
  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    switch (i) {
    case 0:
      // 附近门店
      startActivity(new Intent(getContext(), ShopListActivity.class));
      break;
    case 1:
      // 拼团上课
      ToastUtil.toastSuccess(getContext(), "拼团上课");
      break;
    case 2:
      // 全部课程
      startActivity(new Intent(getContext(), SubjectListActivity.class));
      break;
    case 3:
      // 叮咚公益
      ToastUtil.toastSuccess(getContext(), "全部课程");
      break;
    }

  }

  /**
   * 更新数据
   */
  private void refreshData() {
    // 获取当前地理位置
    LocationMgr.startLocation(new LocationMgr.ILocationCallback() {
      @Override
      public void onSuccess(AMapLocation location) {
        SessionMgr.SessionAddress add = new SessionMgr.SessionAddress();
        Region city = new Region();
        city.setId(location.getCityCode());
        city.setText(location.getCity());
        add.setCity(city);
        add.setLongitude(location.getLongitude() + "");
        add.setLatitude(location.getLatitude() + "");
        SessionMgr.setCurrentAdd(add);
        binding.txtProvince.setText(location.getProvince());
        RegionStorageUtil.add(city);
        listHotSubject();
      }

      @Override
      public void onFailure(int errorCode, String msg) {
        listHotSubject();
      }
    });

  }

  /**
   * 获取热门推荐课程
   */
  private void listHotSubject() {
    QueryParam queryParam = new QueryParam();
    queryParam.setLimit(2);
    queryParam.getFilters()
        .add(new FilterParam("cityCode", SessionMgr.getCurrentAdd().getCity().getId()));
    if (!IsEmpty.string(SessionMgr.getCurrentAdd().getLongitude())) {
      queryParam.getFilters()
          .add(new FilterParam("longitude", SessionMgr.getCurrentAdd().getLongitude()));
      queryParam.getFilters()
          .add(new FilterParam("latitude", SessionMgr.getCurrentAdd().getLatitude()));
    }

    new ListHotSubjectCase(queryParam).execute(new HttpSubscriber<List<Subject>>() {
      @Override
      public void onFailure(String errorMsg, Response<List<Subject>> response) {
      }

      @Override
      public void onSuccess(Response<List<Subject>> response) {
        SubjectAdapter subjectAdapter = new SubjectAdapter(getContext());
        subjectAdapter.setPresenter(new Presenter());
        subjectAdapter.set(response.getData());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.lstHotSubject.setLayoutManager(manager);
        binding.lstHotSubject.setAdapter(subjectAdapter);
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
      GlideUtil.load(getContext(), path, imageView);
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

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
      if (data.getSerializableExtra(AppConfig.IntentKey.DATA) == null
          || ((Region) data.getSerializableExtra(AppConfig.IntentKey.DATA)).getId()
              .equals(SessionMgr.getCurrentAdd().getCity().getId()))
        return;
      Region city = (Region) data.getSerializableExtra(AppConfig.IntentKey.DATA);
      SessionMgr.SessionAddress address = new SessionMgr.SessionAddress();
      address.setCity(city);
      SessionMgr.setCurrentAdd(address);
      binding.txtProvince.setText(city.getText());
      listHotSubject();
    }
  }

  public class Presenter implements SubjectPresenter {

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
     * 更多课程
     */
    public void onMoreSubject() {
      startActivity(new Intent(getContext(), SubjectListActivity.class));
    }
  }
}
