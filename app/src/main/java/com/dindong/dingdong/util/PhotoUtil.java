package com.dindong.dingdong.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dindong.dingdong.R;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by wcong on 2018/3/18. 图片工具类
 */

public class PhotoUtil {

  /**
   * 加载imageView图片
   * 
   * @param context
   * @param imgPath
   * @param imageView
   */
  public static void load(Context context, Object imgPath, final ImageView imageView) {
    Glide.with(context).load(imageView)
        .apply(new RequestOptions().placeholder(R.mipmap.img_placeholder)
            .error(R.mipmap.img_load_failed))
        .load(imgPath).listener(new RequestListener<Drawable>() {
          @Override
          public boolean onLoadFailed(@Nullable GlideException e, Object model,
              Target<Drawable> target, boolean isFirstResource) {
            return false;
          }

          @Override
          public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
              DataSource dataSource, boolean isFirstResource) {
            return false;
          }
        }).into(imageView);
  }

  /**
   * 预览单张张大图
   * 
   * @param activity
   * @param url
   */
  public static void preview(Activity activity, String url) {
    List<GlobalImage> images = new ArrayList<>();
    GlobalImage image = new GlobalImage();
    image.setUrl(url);
    PictureSelector.create(activity).externalPicturePreview(0, path2Media(images));
    return;
  }

  /**
   * 预览多张大图
   * 
   * @param activity
   * @param position
   * @param images
   */
  public static void preview(Activity activity, int position, List<GlobalImage> images) {
    Glide.get(activity).clearMemory();
    System.gc();
    PictureSelector.create(activity).externalPicturePreview(position, path2Media(images));
  }

  private static List<LocalMedia> path2Media(List<GlobalImage> paths) {
    List<LocalMedia> localMedias = new ArrayList<>();
    for (GlobalImage image : paths) {
      LocalMedia media = new LocalMedia();
      media.setPath(image.getUrl());
      localMedias.add(media);
    }
    return localMedias;
  }

}
