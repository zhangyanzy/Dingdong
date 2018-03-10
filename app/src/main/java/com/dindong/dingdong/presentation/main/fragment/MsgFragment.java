package com.dindong.dingdong.presentation.main.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.databinding.FragmentMsgBinding;
import com.dindong.dingdong.presentation.main.MainActivity;

/**
 * Created by wcong on 2018/3/10.
 * 消息
 */

public class MsgFragment extends BaseFragment {
    private FragmentMsgBinding binding;

    @Override
    protected View initComponent(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_msg, container, false);
        return binding.getRoot();
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            //标记已读
            ((MainActivity) getActivity()).updateNotifyItem(MainActivity.IDENTIFICATION_MSG, false);
    }

}
