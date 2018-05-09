package com.dindong.dingdong.network.api.image.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.image.ImageServiceApi;

import okhttp3.MultipartBody;
import rx.Observable;

/**
 * Created by wcong on 2018/5/5.
 * <p>
 * </>
 */

public class UploadImageCase extends BaseUseCase<ImageServiceApi> {
  private MultipartBody.Part file;

  public UploadImageCase(MultipartBody.Part files) {
    this.file = files;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().upload(file);
  }
}
