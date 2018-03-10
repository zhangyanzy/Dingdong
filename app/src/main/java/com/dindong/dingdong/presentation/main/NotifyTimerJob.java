package com.dindong.dingdong.presentation.main;

import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.usecase.TestUseCase;
import com.dindong.dingdong.network.bean.Response;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nullable;

/**
 * Created by wcong on 2018/3/10.
 * 首页定时任务，获取用户新消息
 */

public class NotifyTimerJob extends Timer {
    private int type;
    private long delay = 0;
    private long period = 1000 * 30;//30s更新一次

    public NotifyTimerJob(int type) {
        this.type = type;
    }

    public void execute(@Nullable final TimerListener listener) {
        schedule(new TimerTask() {
            @Override
            public void run() {
                if (type == MainActivity.IDENTIFICATION_DISCOVERY)
                    getDiscovery(listener);
                else if (type == MainActivity.IDENTIFICATION_MSG)
                    getMsg(listener);
            }
        }, delay, period);
    }

    private void getDiscovery(final TimerListener listener) {
        new TestUseCase().execute(new HttpSubscriber<Response>() {
            @Override
            public void onFailure(String errorMsg, Response<Response> response) {

            }

            @Override
            public void onSuccess(Response<Response> response) {
                if (listener != null)
                    listener.onSuccess(true);
            }
        });
    }

    private void getMsg(final TimerListener listener) {
        new TestUseCase().execute(new HttpSubscriber<Response>() {
            @Override
            public void onFailure(String errorMsg, Response<Response> response) {

            }

            @Override
            public void onSuccess(Response<Response> response) {
                if (listener != null)
                    listener.onSuccess(true);
            }
        });
    }

    public interface TimerListener {

        void onSuccess(boolean hasNew);

    }
}
