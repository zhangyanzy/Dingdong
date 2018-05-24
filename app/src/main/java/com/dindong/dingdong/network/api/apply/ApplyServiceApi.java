package com.dindong.dingdong.network.api.apply;

import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.apply.RequestApply;
import com.dindong.dingdong.network.bean.apply.RequestInstitutionApply;
import com.dindong.dingdong.network.bean.apply.RequestTeacherApply;
import com.dindong.dingdong.network.bean.apply.ResponseApply;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wcong on 2018/5/25.
 * <p>
 * 合作申请API </>
 */

public interface ApplyServiceApi {
  /**
   * 申请成为代理
   * 
   * @param requestApply
   * @return
   */
  @POST("apply/proxy")
  Observable<Response<ResponseApply>> applyProxy(@Body RequestApply requestApply);

  /**
   * 申请机构入驻
   * 
   * @param requestInstitutionApply
   * @return
   */
  @POST("apply/institution")
  Observable<Response<ResponseApply>> applyInstitution(
      @Body RequestInstitutionApply requestInstitutionApply);

  /**
   * 申请成为平台老师
   *
   * @param requestTeacherApply
   * @return
   */
  @POST("apply/teacher")
  Observable<Response<ResponseApply>> applyTeacher(@Body RequestTeacherApply requestTeacherApply);
}
