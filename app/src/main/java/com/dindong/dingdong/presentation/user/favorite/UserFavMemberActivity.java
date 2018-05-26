package com.dindong.dingdong.presentation.user.favorite;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityUserFavMemberBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.favorite.usecase.ListFavMemberCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.auth.User;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.presentation.user.UserMainActivity;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseFooterView;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseHeaderView;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

/**
 * 用户关注列表
 */
public class UserFavMemberActivity extends BaseActivity {

  ActivityUserFavMemberBinding binding;

  private SingleTypeAdapter adapter;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_user_fav_member);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    binding.refreshLayout.setHasFooter(false);
    adapter = new SingleTypeAdapter(this, R.layout.item_user_fav_member);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {

    listMember(true, true);
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
    binding.refreshLayout.setOnRefreshListener(new BaseHeaderView.OnRefreshListener() {
      @Override
      public void onRefresh(BaseHeaderView baseHeaderView) {
        listMember(false, true);
      }
    });
    binding.refreshLayout.setOnLoadListener(new BaseFooterView.OnLoadListener() {
      @Override
      public void onLoad(BaseFooterView baseFooterView) {
        listMember(false, false);
      }
    });
  }

  /**
   * 查询用户关注列表
   *
   * @param showProgress
   * @param isRefresh
   */
  private void listMember(boolean showProgress, final boolean isRefresh) {
    final QueryParam param = new QueryParam();
    if (isRefresh)
      param.setStart(0);
    else
      param.setStart(adapter.getData().size());

    new ListFavMemberCase(param)
        .execute(new HttpSubscriber<List<User>>(showProgress ? this : null) {
          @Override
          public void onFailure(String errorMsg, Response<List<User>> response) {
            DialogUtil.getErrorDialog(UserFavMemberActivity.this, errorMsg).show();
          }

          @Override
          public void onSuccess(Response<List<User>> response) {
            loadRecyclerView(response.getData(), isRefresh, response.isMore());
          }
        });
  }

  private void loadRecyclerView(List<User> data, boolean isRefresh, boolean isMore) {
    if (isRefresh) {
      adapter.clear();
      binding.refreshLayout.stopRefresh();
    } else
      binding.refreshLayout.stopLoad();
    adapter.addAll(data);
    adapter.setPresenter(new Presenter());
    binding.refreshLayout.setHasFooter(isMore);
    if (isRefresh) {
      LinearLayoutManager manager = new LinearLayoutManager(this);
      manager.setOrientation(LinearLayoutManager.VERTICAL);
      binding.lst.setLayoutManager(manager);
      binding.lst.setAdapter(adapter);
    }
  }

  public class Presenter implements BaseViewAdapter.Presenter {

    public void onItemClick(User user) {
      Intent intent = new Intent(UserFavMemberActivity.this, UserMainActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, user.getId());
      startActivity(intent);
    }

  }

}
