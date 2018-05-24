package com.dindong.dingdong.presentation.main.fragment;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.FragmentMineBinding;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.favorite.usecase.StatisticFavoriteCase;
import com.dindong.dingdong.network.api.pay.usecase.StatisticOrderCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.favorite.FavoriteStatic;
import com.dindong.dingdong.network.bean.pay.OrderStatictis;
import com.dindong.dingdong.presentation.pay.OrderListActivity;
import com.dindong.dingdong.presentation.user.SettingActivity;
import com.dindong.dingdong.presentation.user.UserAccountActivity;
import com.dindong.dingdong.presentation.user.UserInfoActivity;
import com.dindong.dingdong.presentation.user.UserMainActivity;
import com.dindong.dingdong.presentation.user.cooperation.ApplyMainActivity;
import com.dindong.dingdong.presentation.user.wrist.BlueWristMainActivity;

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
    binding.setStatistics(new FavoriteStatic());
    return binding.getRoot();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {

  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (binding != null && isVisibleToUser) {
      statisticFavorite();
      statisticOrder();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if (binding != null) {
      binding.setUser(SessionMgr.getUser());
      statisticOrder();
      statisticFavorite();
    }

  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
  }

  /**
   * 获取各状态的订单数量
   */
  private void statisticOrder() {
    new StatisticOrderCase().execute(new HttpSubscriber<OrderStatictis>() {
      @Override
      public void onFailure(String errorMsg, Response<OrderStatictis> response) {

      }

      @Override
      public void onSuccess(Response<OrderStatictis> response) {
        binding.txtWaitPayCount
            .setVisibility(response.getData().getWaitPayCount() <= 0 ? View.GONE : View.VISIBLE);
        binding.txtGroupCount
            .setVisibility(response.getData().getGroupingCount() <= 0 ? View.GONE : View.VISIBLE);
        binding.txtConfirmedCount
            .setVisibility(response.getData().getConfirmedCount() <= 0 ? View.GONE : View.VISIBLE);
        binding.txtWaitCommentCount.setVisibility(
            response.getData().getWaitCommentCount() <= 0 ? View.GONE : View.VISIBLE);

        binding.txtWaitPayCount.setText(
            (response.getData().getWaitPayCount() >= 99 ? 99 : response.getData().getWaitPayCount())
                + "");
        binding.txtGroupCount.setText((response.getData().getGroupingCount() >= 99 ? 99
            : response.getData().getGroupingCount()) + "");
        binding.txtConfirmedCount.setText((response.getData().getConfirmedCount() >= 99 ? 99
            : response.getData().getConfirmedCount()) + "");
        binding.txtWaitCommentCount.setText((response.getData().getWaitCommentCount() >= 99 ? 99
            : response.getData().getWaitCommentCount()) + "");
      }
    });
  }

  /**
   * 获取用户关注信息
   */
  private void statisticFavorite() {
    new StatisticFavoriteCase().execute(new HttpSubscriber<FavoriteStatic>() {
      @Override
      public void onFailure(String errorMsg, Response<FavoriteStatic> response) {

      }

      @Override
      public void onSuccess(Response<FavoriteStatic> response) {
        binding.setStatistics(response.getData());
      }
    });
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
      case 1:
        // 团购中
        intent.putExtra(AppConfig.IntentKey.DATA, OrderListActivity.TYPE_GROUP);
        break;
      case 2:
        // 可使用
        intent.putExtra(AppConfig.IntentKey.DATA, OrderListActivity.TYPE_USE);
        break;
      case 3:
        // 已完成
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

    /**
     * 个人主页
     */
    public void onHome() {
      startActivity(new Intent(getContext(), UserMainActivity.class));
    }

    /**
     * 查看账户明细
     */
    public void onAccount() {
      startActivity(new Intent(getContext(), UserAccountActivity.class));
    }

    /**
     * 合作
     */
    public void onCooperation() {
      startActivity(new Intent(getContext(), ApplyMainActivity.class));
    }

    /**
     * 查看个人信息
     */
    public void onUserInfo() {
      startActivity(new Intent(getContext(), UserInfoActivity.class));
    }

    /**
     * 查看全部订单
     */
    public void onAllOrder() {
      Intent intent = new Intent(getContext(), OrderListActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, OrderListActivity.TYPE_ALL);
      startActivity(intent);
    }
  }
}
