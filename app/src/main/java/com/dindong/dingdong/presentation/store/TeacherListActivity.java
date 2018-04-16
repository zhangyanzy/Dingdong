package com.dindong.dingdong.presentation.store;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityTeacherListBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.shop.usecase.ListTeacherCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.network.bean.store.Teacher;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.ToastUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseFooterView;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseHeaderView;
import com.dindong.dingdong.widget.sweetAlert.SweetAlertDialog;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

public class TeacherListActivity extends BaseActivity {
  ActivityTeacherListBinding binding;

  private SingleTypeAdapter adapter;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_list);

    adapter = new SingleTypeAdapter(this, R.layout.item_teahcer_list);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    binding.setStore((Shop) getIntent().getSerializableExtra(AppConfig.IntentKey.DATA));
    listTeacher(true, true);
  }

  @Override
  protected void createEventHandlers() {
    binding.setPresenter(new Presenter());
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
        listTeacher(false, true);
      }
    });
    binding.refreshLayout.setOnLoadListener(new BaseFooterView.OnLoadListener() {
      @Override
      public void onLoad(BaseFooterView baseFooterView) {
        listTeacher(false, false);
      }
    });
  }

  /**
   * 获取教师列表
   * 
   * @param showProgress
   * @param isRefresh
   */
  private void listTeacher(boolean showProgress, final boolean isRefresh) {
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
    new ListTeacherCase(binding.getStore().getId(), param)
        .execute(new HttpSubscriber<List<Teacher>>() {
          @Override
          public void onFailure(String errorMsg, Response<List<Teacher>> response) {
            if (finalSweetAlertDialog != null)
              finalSweetAlertDialog.dismiss();
            DialogUtil.getErrorDialog(TeacherListActivity.this, errorMsg).show();
          }

          @Override
          public void onSuccess(Response<List<Teacher>> response) {
            loadRecyclerView(response.getData(), isRefresh, response.isMore());
            if (finalSweetAlertDialog != null)
              finalSweetAlertDialog.dismiss();

          }
        });
  }

  private void loadRecyclerView(List<Teacher> data, boolean isRefresh, boolean isMore) {
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

    public void onMobile(final String mobile) {
      SweetAlertDialog dialog = DialogUtil.getConfirmDialog(TeacherListActivity.this, mobile);
      dialog.setConfirmText(getString(R.string.shop_main_mobile));
      dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
          try {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile)));
          } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.toastFailure(TeacherListActivity.this,
                getString(R.string.shop_main_mobile_err));
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
      Intent intent = new Intent(TeacherListActivity.this, ShopMapActivity.class);
      intent.putExtra(AppConfig.IntentKey.LATITUDE, latitude);
      intent.putExtra(AppConfig.IntentKey.LONGITUDE, longitude);
      startActivity(intent);
    }

  }

}
