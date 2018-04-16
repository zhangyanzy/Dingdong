package com.dindong.dingdong.network.bean.store;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.network.bean.entity.Ucn;

/**
 * Created by wcong on 2018/4/14.
 * <p>
 * 门店评论</>
 */

public class ShopComment extends Comment {
  private BigDecimal grade = BigDecimal.ZERO;// 评分
  private List<GlobalImage> images;// 添加图片
  private List<Ucn> praiseUsers = new ArrayList<>();// 点赞用户

  public BigDecimal getGrade() {
    return grade;
  }

  public void setGrade(BigDecimal grade) {
    this.grade = grade;
  }

  public List<GlobalImage> getImages() {
    return images;
  }

  public void setImages(List<GlobalImage> images) {
    this.images = images;
  }

  public List<Ucn> getPraiseUsers() {
    return praiseUsers;
  }

  public void setPraiseUsers(List<Ucn> praiseUsers) {
    this.praiseUsers = praiseUsers;
  }
}
