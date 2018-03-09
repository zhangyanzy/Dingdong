package com.dindong.dingdong.network.bean;

/**
 * Created by NeilSpears on 2016/10/20.
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
