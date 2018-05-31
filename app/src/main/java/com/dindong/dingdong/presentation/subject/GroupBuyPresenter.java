package com.dindong.dingdong.presentation.subject;

import com.dindong.dingdong.network.bean.groupbuy.GroupBuy;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;

/**
 * Created by wangcong on 2018/5/31.
 * <p>
 */

public interface GroupBuyPresenter extends BaseViewAdapter.Presenter {
  void onGroupBuyClick(GroupBuy groupBuy);
}
