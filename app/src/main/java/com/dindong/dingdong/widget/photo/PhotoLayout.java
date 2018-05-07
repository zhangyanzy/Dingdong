package com.dindong.dingdong.widget.photo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dindong.dingdong.R;
import com.dindong.dingdong.databinding.ItemWidgetPhotoBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.image.usecase.UploadMultiImageCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.util.DensityUtil;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.PhotoUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by wcong on 2018/4/24.
 * <p>
 * </>
 */

public class PhotoLayout extends GridLayout {
  private List<GlobalImage> source;
  private List<ItemWidgetPhotoBinding> itemUploadPhotoBindings;
  private int maxLength = 2;
  private AddListener listener;
  private ResourceListener resourceListener;
  private boolean isLogo = false;
  private ItemParams itemParams = null;

  public final static String SELECT_COUNT = "selectCount";

  private int columnCount = 3;
  private int margin = 8;// unit dp

  private float ratio = 1f;// 宽高比

  public PhotoLayout(Context context) {
    super(context);
  }

  public PhotoLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public PhotoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void init() {
    init(null);
  }

  public void init(ItemParams itemParams) {
    this.itemParams = itemParams;
    setColumnCount(columnCount);
    source = new ArrayList<>();
    itemUploadPhotoBindings = new ArrayList<>();
    createView();
  }

  public void setMaxLength(int maxLength) {
    this.maxLength = maxLength;
  }

  public void setAddListener(AddListener listener) {
    this.listener = listener;
  }

  public void setResourceListener(ResourceListener resourceListener) {
    this.resourceListener = resourceListener;
  }

  public void setIsLogo(boolean logo) {
    isLogo = logo;
  }

  public void setMargin(int margin) {
    this.margin = margin;
  }

  public void setRatio(float ratio) {
    this.ratio = ratio;
  }

  @Override
  public void setColumnCount(int columnCount) {
    this.columnCount = columnCount;
  }

  public void setSource(List<GlobalImage> source) {
    if (source == null)
      return;
    removeAllViews();
    init(itemParams);
    for (GlobalImage image : source) {
      addItem(image, false);
    }
  }

  public void setSource(List<GlobalImage> source, boolean isPreview) {
    if (isPreview)
      setMaxLength(IsEmpty.list(source) ? 0 : source.size());
    setSource(source);
  }

  public void addItem(final GlobalImage image, boolean isShowDelete) {
    if (image == null)
      return;
    final ItemWidgetPhotoBinding binding = itemUploadPhotoBindings
        .get(itemUploadPhotoBindings.size() - 1);

    Glide.with(getContext()).load(image.getUrl())
        .apply(
            RequestOptions.placeholderOf(R.mipmap.img_placeholder).error(R.mipmap.img_load_failed))
        .listener(new RequestListener<Drawable>() {
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
        }).into(binding.imgPhoto);
    addImg(binding, image, isShowDelete);
  }

  private void addImg(final ItemWidgetPhotoBinding binding, GlobalImage image,
      boolean isShowDelete) {
    if (!isLogo) {
      if (itemParams != null) {
        binding.imgPhoto.getLayoutParams().height = itemParams.height;
        binding.imgPhoto.getLayoutParams().width = itemParams.width;
        binding.imgPhoto.setLayoutParams(binding.imgPhoto.getLayoutParams());
      }
    } else {
      post(new Runnable() {
        @Override
        public void run() {
          int width = (getMeasuredWidth()
              - DensityUtil.dip2px(getContext(), margin) * (columnCount - 1)) / columnCount;
          binding.imgPhoto.getLayoutParams().height = (int) (width * ratio);
          binding.imgPhoto.getLayoutParams().width = width;
          binding.imgPhoto.setLayoutParams(binding.imgPhoto.getLayoutParams());
        }
      });
    }
    // if (resource != null)
    // binding.imgPhoto.setImageBitmap(drawableToBitmap(resource));
    // binding.txtUpload.setText(null);
    binding.txtUpload.setVisibility(GONE);
    binding.setPresenter(new Presenter());
    binding.setHasImage(true);
    if (isShowDelete)
      binding.imgDel.setVisibility(VISIBLE);
    source.add(image);
    if (resourceListener != null)
      resourceListener.onReady(image);
    createView();
  }

  private void createView() {
    if (itemUploadPhotoBindings.size() == maxLength)
      return;

    final ItemWidgetPhotoBinding newBinding = DataBindingUtil
        .inflate(LayoutInflater.from(getContext()), R.layout.item_widget_photo, null, false);

    int i = getChildCount();
    GridLayout.Spec rowSpec = GridLayout.spec(i / columnCount);
    GridLayout.Spec columnSpec = GridLayout.spec(i % columnCount);
    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
    layoutParams.leftMargin = DensityUtil.dip2px(getContext(), i % columnCount == 0 ? 0 : margin);
    layoutParams.bottomMargin = DensityUtil.dip2px(getContext(), margin);
    newBinding.getRoot().setLayoutParams(layoutParams);
    if (itemParams != null) {
      newBinding.imgPhoto.getLayoutParams().height = itemParams.height;
      newBinding.imgPhoto.getLayoutParams().width = itemParams.width;
      newBinding.imgPhoto.setLayoutParams(newBinding.imgPhoto.getLayoutParams());
    } else {
      post(new Runnable() {
        @Override
        public void run() {
          int width = (getMeasuredWidth()
              - DensityUtil.dip2px(getContext(), margin) * (columnCount - 1)) / columnCount;
          newBinding.imgPhoto.getLayoutParams().height = (int) (width * ratio);
          newBinding.imgPhoto.getLayoutParams().width = width;
          newBinding.imgPhoto.setLayoutParams(newBinding.imgPhoto.getLayoutParams());
        }
      });
    }
    newBinding.setPresenter(new Presenter());
    newBinding.setHasImage(false);
    newBinding.setIndex(itemUploadPhotoBindings.size());
    itemUploadPhotoBindings.add(newBinding);
    addView(newBinding.getRoot());
  }

