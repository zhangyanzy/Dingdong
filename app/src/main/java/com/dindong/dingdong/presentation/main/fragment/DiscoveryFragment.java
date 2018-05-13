package com.dindong.dingdong.presentation.main.fragment;

import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.databinding.FragmentDiscoveryBinding;
import com.dindong.dingdong.presentation.discovery.DiscoveryMomentFragment;
import com.dindong.dingdong.presentation.main.MainActivity;
import com.dindong.dingdong.util.ToastUtil;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wcong on 2018/3/10. 发现
 */

public class DiscoveryFragment extends BaseFragment {
  private FragmentDiscoveryBinding binding;

  private List<View> tabViews;
  private List<Fragment> fragments;

  private View lastSelectView;

  @Override
  protected View initComponent(LayoutInflater inflater, ViewGroup container) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discovery, container, false);

    binding.nb.setLeftImageVisiable();
    return binding.getRoot();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    initFragment();
  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
  }

  private void initFragment() {
    tabViews = new ArrayList<>();
    tabViews.add(binding.tabMoment);
    tabViews.add(binding.tabSound);

    fragments = new ArrayList<>();

    fragments.add(new DiscoveryMomentFragment());
    fragments.add(new DiscoveryMomentFragment());
    binding.vp.setOffscreenPageLimit(2);
    binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        setTabSelect(position);
      }

      @Override
      public void onPageScrolled(int arg0, float arg1, int arg2) {
      }

      @Override
      public void onPageScrollStateChanged(int arg0) {
      }
    });

    DiscoveryPagerAdapter adapter = new DiscoveryPagerAdapter(getChildFragmentManager());
    binding.vp.setAdapter(adapter);

    lastSelectView = binding.tabMoment;
    setTabSelect(0);
  }

  private void setTabSelect(int index) {
    if (tabViews == null)
      return;
    if (index==1){
      ToastUtil.toastHint(getContext(),"暂未开放，敬请期待");
      binding.vp.setCurrentItem(0);
      return;
    }
    binding.vp.setCurrentItem(index);
    lastSelectView.setSelected(false);
    lastSelectView = tabViews.get(index);
    lastSelectView.setSelected(true);
  }


  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isVisibleToUser && getActivity() != null)
      // 标记已读
      ((MainActivity) getActivity()).updateNotifyItem(MainActivity.IDENTIFICATION_DISCOVERY, false);
    if (binding == null)
      return;
  }

  private class DiscoveryPagerAdapter extends FragmentPagerAdapter {
    DiscoveryPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      return fragments.get(position);
    }

    @Override
    public int getCount() {
      return fragments.size();
    }
  }

  public class Presenter {

    public void onTabSelect(View view, int index) {
      setTabSelect(index);
    }
  }

}
