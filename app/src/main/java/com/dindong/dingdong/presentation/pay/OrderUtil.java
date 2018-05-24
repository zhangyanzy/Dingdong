package com.dindong.dingdong.presentation.pay;

import java.util.Date;

import com.dindong.dingdong.config.AppConfig;
import com.dindong.dingdong.manager.SessionMgr;
import com.dindong.dingdong.network.bean.activity.ShopActivity;
import com.dindong.dingdong.network.bean.good.ShopGood;
import com.dindong.dingdong.network.bean.pay.Order;
import com.dindong.dingdong.network.bean.pay.OrderType;
import com.dindong.dingdong.network.bean.store.Subject;
import com.dindong.dingdong.util.IsEmpty;

import android.content.Context;
import android.content.Intent;

/**
 * Created by wcong on 2018/5/24.
 * <p>
 * 订单工具类</>
 */

public class OrderUtil {

  /**
   * 根据课程创建订单
   * 
   * @param subject
   * @param groupId
   *          如果为参团商品，比传团Id,否则为空
   * @return
   */
  public static Order createOrder(Subject subject, String groupId) {
    Order order = new Order();
    order.setDate(new Date());
    order.setItemId(subject.getId());
    order.setItemImageUrl(subject.getImage().getUrl());
    order.setItemName(subject.getName());
    order.setItemType(OrderType.course.toString());
    order.setDisPrice(subject.getAmount());
    order.setPrice(subject.getOriginalAmount());
    order.setUserId(SessionMgr.getUser().getId());
    if (!IsEmpty.string(groupId))
      // 参团商品，需添加团ID
      order.setGroupBuyId(groupId);
    return order;
  }

  /**
   * 根据商品创建订单
   * 
   * @param good
   * @return
   */
  public static Order createOrder(ShopGood good) {
    Order order = new Order();
    order.setDate(new Date());
    order.setItemId(good.getId());
    order.setItemImageUrl(good.getImage().getUrl());
    order.setItemName(good.getName());
    order.setItemType(OrderType.goods.toString());
    order.setDisPrice(good.getAmount());
    order.setPrice(good.getOriginalAmount());
    order.setUserId(SessionMgr.getUser().getId());
    return order;
  }

  /**
   * 根据活动创建订单
   * 
   * @param shopActivity
   * @return
   */
  public static Order createOrder(ShopActivity shopActivity) {
    Order order = new Order();
    order.setDate(new Date());
    order.setItemId(shopActivity.getId());
    order.setItemImageUrl(shopActivity.getImage().getUrl());
    order.setItemName(shopActivity.getName());
    order.setItemType(OrderType.activity.toString());
    order.setDisPrice(shopActivity.getAmount());
    order.setPrice(shopActivity.getOriginalAmount());
    order.setUserId(SessionMgr.getUser().getId());
    return order;
  }

  /**
   * 跳转到订单确认界面
   * 
   * @param context
   * @param order
   * @param shopName
   * @param isGroup
   *          是否为团购订单，只适用于课程
   * @param unit
   *          商品单位，非必传
   */
  public static void startOrderConfirmActivity(Context context, Order order, String shopName,
      boolean isGroup, String unit) {
    Intent intent = new Intent(context, OrderConfirmActivity.class);
    intent.putExtra(AppConfig.IntentKey.DATA, order);
    intent.putExtra("isGroup", isGroup);
    intent.putExtra("shop", shopName);
    intent.putExtra("unit", unit);
    context.startActivity(intent);
  }

}
