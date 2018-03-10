package com.dindong.dingdong.network.api.usecase;

import android.content.Context;

import com.dindong.dingdong.network.BaseUseCase;
import com.dindong.dingdong.network.api.GlobalTestApi;

import rx.Observable;

/**
 * Created by wcong on 2018/3/10.
 */

public class TestUseCase extends BaseUseCase<GlobalTestApi>{

    @Override
    protected Observable buildCase() {
        return createConnection().test("13900001092","111111");
    }
}
