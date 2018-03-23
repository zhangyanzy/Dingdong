package com.dindong.dingdong.util;

/**
 * Created by wcong on 2018/3/23.
 * <p>
 * </>
 */

public class CopyUtil {
  public static <T> T copy(T t) {
    String value = GsonUtil.toJson(t);
    if (value == null) {
      return null;
    }
    return GsonUtil.parse(value, (Class<T>) t.getClass());
  }

}
