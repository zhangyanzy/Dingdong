package com.dindong.dingdong.base;

import com.trello.rxlifecycle.components.support.RxFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wcong on 2018/3/10.
 */

public abstract class BaseFragment extends RxFragment {

  protected View mRootView;

  protected boolean isFirstVisible = true;
  protected boolean hasCreate = false;//

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    if (mRootView != null) {
      ViewGroup parent = (ViewGroup) mRootView.getParent();
      if (parent != null) {
        parent.removeView(mRootView);
      }
    } else {
      mRootView = initComponent(inflater, container);
      createEventHandlers();
      loadData(savedInstanceState);
    }
    hasCreate = true;
    if (isFirstVisible && getUserVisibleHint() && hasCreate) {
      isFirstVisible = false;
      firstVisible();
    }

    return mRootView;
  }

  protected abstract View initComponent(LayoutInflater inflater, ViewGroup container);

  protected abstract void loadData(Bundle savedInstanceState);

  protected void createEventHandlers() {
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isVisibleToUser) {
      if (isFirstVisible && hasCreate) {
        isFirstVisible = false;
        firstVisible();
      }
    }
  }

  protected void firstVisible() {
  }
}
