package com.dindong.dingdong.presentation.main.fragment;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityAllShopBinding;

/**
 * Created by wcong on 2018/3/10.
 * 全部门店
 */

public class AllShopActivity extends BaseActivity {

    private ActivityAllShopBinding binding;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_all_shop);
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {

    }
}
