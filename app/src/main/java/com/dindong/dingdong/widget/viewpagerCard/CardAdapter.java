package com.dindong.dingdong.widget.viewpagerCard;

import java.util.List;

import com.dindong.dingdong.util.DensityUtil;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wcong on 2018/4/12.
 * <p>
 * </>
 */

public class CardAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
  public static int sWidthPadding;

  public static int sHeightPadding;

  private List<View> mViewList;

  private OnPageSelectListener listener;
  private ViewChangeListener viewChangeListener;

  private Context mContext;

  public CardAdapter(List<View> mImageViewList, Context context) {
    this.mViewList = mImageViewList;
    mContext = context;
    sWidthPadding = DensityUtil.dip2px(mContext, 16);
    sHeightPadding = DensityUtil.dip2px(mContext, 28);
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView(mViewList.get(position));
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    if (viewChangeListener != null)
      viewChangeListener.instantiateItem(position);
    if (mViewList.get(position) == null)
      return null;
    View view = mViewList.get(position);
    container.addView(view);

    return view;
  }

  @Override
  public int getCount() {
    return mViewList == null ? 0 : mViewList.size();
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
    if (mViewList.size() > 0 && position < mViewList.size()) {
      int outHeightPadding = (int) (positionOffset * sHeightPadding);
      int outWidthPadding = (int) (positionOffset * sWidthPadding);
      mViewList.get(position).setPadding(outWidthPadding, outHeightPadding, outWidthPadding,
          outHeightPadding);
      if (position < mViewList.size() - 1) {
        int inWidthPadding = (int) ((1 - positionOffset) * sWidthPadding);
        int inHeightPadding = (int) ((1 - positionOffset) * sHeightPadding);
        mViewList.get(position + 1).setPadding(inWidthPadding, inHeightPadding, inWidthPadding,
            inHeightPadding);
      }
    }

  }

  @Override
  public void onPageSelected(int position) {
    if (listener != null) {
      listener.select(position);
    }
  }

  @Override
  public void onPageScrollStateChanged(int state) {

  }

  public void setOnPageSelectListener(CardViewPager listener) {
    this.listener = listener;
  }

  public void setViewChangeListener(ViewChangeListener viewChangeListener) {
    this.viewChangeListener = viewChangeListener;
  }

  public interface OnPageSelectListener {
    void select(int position);
  }

  public interface ViewChangeListener {
    void instantiateItem(int position);
  }
}
