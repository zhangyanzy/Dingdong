package com.dindong.dingdong.presentation.main.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseFragment;
import com.dindong.dingdong.databinding.FragmentWorkBinding;
import com.dindong.dingdong.util.ToastUtil;

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

    @Override
    protected void createEventHandlers() {
        super.createEventHandlers();
        binding.setPresenter(new Presenter());
    }

    public class Presenter {
        public void onTabSelect(View view) {
            String tabTxt = ((TextView) ((ViewGroup) view).getChildAt(1)).getText().toString();
            if (tabTxt.equals(getString(R.string.work_tab_course))) {
                //TODO 课程管理
                ToastUtil.toastHint(getContext(), tabTxt);
            } else if (tabTxt.equals(getString(R.string.work_tab_course))) {
                //TODO 学员管理
                ToastUtil.toastHint(getContext(), tabTxt);
            } else if (tabTxt.equals(getString(R.string.work_tab_teacher))) {
                //TODO 师资管理
                ToastUtil.toastHint(getContext(), tabTxt);
            } else if (tabTxt.equals(getString(R.string.work_tab_notice))) {
                //TODO 公告管理
                ToastUtil.toastHint(getContext(), tabTxt);
            } else if (tabTxt.equals(getString(R.string.work_tab_order_manager))) {
                //TODO 订单管理
                ToastUtil.toastHint(getContext(), tabTxt);
            } else if (tabTxt.equals(getString(R.string.work_tab_order_confirm))) {
                //TODO 订单确认
                ToastUtil.toastHint(getContext(), tabTxt);
            } else if (tabTxt.equals(getString(R.string.work_tab_order_cancel))) {
                //TODO 订单核销
                ToastUtil.toastHint(getContext(), tabTxt);
            }
        }
    }

}
