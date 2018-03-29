package com.dindong.dingdong.adapter;

import com.dindong.dingdong.network.bean.store.Shop;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;

/**
 * Created by wangcong on 2018/3/29.
 * <p>
 */

public interface StorePresenter extends BaseViewAdapter.Presenter {
  void onStoreItemClick(Shop shop);
}
