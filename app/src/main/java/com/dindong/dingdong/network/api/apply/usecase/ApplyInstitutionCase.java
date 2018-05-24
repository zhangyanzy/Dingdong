package com.dindong.dingdong.network.api.apply.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.apply.ApplyServiceApi;
import com.dindong.dingdong.network.bean.apply.RequestInstitutionApply;

import rx.Observable;

/**
 * Created by wcong on 2018/5/25.
 * <p>
 * </>
 */

public class ApplyInstitutionCase extends BaseUseCase<ApplyServiceApi> {
  private RequestInstitutionApply requestInstitutionApply;

  public ApplyInstitutionCase(RequestInstitutionApply requestInstitutionApply) {
    this.requestInstitutionApply = requestInstitutionApply;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().applyInstitution(requestInstitutionApply);
  }
}
