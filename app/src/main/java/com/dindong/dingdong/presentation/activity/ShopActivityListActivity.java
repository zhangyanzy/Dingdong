package com.dindong.dingdong.presentation.activity;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityShopActivityListBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.activity.usecase.ListActivityCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.activity.ShopActivity;
import com.dindong.dingdong.network.bean.entity.FilterParam;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.store.Shop;
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
 * 门店活动列表
 */
public class ShopActivityListActivity extends BaseActivity {

  ActivityShopActivityListBinding binding;

  private Shop shop;

  private SingleTypeAdapter adapter;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_activity_list);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    binding.refreshLayout.setHasFooter(false);
    adapter = new SingleTypeAdapter(this, R.layout.item_shop_activity_list);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    shop = (Shop) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);

    listShopActivity(true, true);
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
    binding.refreshLayout.setOnLoadListener(new BaseFooterView.OnLoadListener() {
      @Override
      public void onLoad(BaseFooterView baseFooterView) {
        listShopActivity(false, false);
      }
    });
    binding.refreshLayout.setOnRefreshListener(new BaseHeaderView.OnRefreshListener() {
      @Override
      public void onRefresh(BaseHeaderView baseHeaderView) {
        listShopActivity(false, true);
      }
    });
  }

  /**
   * 查询店铺所有活动
   *
   * @param showProgress
   * @param isRefresh
   */
  private void listShopActivity(boolean showProgress, final boolean isRefresh) {
    final QueryParam param = new QueryParam();
    if (isRefresh)
      param.setStart(0);
    else
      param.setStart(adapter.getData().size());
    param.getFilters().add(new FilterParam("shop", shop.getId()));

    new ListActivityCase(param)
        .execute(new HttpSubscriber<List<ShopActivity>>(showProgress ? this : null) {
          @Override
          public void onFailure(String errorMsg, Response<List<ShopActivity>> response) {
            DialogUtil.getErrorDialog(ShopActivityListActivity.this, errorMsg).show();
          }

          @Override
          public void onSuccess(Response<List<ShopActivity>> response) {
            loadRecyclerView(response.getData(), isRefresh, response.isMore());
          }
        });
  }

  private void loadRecyclerView(List<ShopActivity> data, boolean isRefresh, boolean isMore) {
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

    public void onItemClick(ShopActivity shopActivity) {
      Intent intent = new Intent(ShopActivityListActivity.this, ShopActivityDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, shopActivity);
      intent.putExtra(AppConfig.IntentKey.SUMMARY, shop);
      startActivity(intent);
    }

  }
}
