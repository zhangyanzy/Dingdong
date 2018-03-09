package com.dindong.dingdong.network.bean;

/**
 * Created by wangcong on 2018/3/9.
 */

public class SumResponse<S, T> extends Response<T> {
  private S summary;

  public S getSummary() {
    return summary;
  }

  public void setSummary(S summary) {
    this.summary = summary;
  }

}
