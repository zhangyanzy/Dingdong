package com.dindong.dingdong.presentation.main;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityShopInfoBinding;
import com.dindong.dingdong.widget.NavigationTopBar;

public class ShopInfoActivity extends BaseActivity {

    private NavigationTopBar mNavigationTopBar;
    private ActivityShopInfoBinding binding;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_info);
        binding.shopInfoTopBar.setCenterTitleText("门店详情");

    }

    @Override
    protected void loadData(Bundle savedInstanceState) {

    }
}
