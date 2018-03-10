package com.dindong.dingdong.presentation.main.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.databinding.FragmentWorkBinding;

/**
 * Created by wcong on 2018/3/10.
 * 工作
 */

public class WorkFragment extends BaseFragment {
    private FragmentWorkBinding binding;

    @Override
    protected View initComponent(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work, container, false);
        return binding.getRoot();
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {

    }
}
