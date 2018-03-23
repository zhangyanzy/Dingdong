package com.dindong.dingdong.network.bean;

import java.util.Map;

/**
 * Created by wangcong on 2018/3/9.
 */

public class Response<T> {
  private boolean success;
  private Map<String, String> fields;
  private String message;
  private T data;
  private int total;
  private boolean more;

  public boolean isMore() {
    return more;
  }

  public void setMore(boolean more) {
    this.more = more;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public Map<String, String> getFields() {
    return fields;
  }

  public void setFields(Map<String, String> fields) {
    this.fields = fields;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
