package com.dindong.dingdong.adapter;

import com.dindong.dingdong.R;
import com.dindong.dingdong.databinding.ItemShopListBinding;
import com.dindong.dingdong.databinding.ItemShopListSubjectBinding;
import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.widget.baseadapter.BaseViewAdapter;
import com.dindong.dingdong.widget.baseadapter.BindingViewHolder;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by wangcong on 2018/3/29.
 * <p>
 */

public class StoreAdapter extends SingleTypeAdapter {
  public StoreAdapter(Context context) {
    super(context, R.layout.item_shop_list);
    setDecorator(new Decorator());
  }

  public void setPresenter(StorePresenter subjectPresenter) {
    super.setPresenter(subjectPresenter);
  }

  class Decorator implements BaseViewAdapter.Decorator {

    @Override
    public void decorator(BindingViewHolder holder, int position, int viewType) {
      if (holder == null || holder.getBinding() == null)
        return;
      ItemShopListBinding itemShopListBinding = (ItemShopListBinding) holder.getBinding();
      itemShopListBinding.layoutSubject.removeAllViews();
      itemShopListBinding.layoutSubject.setVisibility(View.GONE);
      itemShopListBinding.divider.setVisibility(View.GONE);
      for (Subject subject : itemShopListBinding.getItem().getSubjects()) {
        ItemShopListSubjectBinding itemShopListSubjectBinding = DataBindingUtil
            .inflate(LayoutInflater.from(context), R.layout.item_shop_list_subject, null, false);
        itemShopListSubjectBinding.setItem(subject);
        itemShopListBinding.layoutSubject.addView(itemShopListSubjectBinding.getRoot());
        itemShopListBinding.divider.setVisibility(View.VISIBLE);
        itemShopListBinding.layoutSubject.setVisibility(View.VISIBLE);
      }

    }
  }

}
