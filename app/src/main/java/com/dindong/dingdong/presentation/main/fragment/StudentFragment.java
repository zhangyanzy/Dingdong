package com.dindong.dingdong.presentation.main.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.databinding.FragmentStudentBinding;

/**
 * Created by wcong on 2018/3/10.
 * 学员
 */

public class StudentFragment extends BaseFragment {
    private FragmentStudentBinding binding;

    @Override
    protected View initComponent(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student, container, false);
        return binding.getRoot();
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {

    }
}
