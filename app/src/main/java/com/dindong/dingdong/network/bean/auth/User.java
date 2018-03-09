package com.dindong.dingdong.network.bean.auth;

/**
 * Created by wcong on 2018/3/9.
 */

public class User {
    private String id;//用户ID
    private AuthIdentity identity;//用户身份

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AuthIdentity getIdentity() {
        return identity;
    }

    public void setIdentity(AuthIdentity identity) {
        this.identity = identity;
    }
}
