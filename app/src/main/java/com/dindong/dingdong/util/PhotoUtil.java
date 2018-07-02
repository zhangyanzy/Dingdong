package com.dindong.dingdong.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dindong.dingdong.DDApp;
import com.dindong.dingdong.R;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
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

  /**
   * 图片按比例大小压缩方法
   *
   * @param srcPath
   *          （根据路径获取图片并压缩）
   * @return
   */
  public static File getCompressFile(String srcPath) throws IOException {
    BitmapFactory.Options newOpts = new BitmapFactory.Options();
    // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
    newOpts.inJustDecodeBounds = true;
    Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

    newOpts.inJustDecodeBounds = false;
    int w = newOpts.outWidth;
    int h = newOpts.outHeight;

    float hh = 800f;
    float ww = 480f;

    // float hh = 640f;
    // float ww = 480f;
    // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
    int be = 1;// be=1表示不缩放
    if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
      be = (int) (newOpts.outWidth / ww);
    } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
      be = (int) (newOpts.outHeight / hh);
    }
    if (be <= 0)
      be = 1;

    newOpts.inSampleSize = be;// 设置缩放比例

    // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
    Bitmap tagBitmap = BitmapFactory.decodeFile(srcPath, newOpts);
    ByteArrayOutputStream stream = new ByteArrayOutputStream();

    int orientation = readPictureDegree(srcPath);
    if (Math.abs(orientation) > 0) {
      tagBitmap = rotatingImageView(orientation, bitmap);
    }
    tagBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    tagBitmap.recycle();

    File file = getImageCacheFile(DDApp.getInstance());

    FileOutputStream fos = new FileOutputStream(file);
    fos.write(stream.toByteArray());
    fos.flush();
    fos.close();
    stream.flush();
    stream.close();
    return file;
  }

  private static int readPictureDegree(String path) {
    int degree = 0;
    try {
      ExifInterface exifInterface = new ExifInterface(path);
      int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
          ExifInterface.ORIENTATION_NORMAL);
      switch (orientation) {
      case ExifInterface.ORIENTATION_ROTATE_90:
        degree = 90;
        break;
      case ExifInterface.ORIENTATION_ROTATE_180:
        degree = 180;
        break;
      case ExifInterface.ORIENTATION_ROTATE_270:
        degree = 270;
        break;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return degree;
  }

  private static Bitmap rotatingImageView(int angle, Bitmap bitmap) {
    if (bitmap == null)
      return null;
    Matrix matrix = new Matrix();
    matrix.postRotate(angle);
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
  }

  private static File getImageCacheFile(Context context) {
    if (getImageCacheDir(context) != null) {
      return new File(getImageCacheDir(context) + "/" + System.currentTimeMillis()
          + (int) (Math.random() * 1000) + ".jpg");
    }
    return null;
  }

  private static final String DEFAULT_DISK_CACHE_DIR = "upload_disk_cache";

  private static File getImageCacheDir(Context context) {
    return getImageCacheDir(context, DEFAULT_DISK_CACHE_DIR);
  }

  private static File getImageCacheDir(Context context, String cacheName) {
    File cacheDir = context.getExternalCacheDir();
    if (cacheDir != null) {
      File result = new File(cacheDir, cacheName);
      if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
        return null;
      }
      return result;
    }

    return null;
  }
}
