package com.dindong.dingdong.util;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dindong.dingdong.R;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

/**
 * Created by wcong on 2018/3/15.
 *
 * 用于xml绑定数据
 */

public class AppBindingAdapter {

  @BindingAdapter("imageResourceUrl")
  public static void loadImage(ImageView view, Object resourceUrl) {
    loadImage(view, resourceUrl, false);
  }

  @BindingAdapter("imageUserResourceUrl")
  public static void loadUserImage(ImageView view, Object resourceUrl) {
    loadImage(view, resourceUrl, true);
  }

  private static void loadImage(ImageView view, Object resourceUrl, boolean isUser) {
    RequestOptions options = new RequestOptions();
    options.placeholder(isUser ? R.mipmap.img_jigou : R.mipmap.img_placeholder);
    options.error(isUser ? R.mipmap.img_jigou : R.mipmap.img_load_failed);

    if (IsEmpty.object(resourceUrl) | IsEmpty.string(String.valueOf(resourceUrl))) {
      Glide.with(view.getContext()).load(isUser ? R.mipmap.img_jigou : R.mipmap.img_placeholder)
          .apply(options).into(view);
    } else if (resourceUrl instanceof String) {
      Glide.with(view.getContext()).load(resourceUrl).apply(options).into(view);
    } else if (resourceUrl instanceof Integer) {
      Glide.with(view.getContext()).load((Integer) resourceUrl).apply(options).into(view);
    } else {
      Glide.with(view.getContext()).load(R.mipmap.img_placeholder).apply(options).into(view);
    }
  }
}
