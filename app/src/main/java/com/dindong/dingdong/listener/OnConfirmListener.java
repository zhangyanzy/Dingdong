package com.dindong.dingdong.listener;

/**
 * Created by wangcong on 2018/5/31.
 * <p>
 */

public interface OnConfirmListener<T, S> {
  void onConfirm(T o, S data);
}
