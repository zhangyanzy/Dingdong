package com.dindong.dingdong.presentation.main.fragment;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.databinding.FragmentMsgTabBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.notice.usecase.ListShopMsgCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.FilterParam;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.network.bean.message.Message;
import com.dindong.dingdong.network.bean.message.MessageType;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseFooterView;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseHeaderView;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wcong on 2018/4/22.
 * <p>
 * </>
 */

@SuppressLint("ValidFragment")
public class MsgTabFragment extends BaseFragment {
  FragmentMsgTabBinding binding;

  private SingleTypeAdapter adapter;

  private int type;// 0-系统消息，1-公告

  public MsgTabFragment(int type) {
    this.type = type;
  }

  @Override
  protected View initComponent(LayoutInflater inflater, ViewGroup container) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg_tab, container, false);

    adapter = new SingleTypeAdapter(getContext(), R.layout.item_msg);
    return binding.getRoot();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {

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
  protected void firstVisible() {
    loadMsg(true, true);
  }

  private void loadMsg(boolean showProgress, final boolean isRefresh) {
    final QueryParam param = new QueryParam();
    if (isRefresh)
      param.setStart(0);
    else
      param.setStart(adapter.getData().size());
    param.getFilters()
        .add(new FilterParam("type", type == 0 ? MessageType.system : MessageType.notice));

    new ListShopMsgCase(param)
        .execute(new HttpSubscriber<List<Message>>(showProgress ? getContext() : null) {
          @Override
          public void onFailure(String errorMsg, Response<List<Message>> response) {
            DialogUtil.getErrorDialog(getContext(), errorMsg).show();
          }

          @Override
          public void onSuccess(Response<List<Message>> response) {
            loadRecyclerView(response.getData(), isRefresh, response.isMore());

          }
        });
  }

  private void loadRecyclerView(List<Message> data, boolean isRefresh, boolean isMore) {
    if (isRefresh) {
      adapter.clear();
      binding.refreshLayout.stopRefresh();
    } else
      binding.refreshLayout.stopLoad();
    adapter.addAll(data);
    binding.refreshLayout.setHasFooter(isMore);
    if (isRefresh) {
      LinearLayoutManager manager = new LinearLayoutManager(getContext());
      manager.setOrientation(LinearLayoutManager.VERTICAL);
      binding.lst.setLayoutManager(manager);
      binding.lst.setAdapter(adapter);
    }
  }

}
