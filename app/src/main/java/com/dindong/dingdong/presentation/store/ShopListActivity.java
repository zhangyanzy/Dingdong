package com.dindong.dingdong.presentation.store;

import java.io.Serializable;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.adapter.StoreAdapter;
import com.dindong.dingdong.adapter.StorePresenter;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityShopListBinding;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.shop.usecase.ListShopCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.FilterParam;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.KeyboardUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseFooterView;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseHeaderView;
import com.dindong.dingdong.widget.sweetAlert.SweetAlertDialog;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

/**
 * Created by wcong on 2018/3/10. 门店列表
 */

public class ShopListActivity extends BaseActivity {

  private ActivityShopListBinding binding;

  private StoreAdapter adapter;

  private ShopQueryType shopQueryType = ShopQueryType.all;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_list);

    adapter = new StoreAdapter(this);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    if (getIntent().getSerializableExtra(AppConfig.IntentKey.DATA) != null) {
      shopQueryType = (ShopQueryType) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
    }
    setTitle();
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
    binding.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          handled = true;

          listShop(true, true);
          KeyboardUtil.control(binding.edtSearch, false);
        }
        return handled;
      }
    });
  }

  private void setTitle() {
    String title = "";
    if (shopQueryType.equals(ShopQueryType.groupbuy)) {
      title = getString(R.string.shop_list_title_group);
    } else if (shopQueryType.equals(ShopQueryType.audition)) {
      title = getString(R.string.shop_list_title_audition);
    } else if (shopQueryType.equals(ShopQueryType.all)) {
      title = getString(R.string.shop_list_title_all);
    } else if (shopQueryType.equals(ShopQueryType.recommend)) {
      title = getString(R.string.shop_list_title_recommend);
    } else if (shopQueryType.equals(ShopQueryType.near)) {
      title = getString(R.string.shop_list_title_near);
    }
    binding.nb.setCenterTitleText(title);
  }

  /**
   * 查询门店
   * 
   * @param showProgress
   * @param isRefresh
   */
  private void listShop(boolean showProgress, final boolean isRefresh) {
    QueryParam queryParam = new QueryParam();
    if (isRefresh)
      queryParam.setStart(0);
    else
      queryParam.setStart(adapter.getData().size());
    if (!IsEmpty.string(binding.edtSearch.getText().toString())) {
      queryParam.getFilters()
          .add(new FilterParam("keyword", binding.edtSearch.getText().toString()));
    }
    queryParam.getFilters().add(new FilterParam("queryType:", shopQueryType.toString()));
    queryParam.getFilters()
        .add(new FilterParam("cityCode", SessionMgr.getCurrentAdd().getCity().getId()));
    if (!IsEmpty.string(SessionMgr.getCurrentAdd().getLongitude())) {
      queryParam.getFilters()
          .add(new FilterParam("longitude", SessionMgr.getCurrentAdd().getLongitude()));
      queryParam.getFilters()
          .add(new FilterParam("latitude", SessionMgr.getCurrentAdd().getLatitude()));
    }

    SweetAlertDialog sweetAlertDialog = null;
    if (showProgress) {
      sweetAlertDialog = DialogUtil.getProgressDialog(this);
      sweetAlertDialog.show();
    }

    final SweetAlertDialog finalSweetAlertDialog = sweetAlertDialog;
    new ListShopCase(queryParam).execute(new HttpSubscriber<List<Shop>>() {
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
    binding.refreshLayout.setHasFooter(isMore);
    if (isRefresh) {
      LinearLayoutManager manager = new LinearLayoutManager(this);
      manager.setOrientation(LinearLayoutManager.VERTICAL);
      binding.lst.setLayoutManager(manager);
      binding.lst.setAdapter(adapter);
    }
  }

  public class Presenter implements BaseViewAdapter.Presenter, StorePresenter {

    @Override
    public void onStoreItemClick(Shop shop) {
      Intent intent = new Intent(ShopListActivity.this, ShopMainActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, shop);
      startActivity(intent);
    }
  }

  public enum ShopQueryType implements Serializable {
    /**
     * 试听课
     */
    audition
    /**
     * 拼团上课
     */
    , groupbuy
    /**
     * 全部课程
     */
    , all
    /**
     * 品质优选
     */
    , recommend
    /**
     * 附近门店
     */
    ,near
  }
}
