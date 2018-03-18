package com.dindong.dingdong.presentation.shop;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityShopListBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.shop.usecase.ListShopCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.shop.Shop;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.BindingViewHolder;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseFooterView;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseHeaderView;
import com.dindong.dingdong.widget.sweetAlert.SweetAlertDialog;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by wcong on 2018/3/10. 门店列表
 */

public class ShopListActivity extends BaseActivity {

  private ActivityShopListBinding binding;

  private SingleTypeAdapter adapter;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_list);
    binding.nb.setCenterTitleText(R.string.all_shop);

    adapter = new SingleTypeAdapter(this, R.layout.item_shop_list);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    listShop(true, true);
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
        listShop(false, true);
      }
    });
    binding.refreshLayout.setOnLoadListener(new BaseFooterView.OnLoadListener() {
      @Override
      public void onLoad(BaseFooterView baseFooterView) {
        listShop(false, false);
      }
    });
  }

  /**
   * 查询附近门店
   * 
   * @param showProgress
   * @param isRefresh
   */
  private void listShop(boolean showProgress, final boolean isRefresh) {
    final QueryParam param = new QueryParam();
    if (isRefresh)
      param.setStart(0);
    else
      param.setStart(adapter.getData().size());

    SweetAlertDialog sweetAlertDialog = null;
    if (showProgress) {
      sweetAlertDialog = DialogUtil.getProgressDialog(this);
      sweetAlertDialog.show();
    }

    final SweetAlertDialog finalSweetAlertDialog = sweetAlertDialog;
    new ListShopCase(param).execute(new HttpSubscriber<List<Shop>>() {
      @Override
      public void onFailure(String errorMsg, Response<List<Shop>> response) {
        if (finalSweetAlertDialog != null)
          finalSweetAlertDialog.dismiss();
        DialogUtil.getErrorDialog(ShopListActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<List<Shop>> response) {
        loadRecyclerView(response.getData(), isRefresh, response.isMore());
        if (finalSweetAlertDialog != null)
          finalSweetAlertDialog.dismiss();

      }
    });
  }

  private void loadRecyclerView(List<Shop> data, boolean isRefresh, boolean isMore) {
    if (isRefresh) {
      adapter.clear();
      binding.refreshLayout.stopRefresh();
    } else
      binding.refreshLayout.stopLoad();
    adapter.addAll(data);
    adapter.setPresenter(new Presenter());
    adapter.setDecorator(new Decorator());
    binding.refreshLayout.setHasFooter(isMore);
    if (isRefresh) {
      LinearLayoutManager manager = new LinearLayoutManager(this);
      manager.setOrientation(LinearLayoutManager.VERTICAL);
      binding.lst.setLayoutManager(manager);
      binding.lst.setAdapter(adapter);
    }
  }

  class Decorator implements BaseViewAdapter.Decorator {

    @Override
    public void decorator(BindingViewHolder holder, int position, int viewType) {
      if (holder == null || holder.getBinding() == null)
        return;

    }
  }

  public class Presenter implements BaseViewAdapter.Presenter {

  }
}
