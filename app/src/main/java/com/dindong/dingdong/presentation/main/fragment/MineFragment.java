package com.dindong.dingdong.presentation.main.fragment;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.FragmentMineBinding;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.presentation.pay.OrderListActivity;
import com.dindong.dingdong.presentation.user.wrist.BlueWristMainActivity;
import com.dindong.dingdong.presentation.user.wrist.BlueWristScanActivity;
import com.dindong.dingdong.presentation.user.SettingActivity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wcong on 2018/3/10. 我的
 */

public class MineFragment extends BaseFragment {
  private FragmentMineBinding binding;

  @Override
  protected View initComponent(LayoutInflater inflater, ViewGroup container) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine, container, false);

    binding.setUser(SessionMgr.getUser());
    return binding.getRoot();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {

  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
  }

  public class Presenter {
    /**
     * 订单选择
     * 
     * @param index
     */
    public void onOrderStateSelect(int index) {
      Intent intent = new Intent(getContext(), OrderListActivity.class);
      switch (index) {
      case 0:
        // 待支付
        intent.putExtra(AppConfig.IntentKey.DATA, OrderListActivity.TYPE_WAIT_PAY);
        break;
      case 2:
        // 可使用
        intent.putExtra(AppConfig.IntentKey.DATA, OrderListActivity.TYPE_USE);
        break;
      case 3:
        // 待评价
        intent.putExtra(AppConfig.IntentKey.DATA, OrderListActivity.TYPE_FINISH);
        break;
      }
      startActivity(intent);
    }

    /**
     * 关注选择
     * 
     * @param index
     */
    public void onFavSelect(int index) {
      switch (index) {
      case 0:
        // 门店关注
        break;
      case 1:
        // 课程关注
        break;
      case 2:
        // 关注
        break;
      case 3:
        // 粉丝
        break;
      }
    }

    /**
     * 个人设置
     * 
     * @param view
     */
    public void onSetting(View view) {
      startActivity(new Intent(getContext(), SettingActivity.class));
    }

    /**
     * 叮咚公益
     */
    public void onWelfare() {
      startActivity(new Intent(getContext(), BlueWristMainActivity.class));
    }
  }
}
