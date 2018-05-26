package com.dindong.dingdong.network.api.member.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.member.MemberServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * </>
 */

public class ModifySexCase extends BaseUseCase<MemberServiceApi> {
  private String sex;

  public ModifySexCase(String sex) {
    this.sex = sex;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().modifySex(sex);
  }
}
