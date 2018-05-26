package com.dindong.dingdong.network.api.member.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.member.MemberServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/26.
 * <p>
 * </>
 */

public class GetMemberCase extends BaseUseCase<MemberServiceApi> {
  private String id;

  public GetMemberCase(String id) {
    this.id = id;
  }

  @Override
  protected Observable buildCase() {
    return createConnection().getMember(id);
  }
}