  private void startPhotoSelect() {
    Intent intent = new Intent(getContext(), PhotoSelectActivity.class);
    int count = getSource().size();
    // if (count <= maxLength) {
    // intent.putExtra(SELECT_COUNT, maxLength - count);
    // } else {
    intent.putExtra(SELECT_COUNT, 1);
    // }
    getContext().startActivity(intent);
    PhotoSelectActivity.setPhotoListener(new PhotoSelectActivity.PhotoListener() {
      @Override
      public void onChoose(final ArrayList<String> photos) {
        uploadPhoto(photos);
      }
    });
  }

  private void uploadPhoto(ArrayList<String> photos) {
    if (IsEmpty.list(photos))
      return;

    // MultipartBody.Part[] parts = new MultipartBody.Part[photos.size()];
    File file = new File(photos.get(0));
    RequestBody requestFile;
    requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(),
        requestFile);
    // parts[i] = imagePart;
    MultipartBody.Part[] parts = new MultipartBody.Part[] {
        imagePart };

    // MultipartBody.Builder builder = new MultipartBody.Builder()
    // .setType(MultipartBody.FORM)//表单类型
    // .addFormDataPart(ParamKey.TOKEN, token);//ParamKey.TOKEN 自定义参数key常量类，即参数名
    // RequestBody imageBody =
    // RequestBody.create(MediaType.parse("multipart/form-data"), file);
    // builder.addFormDataPart("imgfile", file.getName(), imageBody);//imgfile
    // 后台接收图片流的参数名
    //
    // List<MultipartBody.Part> parts = builder.build().parts();
    // for (int i = 0; i < photos.size(); i++) {
    // // 上传图片
    // File file = new File(photos.get(i));
    // RequestBody requestFile;
    // requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),
    // file);
    // MultipartBody.Part imagePart = MultipartBody.Part.createFormData("files",
    // file.getName(),
    // requestFile);
    //// parts[i] = imagePart;
    // parts=new MultipartBody.Part[]{imagePart};
    // }
    new UploadMultiImageCase(parts).execute(new HttpSubscriber<GlobalImage>(getContext()) {
      @Override
      public void onFailure(String errorMsg, Response<GlobalImage> response) {
        DialogUtil.getErrorDialog(getContext(), errorMsg).show();
      }

      @Override
      public void onSuccess(Response<GlobalImage> response) {

      }
    });

  }

  private Bitmap drawableToBitmap(Drawable drawable) {
    int w = drawable.getIntrinsicWidth();
    int h = drawable.getIntrinsicHeight();
    Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
        : Bitmap.Config.RGB_565;
    Bitmap bitmap = Bitmap.createBitmap(w, h, config);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, w, h);
    drawable.draw(canvas);
    return bitmap;
  }

  public List<GlobalImage> getSource() {
    return source;
  }

  public void setDeleteVisible(boolean isVisible) {
    for (int i = 0; i < getChildCount(); i++) {
      if (getChildAt(i).findViewById(R.id.txt_upload).getVisibility() == View.GONE) {
        getChildAt(i).findViewById(R.id.img_del).setVisibility(isVisible ? View.VISIBLE : GONE);
      }
    }
  }

  public class Presenter {
    public void onAdd(boolean hasPhoto, int index) {
      if (hasPhoto) {
        // 有图片则显示大图
        PhotoUtil.preview((Activity) getContext(), index, getSource());
        return;
      }
      startPhotoSelect();
      if (listener != null)
        listener.onAdd(new UploadListener() {
          @Override
          public void upload(GlobalImage image, boolean isShowDelete) {
            addItem(image, isShowDelete);
          }
        });
    }

    public void onRemove(View view, int index) {
      removeView(itemUploadPhotoBindings.get(index).getRoot());
      source.remove(index);
      itemUploadPhotoBindings.remove(index);
      boolean hasAdd = false;
      for (ItemWidgetPhotoBinding binding : itemUploadPhotoBindings) {
        boolean hasImg = binding.getHasImage();
        if (!hasImg) {
          hasAdd = true;
          break;
        }
      }
      if (!hasAdd)
        createView();
      for (int i = index; i < itemUploadPhotoBindings.size(); i++) {
        itemUploadPhotoBindings.get(i).setIndex(i);
      }
      if (resourceListener != null)
        resourceListener.onRemove();
    }
  }

  public interface AddListener {
    void onAdd(UploadListener uploadListener);
  }

  public interface UploadListener {
    void upload(GlobalImage image, boolean isShowDelete);
  }

  public interface ResourceListener {
    void onReady(GlobalImage image);

    void onRemove();
  }

  public static class ItemParams {
    int width;
    int height;

    public ItemParams(int width, int height) {
      this.width = width;
      this.height = height;
    }
  }
}
