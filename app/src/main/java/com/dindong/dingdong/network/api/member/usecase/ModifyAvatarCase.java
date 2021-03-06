package com.dindong.dingdong.network.api.member.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.member.MemberServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * </>
 */

public class ModifyAvatarCase extends BaseUseCase<MemberServiceApi> {
  private String avatar;

  public ModifyAvatarCase(String avatar) {
    this.avatar = avatar;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().modifyAvatar(avatar);
  }
}
