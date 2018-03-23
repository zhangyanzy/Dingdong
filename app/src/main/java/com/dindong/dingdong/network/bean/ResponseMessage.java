package com.dindong.dingdong.network.bean;

/**
 * Created by wcong on 2018/3/23.
 * <p>
 * </>
 */

public class ResponseMessage<T> {
  private String message;// 调用结果消息
  private T result; // 成功时响应数据
  private int status; // 状态码
  private int timestamp;// 时间戳

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(int timestamp) {
    this.timestamp = timestamp;
  }
}
