package com.dindong.dingdong.manager;

import com.dindong.dingdong.network.bean.auth.User;

/**
 * Created by wcong on 2018/3/10.
 */

public class AuthSession {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
