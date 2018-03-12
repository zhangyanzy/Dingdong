package com.dindong.dingdong.widget.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.dindong.dingdong.widget.pullrefresh.layout.BaseFooterView;
import com.dindong.dingdong.widget.pullrefresh.support.type.LayoutType;

/**
 * Created by NeilSpears on 2016/10/19.
 */

public class DefaultFooterView extends BaseFooterView {

  private boolean loading;
  private int bottomPadding;

  public DefaultFooterView(Context context) {
    super(context);
  }

  public DefaultFooterView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public DefaultFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public float getSpanHeight() {
    return bottomPadding + (getHeight() == 0 ? 60 : getHeight());
  }

  @Override
  protected void onStateChange(int state) {
    loading = state == BaseFooterView.LOADING;
  }

  public boolean isLoading() {
    return loading;
  }

  @Override
  public int getLayoutType() {
    return LayoutType.LAYOUT_NORMAL;
  }

  public void setBottomPadding(int bottomPadding) {
    this.bottomPadding = bottomPadding;
  }
}
