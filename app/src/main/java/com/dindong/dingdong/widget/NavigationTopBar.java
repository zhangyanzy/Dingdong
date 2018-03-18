package com.dindong.dingdong.widget;

import com.dindong.dingdong.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by wcong on 2018/3/11. 全局标题栏
 */

public class NavigationTopBar extends RelativeLayout implements View.OnClickListener {

  private LayoutInflater mLayoutInflater;
  private View mRootView;
  private ImageView mLeftImage;
  private TextView mTitleText;

  private NavigationTopBarClickListener mNavigationTopBarClickListener;

  public NavigationTopBar(Context context) {
    super(context);
    initLayout(context);
  }

  public NavigationTopBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    initLayout(context);
  }

  public NavigationTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initLayout(context);
  }

  private void initLayout(Context context) {
    mLayoutInflater = LayoutInflater.from(context);
    mRootView = mLayoutInflater.inflate(R.layout.layout_navigation_bar, this, true);
    mLeftImage = (ImageView) findViewById(R.id.img_left);
    mTitleText = (TextView) findViewById(R.id.center_title_top_bar);
    findViewById(R.id.layout_left).setOnClickListener(this);

  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
    case R.id.layout_left:
      if (mNavigationTopBarClickListener != null)
        mNavigationTopBarClickListener.leftImageClick();
      break;
    default:
      break;

    }
  }

  public void setNavigationTopBarClickListener(
      @NonNull NavigationTopBarClickListener navigationTopBarClickListener) {
    mNavigationTopBarClickListener = navigationTopBarClickListener;
  }

  public void setLeftImageResource(int resId) {
    mLeftImage.setImageResource(resId);
  }

  public void setLeftImageDrawable(Drawable drawable) {
    mLeftImage.setImageDrawable(drawable);
  }

  public void setLeftImageBitmap(Bitmap bitmap) {
    mLeftImage.setImageBitmap(bitmap);
  }

  public void setLeftImageVisiable(int visiable) {
    mLeftImage.setVisibility(visiable);
  }

  public void setCenterTitleText(String title) {
    mTitleText.setText(title);
  }

  public void setCenterTitleText(int resId) {
    mTitleText.setText(resId);
  }

  public void setCenterTitleText(CharSequence charSequence) {
    mTitleText.setText(charSequence);
  }

  public void setCenterTitleVisiable(int visiable) {
    mTitleText.setVisibility(visiable);
  }

  public interface NavigationTopBarClickListener {
    public void leftImageClick();
  }
}
