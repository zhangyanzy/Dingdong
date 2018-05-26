package com.dindong.dingdong.network.api.member.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.member.MemberServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * </>
 */

public class ModifyBirthdayCase extends BaseUseCase<MemberServiceApi> {
  private String birthday;

  public ModifyBirthdayCase(String birthday) {
    this.birthday = birthday;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().modifyBirthday(birthday);
  }
}
