package com.dindong.dingdong.network.bean.account;

import java.math.BigDecimal;

/**
 * Created by wcong on 2018/5/20.
 * <p>
 * 账户流水明细</>
 */

public class AccountLog {
  private String accountId;// 所属账户ID
  private BigDecimal amount = BigDecimal.ZERO;// 变动金额
  private String bizFlow;// 交易流水号
  private String bizType;// 业务类型
  private String created;// 创建时间
  private String description;// 说明
  private String id;
  private BigDecimal settleAmount = BigDecimal.ZERO;// 变动后金额
  private String type;// 变动类型['余额', '冻结金额']

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getBizFlow() {
    return bizFlow;
  }

  public void setBizFlow(String bizFlow) {
    this.bizFlow = bizFlow;
  }

  public String getBizType() {
    return bizType;
  }

  public void setBizType(String bizType) {
    this.bizType = bizType;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BigDecimal getSettleAmount() {
    return settleAmount;
  }

  public void setSettleAmount(BigDecimal settleAmount) {
    this.settleAmount = settleAmount;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
