package com.dindong.dingdong.network.api.member.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.member.MemeberServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * </>
 */

public class ModifyAvatarCase extends BaseUseCase<MemeberServiceApi> {
  private String avatar;

  public ModifyAvatarCase(String avatar) {
    this.avatar = avatar;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().modifyAvatar(avatar);
  }
}
