package com.dindong.dingdong.presentation.main.fragment;

import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.databinding.FragmentMsgBinding;
import com.dindong.dingdong.databinding.TabOrderListBinding;
import com.dindong.dingdong.presentation.main.MainActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wcong on 2018/3/10. 消息
 */

public class MsgFragment extends BaseFragment {
  private FragmentMsgBinding binding;

  private List<Fragment> fragments;

  @Override
  protected View initComponent(LayoutInflater inflater, ViewGroup container) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg, container, false);

    binding.nb.setLeftImageVisiable(View.GONE);
    initViewPager();
    return binding.getRoot();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {

  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isVisibleToUser)
      // 标记已读
      ((MainActivity) getActivity()).updateNotifyItem(MainActivity.IDENTIFICATION_MSG, false);
  }

  private void initViewPager() {
    fragments = new ArrayList<>();
    fragments.add(new MsgTabFragment(0));
    fragments.add(new MsgTabFragment(1));

    binding.vp.setAdapter(new TabAdapter(getChildFragmentManager()));
    binding.vp.setOffscreenPageLimit(3);
    final String[] tabs = getResources().getStringArray(R.array.msg_tabs);
    binding.stLayout.setCustomTabView(new SmartTabLayout.TabProvider() {
      @Override
      public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        TabOrderListBinding tabOrderListBinding = DataBindingUtil
            .inflate(LayoutInflater.from(getContext()), R.layout.tab_order_list, container, false);
        tabOrderListBinding.txt.setText(tabs[position]);
        if (position == 0)
          tabOrderListBinding.getRoot().setSelected(true);
        return tabOrderListBinding.getRoot();
      }
    });
    binding.stLayout.setViewPager(binding.vp);
  }

  private class TabAdapter extends FragmentStatePagerAdapter {
    public TabAdapter(FragmentManager fm) {
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

}
