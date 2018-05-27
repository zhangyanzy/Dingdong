package com.dindong.dingdong.presentation.main.fragment;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.databinding.FragmentMsgBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.notice.usecase.ListMsgCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.notice.PublicNotice;
import com.dindong.dingdong.presentation.main.MainActivity;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseFooterView;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseHeaderView;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wcong on 2018/3/10. 消息
 */

public class MsgFragment extends BaseFragment {
  private FragmentMsgBinding binding;

  private SingleTypeAdapter adapter;

  @Override
  protected View initComponent(LayoutInflater inflater, ViewGroup container) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg, container, false);

    binding.nb.setLeftImageVisiable();
    adapter = new SingleTypeAdapter(getContext(), R.layout.item_msg);
    return binding.getRoot();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {

  }

  @Override
  protected void firstVisible() {
    loadMsg(true, true);
  }

  @Override
  protected void createEventHandlers() {
    binding.refreshLayout.setOnRefreshListener(new BaseHeaderView.OnRefreshListener() {
      @Override
      public void onRefresh(BaseHeaderView baseHeaderView) {
        loadMsg(false, true);
      }
    });
    binding.refreshLayout.setOnLoadListener(new BaseFooterView.OnLoadListener() {
      @Override
      public void onLoad(BaseFooterView baseFooterView) {
        loadMsg(false, false);
      }
    });
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isVisibleToUser)
      // 标记已读
      ((MainActivity) getActivity()).updateNotifyItem(MainActivity.IDENTIFICATION_MSG, false);
  }

  /**
   * 获取公告
   * 
   * @param showProgress
   * @param isRefresh
   */
  private void loadMsg(boolean showProgress, final boolean isRefresh) {
    final QueryParam param = new QueryParam();
    if (isRefresh)
      param.setStart(0);
    else
      param.setStart(adapter.getData().size());

    new ListMsgCase(param)
        .execute(new HttpSubscriber<List<PublicNotice>>(showProgress ? getContext() : null) {
          @Override
          public void onFailure(String errorMsg, Response<List<PublicNotice>> response) {
            DialogUtil.getErrorDialog(getContext(), errorMsg).show();
          }

          @Override
          public void onSuccess(Response<List<PublicNotice>> response) {
            loadRecyclerView(response.getData(), isRefresh, response.isMore());

          }
        });
  }

  private void loadRecyclerView(List<PublicNotice> data, boolean isRefresh, boolean isMore) {
    if (isRefresh) {
      adapter.clear();
      binding.refreshLayout.stopRefresh();
    } else
      binding.refreshLayout.stopLoad();
    adapter.addAll(data);
    adapter.setPresenter(new Presenter());
    binding.refreshLayout.setHasFooter(isMore);
    if (isRefresh) {
      LinearLayoutManager manager = new LinearLayoutManager(getContext());
      manager.setOrientation(LinearLayoutManager.VERTICAL);
      binding.lst.setLayoutManager(manager);
      binding.lst.setAdapter(adapter);
    }
  }

  public class Presenter implements BaseViewAdapter.Presenter {
    public void onItemClick(PublicNotice notice) {
      Intent intent = new Intent(getContext(), NoticeDetailActivity.class);
      intent.putExtra(AppConfig.IntentKey.DATA, notice);
      startActivity(intent);
    }
  }

}
