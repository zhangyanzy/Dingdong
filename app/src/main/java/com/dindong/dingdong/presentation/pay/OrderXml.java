package com.dindong.dingdong.presentation.pay;

import com.dindong.dingdong.DDApp;
import com.dindong.dingdong.R;
import com.dindong.dingdong.network.bean.pay.OrderState;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

/**
 * Created by wcong on 2018/3/25.
 * <p>
 * </>
 */

public class OrderXml {
  /**
   * 根据订单状态设置文本颜色
   * 
   * @param orderState
   * @return
   */
  public static int getTextColorByState(String orderState) {
    if (orderState == null)
      return Color.parseColor("#333333");
    if (orderState.equals(OrderState.waitPay.toString()))
      return Color.parseColor("#FF9C09");
    else if (orderState.equals(OrderState.grouping.toString()))
      return Color.parseColor("#468DE6");
    else if (orderState.equals(OrderState.confirmed.toString()))
      return Color.parseColor("#FF8082");
    else if (orderState.equals(OrderState.finished.toString()))
      return Color.parseColor("#888888");
    return Color.parseColor("#333333");
  }

  /**
   * 获取订单状态对应文本
   * 
   * @param orderState
   * @return
   */
  public static String getStateStr(String orderState) {
    if (orderState == null)
      return DDApp.getInstance().getString(R.string.order_detail_state_wait);
    if (orderState.equals(OrderState.waitPay.toString()))
      return DDApp.getInstance().getString(R.string.order_detail_state_wait);
    else if (orderState.equals(OrderState.grouping.toString()))
      return DDApp.getInstance().getString(R.string.order_detail_state_wait_group);
    else if (orderState.equals(OrderState.confirmed.toString()))
      return DDApp.getInstance().getString(R.string.order_detail_state_can_use);
    else if (orderState.equals(OrderState.finished.toString()))
      return DDApp.getInstance().getString(R.string.order_detail_state_success);
    return DDApp.getInstance().getString(R.string.order_detail_state_wait);
  }

  /**
   * 订单背景色
   * 
   * @param orderState
   * @return
   */
  public static Drawable getStateDrawable(String orderState) {
    if (orderState == null)
      return DDApp.getInstance().getResources().getDrawable(R.drawable.bg_order_state_can_use);
    if (orderState.equals(OrderState.grouping.toString()))
      return DDApp.getInstance().getResources().getDrawable(R.drawable.bg_order_state_wait_group);
    else if (orderState.equals(OrderState.confirmed.toString()))
      return DDApp.getInstance().getResources().getDrawable(R.drawable.bg_order_state_can_use);
    return DDApp.getInstance().getResources().getDrawable(R.drawable.bg_order_state_can_use);
  }
}
