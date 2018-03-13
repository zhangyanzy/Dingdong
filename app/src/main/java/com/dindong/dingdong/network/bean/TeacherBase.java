package com.dindong.dingdong.network.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyan on 2018/3/12.
 * Tests
 */

public class TeacherBase {

    private int Count;
    private List<TeacherInfo> teacherInfos = new ArrayList<>();

    public TeacherBase(int count, List<TeacherInfo> teacherInfos) {
        Count = count;
        this.teacherInfos = teacherInfos;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public List<TeacherInfo> getTeacherInfos() {
        return teacherInfos;
    }

    public void setTeacherInfos(List<TeacherInfo> teacherInfos) {
        this.teacherInfos = teacherInfos;
    }


}

