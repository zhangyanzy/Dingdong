package com.dindong.dingdong.network.api.subject.usecase;

import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.subject.SubjectServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/21.
 * <p>
 * </>
 */

public class GetSubjectCase extends BaseUseCase<SubjectServiceApi> {
  private String id;

  public GetSubjectCase(String id) {
    this.id = id;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().getSubject(id, SessionMgr.getCurrentAdd().getLongitude(),
        SessionMgr.getCurrentAdd().getLatitude());
  }
}
