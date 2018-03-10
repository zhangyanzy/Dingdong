package com.dindong.dingdong.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by wcong on 2018/3/10.
 */

public abstract class BaseFragment extends RxFragment {


    protected View mRootView;

    private boolean isFirstCreate = true;

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
            if (isFirstCreate) {
                isFirstCreate = false;
                firstCreate();
            }
        }
    }

    protected void firstCreate() {
    }
}
