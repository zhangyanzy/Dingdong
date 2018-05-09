package com.dindong.dingdong.presentation.store;

import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.adapter.SubjectPresenter;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityShopMainBinding;
import com.dindong.dingdong.databinding.TabOrderListBinding;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.presentation.subject.SubjectDetailActivity;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.PhotoUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.sweetAlert.SweetAlertDialog;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * 门店主页
 */
public class ShopMainActivity extends BaseActivity {

  ActivityShopMainBinding binding;

  private Shop shop;

  private List<Fragment> fragments;

  private List<TabOrderListBinding> tabBindings;
  private List<TabOrderListBinding> tabSuspendBindings;

  private int topHeight = -1;// 顶部布局高度

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_main);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (getIntent().getSerializableExtra(AppConfig.IntentKey.DATA) != null) {
      shop = (Shop) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
      binding.setItem(shop);
      initViewPager(binding);
      initShopImg(binding);

      binding.topLayout.post(new Runnable() {
        @Override
        public void run() {
          topHeight = binding.topLayout.getMeasuredHeight();
        }
      });
    }
  }

  /**
   * 实时更新viewpager item高度
   */
  public void updateViewPagerHeight() {
    if (binding.vp == null)
      return;
    binding.vp.post(new Runnable() {
      @Override
      public void run() {
        binding.vp.updateHeight(binding.vp.getCurrentItem());
      }
    });

  }

  /**
   * 加载门店图片
   *
   * @param activityShopMainBinding
   */
  private void initShopImg(ActivityShopMainBinding activityShopMainBinding) {
    WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics dm = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) activityShopMainBinding.imgShop
        .getLayoutParams();
    params.height = (int) (width * 0.5);
    activityShopMainBinding.imgShop.setLayoutParams(params);
    PhotoUtil.load(this,
        IsEmpty.string(activityShopMainBinding.getItem().getLogoImage().getUrl()) ? ""
            : activityShopMainBinding.getItem().getLogoImage().getUrl(),
        activityShopMainBinding.imgShop);
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
    binding.setPresenter(new Presenter());
    binding.sv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
      @Override
      public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
          int oldScrollY) {

        if (topHeight > 0) {
          // 滑动距离超过tab时，显示悬浮tab否则隐藏
          binding.stLayoutSuspend.setVisibility(scrollY >= topHeight ? View.VISIBLE : View.GONE);
        }

        if (scrollY - oldScrollY > 0)
          updateViewPagerHeight();

        if (binding == null || binding.vp.getCurrentItem() != 1 || fragments == null)
          return;
        final RecyclerView recyclerView = ((ShopMainMomentFragment) fragments
            .get(binding.vp.getCurrentItem())).getRecyclerView();
        // 滑动顶部或底部时，允许用户下拉刷新上拉加载
        binding.sv.setOnTouchListener(null);
        if (scrollY - oldScrollY > 0) {
          if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
            recyclerView.setNestedScrollingEnabled(true);
          } else {
            recyclerView.setNestedScrollingEnabled(false);
          }
        } else {
          if (scrollY == 0) {
            final float[] mLastX = {
                0 };
            final float[] mLastY = {
                0 };
            binding.sv.setOnTouchListener(new View.OnTouchListener() {
              @Override
              public boolean onTouch(View v, MotionEvent event) {
                int dx = 0;
                int dy = 0;
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                  mLastX[0] = x;
                  mLastY[0] = y;
                  break;
                case MotionEvent.ACTION_MOVE:
                  dx = (int) (mLastX[0] - x);
                  dy = (int) (mLastY[0] - y);
                  Log.i("ACTION_MOVE", dx + "-" + dy);
                  recyclerView.setNestedScrollingEnabled(true);
                  break;
                }
                return false;
              }
            });
          } else {
            recyclerView.setNestedScrollingEnabled(false);
          }
        }
      }
    });
  }

  public class Presenter implements SubjectPresenter {

    @Override
    public void onSubjectItemClick(Subject subject) {
      Intent intent = new Intent(ShopMainActivity.this, SubjectDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, subject);
      intent.putExtra(AppConfig.IntentKey.SUMMARY, shop);
      startActivity(intent);
    }

    /**
     * 写评论
     */
    public void onComment(View view) {
      ToastUtil.toastHint(ShopMainActivity.this, "暂不支持");
    }

    public void onMobile(final String mobile) {
      SweetAlertDialog dialog = DialogUtil.getConfirmDialog(ShopMainActivity.this, mobile);
      dialog.setConfirmText(getString(R.string.shop_main_mobile));
      dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
          try {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile)));
          } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.toastFailure(ShopMainActivity.this, getString(R.string.shop_main_mobile_err));
          }
          sweetAlertDialog.dismiss();
        }
      });
      dialog.setCanceledOnTouchOutside(true);
      dialog.show();
    }

    /**
     * 地址详情
     *
     * @param latitude
     * @param longitude
     */
    public void onAdd(String latitude, String longitude) {
      Intent intent = new Intent(ShopMainActivity.this, ShopMapActivity.class);
      intent.putExtra(AppConfig.IntentKey.LATITUDE, latitude);
      intent.putExtra(AppConfig.IntentKey.LONGITUDE, longitude);
      startActivity(intent);
    }

  }

  private void initViewPager(ActivityShopMainBinding activityShopMainBinding) {
    tabBindings = new ArrayList<>();
    tabSuspendBindings = new ArrayList<>();
    fragments = new ArrayList<>();

    // 首页
    ShopMainHomeFragment shopMainHomeFragment = new ShopMainHomeFragment();
    Bundle bundle = new Bundle();
    bundle.putSerializable(AppConfig.IntentKey.DATA, shop);
    shopMainHomeFragment.setArguments(bundle);

    // 动态
    ShopMainMomentFragment shopMainMomentFragment = new ShopMainMomentFragment();
    shopMainMomentFragment.setArguments(bundle);

    // 详情
    ShopMainRemarkFragment mainRemarkFragment = new ShopMainRemarkFragment();
    mainRemarkFragment.setArguments(bundle);

    fragments.add(shopMainHomeFragment);
    fragments.add(shopMainMomentFragment);
    fragments.add(mainRemarkFragment);

    // 初始化tab
    activityShopMainBinding.vp.setAdapter(new TabAdapter(getSupportFragmentManager()));
    activityShopMainBinding.vp.setOffscreenPageLimit(3);
    final String[] tabs = getResources().getStringArray(R.array.shop_main_tabs);
    activityShopMainBinding.stLayout.setCustomTabView(new SmartTabLayout.TabProvider() {
      @Override
      public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        TabOrderListBinding tabOrderListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(ShopMainActivity.this), R.layout.tab_order_list, container, false);
        tabOrderListBinding.txt.setText(tabs[position]);
        tabOrderListBinding.txt
            .setTextColor(getResources().getColorStateList(R.color.color_tab_shop_main));
        if (position == 0) {
          tabOrderListBinding.txt.getPaint().setFakeBoldText(true);
          tabOrderListBinding.getRoot().setSelected(true);
        }

        tabBindings.add(tabOrderListBinding);
        return tabOrderListBinding.getRoot();
      }
    });

    // 同步到悬浮tab
    activityShopMainBinding.stLayoutSuspend.setCustomTabView(new SmartTabLayout.TabProvider() {
      @Override
      public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        TabOrderListBinding tabOrderListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(ShopMainActivity.this), R.layout.tab_order_list, container, false);
        tabOrderListBinding.txt.setText(tabs[position]);
        tabOrderListBinding.txt
            .setTextColor(getResources().getColorStateList(R.color.color_tab_shop_main));
        if (position == 0) {
          tabOrderListBinding.txt.getPaint().setFakeBoldText(true);
          tabOrderListBinding.getRoot().setSelected(true);
        }

        tabSuspendBindings.add(tabOrderListBinding);
        return tabOrderListBinding.getRoot();
      }
    });

    activityShopMainBinding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        if (tabBindings == null)
          return;
        for (TabOrderListBinding binding : tabBindings) {
          binding.txt.getPaint().setFakeBoldText(false);
        }
        tabBindings.get(position).txt.getPaint().setFakeBoldText(true);

        if (tabSuspendBindings == null)
          return;
        for (TabOrderListBinding binding : tabSuspendBindings) {
          binding.txt.getPaint().setFakeBoldText(false);
        }
        tabSuspendBindings.get(position).txt.getPaint().setFakeBoldText(true);
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
    activityShopMainBinding.stLayout.setViewPager(activityShopMainBinding.vp);
    activityShopMainBinding.stLayoutSuspend.setViewPager(activityShopMainBinding.vp);
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
