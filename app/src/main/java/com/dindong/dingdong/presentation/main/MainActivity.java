package com.dindong.dingdong.presentation.main;

import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityMainBinding;
import com.dindong.dingdong.network.bean.auth.AuthIdentity;
import com.dindong.dingdong.presentation.main.fragment.DiscoveryFragment;
import com.dindong.dingdong.presentation.main.fragment.HomeFragment;
import com.dindong.dingdong.presentation.main.fragment.MineFragment;
import com.dindong.dingdong.presentation.main.fragment.MsgFragment;
import com.dindong.dingdong.presentation.main.fragment.WorkFragment;
import com.dindong.dingdong.util.ExitHelper;
import com.dindong.dingdong.util.KeyboardUtil;
import com.dindong.dingdong.util.PermissionUtil;
import com.dindong.dingdong.util.UpgradeUtil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

  private ActivityMainBinding binding;

  private String[] tab_member;
  private String[] tab_institution;
  private String[] tabs;

  private List<ViewGroup> tabViews;
  private List<Fragment> fragments;

  private View lastSelectView;

  public static final int IDENTIFICATION_DISCOVERY = 0X01;
  public static final int IDENTIFICATION_MSG = 0X02;

  private NotifyTimerJob discoveryJob;
  private NotifyTimerJob msgJob;

  String[] perms = {
      Manifest.permission.ACCESS_COARSE_LOCATION };

  private ExitHelper.TwicePressHolder mExitHelper;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    tab_member = new String[] {
        getString(R.string.main_tab_home), getString(R.string.main_tab_discovery),
        getString(R.string.main_tab_msg), getString(R.string.main_tab_mine) };
    tab_institution = new String[] {
        getString(R.string.main_tab_work), getString(R.string.main_tab_msg),
        getString(R.string.main_tab_mine) };

    // executeJob();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (getIntent().getSerializableExtra(AppConfig.IntentKey.DATA) != null) {
      AuthIdentity identity = (AuthIdentity) getIntent()
          .getSerializableExtra(AppConfig.IntentKey.DATA);
      initFragment(identity);
      initTab(identity);
    }

    UpgradeUtil.getInstance().checkVersion(this);

  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
    mExitHelper = new ExitHelper.TwicePressHolder(new ExitHelper.IExitInterface() {

      @Override
      public void showExitTip() {
        Toast.makeText(MainActivity.this, R.string.then_click_one_exit_procedure, Toast.LENGTH_LONG)
            .show();

      }

      @Override
      public void exit() {
        finish();
      }
    }, 3000);
    KeyboardUtil.setKeyboardVisibleListener(binding.getRoot(), this,
        new KeyboardUtil.OnKeyboardVisibleListener() {
          @Override
          public void onKeyboardVisible(boolean visible) {
            binding.layoutBottomTab.setVisibility(visible ? View.GONE : View.VISIBLE);
          }
        });
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    return mExitHelper.onKeyDown(keyCode, event);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
      // 申请WRITE_EXTERNAL_STORAGE权限
      ActivityCompat.requestPermissions(this, new String[] {
          Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA }, 100);// 自定义的code
    }
    PermissionUtil.check(this, perms, 100, getString(R.string.permission_req_location_camera),
        getString(R.string.permission_dialog));

  }

  private void initTab(AuthIdentity identity) {
    tabViews = new ArrayList<>();
    int tabPosition = 0;
    if (identity.equals(AuthIdentity.MEMBER))
      tabs = tab_member;
    else if (identity.equals(AuthIdentity.INSTITUTION))
      tabs = tab_institution;
    for (int i = 0; i < binding.tab.getChildCount(); i++) {
      if (binding.tab.getChildAt(i) instanceof ImageView) {
        if (identity.equals(AuthIdentity.INSTITUTION))
          // 机构身份，隐藏中间btn
          binding.tab.getChildAt(i).setVisibility(View.GONE);
        continue;
      }
      if (identity.equals(AuthIdentity.INSTITUTION) && i == 0) {
        // 机构身份，隐藏第一个tab
        binding.tab.getChildAt(i).setVisibility(View.GONE);
        continue;
      }
      tabViews.add((ViewGroup) ((ViewGroup) binding.tab.getChildAt(i)).getChildAt(0));
      if (tabPosition == 0) {
        lastSelectView = tabViews.get(0);
        setTabSelect(0);
      }
      ((TextView) ((ViewGroup) tabViews.get(tabPosition)).getChildAt(1))
          .setText(tabs[tabPosition++]);
    }
  }

  private void initFragment(AuthIdentity identity) {
    fragments = new ArrayList<>();
    if (identity.equals(AuthIdentity.MEMBER)) {
      fragments.add(new HomeFragment());
      fragments.add(new DiscoveryFragment());
      fragments.add(new MsgFragment());
      fragments.add(new MineFragment());
    } else if (identity.equals(AuthIdentity.INSTITUTION)) {
      fragments.add(new WorkFragment());
      fragments.add(new MsgFragment());
      fragments.add(new MineFragment());
    }
    binding.vp.setOffscreenPageLimit(identity.equals(AuthIdentity.MEMBER) ? 4 : 3);
    binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        // if (position == 1 ) {
        // ToastUtil.toastHint(MainActivity.this, "敬请期待");
        // binding.vp.setCurrentItem(0);
        // setTabSelect(0);
        // return;
        // }
        setTabSelect(position);
      }

      @Override
      public void onPageScrolled(int arg0, float arg1, int arg2) {
      }

      @Override
      public void onPageScrollStateChanged(int arg0) {
      }
    });

    MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
    binding.vp.setAdapter(adapter);

    binding.vp.setCurrentItem(0);
  }

  private void setTabSelect(int index) {
    if (tabViews == null)
      return;
    lastSelectView.setSelected(false);
    lastSelectView = tabViews.get(index);
    lastSelectView.setSelected(true);
  }

  /**
   * 启动定时任务，用于处理新消息
   */
  private void executeJob() {
    // TODO 更新发现频道
    discoveryJob = new NotifyTimerJob(IDENTIFICATION_DISCOVERY);
    discoveryJob.execute(new NotifyTimerJob.TimerListener() {
      @Override
      public void onSuccess(boolean hasNew) {
        if (hasNew)
          updateNotifyItem(IDENTIFICATION_DISCOVERY, true);
      }
    });

    // TODO 更新消息频道
    msgJob = new NotifyTimerJob(IDENTIFICATION_MSG);
    msgJob.execute(new NotifyTimerJob.TimerListener() {
      @Override
      public void onSuccess(boolean hasNew) {
        if (hasNew)
          updateNotifyItem(IDENTIFICATION_MSG, true);
      }
    });
  }

  /**
   * 同步未读消息
   *
   * @param identification
   *          {@link this#IDENTIFICATION_DISCOVERY}{@link this#IDENTIFICATION_MSG}
   * @param visible
   */
  public void updateNotifyItem(int identification, boolean visible) {
    if (tabViews == null)
      return;
    for (ViewGroup viewGroup : tabViews) {
      String tag = "";
      if (identification == IDENTIFICATION_DISCOVERY) {
        tag = getString(R.string.main_tab_discovery);
      } else if (identification == IDENTIFICATION_MSG) {
        tag = getString(R.string.main_tab_msg);
      }
      if (((TextView) viewGroup.getChildAt(1)).getText().toString().trim().equals(tag)) {
        ((ViewGroup) viewGroup.getChildAt(0)).getChildAt(1)
            .setVisibility(visible ? View.VISIBLE : View.GONE);
        break;
      }

    }
  }

  private class MainPagerAdapter extends FragmentPagerAdapter {
    MainPagerAdapter(FragmentManager fm) {
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
    public void onTabSelect(View view) {
      for (int i = 0; i < tabs.length; i++) {
        if (((TextView) (((ViewGroup) view).getChildAt(1))).getText().toString().equals(tabs[i])) {
          // if (i == 1 ) {
          // ToastUtil.toastHint(MainActivity.this, "敬请期待");
          // return;
          // }
          binding.vp.setCurrentItem(i);
          setTabSelect(i);
          return;
        }
      }
    }
  }
}
