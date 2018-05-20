package com.dindong.dingdong.network.api.groupbuy.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.groupbuy.GroupBuyServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * </>
 */

public class ListGroupBuyCase extends BaseUseCase<GroupBuyServiceApi> {
  private String itemId;

  public ListGroupBuyCase(String itemId) {
    this.itemId = itemId;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().listGroupBuy(itemId);
  }
}
