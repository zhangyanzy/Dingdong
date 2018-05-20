package com.dindong.dingdong.presentation.user;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.adapter.SubjectAdapter;
import com.dindong.dingdong.adapter.SubjectPresenter;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.ActivityUserMainBinding;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.moment.usecase.ListMomentCase;
import com.dindong.dingdong.network.api.subject.usecase.ListHotSubjectCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.network.bean.entity.FilterParam;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.presentation.subject.SubjectDetailActivity;
import com.dindong.dingdong.presentation.subject.SubjectListActivity;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

/**
 * 个人主页
 */
public class UserMainActivity extends BaseActivity {
  ActivityUserMainBinding binding;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_user_main);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    binding.setUser(SessionMgr.getUser());
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    listHotSubject();
    listMoment(true, true);
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
    binding.setPresenter(new Presenter());
  }

  /**
   * 获取热门推荐课程
   */
  private void listHotSubject() {
    QueryParam queryParam = new QueryParam();
    queryParam.setLimit(2);
    queryParam.getFilters()
        .add(new FilterParam("cityCode", SessionMgr.getCurrentAdd().getCity().getId()));
    if (!IsEmpty.string(SessionMgr.getCurrentAdd().getLongitude())) {
      queryParam.getFilters()
          .add(new FilterParam("longitude", SessionMgr.getCurrentAdd().getLongitude()));
      queryParam.getFilters()
          .add(new FilterParam("latitude", SessionMgr.getCurrentAdd().getLatitude()));
    }

    new ListHotSubjectCase(queryParam).execute(new HttpSubscriber<List<Subject>>() {
      @Override
      public void onFailure(String errorMsg, Response<List<Subject>> response) {
      }

      @Override
      public void onSuccess(Response<List<Subject>> response) {
        SubjectAdapter subjectAdapter = new SubjectAdapter(UserMainActivity.this);
        subjectAdapter.setPresenter(new Presenter());
        subjectAdapter.set(response.getData());
        LinearLayoutManager manager = new LinearLayoutManager(UserMainActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.lstHotSubject.setLayoutManager(manager);
        binding.lstHotSubject.setAdapter(subjectAdapter);
        binding.lstHotSubject.setNestedScrollingEnabled(false);
      }
    });

  }

  /**
   * 获取个人状态
   * 
   * @param showProgress
   * @param isRefresh
   */
  private void listMoment(boolean showProgress, final boolean isRefresh) {
    final QueryParam param = new QueryParam();
    if (isRefresh) {
      param.setStart(0);
    }
    param.getFilters().add(new FilterParam("relationId", SessionMgr.getUser().getId()));

    new ListMomentCase(param)
        .execute(new HttpSubscriber<List<Comment>>(showProgress ? UserMainActivity.this : null) {
          @Override
          public void onFailure(String errorMsg, Response<List<Comment>> response) {
            DialogUtil.getErrorDialog(UserMainActivity.this, errorMsg).show();
          }

          @Override
          public void onSuccess(Response<List<Comment>> response) {
            loadRecyclerView(response.getData(), isRefresh, response.isMore());
          }
        });
  }

  private void loadRecyclerView(List<Comment> data, boolean isRefresh, boolean isMore) {
    SingleTypeAdapter adapter = new SingleTypeAdapter(this, R.layout.item_user_moment);
    if (isRefresh) {
      adapter.clear();
    }
    adapter.addAll(data);
    if (isRefresh) {
      final LinearLayoutManager manager = new LinearLayoutManager(this);
      manager.setOrientation(LinearLayoutManager.VERTICAL);
      manager.setSmoothScrollbarEnabled(true);
      manager.setAutoMeasureEnabled(true);
      binding.lstMoment.setLayoutManager(manager);
      binding.lstMoment.setAdapter(adapter);

      binding.lstMoment.setHasFixedSize(true);
      binding.lstMoment.setNestedScrollingEnabled(false);

    }
  }

  public class Presenter implements SubjectPresenter {
    @Override
    public void onSubjectItemClick(Subject subject) {
      Intent intent = new Intent(UserMainActivity.this, SubjectDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, subject);
      startActivity(intent);
    }

    /**
     * 更多课程
     */
    public void onMoreSubject() {
      startActivity(new Intent(UserMainActivity.this, SubjectListActivity.class));
    }
  }

}
