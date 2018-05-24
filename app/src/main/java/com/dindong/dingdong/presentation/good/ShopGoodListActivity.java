package com.dindong.dingdong.presentation.good;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityShopGoodListBinding;
import com.dindong.dingdong.databinding.ItemShopGoodListBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.good.usecase.ListGoodsCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.FilterParam;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.good.ShopGood;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.presentation.subject.SubjectDetailActivity;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.BindingViewHolder;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseFooterView;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseHeaderView;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

/**
 * 门店商品列表
 */
public class ShopGoodListActivity extends BaseActivity {

  ActivityShopGoodListBinding binding;

  private Shop shop;

  private SingleTypeAdapter adapter;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_good_list);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    binding.refreshLayout.setHasFooter(false);
    adapter = new SingleTypeAdapter(this, R.layout.item_shop_good_list);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    shop = (Shop) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);

    listGood(true, true);
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
        listGood(false, false);
      }
    });
    binding.refreshLayout.setOnRefreshListener(new BaseHeaderView.OnRefreshListener() {
      @Override
      public void onRefresh(BaseHeaderView baseHeaderView) {
        listGood(false, true);
      }
    });
  }

  /**
   * 查询店铺所有商品
   *
   * @param showProgress
   * @param isRefresh
   */
  private void listGood(boolean showProgress, final boolean isRefresh) {
    final QueryParam param = new QueryParam();
    if (isRefresh)
      param.setStart(0);
    else
      param.setStart(adapter.getData().size());
    param.getFilters().add(new FilterParam("shop", shop.getId()));

    new ListGoodsCase(param)
        .execute(new HttpSubscriber<List<ShopGood>>(showProgress ? this : null) {
          @Override
          public void onFailure(String errorMsg, Response<List<ShopGood>> response) {
            DialogUtil.getErrorDialog(ShopGoodListActivity.this, errorMsg).show();
          }

          @Override
          public void onSuccess(Response<List<ShopGood>> response) {
            loadRecyclerView(response.getData(), isRefresh, response.isMore());
          }
        });
  }

  private void loadRecyclerView(List<ShopGood> data, boolean isRefresh, boolean isMore) {
    if (isRefresh) {
      adapter.clear();
      binding.refreshLayout.stopRefresh();
    } else
      binding.refreshLayout.stopLoad();
    adapter.addAll(data);
    adapter.setPresenter(new Presenter());
    adapter.setDecorator(new ShopGoodDecorator());
    binding.refreshLayout.setHasFooter(isMore);
    if (isRefresh) {
      LinearLayoutManager manager = new LinearLayoutManager(this);
      manager.setOrientation(LinearLayoutManager.VERTICAL);
      binding.lst.setLayoutManager(manager);
      binding.lst.setAdapter(adapter);
    }
  }

  public class ShopGoodDecorator implements BaseViewAdapter.Decorator {

    @Override
    public void decorator(BindingViewHolder holder, int position, int viewType) {
      if (holder == null || holder.getBinding() == null)
        return;
      ItemShopGoodListBinding itemBinding = (ItemShopGoodListBinding) holder.getBinding();

      // 设置商品单位
      itemBinding.unit.setText("元" + (IsEmpty.string(itemBinding.getItem().getUnit()) ? ""
          : "/" + itemBinding.getItem().getUnit()));

      // 设置商品原价
      itemBinding.layoutOriginalAmount.setVisibility(itemBinding.getItem().getAmount()
          .compareTo(itemBinding.getItem().getOriginalAmount()) == 0 ? View.GONE : View.VISIBLE);
      itemBinding.txtOriginalAmount
          .setText("¥\b" + StringUtil.amount(itemBinding.getItem().getOriginalAmount()));
      itemBinding.txtOriginalAmount.getPaint()
          .setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }
  }

  public class Presenter implements BaseViewAdapter.Presenter {

    public void onItemClick(ShopGood good) {
      Intent intent = new Intent(ShopGoodListActivity.this, SubjectDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, good);
      intent.putExtra(AppConfig.IntentKey.SUMMARY, shop);
      startActivity(intent);
    }

  }

}
