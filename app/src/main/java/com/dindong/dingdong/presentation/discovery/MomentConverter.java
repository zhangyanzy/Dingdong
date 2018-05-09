package com.dindong.dingdong.presentation.discovery;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.bean.comment.Comment;
import com.dindong.dingdong.network.bean.entity.GlobalImage;
import com.dindong.dingdong.network.bean.entity.Ucn;

/**
 * Created by wcong on 2018/5/6.
 * <p>
 * </>
 */

public class MomentConverter {
  /**
   * 创建新评论
   * 
   * @param message
   *          评论内容
   * @param relationId
   *          关联id
   * @return
   */
  public static Comment createComment(String message, String relationId) {
    Comment comment = new Comment();
    comment.setId(UUID.randomUUID().toString());
    comment.setMessage(message);
    comment.setRelationId(relationId);
    comment.setDate(new Date());
    Ucn ucn = new Ucn();
    ucn.setUserId(SessionMgr.getUser().getId());
    ucn.setUserName(SessionMgr.getUser().getName());
    ucn.setImage(SessionMgr.getUser().getUserImage());
    return comment;
  }

  /**
   * 创建新动态
   * 
   * @param images
   * @param message
   * @return
   */
  public static Comment createMoment(List<GlobalImage> images, String message) {
    Comment comment = new Comment();
    comment.setId(UUID.randomUUID().toString());
    comment.setMessage(message);
    comment.setDate(new Date());
    Ucn ucn = new Ucn();
    ucn.setUserId(SessionMgr.getUser().getId());
    ucn.setUserName(SessionMgr.getUser().getName());
    ucn.setImage(SessionMgr.getUser().getUserImage());
    comment.setImages(images);
    return comment;
  }
}
