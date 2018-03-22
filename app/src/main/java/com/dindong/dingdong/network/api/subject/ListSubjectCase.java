package com.dindong.dingdong.network.api.subject;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import rx.Observable;

/**
 * Created by wcong on 2018/3/22.
 * <p>
 * 查询课程 </>
 */

public class ListSubjectCase extends BaseUseCase<SubjectServiceApi> {
  private QueryParam queryParam;

  public ListSubjectCase(QueryParam queryParam) {
    this.queryParam = queryParam;
  }

  @Override
  protected Observable buildCase() {
    return createConnection(true).listSubject(queryParam);
  }
}
