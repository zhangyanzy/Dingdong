package com.dindong.dingdong.network.api.member.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.member.MemeberServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * </>
 */

public class ModifyBirthdayCase extends BaseUseCase<MemeberServiceApi> {
  private String birthday;

  public ModifyBirthdayCase(String birthday) {
    this.birthday = birthday;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().modifyBirthday(birthday);
  }
}
