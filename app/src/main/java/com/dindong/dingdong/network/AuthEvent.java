package com.dindong.dingdong.network;

/**
 * Created by wangcong on 2018/3/9.
 */

public class AuthEvent {
    public static int TOKEN_EXPIRED = 1;

    public int type;

    public AuthEvent(int type) {
        this.type = type;
    }
}
