package com.dindong.dingdong.presentation.subject;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.adapter.SubjectAdapter;
import com.dindong.dingdong.adapter.SubjectPresenter;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityShopSubjectListBinding;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.subject.usecase.ListSubjectCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.FilterParam;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.KeyboardUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
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
 * 门店课程列表
 */
public class ShopSubjectListActivity extends BaseActivity {

  ActivityShopSubjectListBinding binding;

  private SubjectAdapter adapter;

  private Shop shop;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_subject_list);

    adapter = new SubjectAdapter(this);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    shop = (Shop) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA);
    listSubject(true, true);
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
        listSubject(false, true);
      }
    });
    binding.refreshLayout.setOnLoadListener(new BaseFooterView.OnLoadListener() {
      @Override
      public void onLoad(BaseFooterView baseFooterView) {
        listSubject(false, false);
      }
    });
    binding.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          handled = true;

          listSubject(true, true);
          KeyboardUtil.control(binding.edtSearch, false);
        }
        return handled;
      }
    });

  }

  /**
   * 查询所有课程
   *
   * @param showProgress
   * @param isRefresh
   */
  private void listSubject(boolean showProgress, final boolean isRefresh) {
    final QueryParam param = new QueryParam();
    if (isRefresh)
      param.setStart(0);
    else
      param.setStart(adapter.getData().size());
    if (!IsEmpty.string(binding.edtSearch.getText().toString())) {
      param.getFilters().add(new FilterParam("keyword", binding.edtSearch.getText().toString()));
    }
    param.getFilters()
        .add(new FilterParam("cityCode", SessionMgr.getCurrentAdd().getCity().getId()));
    if (!IsEmpty.string(SessionMgr.getCurrentAdd().getLongitude())) {
      param.getFilters()
          .add(new FilterParam("longitude", SessionMgr.getCurrentAdd().getLongitude()));
      param.getFilters().add(new FilterParam("latitude", SessionMgr.getCurrentAdd().getLatitude()));
    }
    param.getFilters().add(new FilterParam("shop", shop.getId()));

    SweetAlertDialog sweetAlertDialog = null;
    if (showProgress) {
      sweetAlertDialog = DialogUtil.getProgressDialog(this);
      sweetAlertDialog.show();
    }

    final SweetAlertDialog finalSweetAlertDialog = sweetAlertDialog;
    new ListSubjectCase(param).execute(new HttpSubscriber<List<Subject>>() {
      @Override
      public void onFailure(String errorMsg, Response<List<Subject>> response) {
        if (finalSweetAlertDialog != null)
          finalSweetAlertDialog.dismiss();
        DialogUtil.getErrorDialog(ShopSubjectListActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<List<Subject>> response) {
        loadRecyclerView(response.getData(), isRefresh, response.isMore());
        if (finalSweetAlertDialog != null)
          finalSweetAlertDialog.dismiss();

      }
    });
  }

  private void loadRecyclerView(List<Subject> data, boolean isRefresh, boolean isMore) {
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

  public class Presenter implements SubjectPresenter {

    @Override
    public void onSubjectItemClick(Subject subject) {
      Intent intent = new Intent(ShopSubjectListActivity.this, SubjectDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, subject);
      intent.putExtra(AppConfig.IntentKey.SUMMARY, shop);
      startActivity(intent);
    }

  }
}
