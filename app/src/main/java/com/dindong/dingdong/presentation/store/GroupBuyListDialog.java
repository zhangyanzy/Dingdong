package com.dindong.dingdong.presentation.store;

import java.util.List;

import com.dindong.dingdong.R;
import com.dindong.dingdong.databinding.DialogGroupBuyListBinding;
import com.dindong.dingdong.listener.OnConfirmListener;
import com.dindong.dingdong.network.bean.groupbuy.GroupBuy;
import com.dindong.dingdong.presentation.subject.GroupBuyPresenter;
import com.dindong.dingdong.util.DensityUtil;
import com.dindong.dingdong.widget.baseadapter.SingleTypeAdapter;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by wangcong on 2018/5/31.
 * <p>
 */

public class GroupBuyListDialog extends Dialog {
  private List<GroupBuy> groupBuys;
  private OnConfirmListener onConfirmListener;

  DialogGroupBuyListBinding binding;

  public GroupBuyListDialog(Context context, List<GroupBuy> groupBuys,
      OnConfirmListener onConfirmListener) {
    super(context, R.style.FullScreenDialog);
    this.groupBuys = groupBuys;
    this.onConfirmListener = onConfirmListener;

    binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
        R.layout.dialog_group_buy_list, null, false);
    setContentView(binding.getRoot());

    initList(groupBuys);
  }

  private void initList(List<GroupBuy> groupBuys) {
    SingleTypeAdapter adapter = new SingleTypeAdapter(getContext(), R.layout.item_group_buy);
    adapter.addAll(groupBuys);
    adapter.setPresenter(new Presenter());
    LinearLayoutManager manager = new LinearLayoutManager(getContext());
    binding.lst.setLayoutManager(manager);
    binding.lst.setNestedScrollingEnabled(false);
    binding.lst.setAdapter(adapter);
  }

  @Override
  public void show() {
    super.show();

    Window win = getWindow();
    win.getDecorView().setPadding(0, 0, 0, 0);
    WindowManager.LayoutParams lp = win.getAttributes();
    lp.height = DensityUtil.getScreenHeight(getContext()) / 2;
    win.setAttributes(lp);
  }

  public class Presenter implements GroupBuyPresenter {

    @Override
    public void onGroupBuyClick(GroupBuy groupBuy) {
      if (onConfirmListener != null)
        onConfirmListener.onConfirm(GroupBuyListDialog.this, groupBuy);

    }
  }
}
