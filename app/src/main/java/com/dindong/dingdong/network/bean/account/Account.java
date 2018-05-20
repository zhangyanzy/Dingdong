package com.dindong.dingdong.network.bean.account;

import java.math.BigDecimal;

/**
 * Created by wcong on 2018/5/21.
 * <p>
 * 账户信息</>
 */

public class Account {
  private BigDecimal forzenTotal = BigDecimal.ZERO;// 冻结金额
  private BigDecimal total = BigDecimal.ZERO;// 账户余额
  private String id;
  private String ownerId;// 账户拥有者ID
  private String type;// 账户类型

  public BigDecimal getForzenTotal() {
    return forzenTotal;
  }

  public void setForzenTotal(BigDecimal forzenTotal) {
    this.forzenTotal = forzenTotal;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
