package com.dindong.dingdong.network.bean.pay;

import java.io.Serializable;
import java.math.BigDecimal;

import com.dindong.dingdong.network.bean.shop.Shop;
import com.dindong.dingdong.network.bean.shop.SubjectType;

/**
 * Created by wangcong on 2018/3/22.
 * <p>
 */

public class SubjectOrder implements Serializable {
  private String id;// UUID
  private String orderNum;// 订单号
  private String subjectName;// 课程名称
  private SubjectType subjectType;// 课程类型
  private Shop shop;// 所属门店

  private OrderState orderState;// 订单状态
  private BigDecimal subjectAmount = BigDecimal.ZERO;// 课程现价
  private BigDecimal subjectOriginalAmount = BigDecimal.ZERO;// 课程原价
  private BigDecimal count = BigDecimal.ZERO;// 购买数量
  private BigDecimal payAmount = BigDecimal.ZERO;// 支付金额
  private BigDecimal favourAmount = BigDecimal.ZERO;// 优惠金额
  private PayMode payMode;// 支付方式
}
