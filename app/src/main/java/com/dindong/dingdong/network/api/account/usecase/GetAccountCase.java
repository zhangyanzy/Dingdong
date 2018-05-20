package com.dindong.dingdong.network.api.account.usecase;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.account.AccountServiceApi;

import rx.Observable;

/**
 * Created by wcong on 2018/5/21.
 * <p></>
 */

public class GetAccountCase extends BaseUseCase<AccountServiceApi>{
    @Override
    protected Observable buildCase() {
        return createConnection().getAccount();
    }
}
