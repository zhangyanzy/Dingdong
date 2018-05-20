package com.dindong.dingdong.network.api.member.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.member.MemeberServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * </>
 */

public class ModifyDescriptionCase extends BaseUseCase<MemeberServiceApi> {
  private String description;

  public ModifyDescriptionCase(String description) {
    this.description = description;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().modifyDescription(description);
  }
}
