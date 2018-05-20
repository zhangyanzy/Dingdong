package com.dindong.dingdong.presentation.user;

import java.math.BigDecimal;
import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.base.BaseActivity;
import com.dindong.dingdong.databinding.ActivityUserAccountBinding;
import com.dindong.dingdong.network.HttpSubscriber;
import com.dindong.dingdong.network.api.account.usecase.GetAccountCase;
import com.dindong.dingdong.network.api.account.usecase.ListAccountLogCase;
import com.dindong.dingdong.network.bean.Response;
import com.dindong.dingdong.network.bean.account.Account;
import com.dindong.dingdong.network.bean.account.AccountLog;
import com.dindong.dingdong.network.bean.entity.QueryParam;
import com.dindong.dingdong.util.DialogUtil;
import com.dindong.dingdong.util.IsEmpty;
import com.dindong.dingdong.util.StringUtil;
import com.dindong.dingdong.widget.NavigationTopBar;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseFooterView;
import com.dindong.dingdong.widget.pullrefresh.layout.BaseHeaderView;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

/**
 * 我的账户
 */
public class UserAccountActivity extends BaseActivity {

  ActivityUserAccountBinding binding;

  SingleTypeAdapter adapter;

  @Override
  protected void initComponent() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_user_account);

    binding.nb.setContent(NavigationTopBar.ContentType.WHITE);
    binding.refreshLayout.setHasFooter(false);
    adapter = new SingleTypeAdapter(this, R.layout.item_account_log);
  }

  @Override
  protected void loadData(Bundle savedInstanceState) {
    getAccount();
    listAccountLog(false, true);
  }

  @Override
  protected void createEventHandlers() {
    binding.nb
        .setNavigationTopBarClickListener(new NavigationTopBar.NavigationTopBarClickListener() {
          @Override
          public void leftImageClick() {
            finish();
          }
        });
    binding.refreshLayout.setOnRefreshListener(new BaseHeaderView.OnRefreshListener() {
      @Override
      public void onRefresh(BaseHeaderView baseHeaderView) {
        listAccountLog(false, true);
      }
    });
    binding.refreshLayout.setOnLoadListener(new BaseFooterView.OnLoadListener() {
      @Override
      public void onLoad(BaseFooterView baseFooterView) {
        listAccountLog(false, false);
      }
    });

  }

  /**
   * 获取账户信息
   */
  private void getAccount() {
    new GetAccountCase().execute(new HttpSubscriber<Account>(this) {
      @Override
      public void onFailure(String errorMsg, Response<Account> response) {
        DialogUtil.getErrorDialog(UserAccountActivity.this, errorMsg).show();
      }

      @Override
      public void onSuccess(Response<Account> response) {
        binding.txtAmount.setText(StringUtil.amount(response.getData().getTotal()));
      }
    });
  }

  /**
   * 获取账户流水明细
   * 
   * @param showProgress
   * @param isRefresh
   */
  private void listAccountLog(boolean showProgress, final boolean isRefresh) {
    final QueryParam param = new QueryParam();
    if (isRefresh)
      param.setStart(0);
    else
      param.setStart(adapter.getData().size());

    new ListAccountLogCase(param).execute(
        new HttpSubscriber<List<AccountLog>>(showProgress ? UserAccountActivity.this : null) {
          @Override
          public void onFailure(String errorMsg, Response<List<AccountLog>> response) {
            DialogUtil.getErrorDialog(UserAccountActivity.this, errorMsg).show();
          }

          @Override
          public void onSuccess(Response<List<AccountLog>> response) {
            loadRecyclerView(response.getData(), isRefresh, response.isMore());
          }
        });
  }

  private void loadRecyclerView(List<AccountLog> data, boolean isRefresh, boolean isMore) {
    if (isRefresh) {
      adapter.clear();
      binding.refreshLayout.stopRefresh();
    } else
      binding.refreshLayout.stopLoad();
    adapter.addAll(data);
    binding.refreshLayout.setHasFooter(isMore);
    if (isRefresh) {
      LinearLayoutManager manager = new LinearLayoutManager(this);
      manager.setOrientation(LinearLayoutManager.VERTICAL);
      binding.lst.setLayoutManager(manager);
      binding.lst.setAdapter(adapter);
    }
  }

  public static class AccountXml {
    /**
     * 获取明细金额字体颜色
     * 
     * @param bizType
     *          业务类型
     * @return
     */
    public static int getItemAmountColor(String bizType) {
      if (IsEmpty.string(bizType))
        return Color.parseColor("#89C850");
      if (bizType.equals("打赏"))
        return Color.parseColor("#89C850");
      else if (bizType.equals("提成"))
        return Color.parseColor("#468DE6");
      else if (bizType.equals("提现"))
        return Color.parseColor("#FF8082");
      else if (bizType.equals("消费"))
        return Color.parseColor("#FF9C09");
      return Color.parseColor("#89C850");
    }

    /**
     * 格式变动金额文本
     * 
     * @param amount
     *          变动金额
     * @return
     */
    public static String parse(BigDecimal amount) {
      return (amount.compareTo(BigDecimal.ZERO) >= 0 ? "+\b¥" : "-\b¥") + StringUtil.amount(amount);
    }
  }
}
