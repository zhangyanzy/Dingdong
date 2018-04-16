package com.dindong.dingdong.network.bean.comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dindong.dingdong.network.bean.entity.Ucn;

/**
 * Created by wcong on 2018/4/14.
 * <p>
 * 评论/回复实体 </>
 */

public class Comment {
  private String id;
  private Ucn ucn;// 评论/回复人信息
  private String message;// 评论/回复内容
  private Date date;// 评论/回复时间 格式:YYYY-MM-DD HH:mm:ss
  private List<Comment> comments=new ArrayList<>();// 如果当前为评论实体，则可能包含回复

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Ucn getUcn() {
    return ucn;
  }

  public void setUcn(Ucn ucn) {
    this.ucn = ucn;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }
}
