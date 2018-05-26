package com.dindong.dingdong.network.api.member.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.member.MemberServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * </>
 */

public class ModifyNameCase extends BaseUseCase<MemberServiceApi> {
  private String name;

  public ModifyNameCase(String name) {
    this.name = name;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().modifyName(name);
  }
}
