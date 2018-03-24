package com.dindong.dingdong.presentation.pay;

import com.dindong.dingdong.network.bean.pay.OrderState;

import android.graphics.Color;

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
  public static int getTextColorByState(OrderState orderState) {
    if (orderState == null)
      return Color.parseColor("#FF9C09");
    if (orderState.equals(OrderState.wait))
      return Color.parseColor("#FF9C09");
    else if (orderState.equals(OrderState.wait_group))
      return Color.parseColor("#468DE6");
    else if (orderState.equals(OrderState.can_use))
      return Color.parseColor("#FF8082");
    else if (orderState.equals(OrderState.success))
      return Color.parseColor("#888888");
    return Color.parseColor("#FF9C09");
  }
}
