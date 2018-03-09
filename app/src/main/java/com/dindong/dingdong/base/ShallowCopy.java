package com.dindong.dingdong.base;

/**
 * Created by wcong on 2018/3/10.
 * 浅拷贝
 */

public class ShallowCopy<T> implements Cloneable {

    public T clone() {
        T o = null;
        try {
            o = (T) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

}
