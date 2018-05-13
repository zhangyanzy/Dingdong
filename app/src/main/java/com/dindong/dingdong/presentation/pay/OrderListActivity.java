package com.dindong.dingdong.presentation.pay;

import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityOrderListBinding;
import com.dindong.dingdong.databinding.TabOrderListBinding;
import com.dindong.dingdong.network.bean.pay.OrderState;
import com.dindong.dingdong.widget.NavigationTopBar;
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

public class OrderListActivity extends BaseActivity {

  ActivityOrderListBinding binding;

  public static final int TYPE_ALL = 0X00;// 全部
  public static final int TYPE_WAIT_PAY = 0X01;// 代付款
  public static final int TYPE_USE = 0X02;// 可用
  public static final int TYPE_FINISH = 0X03;// 已完成，待评价
  public static final int TYPE_GROUP = 0X04;// 团购中

  private int tabPosition = 0;

  private List<Fragment> fragments;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_order_list);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    initViewPager();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    int intentType = getIntent().getIntExtra(AppConfig.IntentKey.DATA, -1);
    if (intentType != -1) {
      switch (intentType) {
      case OrderListActivity.TYPE_WAIT_PAY:
        tabPosition = 1;
        break;
      case OrderListActivity.TYPE_GROUP:
        tabPosition = 2;
        break;
      case OrderListActivity.TYPE_USE:
        tabPosition = 3;
        break;
      case OrderListActivity.TYPE_FINISH:
        tabPosition = 4;
        break;
      }
    }
    initViewPager();
  }

  @Override
  protected void createEventHandlers() {
    binding.nb
        .setNavigationTopBarClickListener(new NavigationTopBar.NavigationTopBarClickListener() {
          @Override
          public void leftImageClick() {
            finish();
          }
        });
  }

  private void initViewPager() {
    fragments = new ArrayList<>();
    fragments.add(new OrderListFragment(null));
    fragments.add(new OrderListFragment(OrderState.wait));
    fragments.add(new OrderListFragment(OrderState.wait_group));
    fragments.add(new OrderListFragment(OrderState.can_use));
    fragments.add(new OrderListFragment(OrderState.success));

    binding.vp.setAdapter(new CheckMainAdapter(getSupportFragmentManager()));
    binding.vp.setOffscreenPageLimit(fragments.size());
    final String[] tabs = getResources().getStringArray(R.array.order_list_tab);
    binding.stLayout.setCustomTabView(new SmartTabLayout.TabProvider() {
      @Override
      public View createTabView(ViewGroup container, final int position, PagerAdapter adapter) {
        TabOrderListBinding tabOrderListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(OrderListActivity.this), R.layout.tab_order_list, container, false);
        tabOrderListBinding.txt.setText(tabs[position]);
        if (position == tabPosition)
          tabOrderListBinding.getRoot().setSelected(true);
        return tabOrderListBinding.getRoot();
      }
    });
    binding.vp.setCurrentItem(tabPosition);
    binding.stLayout.setViewPager(binding.vp);
  }

  private class CheckMainAdapter extends FragmentStatePagerAdapter {
    public CheckMainAdapter(FragmentManager fm) {
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
