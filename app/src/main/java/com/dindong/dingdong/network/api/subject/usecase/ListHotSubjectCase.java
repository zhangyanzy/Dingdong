package com.dindong.dingdong.network.api.subject.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.subject.SubjectServiceApi;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import rx.Observable;

/**
 * Created by wcong on 2018/3/18.
 * <p>
 * 查询热门推荐课程</>
 */

public class ListHotSubjectCase extends BaseUseCase<SubjectServiceApi> {
  private QueryParam queryParam;

  public ListHotSubjectCase(QueryParam queryParam) {
    this.queryParam = queryParam;
  }

  @Override
  protected Observable buildCase() {
    return createConnection(true).listHotSubject(queryParam);
  }
}
