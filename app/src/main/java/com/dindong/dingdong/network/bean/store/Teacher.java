package com.dindong.dingdong.network.bean.store;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.network.bean.entity.GlobalImage;

/**
 * Created by wcong on 2018/3/13.
 */

public class Teacher {
  private String id;// UUID
  private String name;// 教师名称
  private String remark;// 个人简介
  private String dingdongId;// 叮咚号
  private BigDecimal favCount = BigDecimal.ZERO;// 粉丝数量
  private List<String> tags = new ArrayList<>();// 标签
  private String type;// 老师类型：platform-平台认证，org-机构老师
  private GlobalImage image;// 教师图片

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getDingdongId() {
    return dingdongId;
  }

  public void setDingdongId(String dingdongId) {
    this.dingdongId = dingdongId;
  }

  public BigDecimal getFavCount() {
    return favCount;
  }

  public void setFavCount(BigDecimal favCount) {
    this.favCount = favCount;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public GlobalImage getImage() {
    return image;
  }

  public void setImage(GlobalImage image) {
    this.image = image;
  }
}
