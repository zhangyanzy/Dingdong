package com.dindong.dingdong.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dindong.dingdong.util.DensityUtil;

/**
 * Created by wcong on 2018/5/9.
 * <p>
 * </>
 */

public class CustomViewPager extends ViewPager {

  private List<Integer> itemHeights;

  private NestedScrollView scrollView;

  public CustomViewPager(Context context) {
    super(context);
    addOnPageChangeListener(onPageChangeListener);
  }

  public CustomViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    addOnPageChangeListener(onPageChangeListener);
  }

  public void attachToScroll(NestedScrollView scrollView) {
    this.scrollView = scrollView;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    int height = 0;
    itemHeights = new ArrayList<>();
    for (int i = 0; i < getChildCount(); i++) {
      View child = getChildAt(i);
      child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
      int h = child.getMeasuredHeight();
      itemHeights.add(i==0?h+ DensityUtil.dip2px(getContext(),50):h);
      height = h;
    }

    // heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  public void updateHeight(final int position) {
    if (itemHeights == null || itemHeights.size() - 1 < position)
      return;
    post(new Runnable() {
      @Override
      public void run() {
        getLayoutParams().height = itemHeights.get(position);
        requestLayout();
        post(new Runnable() {
          @Override
          public void run() {
            if (scrollView != null)
              scrollView.scrollTo(0, 0);
          }
        });
      }
    });
  }

  private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(final int position) {
      updateHeight(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  };

  @Override
  public boolean onTouchEvent(MotionEvent arg0) {
    return false;
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent arg0) {
    return false;
  }

}
