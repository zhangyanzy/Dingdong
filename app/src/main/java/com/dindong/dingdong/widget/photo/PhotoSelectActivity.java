package com.dindong.dingdong.widget.photo;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PhotoSelectActivity extends AppCompatActivity {
  private static PhotoListener photoListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    PictureSelector.create(this).openGallery(PictureMimeType.ofImage())
        .maxSelectNum(getIntent().getIntExtra(PhotoLayout.SELECT_COUNT, 1))
        .minSelectNum(1).imageSpanCount(4)
        .previewImage(false).isCamera(true)
        .isGif(true).forResult(PictureConfig.CHOOSE_REQUEST);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    finish();
    if (resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
      if (data != null) {
        ArrayList<String> photos = pathToString(PictureSelector.obtainMultipleResult(data));
        if (photoListener != null)
          photoListener.onChoose(photos);
      }
    }

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Glide.get(this).clearMemory();
  }

  private ArrayList<String> pathToString(List<LocalMedia> medias) {
    ArrayList<String> list = new ArrayList<>();
    for (LocalMedia media : medias) {
      list.add(media.getPath());
    }
    return list;
  }

  public static void setPhotoListener(PhotoListener photoListener) {
    PhotoSelectActivity.photoListener = photoListener;
  }

  public interface PhotoListener {
    void onChoose(ArrayList<String> photos);
  }
}
