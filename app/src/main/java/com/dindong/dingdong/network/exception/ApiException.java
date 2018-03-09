package com.dindong.dingdong.network.exception;

import java.io.IOException;

/**
 * Created by dzq on 2016/10/18.
 */

public class ApiException extends IOException {

  private int code;

  public ApiException(int code, String message) {
    super(message);
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
