package com.dindong.dingdong.network.bean.entity;

import java.io.Serializable;

/**
 * Created by wcong on 2018/5/7.
 * <p>
 * </>
 */

public class FileInfo implements Serializable {
  private String id;// 文件ID
  private String url;// 文件完整URL

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
