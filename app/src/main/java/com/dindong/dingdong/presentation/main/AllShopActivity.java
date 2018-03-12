package com.dindong.dingdong.presentation.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.dindong.dingdong.R;
import com.dindong.dingdong.adapter.ShopListAdapter;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityAllShopBinding;
import com.dindong.dingdong.widget.NavigationTopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcong on 2018/3/10.
 * 全部门店
 */

public class AllShopActivity extends BaseActivity {

    private ActivityAllShopBinding binding;
    private NavigationTopBar mNavigationTopBar;
    private ShopListAdapter mShopListAdapter;
    private List<Object> mShopList;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_shop);
        initTopBar();
    }

    private void initTopBar() {
        mNavigationTopBar = (NavigationTopBar) findViewById(R.id.shop_list_top_bar);
        mNavigationTopBar.setCenterTitleText(R.string.all_shop);
    }

    private void initAdapter() {
        mShopListAdapter = new ShopListAdapter(this, mShopList);
        binding.shopList.setLayoutManager(new LinearLayoutManager(this));
        binding.shopList.setItemAnimator(new DefaultItemAnimator());
        binding.shopList.setAdapter(mShopListAdapter);
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        //数据加入
        mShopList = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            mShopList.add("" + i);
        }
        initAdapter();
    }

}
