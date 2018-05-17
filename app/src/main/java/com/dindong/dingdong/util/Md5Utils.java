package com.dindong.dingdong.util;

import android.util.Log;

import java.security.MessageDigest;

/**
 * Created by wcong on 2018/5/18.
 * <p>
 * </>
 */

public class Md5Utils {
  private static final String TAG="MD5";

  private static byte[] md5(String s) {
    MessageDigest algorithm;
    try {
      algorithm = MessageDigest.getInstance("MD5");
      algorithm.reset();
      algorithm.update(s.getBytes("UTF-8"));
      byte[] messageDigest = algorithm.digest();
      return messageDigest;
    } catch (Exception e) {
        Log.e(TAG,e.toString());
    }
    return null;
  }

  private static final String toHex(byte hash[]) {
    if (hash == null) {
      return null;
    }
    StringBuffer buf = new StringBuffer(hash.length * 2);
    int i;

    for (i = 0; i < hash.length; i++) {
      if ((hash[i] & 0xff) < 0x10) {
        buf.append("0");
      }
      buf.append(Long.toString(hash[i] & 0xff, 16));
    }
    return buf.toString();
  }

  public static String hash(String s) {
    try {
      return new String(toHex(md5(s)).getBytes("UTF-8"), "UTF-8");
    } catch (Exception e) {
        Log.e(TAG,e.toString());
      return s;
    }
  }

  /**
   * 对密码按照用户名，密码，盐进行加密
   * 
   * @param username
   *          用户名
   * @param password
   *          密码
   * @param salt
   *          盐
   * @return
   */
  public static String encryptPassword(String username, String password, String salt) {
    return Md5Utils.hash(username + password + salt);
  }

  /**
   * 对密码按照密码，盐进行加密
   * 
   * @param password
   *          密码
   * @param salt
   *          盐
   * @return
   */
  public static String encryptPassword(String password, String salt) {
    return Md5Utils.hash(password + salt);
  }
}