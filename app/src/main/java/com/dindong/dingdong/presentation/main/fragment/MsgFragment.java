package com.dindong.dingdong.presentation.main.fragment;

import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.databinding.FragmentMsgBinding;
import com.dindong.dingdong.presentation.main.MainActivity;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;

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

  SingleTypeAdapter adapter;

  @Override
  protected View initComponent(LayoutInflater inflater, ViewGroup container) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg, container, false);

    List<String> list = new ArrayList<>();
    list.add("1");
    list.add("2");
    adapter = new SingleTypeAdapter(getContext(), R.layout.item_msg);
    adapter.set(list);
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    manager.setOrientation(LinearLayoutManager.VERTICAL);
    binding.lst.setLayoutManager(manager);
    binding.lst.setAdapter(adapter);

    return binding.getRoot();
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {

  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isVisibleToUser)
      // 标记已读
      ((MainActivity) getActivity()).updateNotifyItem(MainActivity.IDENTIFICATION_MSG, false);
  }

}
