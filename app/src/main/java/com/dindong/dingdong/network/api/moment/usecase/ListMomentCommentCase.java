package com.dindong.dingdong.network.api.moment.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.moment.MomentServiceApi;
import com.dindong.dingdong.network.bean.entity.FilterParam;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import rx.Observable;

/**
 * Created by wcong on 2018/5/6.
 * <p>
 * </>
 */

public class ListMomentCommentCase extends BaseUseCase<MomentServiceApi> {

  private QueryParam queryParam;

  public ListMomentCommentCase(String relationId) {
    queryParam = new QueryParam();
    queryParam.setLimit(99);
    queryParam.getFilters().add(new FilterParam("relationId", relationId));
  }

  @Override
  protected Observable buildCase() {
    return createConnection().listComment(queryParam);
  }
}
