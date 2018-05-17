package com.dindong.dingdong.manager;

/**
 * Created by wcong on 2018/5/18.
 * <p>
 * </>
 */

public class PayCallback {

  private static Callback callback;

  /**
   * 注册监听
   * 
   * @param callback
   */
  public static void register(Callback callback) {
    PayCallback.callback = callback;
  }

  /**
   * 清空监听
   */
  public static void clean() {
    PayCallback.callback = null;
  }

  /**
   * 支付成功
   */
  public static void requestSuccess() {
    if (PayCallback.callback != null)
      PayCallback.callback.onPaySuccess();
  }

  /**
   * 支付失败
   */
  public static void requestFailure() {
    if (PayCallback.callback != null)
      PayCallback.callback.onPayFailure();
  }

  public interface Callback {
    /**
     * 支付成功
     */
    void onPaySuccess();

    /**
     * 支付失败
     */
    void onPayFailure();
  }
}
