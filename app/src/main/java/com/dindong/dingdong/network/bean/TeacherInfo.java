package com.dindong.dingdong.network.bean;

/**
 * Created by zhangyan on 2018/3/13.
 */

public class TeacherInfo {

    private String name;
    private String info;

    public TeacherInfo(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "TeacherInfo{" +
                "name='" + name + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}

