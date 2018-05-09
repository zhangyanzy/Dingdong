package com.dindong.dingdong.network.api.shop.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.shop.ShopServiceApi;
import com.dindong.dingdong.network.bean.entity.QueryParam;

import rx.Observable;

/**
 * Created by wcong on 2018/4/12.
 * <p>
 * </>
 */

public class ListTeacherCase extends BaseUseCase<ShopServiceApi> {
  private String shopId;
  private QueryParam queryParam;

  public ListTeacherCase(String shopId, QueryParam queryParam) {
    this.shopId = shopId;
    this.queryParam = queryParam;
  }

  @Override
  protected Observable buildCase() {
    return createConnection(true).listTeacher(shopId, queryParam);
  }
}
