package com.dindong.dingdong.widget.baseadapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by wcong on 2018/3/18.
 */

public class BindingViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

  protected final T mBinding;

  public BindingViewHolder(T binding) {
    super(binding.getRoot());
    mBinding = binding;
  }

  public T getBinding() {
    return mBinding;
  }
}