package com.dindong.dingdong.network.api.apply.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.apply.ApplyServiceApi;
import com.dindong.dingdong.network.bean.apply.RequestTeacherApply;

import rx.Observable;

/**
 * Created by wcong on 2018/5/25.
 * <p>
 * </>
 */

public class ApplyTeacherCase extends BaseUseCase<ApplyServiceApi> {
  private RequestTeacherApply requestTeacherApply;

  public ApplyTeacherCase(RequestTeacherApply requestTeacherApply) {
    this.requestTeacherApply = requestTeacherApply;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().applyTeacher(requestTeacherApply);
  }
}
