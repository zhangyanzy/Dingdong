package com.dindong.dingdong.util;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dindong.dingdong.R;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by wcong on 2018/3/18.
 */

public class GlideUtil {

  /**
   * 加载图片
   * 
   * @param context
   * @param imgPath
   * @param imageView
   */
  public static void load(Context context, Object imgPath, ImageView imageView) {
    Glide
        .with(context).load(imageView).apply(new RequestOptions()
            .placeholder(R.mipmap.img_placeholder).error(R.mipmap.img_load_failed))
        .load(imgPath).into(imageView);
  }
}
