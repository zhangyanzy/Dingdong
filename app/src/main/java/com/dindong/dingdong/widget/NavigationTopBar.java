package com.dindong.dingdong.widget;

import com.dindong.dingdong.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    initLayout(context, null);
  }

  public NavigationTopBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    initLayout(context, attrs);
  }

  public NavigationTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initLayout(context, attrs);
  }

  private void initLayout(Context context, AttributeSet attrs) {
    mLayoutInflater = LayoutInflater.from(context);
    mRootView = mLayoutInflater.inflate(R.layout.layout_navigation_bar, this, true);
    mLeftImage = (ImageView) findViewById(R.id.img_left);
    mTitleText = (TextView) findViewById(R.id.center_title_top_bar);
    findViewById(R.id.layout_left).setOnClickListener(this);
    if (attrs != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
          R.styleable.NavigationTopBar);
      setCenterTitleText(typedArray.getString(R.styleable.NavigationTopBar_title_text));
    }
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

  public void setContent(ContentType contentType) {
    if (contentType.equals(ContentType.WHITE)) {
      mRootView.findViewById(R.id.root)
          .setBackgroundColor(getContext().getResources().getColor(R.color.white));
      mTitleText.setTextColor(Color.parseColor("#292C40"));
      mLeftImage
          .setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_back_blue));
    } else if (contentType.equals(ContentType.BLUE)) {
      mRootView.findViewById(R.id.root).setBackgroundDrawable(
          getContext().getResources().getDrawable(R.drawable.bg_gradient_blue));
      mTitleText.setTextColor(getContext().getResources().getColor(R.color.white));
      mLeftImage
          .setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_global_back_white));
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

  public void addRightView(View view) {
    ((LinearLayout) mRootView.findViewById(R.id.layout_right)).addView(view);
  }

  public enum ContentType {
    WHITE, BLUE;
  }
}
