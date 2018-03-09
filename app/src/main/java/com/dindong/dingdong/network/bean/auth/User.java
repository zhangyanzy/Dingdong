package com.dindong.dingdong.network.bean.auth;

import com.dindong.dingdong.base.ShallowCopy;

import java.io.Serializable;

/**
 * Created by wcong on 2018/3/9.
 */

public class User extends ShallowCopy<User> implements Serializable, Cloneable {
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
