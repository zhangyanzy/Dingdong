package com.dindong.dingdong.widget.viewpagerCard;

import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.util.DensityUtil;
import com.dindong.dingdong.util.IsEmpty;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by wcong on 2018/4/12.
 * <p>
 * </>
 */

public class CardViewPager extends RelativeLayout implements CardAdapter.OnPageSelectListener {
  private CardAdapter mAdapter;

  private ViewPager mViewPager;

  private List<View> mViewList = new ArrayList<>();

  private CardAdapter.OnPageSelectListener listener;
  private CardAdapter.ViewChangeListener viewChangeListener;

  public CardViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    inflate(context, R.layout.layout_card_view_pager, this);
    mViewPager = (ViewPager) findViewById(R.id.vp);
    invalidateLayout();
    init();
  }

  public void init() {
    mAdapter = new CardAdapter(mViewList, getContext());
    mAdapter.setOnPageSelectListener(this);
    mAdapter.setViewChangeListener(viewChangeListener);
    mViewPager.setAdapter(mAdapter);
    mViewPager.addOnPageChangeListener(mAdapter);
    mViewPager.setOffscreenPageLimit(0);
    mViewList.clear();
    mAdapter.notifyDataSetChanged();

    setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        return mViewPager.dispatchTouchEvent(event);
      }
    });

  }

  public void setViewList(List<View> lists) {
    if (lists == null) {
      return;
    }
    mViewList.clear();
    for (View view : lists) {
      FrameLayout layout = new FrameLayout(getContext());
      layout.setPadding(CardAdapter.sWidthPadding, CardAdapter.sHeightPadding,
          CardAdapter.sWidthPadding, CardAdapter.sHeightPadding);
      layout.addView(view);
      mViewList.add(layout);
    }
    mAdapter.notifyDataSetChanged();
  }

  public void setOnPageSelectListener(CardAdapter.OnPageSelectListener listener) {
    this.listener = listener;
  }

  public void setViewChangeListener(CardAdapter.ViewChangeListener viewChangeListener) {
    mAdapter.setViewChangeListener(viewChangeListener);
  }

  public List<View> getmViewList() {
    return mViewList;
  }

  @Override
  public void select(int position) {
    if (listener != null) {
      listener.select(position);
    }
  }

  public void setCurrentItem(int index) {
    if (!IsEmpty.object(mViewPager))
      mViewPager.setCurrentItem(index);
  }

  public int getCurrentItem() {
    if (!IsEmpty.object(mViewPager))
      return mViewPager.getCurrentItem();
    return 0;
  }

  public void invalidateLayout() {
    int marginWidth = DensityUtil.dip2px(getContext(), 50);
    int displayWidth = getContext().getResources().getDisplayMetrics().widthPixels;
    int height = DensityUtil.dip2px(getContext(), 400);
    LayoutParams layoutParams = new LayoutParams(displayWidth - marginWidth,
        ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
    mViewPager.setLayoutParams(layoutParams);
  }

  public ViewPager getmViewPager() {
    return mViewPager;
  }

  public CardAdapter getAdapter() {
    return mAdapter;
  }
}
