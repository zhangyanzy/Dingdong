package com.dindong.dingdong.network.api.image;

import java.util.List;

import com.dindong.dingdong.network.PartName;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.entity.GlobalImage;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by wcong on 2018/5/5.
 * <p>
 * 图片上传api </>
 */

public interface ImageServiceApi {

  /**
   * 上传单张张图片
   * 
   * @param files
   * @return
   */
  @POST("image/upload")
  @Multipart
  Observable<Response<GlobalImage>> upload(@Part MultipartBody.Part files);

  /**
   * 上传多张图片
   * 
   * @param files
   * @return
   */
  @POST("image/upload/multi")
  @Multipart
  Observable<Response<List<GlobalImage>>> uploadMulti(@PartName("files") @Part MultipartBody.Part[] files);
}
