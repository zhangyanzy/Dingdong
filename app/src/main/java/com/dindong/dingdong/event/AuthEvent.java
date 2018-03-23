package com.dindong.dingdong.event;

/**
 * Created by wcong on 2018/3/23.
 * <p>
 * </>
 */

public class AuthEvent {
  public static int TOKEN_EXPIRED = 1;
  public static int EXIT = 2;
  public static int SHOP_LOCKED = 3;
  public static int SHOP_SUSPEND = 4;

  public int type;

  public AuthEvent(int type) {
    this.type = type;
  }
}
